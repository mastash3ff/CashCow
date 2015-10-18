import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import mongodb.Mongodb_Driver;
import parser.CowParser;
import utils.Dumps;

/**
 * 
 * @author Brandon Sheffield
 * 
 * Cash main driver that reads daily cattle rates.  Passes it to
 * database for storage.
 */
public class CashCowMain {
	private static Map<String, Object> metaData_map = new HashMap<>();
	private static ArrayList<Map<String, Object>> prices_list = new ArrayList<>();


	public static void main(String[] args) throws IOException {
		readInFile();
		Mongodb_Driver.insertDailyData(metaData_map, prices_list);
	}
	
	//reads in cattle data and hands off to parse to return useful data
	public static void readInFile() throws IOException{
		//TODO eventually do stuff with these 
		String reportName;
		String metaData;
		String locationDailySummary;
		String dateOfLiveAuctions;
		String cattleReceipts;
		String description;
		String line = "";
		String headers;//TODO currently no use. keep for now.
		final String DAILY_WEB_LINK = "http://www.ams.usda.gov/mnreports/mg_ls144.txt";
		
		//TODO need to change static http reference eventually
		File f = new File("./src/read_this");
		BufferedReader in = new BufferedReader(new FileReader(f));
		
		reportName = in.readLine();
		metaData = in.readLine();
		
		in.readLine(); //blank line throwaway
		locationDailySummary = in.readLine();
		dateOfLiveAuctions = in.readLine();
		in.readLine();
		cattleReceipts = in.readLine();
		in.readLine();
		
		StringBuilder descripStr = new StringBuilder();
		line = in.readLine();
		descripStr.append(line);
		while (!line.matches(""))
			descripStr.append(line = in.readLine().trim());
		
		description = descripStr.toString();	
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
}
