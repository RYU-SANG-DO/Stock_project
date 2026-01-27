package egovframework.stock.info.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.web.PagingManageController;
import egovframework.com.ext.ldapumt.service.EgovOrgManageLdapService;
import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.ExcelUtil;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.stockUtil;
import egovframework.stock.com.naver.naverUtil;
import egovframework.stock.info.service.StockInfoService;
import egovframework.stock.vo.naver.NaverResearchVO;
import egovframework.stock.vo.naver.NaverSearchResponseVO;
import egovframework.stock.vo.naver.NaverThemeVO;

@Controller
public class StockInfoController {
	
	@Autowired
	private StockInfoService stockInfoService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    @Resource(name = "pagingManageController")
   	private PagingManageController pagingManageController;

	
    /**
	 * My 주식 정보
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="My Stock",order = 15000 ,gid = 200 ,keyL1="stock" ,keyL2="my",lv=0)
    @RequestMapping("/stock/info/selectMyInfoMain.do")
    public String selectMyInfoMain(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
        return "egovframework/stock/info/infoMain";
    }
	
	/**
	 * 네이버 주식 정보(테마)
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="거래내역",order = 15010 ,gid = 200 ,keyL1="stock" ,keyL2="my" ,lv=1)
    @RequestMapping("/stock/info/selectMyStockList.do")
    public String selectMyStockList(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("거래내역 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".list.title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		int totCnt = stockInfoService.selectMyStockListTotCnt(commandMap);
		pagingManageController.PagingManage(commandMap, model, totCnt);
		List<Map<String, Object>> list = stockInfoService.selectMyStockList(commandMap);
		
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("list",list);
		model.addAllAttributes(commandMap);
		System.out.println("거래내역 종료");
        return "egovframework/stock/info/myStockList";
    }
	
	/*
	 * 거래내역 엑셀 다운로드
	 * */
	@RequestMapping("/stock/info/selectMyStockListExclDownAjax.do")
    public ModelAndView selectMyStockListExclDownAjax(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		System.out.println("거래내역 엑셀 다운로드 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String downGubun = StringUtil.nvl(commandMap.get("downGubun"),"");
		System.out.println(commandMap);
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String filePath = egovMessageSource.getMessage("stock.com.filePath");
		String resultFilePath = egovMessageSource.getMessage("stock.info.filePath");
		String fileName = "stock_form.xlsx";
		String fileResultName = "거래내역_"+today+".xlsx";
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		int totCnt = stockInfoService.selectMyStockListTotCnt(commandMap);
		commandMap.put("pageUnit", totCnt);
		List<Map<String, Object>> allList = stockInfoService.selectMyStockList(commandMap);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", filePath);
		paramMap.put("fileName", fileName);
		paramMap.put("fileResultName", fileResultName);
		paramMap.put("resultFilePath", resultFilePath);
		ExcelUtil.resultSetMnspXlsxExcelCreateV02(allList , paramMap ,0 , 0);
		modelAndView.addObject("fileResultName", fileResultName);
		modelAndView.addObject("checkCount", allList.size());
		System.out.println("거래내역 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	
	/**
	 * 등록 , 수정 , 삭제
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/moveMyStock.do")
    public String moveMyStock(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("등록/수정/삭제 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("등록/수정/삭제 종료");
        return "forward:/stock/info/selectMyStockList.do";
    }

}
