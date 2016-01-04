/**
 * 
 */
package parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import utils.Logging;

/**
 * @author Brandon Sheffield - ISPA Technology, LLC.
 * Created on  Jan 3, 2016
 * Description:  TODO
 */
public class DailyParser implements CashCowParser{
	private final static String REPORT = "Report";
	private final static String LOCATION = "Location";
	private final static String DATE = "Date";
	private final static String USDA_NEWS = "USDA News";
	private final static String LOCATION_DAILY_SUMMARY = "Location Daily Summary";
	private final static String DATE_OF_LIVE_AUCTIONS = "Date of Live Auctions";
	private final static String RECEIPTS = "Receipts";
	private final static String SUMMARY = "Summary";

	private final static String HEAD = "Head";
	private final static String WT_RNG = "Wt Range";
	private final static String AVG_WT = "Avg Wt";
	private final static String PRICE_RNG = "Price Range";
	private final static String AVG_PRICE = "Avg Price";

	private List<Map<String,Object>> prices_list;
	private Map<String,Object> metaData_map;

	public DailyParser() {
		prices_list = new ArrayList<>();
		metaData_map = new HashMap<>();
	}

	/* (non-Javadoc)
	 * @see readers.Summary#getSummaryName()
	 */
	@Override
	public String getParserName() {
		return DailyParser.class.getSimpleName();
	}

	/* (non-Javadoc)
	 * @see readers.Summary#readInSummary(java.lang.String)
	 */
	@Override
	public Boolean readInSummary(final String URL) throws IOException {

		String reportName;
		String metaData;
		String locationDailySummary;
		String dateOfLiveAuctions;
		String cattleReceipts;
		String description;
		String line = "";
		String headers;//TODO currently no use. keep for now.
		final String DAILY_WEB_LINK = "http://www.ams.usda.gov/mnreports/mg_ls144.txt";

		//for offline testing
		//File f = new File("./src/read_this");
		//BufferedReader in = new BufferedReader(new FileReader(f));

		URL url = new URL("http://www.ams.usda.gov/mnreports/mg_ls144.txt");
		BufferedReader in = new BufferedReader( new InputStreamReader(url.openStream()));

		reportName = in.readLine();
		metaData = in.readLine();

		in.readLine(); //blank line throw away
		locationDailySummary = in.readLine();
		dateOfLiveAuctions = in.readLine();
		in.readLine();
		cattleReceipts = in.readLine();
		in.readLine();

		StringBuilder descripStr = new StringBuilder();
		line = in.readLine();
		descripStr.append(line);
		while (!line.matches(""))
			descripStr.append(line = in.readLine());

		description = descripStr.toString();	
		metaData_map = buildMetaDataMap(reportName, metaData,locationDailySummary,
				dateOfLiveAuctions,cattleReceipts,description);

		while (line!=null){
			line = line.trim(); //matches messed up with leading whitespace

			//TODO eventually parse more than just the lazy heifers
			if (line.matches("Feeder Heifers Medium and Large 1")){

				headers = in.readLine();
				line = in.readLine().trim();

				//read in heifer data until blankline
				while(!line.matches("")){
					prices_list.add( converToMapofData(line));
					line = in.readLine().trim();
				}
			}
			line = in.readLine();
		}
		in.close();
		return true;
	}

	/* (non-Javadoc)
	 * @see readers.Summary#getPricesList()
	 */
	@Override
	public List<Map<String, Object>> getPricesList() {
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
		Map<String, Object> m = new HashMap<>();
		String[] delimString = s.split("\\s+");
		Queue<Object> q = new LinkedList<Object>();

		for (String i : delimString)
			q.add(i);

		//Dumps.dumpQueue(q);

		m.put(HEAD, q.poll());
		m.put(WT_RNG, q.poll());
		m.put(AVG_WT, q.poll());
		m.put(PRICE_RNG, q.poll());
		m.put(AVG_PRICE, q.poll());

		return m;
	}

	/* (non-Javadoc)
	 * @see parser.CashCowParser#getMetaDataMap()
	 */
	@Override
	public Map<String, Object> getMetaDataMap() {
		return metaData_map;
	}
}
