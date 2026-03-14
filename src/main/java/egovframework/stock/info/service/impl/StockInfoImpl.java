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

	//거래내역 등록
	@Override
	public void insertMyStock(Map<String, Object> commandMap) throws Exception {
		stockInfoDao.insertMyStock(commandMap);
	}

	//거래내역 수정
	@Override
	public int updateMyStock(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.updateMyStock(commandMap);
	}

	//거래내역 삭제
	@Override
	public int deleteMyStock(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.deleteMyStock(commandMap);
	}

	//거래내역 상세 조회
	@Override
	public Map<String, Object> selectMyStockDetail(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.selectMyStockDetail(commandMap);
	}

	@Override
	public List<Map<String, Object>> selectMyStockHistList(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.selectMyStockHistList(commandMap);
	}

	//거내 이력 상세 조회
	@Override
	public Map<String, Object> selectMyStockHistDetail(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.selectMyStockHistDetail(commandMap);
	}

	@Override
	public void insertMyStockHist(Map<String, Object> commandMap) throws Exception {
		stockInfoDao.insertMyStockHist(commandMap);
	}

	@Override
	public int updateMyStockHist(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.updateMyStockHist(commandMap);
	}

	@Override
	public int deleteMyStockHist(Map<String, Object> commandMap) throws Exception {
		return stockInfoDao.deleteMyStockHist(commandMap);
	}


}
