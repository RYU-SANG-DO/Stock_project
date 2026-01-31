package egovframework.stock.naver.service;

import java.util.List;
import java.util.Map;

import egovframework.stock.vo.naver.NaverResearchVO;

/**
 *  DART
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2009.03.12		최초 생성
 *
 * </pre>
 */
public interface StockNaverService{

	//주식 리서치/리포트 등록
	int insertStockResearchData(Map<String, Object> paramMap) throws Exception;
	
	//주식 리서치/리포트 수정
	int updateStockResearchData(Map<String, Object> paramMap) throws Exception;
	
	//주식 리서치/리포트 삭제
	int deleteStockResearchData(Map<String, Object> paramMap) throws Exception;

	//My resserch List 건수
	int selectStockResearchDataListTotCnt(Map<String, Object> commandMap) throws Exception;

	//My resserch List
	List<Map<String, Object>> selectStockResearchDataList(Map<String, Object> commandMap) throws Exception;
	//My resserch 상세 조회
	Map<String, Object> selectStockResearchDataDetail(Map<String, Object> commandMap) throws Exception;
}
