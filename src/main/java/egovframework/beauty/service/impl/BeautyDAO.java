package egovframework.beauty.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("BeautyDAO")
public class BeautyDAO extends EgovComAbstractDAO {

	public List<Map<String, Object>> selectBeautyPaymanetList(Map<String, Object> commandMap) throws Exception {
		return selectList("BeautyDAO.selectBeautyPaymanetList", commandMap);
	}

	public int selectBeautyPaymanetListTotCnt(Map<String, Object> commandMap) throws Exception {
		return selectOne("BeautyDAO.selectBeautyPaymanetListTotCnt", commandMap);
	}

	public int insertBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return update("BeautyDAO.insertBeautyPaymanet", commandMap);
	}

	public int updateBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return update("BeautyDAO.updateBeautyPaymanet", commandMap);
	}

	public int deleteBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return delete("BeautyDAO.deleteBeautyPaymanet", commandMap);
	}

	public Map<String, Object> selectBeautyPaymanetInfo(Map<String, Object> commandMap) throws Exception {
		return selectOne("BeautyDAO.selectBeautyPaymanetInfo", commandMap);
	}

	
}
