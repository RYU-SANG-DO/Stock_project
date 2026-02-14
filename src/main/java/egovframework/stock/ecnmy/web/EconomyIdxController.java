package egovframework.stock.ecnmy.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.dartUtil;
import egovframework.stock.dart.service.StockDartService;
import egovframework.stock.ecnmy.service.EconomyIdxService;
import egovframework.stock.vo.dart.CompanyVO;
import egovframework.stock.vo.dart.FnlttsinglacntallVO;
import egovframework.stock.vo.dart.ListVO;

@Controller
public class EconomyIdxController {

	@Autowired
	private EconomyIdxService economyIdxService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	/**
	 * 경제 정보
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="경제 정보",order = 16000 ,gid = 200 ,keyL1="stock" ,keyL2="ecnmy" ,lv=0)
    @RequestMapping("/stock/ecnmy/selectEcnmyList.do")
    public String selectEcnmyList(@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		String apiGrpCd = StringUtil.nvl(commandMap.get("apiGrpCd"),"DS001");
		commandMap.put("apiGrpCd", apiGrpCd);
		System.out.println(commandMap);
		String dart_api_url = StringUtil.nvl(egovMessageSource.getMessage("stock.dart.api.url"));
		
		model.addAllAttributes(commandMap);
        return "egovframework/stock/ecnmy/selectEcnmyList";
    }
	

}
