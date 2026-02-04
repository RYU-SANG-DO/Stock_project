package egovframework.stock.data.web;

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
import egovframework.com.cmm.web.PagingManageController;
import egovframework.stock.com.StringUtil;
import egovframework.stock.data.service.StockDataService;
import egovframework.stock.data.service.StocksDataVO;

@Controller
public class StockDataController {

	@Autowired
	private StockDataService stockDataService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    @Resource(name = "pagingManageController")
   	private PagingManageController pagingManageController;

	

    /**
	 * 주식 종목 내역
	 * @return
	 * @throws Exception
	 */
    @IncludedInfo(name="종목 내역 정보",order = 10200 ,gid = 200 ,keyL1="stock" ,keyL2="data",lv=0)
    @RequestMapping("/stock/data/selectStocksList.do")
    public String selectStocksList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") StocksDataVO stocksDataVO,  HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println(commandMap);
		String dart_api_url = StringUtil.nvl(egovMessageSource.getMessage("stock.dart.api.url"));
//		model.addAllAttributes(commandMap);
//        return "egovframework/stock/data/stocksList";
        
    	// 내역 조회
		//stocksDataVO.setPageUnit(propertiesService.getInt("pageUnit"));
		stocksDataVO.setPageSize(propertiesService.getInt("pageSize"));
		stocksDataVO.setPageUnit(30);

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(stocksDataVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(stocksDataVO.getPageUnit());
		paginationInfo.setPageSize(stocksDataVO.getPageSize());

		stocksDataVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		stocksDataVO.setLastIndex(paginationInfo.getLastRecordIndex());
		stocksDataVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<Map<String,Object>> resultList = stockDataService.selectStocksList(stocksDataVO);
        model.addAttribute("resultList", resultList);
        
       int totCnt = stockDataService.selectStocksListTotCnt(stocksDataVO);
       paginationInfo.setTotalRecordCount(totCnt);
       model.addAttribute("paginationInfo", paginationInfo);

        model.addAllAttributes(commandMap);
        return "egovframework/stock/data/stocksList";
    }
    
    /**
	 * 주식 종목 내역(팝업)
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/data/selectStocksPopList.do")
    public String selectStocksPopList(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") StocksDataVO stocksDataVO,  HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println(commandMap);
		String dart_api_url = StringUtil.nvl(egovMessageSource.getMessage("stock.dart.api.url"));
        
    	// 내역 조회
		//stocksDataVO.setPageUnit(propertiesService.getInt("pageUnit"));
		stocksDataVO.setPageSize(propertiesService.getInt("pageSize"));
		stocksDataVO.setPageUnit(30);

    	/** pageing */
    	PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(stocksDataVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(stocksDataVO.getPageUnit());
		paginationInfo.setPageSize(stocksDataVO.getPageSize());

		stocksDataVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		stocksDataVO.setLastIndex(paginationInfo.getLastRecordIndex());
		stocksDataVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		List<Map<String,Object>> resultList = stockDataService.selectStocksList(stocksDataVO);
        model.addAttribute("resultList", resultList);
        
       int totCnt = stockDataService.selectStocksListTotCnt(stocksDataVO);
       paginationInfo.setTotalRecordCount(totCnt);
       model.addAttribute("paginationInfo", paginationInfo);

        model.addAllAttributes(commandMap);
        return "egovframework/stock/data/pop/stocksPopList";
    }
    
    /**
	 * 주식 종목 등록 및 수정 화면 이동
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/data/moveStockView.do")
    public String moveStockView(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") StocksDataVO stocksDataVO,  HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println(commandMap);
		String stocksCode = StringUtil.nvl(commandMap.get("stocksCode"),"");
		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(stocksCode)?"insert":"update"));
        String returnUrl = "egovframework/stock/data/stocksRegist";
    	if("update".equals(move)) {
    		Map<String,Object> stockInfo = stockDataService.selectStocksDetail(stocksDataVO);
            model.addAttribute("stockInfo", stockInfo);
            returnUrl = "egovframework/stock/data/stocksUpdt";
    	}

        model.addAllAttributes(commandMap);
        return returnUrl;
    }
    
    /**
   	 * 주식 종목 등록 및 수정
   	 * @return
   	 * @throws Exception
   	 */
       @RequestMapping("/stock/data/saveStocksInfo.do")
       public String saveStocksInfo(@RequestParam Map<String, Object> commandMap , @ModelAttribute("stocksDataVO") StocksDataVO stocksDataVO,  HttpServletRequest request, ModelMap model) throws Exception {
   		System.out.println(commandMap);
   		String stocksCode = StringUtil.nvl(commandMap.get("stocksCode"),"");
   		String move = StringUtil.nvl(commandMap.get("mode"), ("".equals(stocksCode)?"insert":"update"));
           String returnUrl = "forward:/stock/data/selectStocksList.do";
           int cnt = 0;
       	if("insert".equals(move)) {
       		cnt =stockDataService.insertStocksInfo(commandMap);
       	}if("update".equals(move)) {
       		cnt =stockDataService.updateStocksInfo(commandMap);
       	}if("delete".equals(move)) {
       		cnt = stockDataService.deleteStocksInfo(commandMap);
       	}
       	System.out.println(move+":"+cnt);
           model.addAllAttributes(commandMap);
           return returnUrl;
       }
}
