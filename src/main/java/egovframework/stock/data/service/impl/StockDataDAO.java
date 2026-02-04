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
		return selectOne("StockDataDAO.selectStocksList", stocksDataVO);
	}
}
