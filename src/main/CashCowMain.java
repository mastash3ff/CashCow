package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import mongodb.Mongodb_Driver;
import parser.CowParser;

/**
 * 
 * @author Brandon Sheffield
 * 
 * Cash cow main driver that reads cattle rates.  Passes it to
 * database for storage.
 */
public class CashCowMain {
	private static Map<String, Object> metaData_map = new HashMap<>();
	private static ArrayList<Map<String, Object>> prices_list = new ArrayList<>();


	public static void main(String[] args) throws IOException {

		readInDailyData(); //reads data from web text file and parses

		//testing
		//Mongodb_Driver.dropAllData();

		Mongodb_Driver.insertDailyData(metaData_map, prices_list);
	}

	public static void readInWeeklySummary() throws IOException{

		final String URL = "http://www.ams.usda.gov/mnreports/mg_ls145.txt";
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
		metaData_map.clear();
		metaData_map = CowParser.buildMetaDataMap(reportName, metaData, locationDailySummary, dateOfLiveAuctions, null, description);
		
	}
	//weekly summary url http://www.ams.usda.gov/mnreports/mg_ls145.txt
	/**
       reads in cattle data and hands off to parse to return useful data
	 */
	public static void readInDailyData() throws IOException{
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
		metaData_map.clear();
		metaData_map = CowParser.buildMetaDataMap(reportName, metaData,locationDailySummary,
				dateOfLiveAuctions,cattleReceipts,description);

		while (line!=null){
			line = line.trim(); //matches messed up with leading whitespace

			//TODO eventually parse more than just the lazy heifers
			if (line.matches("Feeder Heifers Medium and Large 1")){

				headers = in.readLine();
				line = in.readLine().trim();

				//read in heifer data until blankline
				while(!line.matches("")){
					prices_list.add(CowParser.getFeederHeiferData(line));
					line = in.readLine().trim();
				}
			}
			line = in.readLine();
		}
		in.close();
	}
	/*	
	private static boolean isWeekend(){
		Calendar cal = Calendar.getInstance();
		Integer today = cal.get(Calendar.DAY_OF_WEEK);
		//1 and 7 represent Sunday and Saturday respectively
		if (today == 1 || today == 7)
			return true;
		else
			return false;

	}
	 */
}
