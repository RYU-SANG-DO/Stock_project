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
import egovframework.stock.vo.naver.NaverResearchVO;
import egovframework.stock.vo.naver.NaverSearchResponseVO;
import egovframework.stock.vo.naver.NaverThemeVO;

@Controller
public class StockInfoController {

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
    public String selectMyStockList(@RequestParam Map<String, Object> reqParamMap, @ModelAttribute("naverThemeVO") NaverThemeVO naverThemeVO,
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
		
		model.addAttribute("paramInfo",commandMap);
		model.addAttribute("naverThemeVO",naverThemeVO);
		model.addAllAttributes(commandMap);
		System.out.println("거래내역 종료");
        return "egovframework/stock/info/myStockList";
    }
	

}
