package egovframework.stock.naver.service;

import java.util.List;
import java.util.Map;

/**
 *  DART
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2009.03.12		최초 생성
 *
 * </pre>
 */
public interface StockNaverService{

	//주식 리서치/리포트 등록
	int insertStockResearchData(Map<String, Object> paramMap) throws Exception;
	
	//주식 리서치/리포트 수정
	int updateStockResearchData(Map<String, Object> paramMap) throws Exception;
	
	//주식 리서치/리포트 삭제
	int deleteStockResearchData(Map<String, Object> paramMap) throws Exception;
}
