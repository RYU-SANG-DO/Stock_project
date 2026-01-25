package egovframework.stock.com.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.com.service.StockComService;
import egovframework.stock.dart.service.StockDartService;

@Service("StockComService")
public class StockComImpl extends EgovAbstractServiceImpl implements StockComService {

	@Resource(name = "StockComDAO")
    private StockComDAO stockComDao;

	//테마 내역 조회
	@Override
	public List<Map<String, Object>> selectComThemeCodeList(Map<String, Object> map) throws Exception {
		return stockComDao.selectComThemeCodeList(map);
	}

	//테마 상세 조회
	@Override
	public Map<String, Object> selectComThemeCodeDetail(Map<String, Object> map) throws Exception {
		return stockComDao.selectComThemeCodeDetail(map);
	}

	//테마 등록
	@Override
	public void insertComThemeCode(Map<String, Object> map) throws Exception {
		stockComDao.insertComThemeCode(map);
	}

	//테마 수정
	@Override
	public int updateComThemeCode(Map<String, Object> map) throws Exception {
		return stockComDao.updateComThemeCode(map);
	}

	//테마 삭제
	@Override
	public int deleteComThemeCode(Map<String, Object> map) throws Exception {
		return stockComDao.deleteComThemeCode(map);
	}



}
