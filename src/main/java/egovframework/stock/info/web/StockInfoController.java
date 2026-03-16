package egovframework.stock.info.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.cmm.web.PagingManageController;
import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.ExcelUtil;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.naver.naverUtil;
import egovframework.stock.info.service.StockInfoService;

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
	 * My 주식 내역
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
		for(Map<String, Object> map : list) {
			String stockCode = StringUtil.nvl(map.get("code"),"");
			int nowPrice = 0;
			int dyaNowPrice = 0;
			String dyaNowPecent = "0";
			double indepercent = 0.0;
			if(!"".equals(stockCode)) {
				Map<String, Object> stockMap = naverUtil.getStockInfoType(stockCode, 0);
				if(stockMap != null) {
					nowPrice = Integer.parseInt(StringUtil.nvl(stockMap.get("parameter1"),"").replaceAll(",", ""));//현재단가
					indepercent = Double.parseDouble(StringUtil.nvl(stockMap.get("parameter2"),"0.0"));//현재증감률
					if("하락".equals(StringUtil.nvl(stockMap.get("parameter7"),""))) {
						indepercent = indepercent * (-1);
					}
					String gubun = StringUtil.nvl(map.get("gubun"),"");
					int unitPrice = Integer.parseInt(StringUtil.nvl(map.get("unitPrice"),"0"));//매수
					int sellPrice = Integer.parseInt(StringUtil.nvl(map.get("sellPrice"),"0"));//매도
					int qy = Integer.parseInt(StringUtil.nvl(map.get("qy"),"0"));
					if("SELL".equals(gubun)){
						dyaNowPrice = (sellPrice*qy)-(unitPrice*qy);
						dyaNowPecent = String.format("%.2f",(sellPrice-unitPrice)/(float)unitPrice*100);
					}else {
						dyaNowPrice = (nowPrice*qy)-(unitPrice*qy);
						dyaNowPecent = String.format("%.2f",(nowPrice-unitPrice)/(float)unitPrice*100);
					}
				}
				map.put("nowPrice", nowPrice);
				map.put("dyaNowPrice", dyaNowPrice);
				map.put("dyaNowPecent", dyaNowPecent);
				map.put("indepercent", indepercent);
			}
		}
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
		for(Map<String, Object> map : allList) {
			String stockCode = StringUtil.nvl(map.get("code"),"");
			if(!"".equals(stockCode)) {
				Map<String, Object> stockMap = naverUtil.getStockInfoType(stockCode, 0);
				String rm = StringUtil.nvl(stockMap.get("parameter10"),"");
				int nowPrice = Integer.parseInt(StringUtil.nvl(stockMap.get("parameter1"),"").replaceAll(",", ""));//현재단가
				int unitPrice = Integer.parseInt(StringUtil.nvl(map.get("unitPrice"),"0"));
				int dyaNowPrice = nowPrice-unitPrice;
				map.put("nowPrice", nowPrice);
				map.put("dyaNowPrice", dyaNowPrice);
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
		System.out.println("거래내역 엑셀 다운로드 종료");
		return modelAndView;
    }
	
	
	/**
	 * 등록 , 수정 화면
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/moveMyStock.do")
    public String moveMyStock(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("등록/수정 화면 시작");
		String returnUrl="egovframework/stock/info/myStockUpdt";
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		commandMap.put("pSeq", seq);
		
		if("insert".equals(move)){
			returnUrl="egovframework/stock/info/myStockRegist";
		}else {
			Map<String, Object> infoMap = stockInfoService.selectMyStockDetail(commandMap);
			model.addAttribute("infoMap",infoMap);
		}
		System.out.println("returnUrl=>"+returnUrl);
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("등록/수정 화면 종료");
		return returnUrl;
    }
    
    /**
	 * 등록 , 수정
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/saveMyStock.do")
    public String saveMyStock(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("등록/수정 시작");

		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		 int cnt = 0;
		if("insert".equals(move)){
			 stockInfoService.insertMyStock(commandMap);
		}else if("update".equals(move)){
			cnt =stockInfoService.updateMyStock(commandMap);
		}else if("delete".equals(move)){
			cnt = stockInfoService.deleteMyStock(commandMap);
		}
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("등록/수정 종료");
		return "forward:/stock/info/selectMyStockList.do";
    }
    
    /**
	 * My 주식 이력 내역
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/selectMyStockHistList.do")
    public String selectMyStockHistList(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("거래 이력 내역 시작");
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".hist.list.title")));
		System.out.println(commandMap);
		String searchKeyword = StringUtil.nvl(commandMap.get("searchKeyword"),"");
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String pSeq = StringUtil.nvl(commandMap.get("pSeq"),"");
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		
		int nowPrice = 0;
		int dyaNowPrice = 0;
		String dyaNowPecent = "0";
		double indepercent = 0.0;
		Map<String, Object> infoMap = stockInfoService.selectMyStockDetail(commandMap);
		String stockCode = StringUtil.nvl(infoMap.get("stocksCode"),"");
		Map<String, Object> stockMap = naverUtil.getStockInfoType(stockCode, 0);
		if(stockMap != null) {
			nowPrice = Integer.parseInt(StringUtil.nvl(stockMap.get("parameter1"),"").replaceAll(",", ""));//현재단가
			indepercent = Double.parseDouble(StringUtil.nvl(stockMap.get("parameter2"),"0.0"));//현재증감률
			if("하락".equals(StringUtil.nvl(stockMap.get("parameter7"),""))) {
				indepercent = indepercent * (-1);
			}
			int unitPrice = Integer.parseInt(StringUtil.nvl(infoMap.get("unitPrice"),"0"));
			int qy = Integer.parseInt(StringUtil.nvl(infoMap.get("qy"),"0"));
			dyaNowPrice = (nowPrice*qy)-(unitPrice*qy);
			dyaNowPecent = String.format("%.2f",(nowPrice-unitPrice)/(float)unitPrice*100);
		}
		infoMap.put("nowPrice", nowPrice);
		infoMap.put("dyaNowPrice", dyaNowPrice);
		infoMap.put("dyaNowPecent", dyaNowPecent);
		infoMap.put("indepercent", indepercent);
		model.addAttribute("infoMap",infoMap);
		
		List<Map<String, Object>> histList = stockInfoService.selectMyStockHistList(commandMap);
		
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("list",histList);
		model.addAllAttributes(commandMap);
		System.out.println("거래 이력 내역 종료");
        return "egovframework/stock/info/myStockHistList";
    }
    
    /**
	 * My 주식 이력 등록 , 수정 화면
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/moveMyStockHist.do")
    public String moveMyStockHist(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("My 주식 이력 등록/수정 화면 시작");
		String returnUrl="egovframework/stock/info/myStockHistUpdt";
		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		commandMap.put("pageTitle", StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".title"))+" "+StringUtil.nvl(egovMessageSource.getMessage("stock."+ commandMap.get("stockSite")+".hist.detail.title")));
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		
		Map<String, Object> infoMap = stockInfoService.selectMyStockDetail(commandMap);
		model.addAttribute("infoMap",infoMap);
		if("insert".equals(move)){
			returnUrl="egovframework/stock/info/myStockHistRegist";
		}else {
			Map<String, Object> infoHistMap = stockInfoService.selectMyStockHistDetail(commandMap);
			model.addAttribute("infoHistMap",infoHistMap);
		}
		System.out.println("returnUrl=>"+returnUrl);
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("My 주식 이력 등록/수정 화면 종료");
		return returnUrl;
    }
    
    /**
	 * 등록 , 수정
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/info/saveMyStockHist.do")
    public String saveMyStockHist(@RequestParam Map<String, Object> reqParamMap, 
    		HttpServletRequest request,
    		ModelMap model) throws Exception {
		System.out.println("등록/수정 시작");

		Map<String, Object> commandMap = StringUtil.mapToMap(request);
		System.out.println(commandMap);
		String seq = StringUtil.nvl(commandMap.get("seq"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(seq)?"insert":"update"));
		String today = ComDateUtil.getToday_v01("yyyyMMddHHmm");
		String today_ko = ComDateUtil.getToday_v01("yyyy년 MM월 dd일 HH시 mm분 ss초");
		commandMap.put("today_ko", today_ko);
		 int cnt = 0;
		if("insert".equals(move)){
			 stockInfoService.insertMyStockHist(commandMap);
		}else if("update".equals(move)){
			cnt =stockInfoService.updateMyStockHist(commandMap);
		}else if("delete".equals(move)){
			cnt = stockInfoService.deleteMyStockHist(commandMap);
		}
		
		model.addAttribute("paramInfo",commandMap);
		model.addAllAttributes(commandMap);
		System.out.println("등록/수정 종료");
		return "forward:/stock/info/selectMyStockHistList.do";
    }

}
