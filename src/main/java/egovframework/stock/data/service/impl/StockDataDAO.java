package egovframework.stock.data.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.stock.data.service.StocksDataVO;

@Repository("StockDataDAO")
public class StockDataDAO extends EgovComAbstractDAO {

	public List<Map<String, Object>> selectStocksList(StocksDataVO stocksDataVO) throws Exception{
		return selectList("StockDataDAO.selectStocksList", stocksDataVO);
	}

	public int selectStocksListTotCnt(StocksDataVO stocksDataVO) throws Exception{
	    return (Integer)selectOne("StockDataDAO.selectStocksListTotCnt", stocksDataVO);
	}

	//종목 정보 상세 조회
	public Map<String, Object> selectStocksDetail(StocksDataVO stocksDataVO) throws Exception{
		return selectOne("StockDataDAO.selectStocksDetail", stocksDataVO);
	}

	public int insertStocksInfo(Map<String, Object> commandMap) throws Exception{
		return update("StockDataDAO.insertStocksInfo", commandMap);
	}

	public int updateStocksInfo(Map<String, Object> commandMap) throws Exception{
		return update("StockDataDAO.updateStocksInfo", commandMap);
	}

	public int deleteStocksInfo(Map<String, Object> commandMap) throws Exception{
		return delete("StockDataDAO.deleteStocksInfo", commandMap);
	}

	public int selectStockHistListTotCnt(Map<String, Object> commandMap) throws Exception{
		return selectOne("StockDataDAO.selectStockHistListTotCnt", commandMap);
	}

	public List<Map<String, Object>> selectStockHistList(Map<String, Object> commandMap) throws Exception{
		return selectList("StockDataDAO.selectStockHistList", commandMap);
	}

	//이력 상세 페이지
	public Map<String, Object> selectStockHistDetail(Map<String, Object> commandMap) throws Exception{
		return selectOne("StockDataDAO.selectStockHistDetail", commandMap);
	}
}
