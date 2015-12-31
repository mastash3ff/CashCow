/**
 * 
 */
package java.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.utils.Logging;

/**
 * @author Brandon Sheffield
 *
 */
public class CowParser {
	final static String REPORT = "Report";
	final static String LOCATION = "Location";
	final static String DATE = "Date";
	final static String USDA_NEWS = "USDA News";
	final static String LOCATION_DAILY_SUMMARY = "Location Daily Summary";
	final static String DATE_OF_LIVE_AUCTIONS = "Date of Live Auctions";
	final static String RECEIPTS = "Receipts";
	final static String SUMMARY = "Summary";
	
	final static String HEAD = "Head";
	final static String WT_RNG = "Wt Range";
	final static String AVG_WT = "Avg Wt";
	final static String PRICE_RNG = "Price Range";
	final static String AVG_PRICE = "Avg Price";
	
	public static Map<String, Object> buildMetaDataMap(String reportName, String metaData, String locationDailySummary,
			String dateOfLiveAuctions, String cattleReceipts, String summary) throws SecurityException, IOException{
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
	
	/**Returns map of data in order -> Head   Wt Range   Avg Wt    Price Range   Avg Price
	using map instead of list so can detect nulls or errors in data later
	*/
	public static Map<String, Object> getFeederHeiferData(String s){
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
	/*
	public static String getMetaTestString(){
		return "Montgomery, AL      Wed Sep 23, 2015     USDA-AL Dept of Ag Market News";
	}
	public static String getHeiferTestString(){
		return "6    385-390    388       195.00          195.00";
	}
	*/
}
