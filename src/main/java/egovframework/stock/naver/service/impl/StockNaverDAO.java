package egovframework.stock.naver.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.stock.vo.naver.NaverResearchCrawVO;

@Repository("StockNaverDAO")
public class StockNaverDAO extends EgovComAbstractDAO {

	//주식 리서치/리포트 상세
	public Map<String, Object> selectStockResearchDataDetail(Map<String, Object> paramMap) {
		return selectOne("StockNaverDAO.selectStockResearchDataDetail" , paramMap);
	}

	//주식 리서치/리포트 등록
	public int insertStockResearchData(Map<String, Object> paramMap) {
		return update("StockNaverDAO.insertStockResearchData" , paramMap);
	}

	//주식 리서치/리포트 수정
	public int updateStockResearchData(Map<String, Object> paramMap) {
		return update("StockNaverDAO.updateStockResearchData" , paramMap);
	}

	//주식 리서치/리포트 삭제
	public int deleteStockResearchData(Map<String, Object> paramMap) {
		return delete("StockNaverDAO.deleteStockResearchData" , paramMap);
	}



}
