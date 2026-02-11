package egovframework.stock.com.web;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.service.EgovProperties;
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
    
    @RequestMapping("/stock/com/uploadEditorImageAjax.do")
    public ModelAndView uploadEditorImageAjax(@RequestParam Map<?, ?> commandMap, final HttpServletRequest request, ModelMap model) {
    	System.out.println("uploadEditorImage start");
    	ModelAndView mav = new ModelAndView("jsonView");
    	// 1. 저장 경로 설정
    	String uploadWebDir = EgovProperties.getProperty("Globals.fileStorePath")+"upload/images/";
        String uploadPath = request.getServletContext().getRealPath("upload/images/");
        File folder = new File(uploadPath);
        File webfolder = new File(uploadWebDir);
        if (!folder.exists()) folder.mkdirs();
        if (!webfolder.exists()) webfolder.mkdirs();

        try {
        	final MultipartHttpServletRequest multiRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
        	// input name="imageFile" (또는 AJAX에서 보낸 이름)로 파일 가져오기
            MultipartFile file = multiRequest.getFile("imageFile");
            if (file != null && !file.isEmpty()) {
                String originalName = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalName;
                
                File destination = new File(uploadPath + File.separator + fileName);
                file.transferTo(destination);
                
                File webdestination = new File(uploadWebDir + File.separator + fileName);
                file.transferTo(webdestination);
                
                String saveUrl = request.getContextPath() + "/upload/images/" + fileName;
                System.out.println("저장 경로: " + saveUrl);
                
                mav.addObject("url", saveUrl);
                mav.addObject("uploaded", 1);
            } else {
                mav.addObject("uploaded", 0);
                mav.addObject("error", "파일이 전송되지 않았습니다.");
            }
        } catch (Exception e) {
        	e.printStackTrace(); // 로그 확인용
            mav.addObject("uploaded", 0);
            mav.addObject("error", e.getMessage());
        }
        System.out.println("uploadEditorImage end");
        return mav;
    }
	
}
