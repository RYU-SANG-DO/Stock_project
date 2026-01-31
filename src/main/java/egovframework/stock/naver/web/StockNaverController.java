package egovframework.stock.naver.web;

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
import egovframework.com.sym.ccm.ccc.service.CmmnClCodeVO;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.ExcelUtil;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.stockUtil;
import egovframework.stock.com.naver.naverUtil;
import egovframework.stock.info.service.StockInfoService;
import egovframework.stock.naver.service.StockNaverService;
import egovframework.stock.vo.naver.NaverResearchVO;
import egovframework.stock.vo.naver.NaverSearchResponseVO;
import egovframework.stock.vo.naver.NaverThemeVO;

@Controller
public class StockNaverController {

	@Autowired
	private StockInfoService stockInfoService;
	
	@Autowired
	private StockNaverService stockNaverService;

	@Resource(name = "CmmnDetailCodeManageService")
	private EgovCcmCmmnDetailCodeManageService cmmnDetailCodeManageService;
	
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    @Resource(name = "pagingManageController")
   	private PagingManageController pagingManageController;

	
    /**
	 * 네이버 주식 정보
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 주식 정보",order = 11000 ,gid = 200 ,keyL1="stock" ,keyL2="naver",lv=0)
    @RequestMapping("/stock/naver/selectNaverMain.do")
    public String selectNaverMain(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
        return "egovframework/stock/naver/naverMain";
    }
	
    /**
	 * 네이버 주식 정보(테마)
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 주식 정보(테마)",order = 11010 ,gid = 200 ,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectThemeList.do")
    public String selectThemeList(@RequestParam Map<String, Object> reqParamMap, @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO,
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("테마 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".theme.title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String listType = StringUtil.nvl(commandMap.get("listType"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		System.out.println("listType=>"+listType);
		System.out.println("gubun=>"+gubun);
		System.out.println("searchKeyword=>"+searchKeyword);
		
		int cnt =0;
		for(int i = 1; i <= 8 ; i++) {
			List<Map<String, Object>> list = naverUtil.getStockThemeSearchList(i , cnt , commandMap);
			List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
			if(!"".equals(searchKeyword)) {
				if("S".equals(gubun)) {
					for(Map<String, Object> map : list) {
						int scnt = stockUtil.getStockUpjongThemeCnt(StringUtil.nvl(map.get("parameter2"),""), "theme", searchKeyword);
						if(scnt > 0) {
							searchList.add(map);
						}
					}
//					System.out.println(i+".종목:"+list.size()+":"+searchList.size());
				}else {
//					for(Map<String, Object> map : list) {
//						String search = StringUtil.nvl(map.get("parameter1"),"");
//						if(search.indexOf(searchKeyword) > (-1)) {
//							searchList.add(map);
//						}
//					}
//					System.out.println(i+".테마"+list.size()+":"+searchList.size());
					searchList = list;
				}
				
			}else {
				searchList = list;
			}
			if(searchList != null && searchList.size() > 0) {
				cnt += searchList.size();
				allList.addAll(searchList);
			}
		}
		System.out.println("allList:"+allList.size());
		
		naverThemeVO = pagingManageController.PagingManageVo(naverThemeVO, model, allList.size());
		System.out.println("FirstIndex=>"+naverThemeVO.getFirstIndex());
		System.out.println("RecordCountPerPage=>"+naverThemeVO.getRecordCountPerPage());
		
		int firstIndex = naverThemeVO.getFirstIndex();
		int recordCountPerPage = naverThemeVO.getRecordCountPerPage();
		
		if("".equals(listType)) {
			List<Map<String, Object>> themeList = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < allList.size() ; i++) {
				Map<String, Object> map = allList.get(i);
				map.put("parameter0", (i+1));
				int num = Integer.parseInt(StringUtil.nvl(map.get("parameter0"),"1"));
				if(num > firstIndex && num <= (firstIndex + recordCountPerPage)) {
				//if((i+1) > firstIndex && (i+1) <= (firstIndex + recordCountPerPage)) {
					themeList.add(map);
				}
			}
			
			allList = themeList;
		}
		System.out.println("allList:"+allList.size());
		
//		for(int i = 0; i < allList.size() ; i++) {
//			Map<String, Object> map = allList.get(i);
//			List<Map<String, Object>> list = stockUtil.getStockUpjongThemeList(StringUtil.nvl(map.get("parameter2"),""), "theme", i+1);
//			themeList.addAll(list);
//		}

//		paramMap.put("filePath", "D:/eclipse_java/com/stock");
//		paramMap.put("fileName", "stock_form.xlsx");
//		paramMap.put("fileResultName", "테마정보내역_"+today+".xlsx");
//		ExcelUtil.resultSetMnspXlsxExcelCreateV02(themeList , paramMap ,0 , 0);
		model.addAttribute("themeList", allList);
//		model.addAttribute("themeDetailList", themeList);
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("naverThemeVO",naverThemeVO);
		model.addAllAttributes(commandMap);
		System.out.println("테마 종료");
        return "egovframework/stock/naver/themeList";
    }
	
	/*
	 * 테마 내역 엑셀 다운로드
	 * */
	@RequestMapping("/stock/naver/selectThemeListExclDownAjax.do")
    public ModelAndView selectThemeListExclDownAjax(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		System.out.println("테마 엑셀 다운로드 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String downGubun = StringUtil.nvl(commandMap.get("downGubun"),"");
		System.out.println(commandMap);
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		String filePath = egovMessageSource.getMessage("stock.com.filePath");
		String resultFilePath = egovMessageSource.getMessage("stock.naver.filePath");
		String fileName = "stock_form.xlsx";
		String fileResultName = "테마정보내역_"+today+".xlsx";
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		int cnt =0;
		for(int i = 1; i <= 10 ; i++) {
			List<Map<String, Object>> list = naverUtil.getStockThemeList(i , cnt);
			List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
			if(!"".equals(searchKeyword)) {
				for(Map<String, Object> map : list) {
					String search = StringUtil.nvl(map.get("parameter1"),"");
					if(search.indexOf(searchKeyword) > (-1)) {
						searchList.add(map);
					}
				}
			}else {
				searchList = list;
			}
			if(searchList != null && searchList.size() > 0) {
				cnt += searchList.size();
				allList.addAll(searchList);
			}
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", filePath);
		paramMap.put("fileName", fileName);
		paramMap.put("fileResultName", fileResultName);
		paramMap.put("resultFilePath", resultFilePath);
		ExcelUtil.resultSetMnspXlsxExcelCreateV02(allList , paramMap ,0 , 0);
		modelAndView.addObject("fileResultName", fileResultName);
		modelAndView.addObject("checkCount", allList.size());
		System.out.println("테마 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	/*
	 * 테마 별 상세 정보(테마별 종목 내역)
	 * */
	@RequestMapping("/stock/naver/selectThemeDetailList.do")
    public String selectThemeDetailList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO,    
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("테마 별 상세 정보(테마별 종목 내역) 시작");
		Map<String, Object> reqParmMap = StringUtil.mapToMap(request);
		commandMap.putAll(reqParmMap);
		String p_type = StringUtil.nvl(commandMap.get("type"),"theme");
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+"."+p_type+".title")));
		System.out.println(commandMap);
		boolean check = true;
		String p_no = StringUtil.nvl(commandMap.get("no"),"");
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		System.out.println("gubun=>"+gubun);
		System.out.println("searchKeyword=>"+searchKeyword);
		
		if(naverThemeVO.getFieldIds() == null) {
			String [] files = naverUtil.getStockUpjongThemeFieldS(p_no , p_type);
			naverThemeVO.setFieldIds(files);
		}
		
		commandMap.put("today_ko", today_ko);
		commandMap.put("fieldIds", naverThemeVO.getJsonFieldIds());

		int cnt =0;
		Map<String, Object> themeMap = new HashMap<String, Object>();

		for(int i = 1; i <= 10 ; i++) {
			List<Map<String, Object>> list = naverUtil.getStockThemeList(i , cnt);
				for(Map<String, Object> map : list) {
					String key = StringUtil.nvl(map.get("parameter2"),"");
					if(key.equals(p_no)) {
						themeMap = map;
						check = false;
						break;
					}
				}
				 if(!check) break;
				cnt += list.size();
		}
		
		System.out.println(themeMap);
		System.out.println(Arrays.toString(naverThemeVO.getFieldIds()));
		
		Map<String, Object> themeDetailInfo = naverUtil.getStockUpjongThemeInfo(StringUtil.nvl(themeMap.get("parameter2"),""), p_type);

		List<Map<String, Object>> themeDetailTitleList = naverUtil.getStockUpjongThemeTitleList_V01(StringUtil.nvl(themeMap.get("parameter2"),""), p_type, naverThemeVO.getFieldIds());
		
		List<Map<String, Object>> list = naverUtil.getStockUpjongThemeList_V01(StringUtil.nvl(themeMap.get("parameter2"),""), p_type, themeDetailTitleList, naverThemeVO.getFieldIds());

		List<Map<String, Object>> themeDetailList = new ArrayList<Map<String, Object>>();
		if("S".equals(gubun) && !"".equals(searchKeyword)) {			
			for(Map<String, Object> map : list) {
				String stock_name = StringUtil.nvl(map.get("stockname"),"");
				if(stock_name.equals(searchKeyword)) {
					themeDetailList.add(map);
				}
			}
		}else {
			themeDetailList = list;
		}
		
		model.addAttribute("themeMap", themeMap);
		model.addAttribute("themeDetailInfo", themeDetailInfo);
		model.addAttribute("themeDetailTitleList", themeDetailTitleList);
		model.addAttribute("themeDetailList", themeDetailList);
		model.addAttribute("themeMap", themeMap);
		model.addAllAttributes(commandMap);
		System.out.println("테마 별 상세 정보(테마별 종목 내역) 종료");
        return "egovframework/stock/naver/themeDetailList";
    }
	
