package egovframework.insurance.service;

import java.util.List;
import java.util.Map;

/**
 *  보험
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2026.02.28		최초 생성
 *
 * </pre>
 */
public interface InsuranceService{

	int selectInsuranceListTotCnt(Map<String, Object> commandMap) throws Exception;

	List<Map<String, Object>> selectInsuranceList(Map<String, Object> commandMap) throws Exception;

	Map<String, Object> selectMyInsuranceDetail(Map<String, Object> commandMap) throws Exception;

	int insertInsurance(Map<String, Object> commandMap) throws Exception;

	int updateInsurance(Map<String, Object> commandMap) throws Exception;

	int deleteInsurance(Map<String, Object> commandMap) throws Exception;



}
