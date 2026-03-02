package egovframework.beauty.web;

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

import egovframework.beauty.service.BeautyVO;
import egovframework.beauty.service.BeautyService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.web.PagingManageController;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.StringUtil;

@Controller
public class BeautyController {

	@Autowired
	private BeautyService beautyService;

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
	 * 미용실 결제 내역
	 * @return
	 * @throws Exception
	 */
    @IncludedInfo(name="미용실 결제 내역",order = 60100 ,gid = 600 ,keyL1="beauty" ,keyL2="paymanet" , lv=0)
    @RequestMapping("/beauty/paymanet/selectBeautyPaymanetList.do")
    public String selectBeautyPaymanetList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") BeautyVO beautyVO,  HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println("selectBeautyPaymanetList start");
    	System.out.println(commandMap);
    	commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("beauty.com.title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("beauty.paymanet.title")));
		int totCnt = beautyService.selectBeautyPaymanetListTotCnt(commandMap);
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
		List<Map<String, Object>> list = beautyService.selectBeautyPaymanetList(commandMap);
		model.addAttribute("reserchList", list);
		
		List<Map<String, Object>> yearList = beautyService.selectBeautyPaymanetYearList(commandMap);
		model.addAttribute("yearList", yearList);
        
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("beautyVO",beautyVO);
        model.addAllAttributes(commandMap);
        return "egovframework/beauty/paymanet/paymanetList";
    }
    
    @RequestMapping("/beauty/paymanet/moveBeautyPaymanet.do")
    public String moveBeautyPaymanet(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("미용실 결제 상세 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("beauty.com.title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("beauty.paymanet.title")));
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		commandMap.put("move", move);
		if("update".equals(move)) {
			Map<String, Object> beautyInfo = beautyService.selectBeautyPaymanetInfo(commandMap);
			model.addAttribute("beautyInfo",beautyInfo);
		}
		CmmnDetailCodeVO searchVO = new CmmnDetailCodeVO();
		searchVO.setFirstIndex(0);
		searchVO.setRecordCountPerPage(100);
		searchVO.setSearchCondition("1");
		searchVO.setClCode("BTY");
		searchVO.setSearchKeyword("STYLE");//스타일
		List<CmmnDetailCodeVO> styleList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("styleList", styleList);
		
		searchVO.setSearchKeyword("PMTYPE");//결재타입
		List<CmmnDetailCodeVO> pmtypeList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("pmtypeList", pmtypeList);
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("미용실 결제 상세 종료");
		return "egovframework/beauty/paymanet/paymanetUpdt";
    }
    
    @RequestMapping("/beauty/paymanet/saveBeautyPaymanet.do")
    public String saveBeautyPaymanet(
    		@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("미용실 결제 내역 수정/삭제 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		System.out.println(commandMap);
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		 int cnt = 0;
		if("update".equals(move)){
			cnt =beautyService.updateBeautyPaymanet(commandMap);
		}else if("delete".equals(move)){
			cnt = beautyService.deleteBeautyPaymanet(commandMap);
		}else if("insert".equals(move)){
			cnt =beautyService.insertBeautyPaymanet(commandMap);
		}
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("미용실 결제 내역 수정/삭제 종료");
		return "redirect:/beauty/paymanet/selectBeautyPaymanetList.do";
    }
    
}
