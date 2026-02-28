package egovframework.beauty.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.beauty.service.BeautyService;

@Service("BeautyService")
public class BeautyImpl extends EgovAbstractServiceImpl implements BeautyService {

	@Resource(name = "BeautyDAO")
    private BeautyDAO beautyDao;

	@Override
	public int selectBeautyPaymanetListTotCnt(Map<String, Object> commandMap) throws Exception {
		return beautyDao.selectBeautyPaymanetListTotCnt(commandMap);
	}

	@Override
	public List<Map<String, Object>> selectBeautyPaymanetList(Map<String, Object> commandMap) throws Exception {
		return beautyDao.selectBeautyPaymanetList(commandMap);
	}

	@Override
	public int updateBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return beautyDao.updateBeautyPaymanet(commandMap);
	}

	@Override
	public int deleteBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return beautyDao.deleteBeautyPaymanet(commandMap);
	}

	@Override
	public int insertBeautyPaymanet(Map<String, Object> commandMap) throws Exception {
		return beautyDao.insertBeautyPaymanet(commandMap);
	}

	@Override
	public Map<String, Object> selectBeautyPaymanetInfo(Map<String, Object> commandMap) throws Exception {
		return beautyDao.selectBeautyPaymanetInfo(commandMap);
	}



}