	/*
	 * 테마 별 상태 주식 내역 엑셀 다운로드
	 * */
	@RequestMapping("/stock/naver/selectThemeDetailListExclDownAjax.do")
    public ModelAndView selectThemeDetailListExclDownAjax(@RequestParam Map<String, Object> commandMap,@ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO, ModelMap model) throws Exception {
		System.out.println("테마 별 상태 주식 내역 엑셀 다운로드 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String no = StringUtil.nvl(naverThemeVO.getNo(),"");
		String type = StringUtil.nvl(naverThemeVO.getType(),"");
		int pageIndex = Integer.parseInt(StringUtil.nvl(naverThemeVO.getPageIndex(),"1"));
		System.out.println(naverThemeVO.toString());
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		String filePath = egovMessageSource.getMessage("stock.com.filePath");
		String resultFilePath = egovMessageSource.getMessage("stock.naver.filePath");
		String fileName = "stock_form.xlsx";
		String fileResultName = "테마별상세정보내역_"+today+".xlsx";
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);

		List<Map<String, Object>> themeList=stockUtil.getStockUpjongThemeList(no, type, pageIndex);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", filePath);
		paramMap.put("fileName", fileName);
		paramMap.put("fileResultName", fileResultName);
		paramMap.put("resultFilePath", resultFilePath);
		ExcelUtil.resultSetMnspXlsxExcelCreateV02(themeList , paramMap ,0 , 0);
		
		modelAndView.addObject("fileResultName", fileResultName);
		modelAndView.addObject("checkCount", allList.size());
		System.out.println("테마 별 상태 주식 내역 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	
	/**
	 * 네이버 주식 정보(업종)
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 주식 정보(업종)",order = 11020 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectUpjongList.do")
    public String selectUpjongList(@RequestParam Map<String, Object> commandMap, @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO, HttpServletRequest request,ModelMap model) throws Exception {
		System.out.println("업종 시작");
		commandMap.putAll(StringUtil.mapToMap(request));
		String type = StringUtil.nvl(commandMap.get("type"),"upjong");
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+"."+type+".title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String listType = StringUtil.nvl(commandMap.get("listType"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		System.out.println("listType=>"+listType);
		System.out.println("gubun=>"+gubun);
		System.out.println("searchKeyword=>"+searchKeyword);
		
			List<Map<String, Object>> list = naverUtil.getStockSectorsList();
			List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
			if(!"".equals(searchKeyword)) {
				if("S".equals(gubun)) {
					for(Map<String, Object> map : list) {
						int scnt = stockUtil.getStockUpjongThemeCnt(StringUtil.nvl(map.get("parameter2"),""), type, searchKeyword);
						if(scnt > 0) {
							searchList.add(map);
						}
					}
//					System.out.println(i+".종목:"+list.size()+":"+searchList.size());
				}else {
//					for(Map<String, Object> map : list) {
//						String search = StringUtil.nvl(map.get("parameter1"),"");
//						if(search.indexOf(searchKeyword) > (-1)) {
//							searchList.add(map);
//						}
//					}
//					System.out.println(i+".테마"+list.size()+":"+searchList.size());
					searchList = list;
				}
				
			}else {
				searchList = list;
			}
			if(searchList != null && searchList.size() > 0) {
				allList.addAll(searchList);
			}
		System.out.println("allList:"+allList.size());
		
		naverThemeVO = pagingManageController.PagingManageVo(naverThemeVO, model, allList.size());
		System.out.println("FirstIndex=>"+naverThemeVO.getFirstIndex());
		System.out.println("RecordCountPerPage=>"+naverThemeVO.getRecordCountPerPage());
		
		int firstIndex = naverThemeVO.getFirstIndex();
		int recordCountPerPage = naverThemeVO.getRecordCountPerPage();
		
		if("".equals(listType)) {
			List<Map<String, Object>> themeList = new ArrayList<Map<String, Object>>();
			for(int i = 0; i < allList.size() ; i++) {
				Map<String, Object> map = allList.get(i);
				map.put("parameter0", (i+1));
				int num = Integer.parseInt(StringUtil.nvl(map.get("parameter0"),"1"));
				if(num > firstIndex && num <= (firstIndex + recordCountPerPage)) {
				//if((i+1) > firstIndex && (i+1) <= (firstIndex + recordCountPerPage)) {
					themeList.add(map);
				}
			}
			
			allList = themeList;
		}
		System.out.println("allList:"+allList.size());
		
		model.addAttribute("themeList", allList);
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("naverThemeVO",naverThemeVO);
		model.addAllAttributes(commandMap);
		System.out.println("업종 종료");
        return "egovframework/stock/naver/upjongList";
    }
	
	/*
	 * 업종 내역 엑셀 다운로드
	 * */
	@RequestMapping("/stock/naver/selectUpjongListExclDownAjax.do")
    public ModelAndView selectUpjongListExclDownAjax(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		System.out.println("업종 엑셀 다운로드 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String downGubun = StringUtil.nvl(commandMap.get("downGubun"),"");
		System.out.println(commandMap);
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		String filePath = egovMessageSource.getMessage("stock.com.filePath");
		String resultFilePath = egovMessageSource.getMessage("stock.naver.filePath");
		String fileName = "stock_form.xlsx";
		String fileResultName = "업종정보내역_"+today+".xlsx";
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);

			List<Map<String, Object>> list = naverUtil.getStockSectorsList();
			List<Map<String, Object>> searchList = new ArrayList<Map<String, Object>>();
			if(!"".equals(searchKeyword)) {
				for(Map<String, Object> map : list) {
					String search = StringUtil.nvl(map.get("parameter1"),"");
					if(search.indexOf(searchKeyword) > (-1)) {
						searchList.add(map);
					}
				}
			}else {
				searchList = list;
			}
			if(searchList != null && searchList.size() > 0) {
				allList.addAll(searchList);
			}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", filePath);
		paramMap.put("fileName", fileName);
		paramMap.put("fileResultName", fileResultName);
		paramMap.put("resultFilePath", resultFilePath);
		ExcelUtil.resultSetMnspXlsxExcelCreateV02(allList , paramMap ,0 , 0);
		modelAndView.addObject("fileResultName", fileResultName);
		modelAndView.addObject("checkCount", allList.size());
		System.out.println("업종 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	/*
	 * 업종 별 상세 정보(업종별 종목 내역)
	 * */
	@RequestMapping("/stock/naver/selectUpjongDetailList.do")
    public String selectUpjongDetailList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO,    
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("업종 별 상세 정보(업종별 종목 내역) 시작");
		Map<String, Object> reqParmMap = StringUtil.mapToMap(request);
		commandMap.putAll(reqParmMap);
		String p_type = StringUtil.nvl(commandMap.get("type"),"upjong");
		System.out.println("type=>"+p_type);
		String return_url = "egovframework/stock/naver/"+p_type+"DetailList";
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+"."+p_type+".title")));
		System.out.println(commandMap);
		boolean check = true;
		String p_no = StringUtil.nvl(commandMap.get("no"),"");
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		System.out.println("no=>"+p_no);
		System.out.println("gubun=>"+gubun);
		System.out.println("searchKeyword=>"+searchKeyword);

		if(naverThemeVO.getFieldIds() == null) {
			String [] files = naverUtil.getStockUpjongThemeFieldS(p_no , p_type);
			naverThemeVO.setFieldIds(files);
		}
		
		commandMap.put("today_ko", today_ko);
		commandMap.put("fieldIds", naverThemeVO.getJsonFieldIds());

//		System.out.println(Arrays.toString(naverThemeVO.getFieldIds()));
		
		Map<String, Object> upjongDetailInfo = naverUtil.getStockUpjongThemeInfo(p_no , p_type);

		List<Map<String, Object>> upjongDetailTitleList = naverUtil.getStockUpjongThemeTitleList_V01(p_no, p_type, naverThemeVO.getFieldIds());
		
		List<Map<String, Object>> dlist = naverUtil.getStockUpjongThemeList_V01(p_no, p_type, upjongDetailTitleList, naverThemeVO.getFieldIds());

		List<Map<String, Object>> upjongDetailList = new ArrayList<Map<String, Object>>();
		if("S".equals(gubun) && !"".equals(searchKeyword)) {			
			for(Map<String, Object> map : dlist) {
				String stock_name = StringUtil.nvl(map.get("stockname"),"");
				if(stock_name.equals(searchKeyword)) {
					upjongDetailList.add(map);
				}
			}
		}else {
			upjongDetailList = dlist;
		}
		
		model.addAttribute(p_type+"DetailInfo", upjongDetailInfo);
		model.addAttribute(p_type+"DetailTitleList", upjongDetailTitleList);
		model.addAttribute(p_type+"DetailList", upjongDetailList);
//		model.addAttribute(p_type+"Map", upjongMap);
		model.addAllAttributes(commandMap);
		System.out.println("업종 별 상세 정보(업종별 종목 내역) 종료");
        return return_url;
    }
	
