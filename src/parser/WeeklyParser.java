/**
 * 
 */
package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.CashCowParser;
import utils.Logging;

/**
 * @author Brandon Sheffield - ISPA Technology, LLC.
 * Created on  Jan 3, 2016
 * Description:  TODO
 */
public class WeeklyParser implements CashCowParser{
	private List<Map<String,Object>> prices_list;
	private Map<String,Object> metaData_map;

	private final static String REPORT = "Report";
	private final static String LOCATION = "Location";
	private final static String DATE = "Date";
	private final static String USDA_NEWS = "USDA News";
	private final static String LOCATION_DAILY_SUMMARY = "Location Daily Summary";
	private final static String DATE_OF_LIVE_AUCTIONS = "Date of Live Auctions";
	private final static String RECEIPTS = "Total Receipts";
	private final static String SUMMARY = "Summary";

	private final static String SLAUGHTER_COWS = "Slaughter Cows";
	private final static String BULLS = "Bulls";
	public WeeklyParser() {
		prices_list = new ArrayList<>();
		metaData_map = new HashMap<>();
	}

	/* (non-Javadoc)
	 * @see readers.Summary#getSummaryName()
	 */
	@Override
	public String getParserName() {
		return WeeklyParser.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see readers.Summary#readInSummary(java.lang.String)
	 */
	@Override
	public Boolean readInSummary(final String URL) throws MalformedURLException, IOException {

		String reportName;
		String metaData;
		String locationDailySummary;
		String dateOfLiveAuctions;
		//String cattleReceipts;
		String description;
		String line = "";
		URL url = new URL(URL);
		BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));

		reportName = in.readLine();
		metaData = in.readLine();
		in.readLine();//blank line
		locationDailySummary = in.readLine();
		while (!locationDailySummary.contains("Summary")){
			locationDailySummary = in.readLine();
		}
		in.readLine();
		dateOfLiveAuctions = in.readLine();
		in.readLine();

		StringBuilder descriptStr = new StringBuilder();
		descriptStr.append(line = in.readLine());

		//main data we care for that has trends in weekly prices
		while (!line.matches(""))
			descriptStr.append(line = in.readLine());

		description = descriptStr.toString();

		metaData_map = buildMetaDataMap(reportName, metaData, locationDailySummary, dateOfLiveAuctions, null, description);
		prices_list.add(converToMapofData(description));
		
		return true;
	}

	/* (non-Javadoc)
	 * @see readers.Summary#getPricesList()
	 */
	@Override
	public List<Map<String, Object>> getPricesList() {
		//TODO not used currently
		return prices_list;
	}

	/* (non-Javadoc)
	 * @see readers.Summary#buildMetaDataMap(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> buildMetaDataMap(String reportName, String metaData, String locationDailySummary,
			String dateOfLiveAuctions, String cattleReceipts, String summary) {
		Map<String, Object> m = new HashMap<>();
		String location, date, usda_news;
		location = date = usda_news = "NOT FOUND";

		Pattern pattern = Pattern.compile("[A-Z]{2}");
		Matcher matcher = pattern.matcher(metaData);
		if (matcher.find()){
			Integer offset = matcher.end();
			location = metaData.substring(matcher.start(), matcher.end());

			pattern = Pattern.compile("\\d{4}");
			matcher = pattern.matcher(metaData);
			if (matcher.find()){
				date = metaData.substring(offset,matcher.end()).trim();
				offset = matcher.end();
				usda_news = metaData.substring(offset).trim();
			}
		}
		else{
			Logging logger = new Logging();
			logger.warn("Unable to find correct header info using RegEx.  Defaults inserted.");
		}

		m.put(REPORT, reportName);
		m.put(LOCATION,location);
		m.put(DATE, date);
		m.put(USDA_NEWS, usda_news);
		m.put(LOCATION_DAILY_SUMMARY, locationDailySummary);
		m.put(DATE_OF_LIVE_AUCTIONS, dateOfLiveAuctions);
		m.put(RECEIPTS, cattleReceipts);
		m.put(SUMMARY, summary);

		return m;

	}

	/* (non-Javadoc)
	 * @see readers.Summary#converToMapofData(java.lang.String)
	 */
	@Override
	public Map<String, Object> converToMapofData(String s) {
		Map<String,Object> m = new HashMap<>();
		String summary = s.trim();
		Pattern pattern = Pattern.compile("[.]\\s{1}");//first occurence of sentence with a space behind it
		Matcher matcher = pattern.matcher(summary);
		//System.out.println(matcher.find());
		int offset = 0;
		if (matcher.find()){

			String totalReceipts = summary.substring(offset, matcher.end());
			m.put(RECEIPTS, totalReceipts);
			offset = matcher.end();
		}
		if (matcher.find()){
			String cowsAndBulls = summary.substring(offset,matcher.end());
			//System.out.println(cowsAndBulls);

			String slaughterCowsTrend = cowsAndBulls.substring(cowsAndBulls.indexOf("Slaughter cows sold "), cowsAndBulls.indexOf("and "));
			m.put(SLAUGHTER_COWS, slaughterCowsTrend);
			//System.out.println(slaughterCowsTrend);
			
			String bullsTrend = cowsAndBulls.substring(cowsAndBulls.indexOf("bulls sold "), cowsAndBulls.lastIndexOf("."));
			//System.out.println(bullsTrend);
			m.put(BULLS, bullsTrend);

			//System.out.println(totalReceipts);
			//System.out.println(summary);
		}
		//TODO parse trends!
		
		return m;
	}
	/*
	public String testWeeklyData(){
		String data =
				"  Total estimated receipts this week 9,500, last week 15,392 and "+
						"11,565 last year. Compared to last week: Slaughter cows sold 1.00"+
						"to 3.00 lower and bulls sold 3.00 to 6.00 lower. Replacement cows"+
						"and pairs sold mostly steady. All feeder classes sold 6.00 to 12.00"+
						"lower. Trade moderate, with moderate demand on feeders.";
		return data;
	}
	*/
	/* (non-Javadoc)
	 * @see parser.CashCowParser#getMetaDataMap()
	 */
	@Override
	public Map<String, Object> getMetaDataMap() {
		return metaData_map;
	}
}
