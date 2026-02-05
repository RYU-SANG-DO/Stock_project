package egovframework.stock.data.service;

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
public class StocksDataVO extends ComDefaultVO{
	

	private String type;
	
	private String no;
	
	private String listType;
	
	private String gubun;
	
	private String stocksCode;
	
	private String cl;
	
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
        return "StocksDataVO{" +
        		"type='"+type+'\''+"," +
        		"no='"+no+'\''+"," +
        		"listType='"+listType+'\''+"," +
        		"gubun='"+gubun+'\''+"," +
        		"stocksCode='"+stocksCode+'\''+"," +
        		"searchKeyword='"+this.getSearchKeyword()+'\''+"," +
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
