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
    private InsuranceDAO ㅑnsuranceDao;

	

}
