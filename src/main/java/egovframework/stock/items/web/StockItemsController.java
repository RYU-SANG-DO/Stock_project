package egovframework.stock.items.web;

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
public class StockItemsController {

	@Autowired
	private EgovOrgManageLdapService orgManageLdapService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    @Resource(name = "pagingManageController")
   	private PagingManageController pagingManageController;

	
    /**
	 * 종목 내역 정보
	 * @return
	 * @throws Exception
	 */
    @RequestMapping("/stock/items/selectItemsMain.do")
    public String selectItemsMain(@RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);
        return "egovframework/stock/items/itemMain";
    }
	

}
