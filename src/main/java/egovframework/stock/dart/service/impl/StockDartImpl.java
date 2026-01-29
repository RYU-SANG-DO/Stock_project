package egovframework.stock.dart.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.dart.service.StockDartService;

@Service("StockDartService")
public class StockDartImpl extends EgovAbstractServiceImpl implements StockDartService {

	@Resource(name = "StockDartDAO")
    private StockDartDAO stockDartDao;

	@Override
	public List<Map<String, Object>> selectDartList(Map<String, Object> commandMap) throws Exception {
		return stockDartDao.selectDartList(commandMap);
	}

	@Override
	public Map<String, Object> selectDetailDartMap(Map<String, Object> commandMap) throws Exception {
		return stockDartDao.selectDetailDartMap(commandMap);
	}


}
