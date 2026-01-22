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


}
