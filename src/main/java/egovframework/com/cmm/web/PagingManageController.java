package egovframework.com.cmm.web;

import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import egovframework.stock.com.StringUtil;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

/**
 * 관리자 코드 처리
 */
@Controller
public class PagingManageController {

	/** log */
    protected static final Log LOG = LogFactory.getLog(PagingManageController.class);

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;


    /**
     * 페이징처리를 한다
     * @param Map commandMap, ModelMap model
     * @exception Exception
     */
    public void PagingManage( Map<String, Object> commandMap, ModelMap model, int TotCnt) throws Exception {

    	int currentPageNo;
    	int pageUnit;
    	int pageSize;

    	try{
    		currentPageNo 	= Integer.parseInt((String) commandMap.get("pageIndex"));
    	} catch (Exception e){
    		currentPageNo 	= 1;
    	}
    	try{
    		pageUnit 		= Integer.parseInt((String) commandMap.get("pageUnit"));
    	} catch (Exception e){
    		pageUnit		= propertiesService.getInt("pageUnit");
    		commandMap.put("pageUnit", pageUnit);
    	}
    	try{
    		pageSize 		= propertiesService.getInt("pageSize");
    	} catch (Exception e){
    		pageSize		= propertiesService.getInt("pageSize");
    	}

    	/* S:PAGEING */
    	PaginationInfo paginationInfo = new PaginationInfo();
    	paginationInfo.setCurrentPageNo(currentPageNo);

    	paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);

    	if(commandMap.get("bbsId") != "" && commandMap.get("bbsId") != null){

	    	if(commandMap.get("bbsId").toString().equals("BBSMSTR_000000000023")){
			 	paginationInfo.setRecordCountPerPage(8);
				paginationInfo.setPageSize(8);

	    	}else if(commandMap.get("bbsId").toString().equals("setCourse")){
	    		paginationInfo.setRecordCountPerPage(12);
				paginationInfo.setPageSize(12);
	    	}

    	}else{
    		paginationInfo.setRecordCountPerPage(pageUnit);
    		paginationInfo.setPageSize(pageSize);
    	}
		/* E:PAGEING */

		commandMap.put("pageIndex", currentPageNo);
		commandMap.put("firstIndex", paginationInfo.getFirstRecordIndex());
		commandMap.put("lastIndex", paginationInfo.getLastRecordIndex());
		commandMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());

		int totCnt = TotCnt;
		commandMap.put("pageTotCnt", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

		model.addAllAttributes(commandMap);

    }
    
    /**
     * vo 페이징처리를 한다
     * @param <vo>
     * @param Map commandMap, ModelMap model
     * @exception Exception
     */
    @SuppressWarnings({ "hiding"})
	public <T> T PagingManageVo( T pvo, ModelMap model, int TotCnt) throws Exception {
    	
    	int currentPageNo;
    	int pageUnit;
    	int pageSize;
    	Map<String, Object> commandMap = StringUtil.voToMap(pvo);
    	try{
    		currentPageNo 	= Integer.parseInt(StringUtil.nvl(commandMap.get("pageIndex"),"1"));
    	} catch (Exception e){
    		currentPageNo 	= 1;
    		e.printStackTrace();
    	}

    	try{
//    		pageUnit 		= Integer.parseInt((String) commandMap.get("pageUnit"));
    		pageUnit 		= Integer.parseInt(StringUtil.nvl(commandMap.get("pageUnit"),"10"));
    	} catch (Exception e){
    		pageUnit		= propertiesService.getInt("pageUnit");
    		commandMap.put("pageUnit", pageUnit);
    		e.printStackTrace();
    	}
    	try{
    		pageSize 		= propertiesService.getInt("pageSize");
    	} catch (Exception e){
    		pageSize		= propertiesService.getInt("pageSize");
    		e.printStackTrace();
    	}

    	/* S:PAGEING */
    	PaginationInfo paginationInfo = new PaginationInfo();
    	paginationInfo.setCurrentPageNo(currentPageNo);

    	paginationInfo.setRecordCountPerPage(pageUnit);
		paginationInfo.setPageSize(pageSize);

    	if(commandMap.get("bbsId") != "" && commandMap.get("bbsId") != null){

	    	if(commandMap.get("bbsId").toString().equals("BBSMSTR_000000000023")){
			 	paginationInfo.setRecordCountPerPage(8);
				paginationInfo.setPageSize(8);

	    	}else if(commandMap.get("bbsId").toString().equals("setCourse")){
	    		paginationInfo.setRecordCountPerPage(12);
				paginationInfo.setPageSize(12);
	    	}

    	}else{
    		paginationInfo.setRecordCountPerPage(pageUnit);
    		paginationInfo.setPageSize(pageSize);
    	}
		/* E:PAGEING */

//		commandMap.put("pageIndex", currentPageNo);
//		commandMap.put("firstIndex", paginationInfo.getFirstRecordIndex());
//		commandMap.put("lastIndex", paginationInfo.getLastRecordIndex());
//		commandMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
		
		Class<?> clazz = pvo.getClass();
		while (clazz != null) { // 상속 계층 탐색
			Field[] fields = clazz.getDeclaredFields();
//			System.out.println(clazz.getSimpleName() +":"+fields.length);
			if("ComDefaultVO".equals(clazz.getSimpleName())) {				
				for (Field field : fields) {
		            field.setAccessible(true); // private 필드 접근 가능 설정
//		            System.out.println(field.getName()+" : "+ field.get(pvo));
		            if("firstIndex".equals(field.getName())) {
		            	 field.set(pvo, paginationInfo.getFirstRecordIndex());
		            }else if("lastIndex".equals(field.getName())) {
		            	 field.set(pvo, paginationInfo.getLastRecordIndex());
		            }else if("recordCountPerPage".equals(field.getName())) {
		            	field.set(pvo, paginationInfo.getRecordCountPerPage());
		            }
//		            System.out.println(field.getName()+" : "+ field.get(pvo));
		        }
			}
			clazz = clazz.getSuperclass();
		}
		int totCnt = TotCnt;
//		commandMap.put("pageTotCnt", totCnt);
		paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);

//		model.addAllAttributes(commandMap);
		return pvo;
    }


}