	/*
	 * 업종 별 상태 주식 내역 엑셀 다운로드
	 * */
	@RequestMapping("/stock/naver/selectUpjongDetailListExclDownAjax.do")
    public ModelAndView selectUpjongDetailListExclDownAjax(@RequestParam Map<String, Object> commandMap,@ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO, ModelMap model) throws Exception {
		System.out.println("업종 별 상태 주식 내역 엑셀 다운로드 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String no = StringUtil.nvl(naverThemeVO.getNo(),"");
		String type = StringUtil.nvl(naverThemeVO.getType(),"");
		int pageIndex = Integer.parseInt(StringUtil.nvl(naverThemeVO.getPageIndex(),"1"));
		System.out.println(naverThemeVO.toString());
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		String filePath = egovMessageSource.getMessage("stock.com.filePath");
		String resultFilePath = egovMessageSource.getMessage("stock.naver.filePath");
		String fileName = "stock_form.xlsx";
		String fileResultName = "업종별상세정보내역_"+today+".xlsx";
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);

		List<Map<String, Object>> upjongList=stockUtil.getStockUpjongThemeList(no, type, pageIndex);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("filePath", filePath);
		paramMap.put("fileName", fileName);
		paramMap.put("fileResultName", fileResultName);
		paramMap.put("resultFilePath", resultFilePath);
		ExcelUtil.resultSetMnspXlsxExcelCreateV02(upjongList , paramMap ,0 , 0);
		
		modelAndView.addObject("fileResultName", fileResultName);
		modelAndView.addObject("checkCount", allList.size());
		System.out.println("업종 별 상태 주식 내역 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	/**
	 * 네이버 주식 정보(상승 , 보합 , 하락)
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 주식 정보(상승_보합_하락)",order = 11050 ,gid = 200 ,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectSiseList.do")
    public String selectSiseList(@RequestParam Map<String, Object> reqParamMap, @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO,
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		Map<String, Object>  paramInfo = new HashMap<>();
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"0");
		String type = StringUtil.nvl(commandMap.get("type"),"rise");
		String sosok = StringUtil.nvl(commandMap.get("sosok"),"0");//1:코스피, 0:코스닥
		String typeNm = StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".sise."+type+".title"));
		System.out.println(typeNm+" 시작");
		commandMap.put("type", type);
		commandMap.put("typeNm", typeNm);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+typeNm);
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String listType = StringUtil.nvl(commandMap.get("listType"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);

		System.out.println("listType=>"+listType);
		System.out.println("searchKeyword=>"+searchKeyword);
		List<Map<String, Object>> titleList = new ArrayList<>();
		List<Map<String, Object>> siseList = new ArrayList<>();
		int columsize = 0;
		int num = 1;
		List<Map<String, Object>> list = naverUtil.getStockSiseSearchList(commandMap);
		for(int i = 0 ; i < list.size() ; i++) {
			Map<String, Object> map = list.get(i);
			if(i == 0) {
				columsize = Integer.parseInt(StringUtil.nvl(map.get("columsize"),"0"));
				paramInfo.put("columsize", columsize+1);
				Map<String, Object> jmap = new HashMap<>();
				for(int j = 0 ; j < columsize ; j++) {
					jmap = new HashMap<>();
					jmap.put("title", map.get("parameter"+j));
					jmap.put("key", "parameter"+j);
					jmap.put("idx", j);
					jmap.put("num", (j+1));
					titleList.add(jmap);
				}	
				jmap = new HashMap<>();
				jmap.put("title", "목표주가");
				jmap.put("key", "parameter"+columsize);
				jmap.put("idx", columsize);
				jmap.put("num", (columsize+1));
				titleList.add(jmap);
			}else {
				List<Map<String, Object>> detailList = new ArrayList<>();
				String ahref =  "";
				String nowPrice =  "1";
				Map<String, Object> jmap = new HashMap<>();
				for(int j = 0 ; j < columsize ; j++) {
					jmap = new HashMap<>();
					if(!"".equals(StringUtil.nvl(map.get("parameter"+j+"_href"),""))) {
						ahref =  StringUtil.nvl(map.get("parameter"+j+"_href"),"");
					}
					if(j == 2) {
						nowPrice = StringUtil.nvl(map.get("parameter"+j),"1");
					}
					jmap.put("idx", j);
					jmap.put("dcn", map.get("parameter"+j));
					jmap.put("ahref", ahref);
					jmap.put("spanClass", map.get("parameter"+j+"_spanClass"));
					jmap.put("styleColor", map.get("parameter"+j+"_color"));
					jmap.put("percent", "0");
					jmap.put("opinionText", "");
					detailList.add(jmap);
				}
				String targetPrice= "";
				if(!"".equals(ahref)) {
					String [] ahrefs = ahref.split("=");
					Map<String, Object> infoMap = naverUtil.getStockInfo_v02(ahrefs[1]);
					targetPrice = StringUtil.nvl(infoMap.get("targetPrice"),"1");
					double percent = Math.round(((Double.parseDouble(targetPrice.replace(",", ""))-Double.parseDouble(nowPrice.replace(",", "")))/Double.parseDouble(nowPrice.replace(",", ""))*100)*100)/100.0;
					
					if(percent <= 0) {
						targetPrice = "";
						//percent = 0.0;
					}else {
						//targetPrice = targetPrice+"("+percent+")";
					}
					jmap = new HashMap<>();
					jmap.put("idx", columsize);
					jmap.put("dcn", targetPrice);
					jmap.put("ahref", "");
					jmap.put("spanClass", "");
					jmap.put("styleColor", "");
					jmap.put("percent", percent);
					jmap.put("opinionText", StringUtil.nvl(infoMap.get("investOpinionText"),""));
					detailList.add(jmap);
				}
				
				if("1".equals(gubun)) {
					if(!"".equals(targetPrice)) {						
						map.put("num", (num++));
						map.put("detailList", detailList);
						//System.out.println(map.get("num")+"."+detailList);
						siseList.add(map);
					}
				}else {
					map.put("num", (num++));
					map.put("detailList", detailList);
					//System.out.println(map.get("num")+"."+detailList);
					siseList.add(map);
				}
			}
		}
			
		System.out.println("siseList:"+siseList.size());
		
		model.addAttribute("titleList", titleList);
		model.addAttribute("siseList", siseList);
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("naverThemeVO",naverThemeVO);
		model.addAllAttributes(commandMap);
		System.out.println(typeNm+" 종료");
        return "egovframework/stock/naver/siseList";
    }
	
	
	/**
	 * 네이버 검색
	 * blog : 블로그(json , xml)
	 * news : 뉴스(json , xml)
	 * book_adv : 책(xml)
	 * encyc : 백과사전(json , xml)
	 * kin : 지식IN(json , xml)
	 * webkr : 웹문서(json , xml)
	 * doc : 전문자료(json , xml)
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 검색",order = 11090 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectSearchApi.do")
    public String selectSearchApi(@RequestParam Map<String, Object> commandMap, HttpServletRequest request,ModelMap model) throws Exception {
		System.out.println("네이버 검색 시작");
		NaverSearchResponseVO naverSearchvo = new NaverSearchResponseVO();
		commandMap.putAll(StringUtil.mapToMap(request));
		System.out.println(commandMap);
		String search_date = StringUtil.nvl(commandMap.get("search_date"),"");
		String serch_type = StringUtil.nvl(commandMap.get("serchType"),"news");
		String return_type = StringUtil.nvl(commandMap.get("returnType"),"json");
		int display = Integer.parseInt(StringUtil.nvl(commandMap.get("display"),"10"));
		int start = Integer.parseInt(StringUtil.nvl(commandMap.get("start"),"1"));		
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		System.out.println("start=>"+start);
		System.out.println("serch_type=>"+serch_type);
		System.out.println("return_type=>"+return_type);
		System.out.println("searchKeyword=>"+searchKeyword);
		if(!"".equals(searchKeyword)) {
			naverSearchvo = naverUtil.getNaverSearchApi(serch_type, return_type, display, start, searchKeyword);
		}
		List<Map<String, Object>> list = naverSearchvo.getItemsFormat();
		List<Map<String, Object>> searchList = new ArrayList<>();
		if(!"".equals(search_date)) {
			for(Map<String, Object> map : list) {
				String bpubDateFormat = StringUtil.nvl(map.get("pubDateFormat"),"");
				String apubDateFormat = ComDateUtil.getDateFormat("yyyy-MM-dd HH:mm", "yyyy-MM-dd", "KOR", bpubDateFormat);
				if(search_date.equals(apubDateFormat)) {
					searchList.add(map);
				}
			}
		}else {
			searchList = list;
		}
		model.addAttribute("searchvo", naverSearchvo);
		model.addAttribute("searchList", searchList);
		model.addAllAttributes(commandMap);
		System.out.println("네이버 검색 종료");
        return "egovframework/stock/naver/serachList";
    }
	
	/**
	 * 네이버 주식 정보
	 * @return
	 * @throws Exception
	 */
