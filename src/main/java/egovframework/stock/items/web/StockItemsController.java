package egovframework.stock.items.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.web.PagingManageController;

@Controller
public class StockItemsController {


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
