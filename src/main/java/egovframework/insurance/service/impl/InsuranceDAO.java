package egovframework.insurance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("InsuranceDAO")
public class InsuranceDAO extends EgovComAbstractDAO {

	public int selectInsuranceListTotCnt(Map<String, Object> commandMap) throws Exception{
		return selectOne("InsuranceDAO.selectInsuranceListTotCnt" , commandMap);
	}

	public List<Map<String, Object>> selectInsuranceList(Map<String, Object> commandMap) throws Exception{
		return selectList("InsuranceDAO.selectInsuranceList" , commandMap);
	}

	public Map<String, Object> selectMyInsuranceDetail(Map<String, Object> commandMap) throws Exception{
		return selectOne("InsuranceDAO.selectMyInsuranceDetail" , commandMap);
	}

	public int insertInsurance(Map<String, Object> commandMap) throws Exception{
		return update("InsuranceDAO.insertInsurance", commandMap);
	}

	public int updateInsurance(Map<String, Object> commandMap) throws Exception{
		return update("InsuranceDAO.updateInsurance", commandMap);
	}

	public int deleteInsurance(Map<String, Object> commandMap) throws Exception{
		return delete("InsuranceDAO.deleteInsurance", commandMap);
	}

	
	
}
