package egovframework.stock.info.service;

import java.util.List;
import java.util.Map;

/**
 *  My Stock
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2026.01.27		최초 생성
 *
 * </pre>
 */
public interface StockInfoService{
	
	List<Map<String, Object>> selectMyStockList(Map<String, Object> commandMap) throws Exception;

	int selectMyStockListTotCnt(Map<String, Object> commandMap) throws Exception;

	//거래내역 등록
	void insertMyStock(Map<String, Object> commandMap) throws Exception;

	//거래내역 수정
	int updateMyStock(Map<String, Object> commandMap) throws Exception;

	//거래내역 삭제
	int deleteMyStock(Map<String, Object> commandMap) throws Exception;

	//거래내역 상세 조회
	Map<String, Object> selectMyStockDetail(Map<String, Object> commandMap) throws Exception;

}
