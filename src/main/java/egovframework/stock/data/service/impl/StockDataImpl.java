package egovframework.stock.data.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.dart.service.StockDartService;
import egovframework.stock.data.service.StockDataService;
import egovframework.stock.data.service.StocksDataVO;

@Service("StockDataService")
public class StockDataImpl extends EgovAbstractServiceImpl implements StockDataService {

	@Resource(name = "StockDataDAO")
    private StockDataDAO stockDataDao;

	@Override
	public List<Map<String, Object>> selectStocksList(StocksDataVO stocksDataVO) throws Exception {
		return stockDataDao.selectStocksList(stocksDataVO);
	}

	@Override
	public int selectStocksListTotCnt(StocksDataVO stocksDataVO) throws Exception {
		return stockDataDao.selectStocksListTotCnt(stocksDataVO);
	}

	//종목 정보 상세 조회
	@Override
	public Map<String, Object> selectStocksDetail(StocksDataVO stocksDataVO) throws Exception {
		return stockDataDao.selectStocksDetail(stocksDataVO);
	}

	@Override
	public int insertStocksInfo(Map<String, Object> commandMap) throws Exception {
		return stockDataDao.insertStocksInfo(commandMap);
	}

	@Override
	public int updateStocksInfo(Map<String, Object> commandMap) throws Exception {
		return stockDataDao.updateStocksInfo(commandMap);
	}

	@Override
	public int deleteStocksInfo(Map<String, Object> commandMap) throws Exception {
		return stockDataDao.deleteStocksInfo(commandMap);
	}


}
