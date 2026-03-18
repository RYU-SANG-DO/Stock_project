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

	
	
}
