package egovframework.stock.com.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.sym.ccm.cca.service.CmmnCodeVO;
import egovframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.service.StockComService;
import egovframework.stock.dart.service.StockDartService;

@Controller
public class StockComController {

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    /** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	@Resource(name = "StockComService")
    private StockComService stockComService;
	
	@Resource(name = "CmmnCodeManageService")
    private EgovCcmCmmnCodeManageService cmmnCodeManageService;

	/**
	 * 주식 공통
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="주식 공통",order = 10000 ,gid = 200 ,keyL1="stock" ,keyL2="com" ,lv=0)
    @RequestMapping("/stock/com/selectComList.do")
    public String selectComList(@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		String apiGrpCd = StringUtil.nvl(commandMap.get("apiGrpCd"),"DS001");
		commandMap.put("apiGrpCd", apiGrpCd);
		System.out.println(commandMap);

		model.addAllAttributes(commandMap);
        return "forward:/stock/com/theme/selectComThemeCodeList.do";
    }
	
	/**
	 * 주식 테마 코드 관리
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="테마 코드 관리",order = 10010 ,gid = 200 ,keyL1="stock" ,keyL2="com" ,lv=1)
    @RequestMapping("/stock/com/theme/selectComThemeCodeList.do")
    public String selectComThemeCodeList(@ModelAttribute("searchVO") CmmnCodeVO searchVO ,@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
//		/** EgovPropertyService.sample */
//		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
//		searchVO.setPageSize(propertiesService.getInt("pageSize"));
//
//		/** pageing */
//		PaginationInfo paginationInfo = new PaginationInfo();
//		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
//		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
//		paginationInfo.setPageSize(searchVO.getPageSize());
//
//		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
//		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
//		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<Map<String, Object>> resultList = stockComService.selectComThemeCodeList(commandMap);
        model.addAttribute("resultList", resultList);

//		int totCnt = stockComService.selectComThemeCodeListTotCnt(commandMap);
//		paginationInfo.setTotalRecordCount(totCnt);
//		model.addAttribute("paginationInfo", paginationInfo);
        return "egovframework/stock/com/theme/stockComThemeList";
    }
	
	@RequestMapping("/stock/com/theme/stockComThemeCodeRegist.do")
    public String stockComThemeCodeRegist(@ModelAttribute("searchVO") CmmnCodeVO searchVO ,@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		String sCmd = (String) commandMap.get("cmd");
        String resultMsg = "";

        if ("insert".equals(sCmd)) {
        	stockComService.insertComThemeCode(commandMap);
            resultMsg = "등록되었습니다.";
        } else if ("update".equals(sCmd)) {
        	stockComService.updateComThemeCode(commandMap);
            resultMsg = "수정되었습니다.";
        } else if ("delete".equals(sCmd)) {
        	stockComService.deleteComThemeCode(commandMap);
            resultMsg = "삭제되었습니다.";
        }
        
        return "redirect:/stock/com/theme/selectComThemeCodeList.do?resultMsg=" + java.net.URLEncoder.encode(resultMsg, "UTF-8");
    }

	/**
	 * 상위 테마 ID 조회 팝업 
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/com/theme/selectComThemeCodeListPop.do")
    public String selectComThemeCodeListPop(@ModelAttribute("searchVO") CmmnCodeVO searchVO ,@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {

		List<Map<String, Object>> resultList = stockComService.selectComThemeCodeList(commandMap);
        model.addAttribute("resultList", resultList);

        return "egovframework/stock/com/theme/stockComThemeListPop";
    }
	
}
