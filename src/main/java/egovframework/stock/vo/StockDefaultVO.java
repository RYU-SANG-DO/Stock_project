package egovframework.stock.vo;

import java.util.Arrays;
import java.util.LinkedList;

import egovframework.com.cmm.ComDefaultVO;

@SuppressWarnings("serial")
public class StockDefaultVO extends ComDefaultVO{
	

	/** 항목제목 *///																				0		1			2		3		  4			 5		  6			 7			8		  9	     (10	 11	  12	  13	   14		15		16		17		18				19			20		)	
    private LinkedList<String> stockTitles = new LinkedList<>(Arrays.asList("순번","회사명","종목코드","업종","주요제품","상장일","결산월","대표자명","홈페이지","지역","종목", "주가","등락률","시가", "고가", "저가","거래량","상태","전일대비","가져오는 시간","회사정보"));
    
    /** 항목 사용여부 *///																				0		1		2		3		4		5		6		7		 8			9	(10		11		12		13		14		  15		16		17		18		19		20)						
    private LinkedList<Boolean> stockUserYn = new LinkedList<>(Arrays.asList(true, true, true, true, true, false, false, false, true, false, false, true, true, false, false, false, true, true, true, true, false));
    

	public LinkedList<String> getStockTitles() {
		return stockTitles;
	}

	public void setStockTitles(LinkedList<String> stockTitles) {
		this.stockTitles = stockTitles;
	}
	
	public void setStockTitles(String title) {		
		this.stockTitles.add(title);
	}
	
	public LinkedList<Boolean> getStockUserYn() {
		return stockUserYn;
	}

	public void setStockUserYn(LinkedList<Boolean> stockUserYn) {
		this.stockUserYn = stockUserYn;
	}
	
	public void setStockUserYn(Boolean userYn) {
		this.stockUserYn.add(userYn);
	}

}
