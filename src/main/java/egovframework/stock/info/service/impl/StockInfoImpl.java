package egovframework.stock.info.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.info.service.StockInfoService;

@Service("StockInfoService")
public class StockInfoImpl extends EgovAbstractServiceImpl implements StockInfoService {

	@Resource(name = "StockInfoDAO")
    private StockInfoDAO stockInfoDao;

	@Override
	public List<Map<String, Object>> selectMyStockList(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.selectMyStockList(commandMap);
	}

	@Override
	public int selectMyStockListTotCnt(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.selectMyStockListTotCnt(commandMap);
	}
	
	

}
