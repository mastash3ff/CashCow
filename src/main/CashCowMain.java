package main;
import java.io.IOException;
import java.util.Map;

import mongodb.Mongodb_Driver;
import parser.CashCowParser;
import parser.WeeklyParser;

/**
 * 
 * @author Brandon Sheffield
 * 
 * Cash cow main driver that reads cattle rates.  Passes it to
 * database for storage.
 */
public class CashCowMain {

	public static void main(String[] args) throws IOException {

		final String URL = "http://www.ams.usda.gov/mnreports/mg_ls145.txt";
		CashCowParser weeklyParser = new WeeklyParser();
		//CashCowParser dailySummary = new DailyParser(); //TODO
		
		weeklyParser.readInSummary(URL); //TODO
		//System.out.println(weeklyParser.getParserName());
		//System.out.println(weeklyParser.converToMapofData(weeklyParser.testWeeklyData()));
		//testing
		//Mongodb_Driver.dropAllData();
		Mongodb_Driver.insertWeeklyData(weeklyParser.getMetaDataMap(), weeklyParser.getPricesList());
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
