package egovframework.stock.ecnmy.service.impl;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.ecnmy.service.EconomyIdxService;

@Service("EconomyIdxService")
public class EconomyIdxImpl extends EgovAbstractServiceImpl implements EconomyIdxService {

	@Resource(name = "EconomyIdxDAO")
    private EconomyIdxDAO economyIdxDao;

	
}
