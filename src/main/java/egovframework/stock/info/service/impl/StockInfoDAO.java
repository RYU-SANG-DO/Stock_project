package egovframework.stock.info.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("StockInfoDAO")
public class StockInfoDAO extends EgovComAbstractDAO {

	public List<Map<String, Object>> selectMyStockList(Map<String, Object> commandMap) throws Exception{
		return selectList("StockInfoDAO.selectMyStockList", commandMap);
	}

	public int selectMyStockListTotCnt(Map<String, Object> commandMap) throws Exception{
		return selectOne("StockInfoDAO.selectMyStockListTotCnt", commandMap);
	}




}
