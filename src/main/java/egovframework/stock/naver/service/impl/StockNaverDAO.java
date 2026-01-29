package egovframework.stock.naver.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.stock.vo.naver.NaverResearchCrawVO;

@Repository("StockNaverDAO")
public class StockNaverDAO extends EgovComAbstractDAO {

	public void insertNaverResearchInfo(NaverResearchCrawVO vo) {
		// MERGE 쿼리 사용시 insert 대신 update 사용 가능, 여기선 insert 호출
        insert("StockNaverDAO.insertNaverResearchInfo", vo);
		
	}



}
