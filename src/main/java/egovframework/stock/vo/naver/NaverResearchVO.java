package egovframework.stock.vo.naver;

import java.util.Arrays;
import java.util.LinkedList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SuppressWarnings("serial")
public class NaverResearchVO extends ComDefaultVO{
	

	private String searchType;
	
	private String writeFromDate;
	
	private String writeToDate;
	
	private String brokerCode;
	
	private String itemCode;
	
	private String itemName;
	
	private String keyword;
	
	private String [] fieldIds = null;
	
	public String getJsonFieldIds() {
		ObjectMapper objectMapper = new ObjectMapper();
		String json = "null";
		try {
			json = objectMapper.writeValueAsString(fieldIds);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	public String toString() {
        return "NaverResearchVO{" +
        		"searchType='"+searchType+'\''+"," +
        		"writeFromDate='"+writeFromDate+'\''+"," +
        		"brokerCode='"+brokerCode+'\''+"," +
        		"itemCode='"+itemCode+'\''+"," +
        		"itemName='"+itemName+'\''+"," +
        		"keyword='"+keyword+'\''+"," +
        		"fieldIds='"+Arrays.toString(fieldIds)+'\''+"," +
        		"pageIndex='"+this.getPageIndex()+'\''+"," +
        		"firstIndex='"+this.getFirstIndex()+'\''+"," +
        		"pageSize='"+this.getPageSize()+'\''+"," +
        		"pageUnit='"+this.getPageUnit()+'\''+"," +
        		"recordCountPerPage='"+this.getRecordCountPerPage()+'\'' +
        		"stockSite='"+this.getStockSite()+'\''+"," +
                '}';
    }
}
