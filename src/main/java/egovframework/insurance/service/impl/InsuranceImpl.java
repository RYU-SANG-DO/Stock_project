package egovframework.insurance.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.beauty.service.BeautyService;
import egovframework.insurance.service.InsuranceService;

@Service("InsuranceService")
public class InsuranceImpl extends EgovAbstractServiceImpl implements InsuranceService {

	@Resource(name = "InsuranceDAO")
    private InsuranceDAO insuranceDao;

	@Override
	public int selectInsuranceListTotCnt(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.selectInsuranceListTotCnt(commandMap);
	}

	@Override
	public List<Map<String, Object>> selectInsuranceList(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.selectInsuranceList(commandMap);
	}

	@Override
	public Map<String, Object> selectMyInsuranceDetail(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.selectMyInsuranceDetail(commandMap);
	}

	@Override
	public int insertInsurance(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.insertInsurance(commandMap);
	}

	@Override
	public int updateInsurance(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.updateInsurance(commandMap);
	}

	@Override
	public int deleteInsurance(Map<String, Object> commandMap) throws Exception {
		return insuranceDao.deleteInsurance(commandMap);
	}

	

}
