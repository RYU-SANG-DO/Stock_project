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
public class NaverThemeVO extends ComDefaultVO{
	

	private String type;
	
	private String no;
	
	private String listType;
	
	private String gubun;
	
	private String searchKeyword;
	
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
        return "NaverThemeVO{" +
        		"type='"+type+'\''+"," +
        		"no='"+no+'\''+"," +
        		"listType='"+listType+'\''+"," +
        		"gubun='"+gubun+'\''+"," +
        		"searchKeyword='"+searchKeyword+'\''+"," +
        		"fieldIds='"+Arrays.toString(fieldIds)+'\''+"," +
        		"pageIndex='"+this.getPageIndex()+'\''+"," +
        		"firstIndex='"+this.getFirstIndex()+'\''+"," +
        		"pageSize='"+this.getPageSize()+'\''+"," +
        		"pageUnit='"+this.getPageUnit()+'\''+"," +
        		"recordCountPerPage='"+this.getRecordCountPerPage()+'\''+"," +
        		"stockSite='"+this.getStockSite()+'\'' +
                '}';
    }
}
