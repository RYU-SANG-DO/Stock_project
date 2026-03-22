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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.beauty.service.BeautyVO;
import egovframework.beauty.service.BeautyService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
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
	
	@Resource(name = "EgovFileMngService")
    private EgovFileMngService fileMngService;

    @Resource(name = "EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

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
    
    /**
   	 * My 보험 등록 , 수정 화면
   	 * @return
   	 * @throws Exception
   	 */
       @RequestMapping("/insurance/moveMyInsurance.do")
       public String moveMyInsurance(@RequestParam Map<String, Object> reqParamMap, 
       		HttpServletRequest request,
       		ModelMap model) throws Exception {
   		System.out.println("My 보험 등록/수정 화면 시작");
   		String returnUrl="egovframework/insurance/insuranceUpdt";
   		Map<String, Object> commandMap = StringUtil.mapToMap(request);
   		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("insurance.com.title")));
   		System.out.println(commandMap);
   		String insCpy = StringUtil.nvl(commandMap.get("insCpy"),"");
   		String ctfcNum = StringUtil.nvl(commandMap.get("ctfcNum"),"");
   		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(insCpy) && "".equals(ctfcNum)?"insert":"update"));
   		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
   		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
   		commandMap.put("move", move);
   		System.out.println("move=>"+move);
   		
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
   		
   		if("update".equals(move)){
   			Map<String, Object> insuranceInfo = insuranceService.selectMyInsuranceDetail(commandMap);
   			model.addAttribute("insuranceInfo",insuranceInfo);
   		}
   		
   		System.out.println("returnUrl=>"+returnUrl);
   		model.addAttribute("paramInfo",commandMap);
   		model.addAllAttributes(commandMap);
   		System.out.println("My 보험 등록/수정 화면 종료");
   		return returnUrl;
       }
       
       @RequestMapping("/insurance/saveInsurance.do")
       public String saveBeautyPaymanet(
    		   final MultipartHttpServletRequest multiRequest, 
       		@RequestParam Map<String, Object> reqParamMap, 
       		ModelMap model) throws Exception {
   		System.out.println("보험 수정/삭제 시작");
   		Map<String, Object> commandMap = StringUtil.mapToMap(multiRequest);
   		String atchFileId = StringUtil.nvl(commandMap.get("atchFileId"),"");
   		String insCpy = StringUtil.nvl(commandMap.get("insCpy"),"");
   		String ctfcNum = StringUtil.nvl(commandMap.get("ctfcNum"),"");
   		System.out.println(commandMap);
   		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(insCpy) && "".equals(ctfcNum)?"insert":"update"));
   		
   		int cnt = 0;
   		if("update".equals(move)){
   			final List<MultipartFile> files = multiRequest.getFiles("file_1");
   		 if (!files.isEmpty()) {
 			if (atchFileId == null || "".equals(atchFileId)) {
 			    List<FileVO> result = fileUtil.parseFileInf(files, "INS_", 0, atchFileId, "");
 			    atchFileId = fileMngService.insertFileInfs(result);
 			   commandMap.put("atchFileId", atchFileId);
 			} else {
 			    FileVO fvo = new FileVO();
 			    fvo.setAtchFileId(atchFileId);
 			    cnt = fileMngService.getMaxFileSN(fvo);
 			    List<FileVO> _result = fileUtil.parseFileInf(files, "INS_", cnt, atchFileId, "");
 			    fileMngService.updateFileInfs(_result);
 			}
 	    }
   			cnt =insuranceService.updateInsurance(commandMap);
   		}else if("delete".equals(move)){
   			cnt = insuranceService.deleteInsurance(commandMap);
   		}else if("insert".equals(move)){
   			List<FileVO> result = null;
   		    atchFileId = "";
   		    
   		    final List<MultipartFile> files = multiRequest.getFiles("file_1");
   		    if (!files.isEmpty()) {
   		    	result = fileUtil.parseFileInf(files, "INS_", 0, "", "");
   		    	atchFileId = fileMngService.insertFileInfs(result);
   		    }
   			cnt =insuranceService.insertInsurance(commandMap);
   		}
   		
   		model.addAttribute("paramInfo",commandMap);
   		model.addAllAttributes(commandMap);
   		System.out.println("미용실 결제 내역 수정/삭제 종료");
   		return "redirect:/insurance/selectInsuranceList.do";
       }
    
}
