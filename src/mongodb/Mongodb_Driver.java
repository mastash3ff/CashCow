package mongodb;

import java.net.UnknownHostException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import static java.util.Arrays.asList;

public class Mongodb_Driver {
	final static String REPORT = "Report";
	final static String LOCATION = "Location";
	final static String DATE = "Date";
	final static String USDA_NEWS = "USDA News";
	final static String LOCATION_DAILY_SUMMARY = "Location Daily Summary";
	final static String DATE_OF_LIVE_AUCTIONS = "Date of Live Auctions";
	final static String RECEIPTS = "Receipts";
	final static String SUMMARY = "Summary";
	final static String FEEDER_HEIFERS = "Feeder Heifers";
	final static String HEAD = "Head";
	final static String WT_RNG = "Wt Range";
	final static String AVG_WT = "Avg Wt";
	final static String PRICE_RNG = "Price Range";
	final static String AVG_PRICE = "Avg Price";
	
	static MongoClient mongoClient = new MongoClient();
	static MongoDatabase db = mongoClient.getDatabase("cashcow");
	
	/**
	 * Parse weekly data
	 */
	public static void insertWeeklySummary(){
		
	}

	/**
	 * @param prices_list 
	 * @param args
	 * @throws UnknownHostException 
	 * @throws ParseException 
	 */	
	public static void insertDailyData(Map<String, Object> metadata_map, ArrayList<Map<String, Object>> prices_list){
		String report = (String) metadata_map.get(REPORT);
		String location = (String) metadata_map.get(LOCATION);
		String date = (String) metadata_map.get(DATE);
		String usdaNews = (String) metadata_map.get(USDA_NEWS);
		String locationDailySummary = (String) metadata_map.get(LOCATION_DAILY_SUMMARY);
		String dateOfLiveAuctions = (String) metadata_map.get(DATE_OF_LIVE_AUCTIONS);
		String receipts = (String) metadata_map.get(RECEIPTS);
		String summary = (String) metadata_map.get(SUMMARY);

		db.getCollection("daily_reports").insertOne(new Document()
				.append(REPORT, report)
				.append(LOCATION, location)
				.append(DATE,date)
				.append(USDA_NEWS, usdaNews)
				.append(LOCATION_DAILY_SUMMARY, locationDailySummary)
				.append(DATE_OF_LIVE_AUCTIONS, dateOfLiveAuctions)
				.append(RECEIPTS, receipts)
				.append(SUMMARY, summary)
				.append(FEEDER_HEIFERS, asList(prices_list.toArray())
						));
	}

	/**
	 * Drop everything in the daily reports
	 */
	public static void dropAllData(){
		db.getCollection("daily_reports").drop();
	}
	
	public static void dropWeeklyData(){
		db.getCollection("weekly_reports").drop();
	}
	/*
	public static void testInsert(){
		db.getCollection("daily_reports").insertOne(
				new Document().append("Date", "Some Date Value")
				.append("Place", "Some Place")
				.append("Receipts", "some receipts")
				.append("Receipts", "some receipts")
				.append("Summary", "some summary")
				.append("Feeder Heifers", asList(
						new Document().append("Head", 6)
						.append("Wt Rng", 66)
						.append("Avg Wt", "666 String"), asList(
								new Document().append("Head", 5)
								.append("Wt Rng", 55)))));
	}
	*/
}
