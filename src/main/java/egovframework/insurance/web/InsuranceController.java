package egovframework.insurance.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.beauty.service.BeautyVO;
import egovframework.beauty.service.BeautyService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.web.PagingManageController;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.insurance.service.InsuranceService;
import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.StringUtil;

@Controller
public class InsuranceController {

	@Autowired
	private InsuranceService insuranceService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    @Resource(name = "pagingManageController")
   	private PagingManageController pagingManageController;
    
    @Resource(name = "CmmnDetailCodeManageService")
	private EgovCcmCmmnDetailCodeManageService cmmnDetailCodeManageService;
    

   
    
}
