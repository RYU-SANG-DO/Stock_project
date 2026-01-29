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

	//거래내역 등록
	public void insertMyStock(Map<String, Object> commandMap) throws Exception{
		insert("StockInfoDAO.insertMyStock", commandMap);
	}

	//거래내역 수정
	public int updateMyStock(Map<String, Object> commandMap) throws Exception{
		return update("StockInfoDAO.updateMyStock", commandMap);
	}

	//거래내역 삭제
	public int deleteMyStock(Map<String, Object> commandMap) throws Exception{
		return delete("StockInfoDAO.deleteMyStock", commandMap);
	}

	//거래내역 상세 조회
	public Map<String, Object> selectMyStockDetail(Map<String, Object> commandMap) throws Exception{
		return selectOne("StockInfoDAO.selectMyStockDetail", commandMap);
	}




}
