package egovframework.stock.data.service;

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
public interface StockDataService{

	List<Map<String, Object>> selectStocksList(StocksDataVO stocksDataVO) throws Exception;

	int selectStocksListTotCnt(StocksDataVO stocksDataVO) throws Exception;

	//종목 정보 상세 조회
	Map<String, Object> selectStocksDetail(StocksDataVO stocksDataVO) throws Exception;

	int insertStocksInfo(Map<String, Object> commandMap) throws Exception;

	int updateStocksInfo(Map<String, Object> commandMap) throws Exception;

	int deleteStocksInfo(Map<String, Object> commandMap) throws Exception;

	int selectStockHistListTotCnt(Map<String, Object> commandMap) throws Exception;

	List<Map<String, Object>> selectStockHistList(Map<String, Object> commandMap) throws Exception;

	Map<String, Object> selectStockHistDetail(Map<String, Object> commandMap) throws Exception;

	int insertStockHist(Map<String, Object> commandMap) throws Exception;

	int updateStockHist(Map<String, Object> commandMap) throws Exception;

	int deleteStockHist(Map<String, Object> commandMap) throws Exception;

}
