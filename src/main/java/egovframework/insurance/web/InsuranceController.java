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
    

    /**
	 * 보험 내역
	 * @return
	 * @throws Exception
	 */
    @IncludedInfo(name="보험 내역",order = 70100 ,gid = 700 ,keyL1="insurance" ,keyL2="list" , lv=0)
    @RequestMapping("/insurance/selectInsuranceList.do")
    public String selectInsuranceList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") BeautyVO beautyVO,  HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println("selectInsuranceList start");
		System.out.println(commandMap);
    	commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("insurance.com.title")));
		int totCnt = insuranceService.selectInsuranceListTotCnt(commandMap);
		beautyVO = pagingManageController.PagingManageVo(beautyVO, model, totCnt);
		System.out.println("FirstIndex=>"+beautyVO.getFirstIndex());
		System.out.println("RecordCountPerPage=>"+beautyVO.getRecordCountPerPage());
		String year = ComDateUtil.getToday_v01("yyyy");
		System.out.println("year=>"+year);
		String searchYear = StringUtil.nvl(commandMap.get("searchYear"),year);
		commandMap.put("searchYear", searchYear);
		
		int firstIndex = beautyVO.getFirstIndex();
		int recordCountPerPage = beautyVO.getRecordCountPerPage();
		commandMap.put("firstIndex", firstIndex);
		commandMap.put("recordCountPerPage", recordCountPerPage);
		List<Map<String, Object>> list = insuranceService.selectInsuranceList(commandMap);
		model.addAttribute("list", list);
		
		CmmnDetailCodeVO searchVO = new CmmnDetailCodeVO();
		searchVO.setFirstIndex(0);
		searchVO.setRecordCountPerPage(100);
		searchVO.setSearchCondition("1");
		searchVO.setClCode("INS");
		searchVO.setSearchKeyword("STATS");//상태
		List<CmmnDetailCodeVO> statsList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("statsList", statsList);
		
		searchVO.setSearchKeyword("INSCPY");//보험사
		List<CmmnDetailCodeVO> inscpyList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("inscpyList", inscpyList);
		
		searchVO.setSearchKeyword("BANK");//은행
		List<CmmnDetailCodeVO> bankList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("bankList", bankList);
        
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("totCnt",totCnt);
        model.addAllAttributes(commandMap);
        return "egovframework/insurance/insuranceList";
    }
    
}
