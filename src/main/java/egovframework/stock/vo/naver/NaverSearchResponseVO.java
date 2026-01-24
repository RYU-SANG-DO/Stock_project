package egovframework.stock.vo.naver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import egovframework.stock.com.ComDateUtil;
import egovframework.stock.com.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverSearchResponseVO {

	private String query;			//검색어. UTF-8로 인코딩되어야 합니다.
	private String lastBuildDate;
	private String title;
	private String link;
	private String description;
	private int total;
	private int start = 1;			//검색 시작 위치(기본값: 1, 최댓값: 1000)
	private int display = 10;		//한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
	private String sort = "sim";	//검색 결과 정렬 방법 	- sim: 정확도순으로 내림차순 정렬(기본값)	- date: 날짜순으로 내림차순 정렬
	private String filter;				//검색 결과에 포함할 상품 유형	- 설정 안 함: 모든 상품(기본값)	- naverpay: 네이버페이 연동 상품
	private List<Map<String, Object>> items;
	
	public List<Map<String, Object>> getItemsFormat() {
		List<Map<String, Object>> resultList = new ArrayList<>();
		String org_format = "EEE, dd MMM yyyy HH:mm:ss Z";
		String new_format = "yyyy-MM-dd HH:mm";
		String lang ="ENGLISH";
		try {
			if(items != null) {
				for(int i = 0 ; i < items.size() ; i++){
					Map<String, Object> map = items.get(i);
					String org_date = StringUtil.nvl(map.get("pubDate"),"");
					if(!"".equals(org_date)) {
						map.put("pubDateFormat", ComDateUtil.getDateFormat(org_format, new_format, lang, org_date));
					}
					map.put("num", (i+1));
					resultList.add(map);
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return resultList;
	}
	
	public List<Map<String, Object>> getItemsNewFormat(String new_form) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		String org_format = "EEE, dd MMM yyyy HH:mm:ss Z";
		String new_format = new_form;
		String lang ="ENGLISH";
		try {
			if(items != null) {
				for(int i = 0 ; i < items.size() ; i++){
					Map<String, Object> map = items.get(i);
					String org_date = StringUtil.nvl(map.get("pubDate"),"");
					if(!"".equals(org_date)) {
						map.put("pubDateFormat", ComDateUtil.getDateFormat(org_format, new_format, lang, org_date));
					}
					map.put("num", (i+1));
					resultList.add(map);
				}
				
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return resultList;
	}

	public String toString() {
        return "NaverSearchResponseVO{" +
        		"lastBuildDate='"+lastBuildDate+'\''+"," +
        		"total='"+total+'\''+"," +
        		"start='"+start+'\''+"," +
        		"display='"+display+'\''+"," +
        		"items='"+items+'\'' +
                '}';
    }
}
