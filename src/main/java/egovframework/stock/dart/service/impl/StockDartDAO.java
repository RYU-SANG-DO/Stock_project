package egovframework.stock.dart.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("StockDartDAO")
public class StockDartDAO extends EgovComAbstractDAO {

	public List<Map<String, Object>> selectDartList(Map<String, Object> commandMap) {
		return selectList("StockDartDAO.selectDartList", commandMap);
	}
	
	public Map<String, Object> selectDetailDartMap(Map<String, Object> commandMap) {
		return selectOne("StockDartDAO.selectDetailDartMap", commandMap);
	}


}
