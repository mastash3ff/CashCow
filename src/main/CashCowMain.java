package main;
import java.io.IOException;
import java.util.Map;

import mongodb.Mongodb_Driver;
import parser.CashCowParser;
import parser.DailyParser;
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

		final String WEEKLY_URL = "https://www.ams.usda.gov/mnreports/mg_ls145.txt";
		final String DAILY_URL = "https://www.ams.usda.gov/mnreports/mg_ls144.txt";
		
		CashCowParser dailyParser = new DailyParser();
		Boolean wasDailyRead = dailyParser.readInSummary(DAILY_URL);
		
		//CashCowParser weeklyParser = new WeeklyParser();
		//Boolean wasRead = weeklyParser.readInSummary(URL); //TODO
		//System.out.println(weeklyParser.getParserName());
		//System.out.println(weeklyParser.converToMapofData(weeklyParser.testWeeklyData()));
		
		
		//Mongodb_Driver.dropAllData();
		/*
		if (wasRead)
			Mongodb_Driver.insertWeeklyData(weeklyParser.getMetaDataMap(), weeklyParser.getPricesList());
		else
			System.err.println("Could not read data!");
			*/
		if (wasDailyRead)
			Mongodb_Driver.insertWeeklyData(dailyParser.getMetaDataMap(), dailyParser.getPricesList());
		else
			System.err.println("Could not read data!");
	}
}
