package egovframework.stock.dart.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.annotation.IncludedInfo;
import egovframework.com.ext.ldapumt.service.EgovOrgManageLdapService;
import egovframework.com.ext.ldapumt.service.UcorgVO;
import egovframework.com.ext.ldapumt.service.UserVO;
import egovframework.stock.com.StringUtil;
import egovframework.stock.com.dartUtil;
import egovframework.stock.dart.service.StockDartService;
import egovframework.stock.vo.dart.CompanyVO;
import egovframework.stock.vo.dart.FnlttsinglacntallVO;
import egovframework.stock.vo.dart.ListVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class StockDartController {

	@Autowired
	private StockDartService stockDartService;

    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	/**
	 * DART 주식 정보
	 * @return
	 * @throws Exception
	 */
	@IncludedInfo(name="DART 주식 정보",order = 12000 ,gid = 200 ,keyL1="stock" ,keyL2="dart" ,lv=0)
    @RequestMapping("/stock/dart/selectDartList.do")
    public String selectDartList(@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		String apiGrpCd = StringUtil.nvl(commandMap.get("apiGrpCd"),"DS001");
		commandMap.put("apiGrpCd", apiGrpCd);
		System.out.println(commandMap);
		String dart_api_url = StringUtil.nvl(egovMessageSource.getMessage("stock.dart.api.url"));
		List<Map<String,Object>> list = stockDartService.selectDartList(commandMap);

		model.addAttribute("list", list);
		model.addAllAttributes(commandMap);
        return "egovframework/stock/dart/dartList";
    }
	
	/**
	 * DART 주식 정보
	 * @return
	 * @throws Exception
	 */
  @RequestMapping("/stock/dart/selectDetailDartList.do")
    public String selectDetailDartList(@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println(commandMap);
		Map<String, Object> dartInfo = stockDartService.selectDetailDartMap(commandMap);
		String apiGrpCd = StringUtil.nvl(commandMap.get("apiGrpCd"),"");
		String apiId = StringUtil.nvl(commandMap.get("apiId"),"");
		String returnPage = "egovframework/stock/dart/type/"+apiGrpCd+"/dartApiResult_"+apiId;
		SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat format_yyyy = new SimpleDateFormat ("yyyy");
		Date time = new Date();
		String nowsdate = format_yyyy.format(time)+"-01-01";
		String nowedate = format_yyyy.format(time)+"-12-31";
		commandMap.put("page_count", StringUtil.nvl(commandMap.get("page_count"),"10"));
		commandMap.put("page_no", StringUtil.nvl(commandMap.get("page_no"),"1"));
		
		model.addAttribute("dartInfo", dartInfo);
		model.addAllAttributes(commandMap);
//		model.addAttribute("bgn_de", nowsdate);
//		model.addAttribute("end_de", nowedate);
        return returnPage;
    }

	/**
	 * DART API 정보 조회
	 * @return
	 * @throws Exception
	 */
  @RequestMapping("/stock/dart/selectDartApiResultList.do")
    public String selectDartApiResultList(@RequestParam Map<String, Object> commandMap , HttpServletRequest request, ModelMap model) throws Exception {
		System.out.println(commandMap);
		List<Map<String, Object>> listm = new ArrayList<>();
		String stock_code = StringUtil.nvl(commandMap.get("stock_code"),"");
		System.out.println("stock_code=>"+stock_code);
		Map<String, Object> dartInfo = stockDartService.selectDetailDartMap(commandMap);
		String apiGrpCd = StringUtil.nvl(dartInfo.get("apiGrpCd"),"");
		String apiId = StringUtil.nvl(dartInfo.get("apiId"),"");
		String returnPage = "egovframework/stock/dart/type/"+apiGrpCd+"/dartApiResult_"+apiId;
		model.addAttribute("dartInfo", dartInfo);
		if(!"".equals(stock_code)) {
			Map<String, Object> codeMap = dartUtil.getCorpCode(stock_code);//고유정보
			String corp_code = StringUtil.nvl(codeMap.get("corp_code"),"");//고유번호
			if(!"".equals(corp_code)) {
				 if("DS001".equals(apiGrpCd)) {//공시정보
					  if("2019001".equals(apiId)) {//공시검색
						  List<ListVO> list = setDartTypeVO(commandMap, dartInfo, codeMap, ListVO.class);
						  model.addAttribute("list", list);
					  }else  if("2019002".equals(apiId)) {//기업개황
						  List<CompanyVO> list = setDartTypeVO(commandMap, dartInfo, codeMap, CompanyVO.class);
						  model.addAttribute("list", list);
					  }
				 }else if("DS002".equals(apiGrpCd)) {//정기보고서 주요정보
				 }else if("DS003".equals(apiGrpCd)) {//정기보고서 재무정보
					
					 if("2019020".equals(apiId)) {//단일회사 전체 재무제표
						 List<FnlttsinglacntallVO> list = setDartTypeVO(commandMap, dartInfo, codeMap, FnlttsinglacntallVO.class);
						  model.addAttribute("list", list);
					 }
				 }
	        	
			}
		}
		model.addAllAttributes(commandMap);
        return returnPage;
    }
  
  private <T> List<T> setDartTypeVO(Map<String, Object> commandMap, Map<String, Object> dartInfo, Map<String, Object> codeMap, Class<T> classType) throws Exception{
		List<T> resultList = new ArrayList<T>(); 
		String apiGrpCd = StringUtil.nvl(dartInfo.get("apiGrpCd"),"");
		String apiId = StringUtil.nvl(dartInfo.get("apiId"),"");
		String corp_code = StringUtil.nvl(codeMap.get("corp_code"),"");//고유번호
	  if("DS001".equals(apiGrpCd)) {
		  if("2019001".equals(apiId)) {
				String format = StringUtil.nvl(commandMap.get("resultType"),"json");
				String bgn_de = StringUtil.nvl(commandMap.get("bgn_de"),"").replaceAll("-", "");
				String end_de = StringUtil.nvl(commandMap.get("end_de"),"").replaceAll("-", "");
				String pblntf_ty = StringUtil.nvl(commandMap.get("pblntf_ty"),"");
				String corp_cls = StringUtil.nvl(commandMap.get("corp_cls"),"");
				String sort = StringUtil.nvl(commandMap.get("sort"),"");
				String sort_mth = StringUtil.nvl(commandMap.get("sort_mth"),"");
				String page_no = StringUtil.nvl(commandMap.get("page_no"),"");
				String page_count = StringUtil.nvl(commandMap.get("page_count"),"");
				
				dartUtil dartutil = new dartUtil(dartInfo , format , corp_code);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				if(!"".equals(bgn_de))paramMap.put("bgn_de", bgn_de);//시작일
	        	if(!"".equals(end_de))paramMap.put("end_de", end_de);//종료일
	        	if(!"".equals(pblntf_ty))paramMap.put("pblntf_ty", pblntf_ty);//정기공시
	        	if(!"".equals(corp_cls))paramMap.put("corp_cls", corp_cls);
	        	if(!"".equals(sort))paramMap.put("sort", sort);
	        	if(!"".equals(sort_mth))paramMap.put("sort_mth", sort_mth);
	        	if(!"".equals(page_no))paramMap.put("page_no", page_no);
	        	if(!"".equals(page_count))paramMap.put("page_count", page_count);
	        	resultList = dartutil.dartApiInfo(paramMap, classType);
		  }else if("2019002".equals(apiId)) {
			  String format = StringUtil.nvl(commandMap.get("resultType"),"json");
			  dartUtil dartutil = new dartUtil(dartInfo , format , corp_code);
			  Map<String, Object> paramMap = new HashMap<String, Object>();
			  resultList = dartutil.dartApiInfo(paramMap, classType);
		  }
		  
  		}else if("DS003".equals(apiGrpCd)) {//정기보고서 재무정보
  			 if("2019020".equals(apiId)) {//단일회사 전체 재무제표
  				String format = StringUtil.nvl(commandMap.get("resultType"),"json");
  				String bsns_year = StringUtil.nvl(commandMap.get("bsns_year"),"");//사업연도
				String reprt_code = StringUtil.nvl(commandMap.get("reprt_code"),"");//보고서 코드
				String fs_div = StringUtil.nvl(commandMap.get("fs_div"),"OFS");//개별/연결구분
				dartUtil dartutil = new dartUtil(dartInfo , format , corp_code);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				if(!"".equals(bsns_year))paramMap.put("bsns_year", bsns_year);
	        	if(!"".equals(reprt_code))paramMap.put("reprt_code", reprt_code);
	        	if(!"".equals(fs_div))paramMap.put("fs_div", fs_div);
	        	resultList = dartutil.dartApiInfo(paramMap, classType);
  			 }
  		}
	  return resultList;
  }

}
