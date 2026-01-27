package egovframework.stock.info.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.com.naver.naverUtil;
import egovframework.stock.dart.service.StockDartService;
import egovframework.stock.info.service.StockInfoService;
import egovframework.stock.naver.service.StockNaverService;
import egovframework.stock.vo.naver.NaverResearchCrawVO;

@Service("StockInfoService")
public class StockInfoImpl extends EgovAbstractServiceImpl implements StockInfoService {

	@Resource(name = "StockInfoDAO")
    private StockInfoDAO stockInfoDao;
	
	

}