//	@IncludedInfo(name="네이버 리서치",order = 11140 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
//    @RequestMapping("/stock/naver/selectNaverResearchMain.do")
//    public String selectNaverResearchMain(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
//		model.addAllAttributes(commandMap);
//        return "egovframework/stock/naver/research/researchMain";
//    }
	@IncludedInfo(name="네이버 리서치",order = 11140 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectNaverResearchMain.do")
    public String selectNaverResearchMain(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
        return "egovframework/stock/naver/research/researchMain";
    }
	
	/**
	 * 네이버 리서치 
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 리서치",order = 11140 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectNaverResearchList.do")
    public String selectNaverResearchList(@RequestParam Map<String, Object> commandMap, @ModelAttribute("naverResearchVO") NaverResearchVO naverResearchVO,
    		HttpServletRequest request,ModelMap model) throws Exception {
		System.out.println("네이버 리서치 시작");
		Map<String, Object>  paramInfo = new HashMap<>();
		commandMap.putAll(StringUtil.mapToMap(request));
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".research.title")));
		System.out.println(commandMap);
		naverResearchVO.setPageUnit(30);
		String page = StringUtil.nvl(commandMap.get("pageIndex"), "1");
		String searchType = StringUtil.nvl(commandMap.get("searchType"),"");
		String searchGubun = StringUtil.nvl(commandMap.get("searchGubun"),"company");
		String searchGubunNm = StringUtil.nvl(commandMap.get("searchGubunNm"),"종목분석");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyy-MM-dd");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("searchGubun", searchGubun);
		commandMap.put("searchGubunNm", searchGubunNm);

		commandMap.put("today", today);
		System.out.println("searchGubun=>"+searchGubun);
		System.out.println("searchGubunNm=>"+searchGubunNm);
		
		List<Map<String, Object>> list = naverUtil.getStockResearchList_V01(commandMap);
		List<Map<String, Object>> titleList = new ArrayList<>();
		List<Map<String, Object>> researchList = new ArrayList<>();
		System.out.println("List:"+list.size());
		int columsize = 0;
		int num = 1;
		int pgOn = 1;
		for(int i = 0 ; i < list.size() ; i++) {
			Map<String, Object> map = list.get(i);
			if(i == 0) {
				int pgLL =  Integer.parseInt(StringUtil.nvl(map.get("pgLL"),"0"));//맨앞
				int pgL = Integer.parseInt(StringUtil.nvl(map.get("pgL"),"0"));//이전
				int pgR = Integer.parseInt(StringUtil.nvl(map.get("pgR"),"0"));//다음
				int pgRR = Integer.parseInt(StringUtil.nvl(map.get("pgRR"),"0"));//맨뒤
				pgOn = Integer.parseInt(StringUtil.nvl(map.get("pgOn"),"1"));//현재 페이지
				String title = StringUtil.nvl(map.get("title"),"");
				columsize = Integer.parseInt(StringUtil.nvl(map.get("columsize"),"0"));
				paramInfo.put("title", title);
				paramInfo.put("columsize", columsize);
				paramInfo.put("pgLL", pgLL);
				paramInfo.put("pgL", pgL);
				paramInfo.put("pgR", pgR);
				paramInfo.put("pgRR", pgRR);
				paramInfo.put("pgOn", pgOn);
				naverResearchVO.setPageIndex(pgOn);
				naverResearchVO.setLastIndex(pgRR);
				for(int j = 0 ; j < columsize ; j++) {
					Map<String, Object> jmap = new HashMap<>();
					jmap.put("title", map.get("parameter"+j));
					jmap.put("key", "parameter"+j);
					jmap.put("idx", j);
					jmap.put("num", (j+1));
					titleList.add(jmap);
				}				
			}else {
				List<Map<String, Object>> detailList = new ArrayList<>();
				String code_nid = map.get("code")+"_"+map.get("nid")+"_"+map.get("date")+"_"+page;
				String fileYn = "Y";
				for(int j = 0 ; j < columsize ; j++) {
					Map<String, Object> jmap = new HashMap<>();
					String dcn = StringUtil.nvl(map.get("parameter"+j),"");
					String dclass = StringUtil.nvl(map.get("parameter"+j+"_class"),"");
					String dhref= StringUtil.nvl(map.get("parameter"+j+"_href"),"");
					jmap.put("idx", j);
					jmap.put("dcn", dcn);
					jmap.put("dclass", dclass);
					jmap.put("dhref", dhref);
					jmap.put("nid", map.get("nid"));
					jmap.put("code", map.get("code"));
					jmap.put("date", map.get("date"));
					jmap.put("page", page);
					if("file".equals(dclass) && "".equals(dhref)){
						fileYn = "N";
					}
					detailList.add(jmap);
				}			
				
				map.put("nuidx", (((pgOn-1)*30)+num));
				map.put("num", (num++));
				map.put("fileYn", fileYn);
				map.put("codeNid", code_nid);
				map.put("detailList", detailList);
				researchList.add(map);
			}
		}
		
		naverResearchVO.setPageSize(30);
		
		naverResearchVO = pagingManageController.PagingManageVo(naverResearchVO, model, naverResearchVO.getLastIndex()*naverResearchVO.getPageSize());
		System.out.println("FirstIndex=>"+naverResearchVO.getFirstIndex());
		System.out.println("RecordCountPerPage=>"+naverResearchVO.getRecordCountPerPage());
		
		int firstIndex = naverResearchVO.getFirstIndex();
		int recordCountPerPage = naverResearchVO.getRecordCountPerPage();
		
		model.addAttribute("titleList", titleList);
		model.addAttribute("researchList", researchList);
		model.addAttribute("paramInfo",paramInfo);
		model.addAttribute("pageIndex",page);
		model.addAttribute("naverResearchVO",naverResearchVO);
		model.addAllAttributes(commandMap);
		System.out.println("네이버 리서치 종료");
        return "egovframework/stock/naver/research/researchList";
    }
	
	/*
	 * 주식 정보 등록
	 * */
	@RequestMapping("/stock/naver/insertNaverResearchAjax.do")
    public ModelAndView insertNaverResearchAjax(
    			@RequestParam Map<String, Object> commandMap,
    			@RequestParam(value = "stocks", required = false) List<String> stocks,
    			@RequestParam(value = "pageIndex", required = false) String pageIndex,
    			ModelMap model
    		) throws Exception {
		System.out.println("주식 정보 등록 시작");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("jsonView");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		List<Map<String, Object>> allList = new ArrayList<Map<String, Object>>();
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		String searchGubun = StringUtil.nvl(commandMap.get("searchGubun"),"company");
		pageIndex = StringUtil.nvl(pageIndex, "1");
		
		Map<String, Object> pmap = new HashMap<>();
		pmap.put("searchGubun", searchGubun);
		pmap.put("pageIndex", pageIndex);
		List<Map<String, Object>> list = naverUtil.getStockResearchList_V01(pmap);
		List<Map<String, Object>> insertList = new ArrayList<>();
		int totcnt = 0;
		for(String params : stocks) {
			Map<String, Object> insertMap = new HashMap<>();
			String [] keys = params.split("_");
			System.out.println(Arrays.toString(keys));
			pmap = new HashMap<>();
			pmap.put("searchGubun", searchGubun);
			if(keys.length == 4) {
				String code = StringUtil.nvl(keys[0]);
				String nid = StringUtil.nvl(keys[1]);
				String date = StringUtil.nvl(keys[2]);
				String page = StringUtil.nvl(keys[3]);
				insertMap.put("relStockCode", code);
				insertMap.put("originUid", nid);
				insertMap.put("rpDate", StringUtil.getFormDate(date, "yy.MM.dd"));
				insertMap.put("originUid", nid);
				insertMap.put("page", page);
				int columsize = 0;
				for(int i = 0 ; i < list.size() ; i++) {
					Map<String, Object> map = list.get(i);
					if(i == 0) {
						columsize = Integer.parseInt(StringUtil.nvl(map.get("columsize"),"0"));
					}else {
						String code_nid = map.get("code")+"_"+map.get("nid")+"_"+map.get("date")+"_"+page;
						if(params.equals(code_nid)) {
							System.out.println(i+"["+params+"]=["+code_nid+"]");
							for(int j = 0 ; j < columsize ; j++) {
								String dcn = StringUtil.nvl(map.get("parameter"+j),"");
								String dclass = StringUtil.nvl(map.get("parameter"+j+"_class"),"");
								String dhref= StringUtil.nvl(map.get("parameter"+j+"_href"),"");
								if(j == 0) {//종목
									insertMap.put("relStockCode", map.get("code"));
									insertMap.put("relStockLink", "https://finance.naver.com"+dhref);
								}else if(j == 1) {//제목
									insertMap.put("rpTitle", dcn);
									insertMap.put("rpTitleLink", "https://finance.naver.com/research/"+dhref);
								}else if(j == 2) {//증권사
									insertMap.put("securities", dcn);
								}else if(j == 3) {//첨부
									insertMap.put("rpLink", dhref);
								}else if(j == 4) {//작성일
									insertMap.put("rpDate", StringUtil.getFormDate(dcn, "yy.MM.dd"));
								}else if(j == 5) {//조회수
									
								}
							}			
							
							insertMap.put("codeNid", code_nid);
							System.out.println(insertMap);
							totcnt += stockNaverService.insertStockResearchData(insertMap);
							insertList.add(insertMap);
						}
					}
				}
				
			}
		}
		System.out.println("totcnt=>"+totcnt);
		modelAndView.addObject("totcnt", totcnt);
		modelAndView.addObject("stocks", stocks);
		System.out.println("주식 정보 등록 종료");
		return modelAndView;
    }
	
	/**
	 * 네이버 나의 리서치 
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="네이버 My 리서치",order = 11160 ,gid = 200,keyL1="stock" ,keyL2="naver" ,lv=1)
    @RequestMapping("/stock/naver/selectNaverMyResearchList.do")
    public String selectNaverMyResearchList(@RequestParam Map<String, Object> reqParamMap, @ModelAttribute("naverResearchVO") NaverResearchVO naverResearchVO,
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("네이버 My 리서치 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".my.research.title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String listType = StringUtil.nvl(commandMap.get("listType"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		System.out.println("listType=>"+listType);
		System.out.println("gubun=>"+gubun);
		System.out.println("searchKeyword=>"+searchKeyword);
		
		int totCnt = stockNaverService.selectStockResearchDataListTotCnt(commandMap);
		
		
		naverResearchVO = pagingManageController.PagingManageVo(naverResearchVO, model, totCnt);
		System.out.println("FirstIndex=>"+naverResearchVO.getFirstIndex());
		System.out.println("RecordCountPerPage=>"+naverResearchVO.getRecordCountPerPage());
		
		int firstIndex = naverResearchVO.getFirstIndex();
		int recordCountPerPage = naverResearchVO.getRecordCountPerPage();
		commandMap.put("firstIndex", firstIndex);
		commandMap.put("recordCountPerPage", recordCountPerPage);
		List<Map<String, Object>> list = stockNaverService.selectStockResearchDataList(commandMap);
		
		model.addAttribute("reserchList", list);
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("naverResearchVO",naverResearchVO);
		model.addAllAttributes(commandMap);
		System.out.println("네이버 My 리서치 종료");
		return "egovframework/stock/naver/research/myReserchList";
    }
	
	@RequestMapping("/stock/naver/selectNaverMyResearchDetail.do")
    public String selectNaverMyResearchDetail(
    		@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("네이버 My 리서치 상세 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".my.research.title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String listType = StringUtil.nvl(commandMap.get("listType"),"");
		String gubun = StringUtil.nvl(commandMap.get("gubun"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		CmmnDetailCodeVO searchVO = new CmmnDetailCodeVO();
		searchVO.setFirstIndex(0);
		searchVO.setSearchCondition("1");
		searchVO.setSearchKeyword("STRE");
		List<CmmnDetailCodeVO> clCodeList = cmmnDetailCodeManageService.selectCmmnDetailCodeList(searchVO);
		model.addAttribute("clCodeList", clCodeList);
		
		Map<String, Object> infoMap = stockNaverService.selectStockResearchDataDetail(commandMap);
		
		model.addAttribute("infoMap", infoMap);
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("네이버 My 리서치 상세 종료");
		return "egovframework/stock/naver/research/myReserchUpdt";
    }

	@RequestMapping("/stock/naver/saveNaverMyResearch.do")
    public String saveNaverMyResearch(
    		@RequestParam Map<String, Object> reqParamMap, 
    		@RequestParam(value = "rpId", required = true) String rpId,
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("네이버 My 리서치 수정/삭제 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".my.research.title")));
		System.out.println(commandMap);
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(rpId)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		 int cnt = 0;
		if("update".equals(move)){
			cnt =stockNaverService.updateStockResearchData(commandMap);
		}else if("delete".equals(move)){
			cnt = stockNaverService.deleteStockResearchData(commandMap);
		}
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("네이버 My 리서치 수정/삭제 종료");
		return "forward:/stock/naver/selectNaverMyResearchList.do";
    }
}
