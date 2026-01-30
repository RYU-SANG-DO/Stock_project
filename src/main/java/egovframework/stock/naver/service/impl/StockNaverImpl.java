package egovframework.stock.naver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.com.naver.naverUtil;
import egovframework.stock.dart.service.StockDartService;
import egovframework.stock.naver.service.StockNaverService;
import egovframework.stock.vo.naver.NaverResearchCrawVO;

@Service("StockNaverService")
public class StockNaverImpl extends EgovAbstractServiceImpl implements StockNaverService {

	@Resource(name = "StockNaverDAO")
    private StockNaverDAO stockNaverDao;
	
	//주식 리서치/리포트 등록
	@Override
	public int insertStockResearchData(Map<String, Object> paramMap) throws Exception {
		int cnt = stockNaverDao.insertStockResearchData(paramMap);
		System.out.println("cnt=>"+cnt);
		return cnt;
	}

	//주식 리서치/리포트 수정
	@Override
	public int updateStockResearchData(Map<String, Object> paramMap) throws Exception {
		return stockNaverDao.updateStockResearchData(paramMap);
	}

	//주식 리서치/리포트 삭제
	@Override
	public int deleteStockResearchData(Map<String, Object> paramMap) throws Exception {
		return stockNaverDao.deleteStockResearchData(paramMap);
	}
		

}
