/**
 * 
 */
package parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * @author Brandon Sheffield - ISPA Technology, LLC.
 * Created on  Jan 3, 2016
 * Description:  TODO
 */
public interface CashCowParser {
	public String getParserName();
	public Boolean readInSummary(final String URL) throws MalformedURLException, IOException;
	public List<Map<String,Object>> getPricesList();
	public Map<String,Object> getMetaDataMap();
	public Map<String,Object> buildMetaDataMap(String reportName, String metaData, String locationDailySummary,
			String dateOfLiveAuctions, String cattleReceipts, String summary);
	public Map<String,Object> converToMapofData(String s);
}
