package egovframework.stock.com.service;

import java.util.List;
import java.util.Map;

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
public interface StockComService{

	//테마 내역 조회
	List<Map<String, Object>> selectComThemeCodeList(Map<String, Object> map) throws Exception;
	//테마 상세 조회
    Map<String, Object> selectComThemeCodeDetail(Map<String, Object> map) throws Exception;
    //테마 등록
    void insertComThemeCode(Map<String, Object> map) throws Exception;
    //테마 수정
    int updateComThemeCode(Map<String, Object> map) throws Exception;
    //테마 삭제
    int deleteComThemeCode(Map<String, Object> map) throws Exception;
}
