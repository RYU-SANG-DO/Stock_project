package egovframework.stock.com.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.stock.com.service.StockComService;
import egovframework.stock.dart.service.StockDartService;

@Service("StockComService")
public class StockComImpl extends EgovAbstractServiceImpl implements StockComService {

	@Resource(name = "StockComDAO")
    private StockComDAO stockComDao;



}
