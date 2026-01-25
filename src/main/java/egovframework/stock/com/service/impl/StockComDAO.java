package egovframework.stock.com.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("StockComDAO")
public class StockComDAO extends EgovComAbstractDAO {

	//테마 내역 조회
	public List<Map<String, Object>> selectComThemeCodeList(Map<String, Object> map) throws Exception{
		return selectList("StockComDAO.selectComThemeCodeList", map);
	}
	//테마 상세 조회
	public Map<String, Object> selectComThemeCodeDetail(Map<String, Object> map) throws Exception{
		return selectOne("StockComDAO.selectComThemeCodeDetail", map);
	}
	//테마 등록
	public void insertComThemeCode(Map<String, Object> map) throws Exception{
		insert("StockComDAO.insertComThemeCode", map);
		
	}
	//테마 수정
	public int updateComThemeCode(Map<String, Object> map) throws Exception{
		return update("StockComDAO.updateComThemeCode", map);
	}
	//테마 삭제
	public int deleteComThemeCode(Map<String, Object> map) throws Exception{
		return delete("StockComDAO.deleteComThemeCode", map);
	}



}
