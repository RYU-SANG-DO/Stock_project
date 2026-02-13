package egovframework.stock.com.naver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.stock.com.ExcelUtil;
import egovframework.stock.com.StringUtil;
import egovframework.stock.vo.StockDefaultVO;
import egovframework.stock.vo.naver.NaverResearchCrawVO;
import egovframework.stock.vo.naver.NaverSearchResponseVO;

public class naverUtil extends StockDefaultVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static String dart_key = "0d0847f7d8387063c48a902013299fea898bb50d";
	public final static String dart_save_dir = "D:/eclipse_java/com/dart/";
	
	public final static String nevar_serach_url = "https://openapi.naver.com/v1/search";//네이버 검색
	public final static String nevar_clientId = "Pvi5oVK5_pILVZL3ueal"; //네이버 검색 애플리케이션 클라이언트 아이디
	public final static String nevar_clientSecret = "5WOpnjR1wc"; //네이버 검색 애플리케이션 클라이언트 시크릿
	
	public final static String dart_api_url = "https://opendart.fss.or.kr/api/";
	public final static String naver_domain_url = "https://finance.naver.com";
	//테마 적용하기
	public final static String naver_field_submit_url = "https://finance.naver.com/sise/field_submit.naver?menu=theme&returnUrl=http%3A%2F%2Ffinance.naver.com%2Fsise%2Fsise_group_detail.naver%3Ftype%3Dtheme%26no%3D";
	//테마 초기 항목으로
	public final static String naver_field_del_url = "https://finance.naver.com/sise/field_del.naver?menu=theme&returnUrl=http%3A%2F%2Ffinance.naver.com%2Fsise%2Fsise_group_detail.naver%3Ftype%3Dtheme%26no%3D";
	
	/**
	 * 코스피 종목 리스트
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockList(String path) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		String excelpath = path;
		try {
			List<Map<String, Object>> list = ExcelUtil.getExcelDataList(excelpath);
			for(int i = 1 ; i < list.size() ; i++){
				Map<String, Object> map = list.get(i);
				System.out.println(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultList;
	}
	
	/**
	 * 코스피 항목 정보 셋팅(네이버 종합정보)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static StockDefaultVO setStockTitle(String stockCode , int startnum, StockDefaultVO vo, String version) throws Exception{
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

			//기업실적분석
			Elements theadtrs = doc.select(".cop_analysis thead tr");		
			if(theadtrs != null && theadtrs.size() > 0) {
				//타이틀
				Elements tline =theadtrs.get(0).select("th");
				String title1 = tline.get(1).text().split(" ")[2] != null ? tline.get(1).text().split(" ")[2] : "";//최근 연간 실적
				String title2 = tline.get(2).text().split(" ")[2] != null ? tline.get(2).text().split(" ")[2] : "";//최근 분기 실적
				
				Elements tdata =theadtrs.get(1).select("th");
				//최근 연간 실적(0~3)
				//최근 분기 실적(4~8)
				for(int i = 0 ; i < 9 ; i++) {
//					vo.setStockTitles(tdata.get(i).text());
//					vo.setStockUserYn(true);
				}
			}
			
			Elements tradecompares = doc.select(".trade_compare tbody tr");
			if(tradecompares != null && tradecompares.size() > 0) {
				for(int i =0 ; i < tradecompares.size() ; i++) {
					String title =tradecompares.get(i).select("th span").text();
					vo.setStockTitles(title);
					if(i < 3) {
						vo.setStockUserYn(false);
					}else {
						vo.setStockUserYn(true);
					}
//					Elements cdatas = tradecompares.get(i).select("td");
//					for(int j = 0 ; j < cdatas.size() ; j++) {
//						String place =cdatas.get(i).text();
//					}
					
				}
				
			}
			System.out.println(vo.getStockTitles());
			//Elements tabCon1 = doc.select("#tab_con1 .rwidth tbody tr");	//투자정보
			Element firstTr = doc.select("#tab_con1 .rwidth tbody tr").first();
			String thText = firstTr.select("th").text();
			String[] titles = thText.split("l");
			String investOpinionTitle = titles[0].trim(); // 투자의견
			String targetPriceTitle   = titles[1].trim(); // 목표주가
			String score = "";//투자의견 점수
			String opinion = "";//투자의견 (매수)
			String targetPrice = "";
			if (firstTr != null) {
			    score = firstTr.select("span.f_up em").text();
			    opinion = firstTr.select("span.f_up").text().replace(score, "").trim();
			    targetPrice = firstTr.select("td > em").text();
			}
			System.out.println(investOpinionTitle+"=>"+score+" "+opinion);
			System.out.println(targetPriceTitle+"=>"+targetPrice);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return vo;
	}

	/**
	 * 코스피 종목 정보
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockInfo(String stockCode , int startnum) throws Exception{
		System.out.println("getStockInfo start:"+stockCode+","+startnum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		int start = startnum;
		StockDefaultVO vo = new StockDefaultVO();
		vo.setStockTitles("기타");
//		System.out.println(vo.getStockTitles());
		try {
			
			//System.out.println("종목\t주가\t등락률\t시가\t고가\t저가\t거래량\t타입\t전일대비\t가져오는 시간");
			//for(int i = 0 ; i < stockCodes.length ; i++) {
				//String code = StringUtil.nvl(stockCodes[i],"");
				String code = StringUtil.nvl(stockCode,"");
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select(".new_totalinfo dl>dd");
				Elements summaryInfoList =doc.select(".summary_info p");
				Elements copAnalysis =doc.select(".cop_analysis");//기업실적분석 section cop_analysis
				Elements theadtrs = doc.select(".cop_analysis thead tr");
				
			
				String summaryInfo ="";
				for(Element element : summaryInfoList) {
					summaryInfo += element.text();
				}
				
				if(todaylist != null && todaylist.size() > 0) {					
					String name = todaylist.get(1).text().split(" ")[1] != null ? todaylist.get(1).text().split(" ")[1] : "";
					String juga = todaylist.get(3).text().split(" ")[1] != null ? todaylist.get(3).text().split(" ")[1] : "";
					String DungRakrate = todaylist.get(3).text().split(" ")[6] != null ? todaylist.get(3).text().split(" ")[6] : "";
					if(todaylist.get(3).text().split(" ")[5] != null && todaylist.get(3).text().split(" ")[5].equals("마이너스")) {
						DungRakrate = "-"+DungRakrate;
					}
					//DungRakrate = DungRakrate+"%";
					String siga =  todaylist.get(5).text().split(" ")[1] != null ? todaylist.get(5).text().split(" ")[1] : "";
					String goga = todaylist.get(6).text().split(" ")[1] != null  ? todaylist.get(6).text().split(" ")[1] : "";
					String zeoga = todaylist.get(8).text().split(" ")[1] != null ? todaylist.get(8).text().split(" ")[1] : "";
					String georaeryang = todaylist.get(10).text().split(" ")[1] != null ? todaylist.get(10).text().split(" ")[1] : "";
	
					String stype = todaylist.get(3).text().split(" ")[3] != null ? todaylist.get(3).text().split(" ")[3] : ""; 
	
					String vsyesterday = todaylist.get(3).text().split(" ")[4] != null ? todaylist.get(3).text().split(" ")[4] : "";
					
					//System.out.println(name+"\t"+juga+"\t"+DungRakrate+"\t"+siga+"\t"+goga+"\t"+zeoga+"\t"+georaeryang+"\t"+stype+"\t"+vsyesterday+"\t"+str[0]+" "+str[1]);
					resultMap.put("parameter"+(startnum++), name);
					resultMap.put("parameter"+(startnum++), juga);
					resultMap.put("parameter"+(startnum++), DungRakrate.replace("퍼센트", ""));
					resultMap.put("parameter"+(startnum++), siga);
					resultMap.put("parameter"+(startnum++), goga);
					resultMap.put("parameter"+(startnum++), zeoga);
					resultMap.put("parameter"+(startnum++), georaeryang);
					resultMap.put("parameter"+(startnum++), stype);
					resultMap.put("parameter"+(startnum++), vsyesterday);
					resultMap.put("parameter"+(startnum++), str[0]+" "+str[1]);
					resultMap.put("parameter"+(startnum++), summaryInfo);				
				}else {
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
				}
				
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println(resultMap);
		System.out.println("getStockInfo end");
		return resultMap;
	}
	
	/**
	 * 코스피 종목 정보
	 * @param stockCode
	 * @param startnum
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockInfoType(String stockCode , int startnum) throws Exception{
		System.out.println("getStockInfoType start:"+stockCode+","+startnum);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식
//		System.out.println(URL);
		SimpleDateFormat format = new SimpleDateFormat ( "HH");
		int time = Integer.parseInt(format.format(new Date()));
//		System.out.println("time=>"+time);
		String type = "KRX";
		if(time >=8 && time <= 16) {
			type = "KRX";
		}else {
			type = "NXT";
		}
		Document doc;
		StockDefaultVO vo = new StockDefaultVO();
		vo.setStockTitles("기타");
//		System.out.println(vo.getStockTitles());
		try {
			
			//System.out.println("종목\t주가\t등락률\t시가\t고가\t저가\t거래량\t타입\t전일대비\t가져오는 시간");
			//for(int i = 0 ; i < stockCodes.length ; i++) {
				//String code = StringUtil.nvl(stockCodes[i],"");
				String code = StringUtil.nvl(stockCode,"");
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select(".new_totalinfo dl>dd");
				Elements summaryInfoList =doc.select(".summary_info p");
				Elements copAnalysis =doc.select(".cop_analysis");//기업실적분석 section cop_analysis
				Elements theadtrs = doc.select(".cop_analysis thead tr");
				
			
				String summaryInfo ="";
				for(Element element : summaryInfoList) {
					summaryInfo += element.text();
				}
				
				Elements krxElem =doc.select("#rate_info_krx");
				Elements nxtElem =doc.select("#rate_info_nxt>.blind");
				if("NXT".equals(type) && nxtElem != null && nxtElem.size() > 0) {
					String name = nxtElem.select("dt>strong").text();
					String juga = nxtElem.select("dd").get(0).text().split(" ")[1];
					String [] points = nxtElem.select("dd").get(1).text().split(" ");
					String [] percents = nxtElem.select("dd").get(2).text().split(" ");
//					System.out.println("name=>"+name);
//					System.out.println("juga=>"+juga);
//					System.out.println(Arrays.toString(points));
//					System.out.println(Arrays.toString(percents));
					
					resultMap.put("parameter"+(startnum++), name);
					resultMap.put("parameter"+(startnum++), juga);
					resultMap.put("parameter"+(startnum++), percents[0].replace("%", ""));
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), points[2]);
					resultMap.put("parameter"+(startnum++), points[0]);
					resultMap.put("parameter"+(startnum++), str[0]+" "+str[1]);
					resultMap.put("parameter"+(startnum++), summaryInfo);				
					
				}else {
					
				
					if(todaylist != null && todaylist.size() > 0) { 
						String name = todaylist.get(1).text().split(" ")[1] != null ? todaylist.get(1).text().split(" ")[1] : "";
						String juga = todaylist.get(3).text().split(" ")[1] != null ? todaylist.get(3).text().split(" ")[1] : "";
						String DungRakrate = todaylist.get(3).text().split(" ")[6] != null ? todaylist.get(3).text().split(" ")[6] : "";
						if(todaylist.get(3).text().split(" ")[5] != null && todaylist.get(3).text().split(" ")[5].equals("마이너스")) {
							DungRakrate = "-"+DungRakrate;
						}
						//DungRakrate = DungRakrate+"%";
						String siga =  todaylist.get(5).text().split(" ")[1] != null ? todaylist.get(5).text().split(" ")[1] : "";
						String goga = todaylist.get(6).text().split(" ")[1] != null  ? todaylist.get(6).text().split(" ")[1] : "";
						String zeoga = todaylist.get(8).text().split(" ")[1] != null ? todaylist.get(8).text().split(" ")[1] : "";
						String georaeryang = todaylist.get(10).text().split(" ")[1] != null ? todaylist.get(10).text().split(" ")[1] : "";
		
						String stype = todaylist.get(3).text().split(" ")[3] != null ? todaylist.get(3).text().split(" ")[3] : ""; 
		
						String vsyesterday = todaylist.get(3).text().split(" ")[4] != null ? todaylist.get(3).text().split(" ")[4] : "";
						
						//System.out.println(name+"\t"+juga+"\t"+DungRakrate+"\t"+siga+"\t"+goga+"\t"+zeoga+"\t"+georaeryang+"\t"+stype+"\t"+vsyesterday+"\t"+str[0]+" "+str[1]);
						resultMap.put("parameter"+(startnum++), name);
						resultMap.put("parameter"+(startnum++), juga);
						resultMap.put("parameter"+(startnum++), DungRakrate.replace("퍼센트", ""));
						resultMap.put("parameter"+(startnum++), siga);
						resultMap.put("parameter"+(startnum++), goga);
						resultMap.put("parameter"+(startnum++), zeoga);
						resultMap.put("parameter"+(startnum++), georaeryang);
						resultMap.put("parameter"+(startnum++), stype);
						resultMap.put("parameter"+(startnum++), vsyesterday);
						resultMap.put("parameter"+(startnum++), str[0]+" "+str[1]);
						resultMap.put("parameter"+(startnum++), summaryInfo);				
					}else {
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
						resultMap.put("parameter"+(startnum++), "");
					}
					
				}
		} catch (Exception e) {
			e.printStackTrace();
		} 
//		System.out.println(resultMap);
		System.out.println("getStockInfoType end");
		return resultMap;
	}
	
	/**
	 * 코스피 종목 정보(네이버)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockInfo(String stockCode , int startnum, StockDefaultVO vo) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String [] stockCodes = {"005930","035720","012450","034020","373220","000660"};
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		int start = startnum;
		//String excelpath = "D:/eclipse_java/com/stock/cospi_20220311.xlsx";
		try {
			//System.out.println("종목\t주가\t등락률\t시가\t고가\t저가\t거래량\t타입\t전일대비\t가져오는 시간");
			//for(int i = 0 ; i < stockCodes.length ; i++) {
				//String code = StringUtil.nvl(stockCodes[i],"");
				String code = StringUtil.nvl(stockCode,"");
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select(".new_totalinfo dl>dd");
				Elements summaryInfoList =doc.select(".summary_info p");
				Elements copAnalysis =doc.select(".cop_analysis");//기업실적분석
			
				String summaryInfo ="";
				for(Element element : summaryInfoList) {
					summaryInfo += element.text();
				}
				
				if(todaylist != null && todaylist.size() > 0) {					
					String name = todaylist.get(1).text().split(" ")[1] != null ? todaylist.get(1).text().split(" ")[1] : "";
					String juga = todaylist.get(3).text().split(" ")[1] != null ? todaylist.get(3).text().split(" ")[1] : "";
					String DungRakrate = todaylist.get(3).text().split(" ")[6] != null ? todaylist.get(3).text().split(" ")[6] : "";
					if(todaylist.get(3).text().split(" ")[5] != null && todaylist.get(3).text().split(" ")[5].equals("마이너스")) {
						DungRakrate = "-"+DungRakrate;
					}
					//DungRakrate = DungRakrate+"%";
					String siga =  todaylist.get(5).text().split(" ")[1] != null ? todaylist.get(5).text().split(" ")[1] : "";
					String goga = todaylist.get(6).text().split(" ")[1] != null  ? todaylist.get(6).text().split(" ")[1] : "";
					String zeoga = todaylist.get(8).text().split(" ")[1] != null ? todaylist.get(8).text().split(" ")[1] : "";
					String georaeryang = todaylist.get(10).text().split(" ")[1] != null ? todaylist.get(10).text().split(" ")[1] : "";
	
					String stype = todaylist.get(3).text().split(" ")[3] != null ? todaylist.get(3).text().split(" ")[3] : ""; 
	
					String vsyesterday = todaylist.get(3).text().split(" ")[4] != null ? todaylist.get(3).text().split(" ")[4] : "";
					
					//System.out.println(name+"\t"+juga+"\t"+DungRakrate+"\t"+siga+"\t"+goga+"\t"+zeoga+"\t"+georaeryang+"\t"+stype+"\t"+vsyesterday+"\t"+str[0]+" "+str[1]);
					resultMap.put("parameter"+(startnum++), name);//9
					resultMap.put("parameter"+(startnum++), juga);//10
					resultMap.put("parameter"+(startnum++), DungRakrate.replace("퍼센트", ""));//11
					resultMap.put("parameter"+(startnum++), siga);//12
					resultMap.put("parameter"+(startnum++), goga);//13
					resultMap.put("parameter"+(startnum++), zeoga);//14
					resultMap.put("parameter"+(startnum++), georaeryang);//15
					resultMap.put("parameter"+(startnum++), stype);//16
					resultMap.put("parameter"+(startnum++), vsyesterday);//17
					resultMap.put("parameter"+(startnum++), str[0]+" "+str[1]);//18
					resultMap.put("parameter"+(startnum++), summaryInfo);//19
				}else {
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
				}
				
			//기업실적분석
			Elements theadtrs = doc.select(".cop_analysis thead tr");//기업실적분석
			if(theadtrs != null && theadtrs.size() > 0) {
				//타이틀
				Elements tline =theadtrs.get(0).select("th");
				String title1 = tline.get(1).text().split(" ")[2] != null ? tline.get(1).text().split(" ")[2] : "";//최근 연간 실적
				String title2 = tline.get(2).text().split(" ")[2] != null ? tline.get(2).text().split(" ")[2] : "";//최근 분기 실적
				
				Elements tdata =theadtrs.get(1).select("th");
				//최근 연간 실적(0~3)
				//최근 분기 실적(4~8)
				String [] tdatas = new String[9];
				String [][] datas = new String[20][9];
				for(int i = 0 ; i < 9 ; i++) {
					tdatas[i] = tdata.get(i).text();
					datas[0][i] = tdata.get(i).text();
				}
				
				for(int j = 1 ; j < 16 ; j++) {
					for(int i = 0 ; i < 9 ; i++) {
						datas[j][i] = tdata.get(i).text();
					}
				}
				
				//1.매출액(억원),2.영업이익(억원),3.당기순이익(억원),4.영업이익률(%),5.순이익률(%),6.ROE(지배주주),7.부채비율,8.당좌비율,9.유보율,10.EPS(원),11.PER(배),12.BPS(원),13.PBR(배),14.주당배당금(원),15.시가배당률(%),16.배당성향(%)
			}else {
				
			}
			
			Elements tradecompares = doc.select(".trade_compare tbody tr");
			if(tradecompares != null && tradecompares.size() > 0) {
				for(int i =0 ; i < tradecompares.size() ; i++) {
					String title =tradecompares.get(i).select("th span").text();
					String text = tradecompares.get(i).select("td").get(0).text().replace("하향", "").replace("%", "");
					resultMap.put("parameter"+(startnum++), text);
//					for(int j = 0 ; j < cdatas.size() ; j++) {
//						String place =cdatas.get(j).text();
//					}
					
				}
				
			}else {
				
			}
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	/**
	 * 코스피 종목 정보(네이버) 투자 정보 계산(네이버 증권 > 종목상세 : 종합정보)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockInfo_v01(String stockCode , int startnum, StockDefaultVO vo , Map<String, Object> orgMap) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String [] stockCodes = {"005930","035720","012450","034020","373220","000660"};
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		int start = startnum;
		
		try {
			//System.out.println("종목\t주가\t등락률\t시가\t고가\t저가\t거래량\t타입\t전일대비\t가져오는 시간");
			//for(int i = 0 ; i < stockCodes.length ; i++) {
				//String code = StringUtil.nvl(stockCodes[i],"");
				String code = StringUtil.nvl(stockCode,"");
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select(".new_totalinfo dl>dd");
				Elements summaryInfoList =doc.select(".summary_info p");//기업 주요 사업
				Elements copAnalysis =doc.select(".cop_analysis");//기업실적분석
			
				String summaryInfo ="";
				for(Element element : summaryInfoList) {
					summaryInfo += element.text();
				}
				String juga = "0";
				if(todaylist != null && todaylist.size() > 0) {					
					String name = todaylist.get(1).text().split(" ")[1] != null ? todaylist.get(1).text().split(" ")[1] : "";
					 juga = todaylist.get(3).text().split(" ")[1] != null ? todaylist.get(3).text().split(" ")[1] : "0";
					String DungRakrate = todaylist.get(3).text().split(" ")[6] != null ? todaylist.get(3).text().split(" ")[6] : "";
					if(todaylist.get(3).text().split(" ")[5] != null && todaylist.get(3).text().split(" ")[5].equals("마이너스")) {
						DungRakrate = "-"+DungRakrate;
					}
					//DungRakrate = DungRakrate+"%";
					String siga =  todaylist.get(5).text().split(" ")[1] != null ? todaylist.get(5).text().split(" ")[1] : "";
					String goga = todaylist.get(6).text().split(" ")[1] != null  ? todaylist.get(6).text().split(" ")[1] : "";
					String zeoga = todaylist.get(8).text().split(" ")[1] != null ? todaylist.get(8).text().split(" ")[1] : "";
					String georaeryang = todaylist.get(10).text().split(" ")[1] != null ? todaylist.get(10).text().split(" ")[1] : "";
	
					String stype = todaylist.get(3).text().split(" ")[3] != null ? todaylist.get(3).text().split(" ")[3] : ""; 
	
					String vsyesterday = todaylist.get(3).text().split(" ")[4] != null ? todaylist.get(3).text().split(" ")[4] : "";
					
					//System.out.println(name+"\t"+juga+"\t"+DungRakrate+"\t"+siga+"\t"+goga+"\t"+zeoga+"\t"+georaeryang+"\t"+stype+"\t"+vsyesterday+"\t"+str[0]+" "+str[1]);
					resultMap.put("parameter"+(startnum++), name);//6
					resultMap.put("parameter"+(startnum++), juga);//7
					resultMap.put("parameter"+(startnum++), DungRakrate.replace("퍼센트", ""));//8
					resultMap.put("parameter"+(startnum++), siga);//9
					resultMap.put("parameter"+(startnum++), goga);//10
					resultMap.put("parameter"+(startnum++), zeoga);//11
					resultMap.put("parameter"+(startnum++), georaeryang);//12
					resultMap.put("parameter"+(startnum++), stype);//13
					resultMap.put("parameter"+(startnum++), vsyesterday);//14
					resultMap.put("parameter"+(startnum++), str[0]+" "+str[1]);//15
					resultMap.put("parameter"+(startnum++), summaryInfo);//16
				}else {
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
					resultMap.put("parameter"+(startnum++), "");
				}
				Long maeibga3 =Long.parseLong(StringUtil.nvl(orgMap.get("parameter3"),"0").replace(",", ""));//매입가
				Long juga7 = Long.parseLong(juga.replace(",", ""));
				Long result1 = juga7-maeibga3;
				int cnt = Integer.parseInt(StringUtil.nvl(orgMap.get("parameter4"),"0").replace(",", ""));
				Long totplace = maeibga3 * cnt;
				float  result2 = (float) Math.round((float)result1/maeibga3*10000)/100;
				//System.out.println(juga7+":"+result1+"/"+maeibga3+"="+Math.round((float)result1/maeibga3*10000)+":"+((float)result1/maeibga3*10000)+":"+result2);
				resultMap.put("parameter"+(startnum++), result2);
				resultMap.put("parameter"+(startnum++), ((int)(totplace * (result2/100))));
			
			
			//}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	/**
	 * 코스피 종목 정보(네이버) 투자 정보 계산(네이버 증권 > 종목상세 : 종합정보(투자정보))
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockInfo_v02(String stockCode) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String URL = naver_domain_url+"/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		
		try {
			doc = Jsoup.connect(URL).get();
			Elements trs = doc.select("#tab_con1 .rwidth tbody tr");
			if (trs != null && trs.size() == 2) {
			Element firstTr = doc.select("#tab_con1 .rwidth tbody tr").first();
			String thText = firstTr.select("th").text();
			String[] titles = thText.split("l");
			String investOpinionTitle = titles[0].trim(); // 투자의견
			String targetPriceTitle   = titles[1].trim(); // 목표주가
			String score = "";//투자의견 점수
			String opinion = "";//투자의견 (매수)
			String targetPrice = "";
			if (firstTr != null) {
			    score = firstTr.select("span.f_up em").text();
			    opinion = firstTr.select("span.f_up").text().replace(score, "").trim();
			    targetPrice = firstTr.select("td > em").text();
			}
			//System.out.println(investOpinionTitle+"=>"+score+" "+opinion);
			//System.out.println(targetPriceTitle+"=>"+targetPrice);
			resultMap.put("investOpinionTitle", investOpinionTitle);
			resultMap.put("investOpinionScore", score);
			resultMap.put("investOpinionText", opinion);
			resultMap.put("targetPriceTitle", targetPriceTitle);
			resultMap.put("targetPrice", targetPrice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	
	
	/**
	 * 업종 내역 조회
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockSectorsList() throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = naver_domain_url+"/sise/sise_group.naver?type=upjong"; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select("#contentarea_left td>a");
				
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String href = todaylist.get(i).attr("href").substring(todaylist.get(i).attr("href").lastIndexOf("=")+1);
					String td1 = todaylist.get(i).parent().parent().select("td").eq(1).text();
					String td2 = todaylist.get(i).parent().parent().select("td").eq(2).text();
					String td3 = todaylist.get(i).parent().parent().select("td").eq(3).text();
					String td4 = todaylist.get(i).parent().parent().select("td").eq(4).text();
					String td5 = todaylist.get(i).parent().parent().select("td").eq(5).text();
					String text = todaylist.get(i).text();
					resultMap.put("parameter"+0, (i+1));
					resultMap.put("parameter"+1, text);
					resultMap.put("parameter"+2, href);
					resultMap.put("parameter"+3, td1);//전일대비
					resultMap.put("parameter"+4, td2);//전체
					resultMap.put("parameter"+5, td3);//상승
					resultMap.put("parameter"+6, td4);//보합
					resultMap.put("parameter"+7, td5);//하락
					if(td1.indexOf("+") > (-1)) {
						resultMap.put("parameter"+8, "red");
					}else if(td1.indexOf("-") > (-1)) {
						resultMap.put("parameter"+8, "blue");
					}else {
						resultMap.put("parameter"+8, "gray");
					}
					//System.out.println(resultMap);
					list.add(resultMap);
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 테마 내역 조회
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockThemeList(Integer page , int cnt) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = naver_domain_url+"/sise/theme.naver?&page="+page; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select("#contentarea_left tr");
				int num = cnt+1;
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					if(todaylist.get(i).select("td.col_type1 a") != null) {
						//테마 기업 내역
						String theme_code = todaylist.get(i).select("td.col_type1 a").attr("href").substring(todaylist.get(i).select("td.col_type1 a").attr("href").lastIndexOf("=")+1);
						String theme_name = todaylist.get(i).select("td.col_type1 a").text();
						String col_type2 = todaylist.get(i).select("td.col_type2 span").text();
						
						if("".equals(theme_name))continue;
						//테마 주도주1 기업
						String company_code1 = todaylist.get(i).select("td.col_type5 a").attr("href").substring(todaylist.get(i).select("td.col_type5 a").attr("href").lastIndexOf("=")+1);
						String company_name1 = todaylist.get(i).select("td.col_type5 a").text();
						//테마 주도주2 기업
						String company_code2 = todaylist.get(i).select("td.col_type6 a").attr("href").substring(todaylist.get(i).select("td.col_type6 a").attr("href").lastIndexOf("=")+1);
						String company_name2 = todaylist.get(i).select("td.col_type6 a").text();
					
						resultMap.put("parameter"+0, (num++));
						resultMap.put("parameter"+1, theme_name);
						resultMap.put("parameter"+2, theme_code);
						resultMap.put("parameter"+3, col_type2);
						resultMap.put("parameter"+4, company_name1);
						resultMap.put("parameter"+5, company_code1);
						resultMap.put("parameter"+6, company_name2);
						resultMap.put("parameter"+7, company_code2);
						if(col_type2.indexOf("+") > (-1)) {
							resultMap.put("parameter"+8, "red");
						}else if(col_type2.indexOf("-") > (-1)) {
							resultMap.put("parameter"+8, "blue");
						}else {
							resultMap.put("parameter"+8, "gray");
						}
//						System.out.println((num-1)+"."+resultMap);
						list.add(resultMap);
					
					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 테마 내역 조회(검색)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockThemeSearchList(Integer page , int cnt , Map<String, Object> paramMap) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String searchKeyword = StringUtil.nvl(paramMap.get("searchKeyword"),"");
		String gubun = StringUtil.nvl(paramMap.get("gubun"),"");
		String URL = naver_domain_url+"/sise/theme.naver?&page="+page; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select("#contentarea_left tr");
				int num = cnt+1;
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					if(todaylist.get(i).select("td.col_type1 a") != null) {
						//테마 기업 내역
						String theme_code = todaylist.get(i).select("td.col_type1 a").attr("href").substring(todaylist.get(i).select("td.col_type1 a").attr("href").lastIndexOf("=")+1);
						String theme_name = todaylist.get(i).select("td.col_type1 a").text();
						String col_type2 = todaylist.get(i).select("td.col_type2 span").text();
						
						if("".equals(theme_name))continue;
						//테마 주도주1 기업
						String company_code1 = todaylist.get(i).select("td.col_type5 a").attr("href").substring(todaylist.get(i).select("td.col_type5 a").attr("href").lastIndexOf("=")+1);
						String company_name1 = todaylist.get(i).select("td.col_type5 a").text();
						//테마 주도주2 기업
						String company_code2 = todaylist.get(i).select("td.col_type6 a").attr("href").substring(todaylist.get(i).select("td.col_type6 a").attr("href").lastIndexOf("=")+1);
						String company_name2 = todaylist.get(i).select("td.col_type6 a").text();
					
						resultMap.put("parameter"+0, (num++));
						resultMap.put("parameter"+1, theme_name);
						resultMap.put("parameter"+2, theme_code);
						resultMap.put("parameter"+3, col_type2);
						resultMap.put("parameter"+4, company_name1);
						resultMap.put("parameter"+5, company_code1);
						resultMap.put("parameter"+6, company_name2);
						resultMap.put("parameter"+7, company_code2);
						if(col_type2.indexOf("+") > (-1)) {
							resultMap.put("parameter"+8, "red");
						}else if(col_type2.indexOf("-") > (-1)) {
							resultMap.put("parameter"+8, "blue");
						}else {
							resultMap.put("parameter"+8, "gray");
						}
						if(!"".equals(searchKeyword) && "".equals(gubun)) {
							String themename = StringUtil.nvl(resultMap.get("parameter1"),"");
							String stockname = StringUtil.nvl(resultMap.get("parameter2"),"");
							if(themename.indexOf(searchKeyword) > (-1)) {
								list.add(resultMap);
							}else {
								num--;
							}
						}else {
							list.add(resultMap);
						}
						
					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	
	/**
	 * 테마 및 업종 상세 정보 내역
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param order : 테마, 업종 순번
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockUpjongThemeList(String no , String type , int order) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
		//System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				//Elements elem = doc.select(".date");
				//String[] str = elem.text().split(" ");
				String title = "";
				String info =  "";
				Elements thead = doc.select(".box_type_l thead tr").eq(0).select("th");
				Elements todaylist = doc.select(".box_type_l tbody tr");
				if("upjong".equals(type)) {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).text();
					info = doc.select(".theme_info_area .info_txt").text();
				}else {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_title").text();//테마명
					info = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_txt").text();	//테마설명			
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				int num = 1;
				resultMap.put("parameter"+0, order);
				resultMap.put("parameter"+1, title);
				resultMap.put("parameter"+2, "0");
				resultMap.put("parameter"+3, "");
				resultMap.put("parameter"+4, "");
				resultMap.put("parameter"+5, "");
				resultMap.put("parameter"+6, "");
				resultMap.put("parameter"+7, info);
				System.out.println("0."+resultMap);
				list.add(resultMap);
				for(int i = 0 ; i < todaylist.size() ; i++) {
					resultMap = new HashMap<String, Object>();
					if(todaylist.get(i).select("td .name_area a") != null) {
						//기업 내역
						
						String code = todaylist.get(i).select("td .name_area a").attr("href").substring(todaylist.get(i).select("td .name_area a").attr("href").lastIndexOf("=")+1);
						String name = todaylist.get(i).select("td .name_area a").text();//종목명
						String dot = todaylist.get(i).select("td .name_area .dot").text();//코스피 , 코스닥
						String info1 = todaylist.get(i).select("td .theme_info_area .info_txt").text();//설명
//						String red01 = StringUtil.nvl(todaylist.get(i).select("td .red01").text());//등락률
//						String nv01 = StringUtil.nvl(todaylist.get(i).select("td .nv01").eq(i).text());
						String td2 = StringUtil.nvl(todaylist.get(i).select("td").eq(2).text());//현재가
						String td3_p11 = StringUtil.nvl(todaylist.get(i).select("td").eq(3).select(".p11").text());//전일비
						String td3_blind = StringUtil.nvl(todaylist.get(i).select("td").eq(3).select(".blind").text());
						String td4_p11 = StringUtil.nvl(todaylist.get(i).select("td").eq(4).select("span").text());//등락률
						String td5 = StringUtil.nvl(todaylist.get(i).select("td").eq(5).text());//매수호가
						String td6 = StringUtil.nvl(todaylist.get(i).select("td").eq(6).text());//매도호가
						String td7 = StringUtil.nvl(todaylist.get(i).select("td").eq(7).text());//거래량
						String td8 = StringUtil.nvl(todaylist.get(i).select("td").eq(8).text());//거래대금
						String td9 = StringUtil.nvl(todaylist.get(i).select("td").eq(9).text());//전일거래량
						String dotGubun = "코스피";
						if(!"".equals(StringUtil.nvl(dot))) {
							dotGubun = "코스닥";
						}
						
						if("".equals(name))continue;
//						if(num == 1) {
//							resultMap.put("parameter"+0, order);
//							resultMap.put("parameter"+1, title);
//						}else {
//							resultMap.put("parameter"+0, "");
//							resultMap.put("parameter"+1, "");
//						}
						resultMap.put("parameter"+0, "");
						resultMap.put("parameter"+1, "");
						resultMap.put("parameter"+2, (num++));
						resultMap.put("parameter"+3, name);
						resultMap.put("parameter"+4, dot);
						resultMap.put("parameter"+5, code);
						resultMap.put("parameter"+6, td4_p11);//등락률
						resultMap.put("parameter"+9, td2);//현재가
						resultMap.put("parameter"+10, td3_p11);//전일비
//						if((num-1) == 1) {
//							resultMap.put("parameter"+6, info);
//						}else {
//							resultMap.put("parameter"+6, info1);
//						}
						resultMap.put("parameter"+7, info1);
						
						String infoURL = naver_domain_url+"/item/main.naver?code="+code; //NAVER 주식 
						Document infodoc = Jsoup.connect(infoURL).get();
						Elements elem = infodoc.select(".date");
						String[] str = elem.text().split(" ");
						Elements summaryInfoList =infodoc.select(".summary_info p");
						String summaryInfo ="";
						int pnum = 0;
						for(Element element : summaryInfoList) {
							pnum++;
							if(!"".equals(summaryInfo))summaryInfo += "\n";
							summaryInfo += pnum+"."+element.text();
						}
						resultMap.put("parameter"+8, summaryInfo);
						
						System.out.println((num-1)+"."+resultMap);
						list.add(resultMap);
					
					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 테마 및 업종 상세 정보 내역 버전 1
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param titleList : 타이틀 리스트
	 * @param fieldIds : 검색 할 항목
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockUpjongThemeList_V01(String no , String type , List<Map<String, Object>> titleList, String [] fieldIds) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String p_fieldIds="";
		if(fieldIds != null) {
			for(String fieldId : fieldIds) {
				p_fieldIds += "&fieldIds="+fieldId;
			}
		}
//		String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no + p_fieldIds; //NAVER 주식
		String URL = naver_field_del_url.replaceAll("theme", type)+no; //NAVER 주식 
		if(!"D".equals(type)) {
			if(fieldIds != null) {
				for(String fieldId : fieldIds) {
					p_fieldIds += "&fieldIds="+fieldId;
				}
			}
			URL = naver_field_submit_url.replaceAll("theme", type)+no+p_fieldIds;
		}
		System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				String title = "";
				String info =  "";
				Elements thead = doc.select(".box_type_l .type_5 thead tr").eq(0).select("th");
				Elements todaylist = doc.select(".box_type_l .type_5 tbody").select("tr");
				if("upjong".equals(type)) {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).text();
					info = doc.select(".theme_info_area .info_txt").text();
				}else {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_title").text();//테마명
					info = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_txt").text();	//테마설명			
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				Map<String, Object> idxMap = new HashMap<String, Object>();
				List<Object> resultList = new ArrayList<Object>();
				
				int num = 0;
				for(int i = 0 ; i < todaylist.size() ; i++) {
					resultMap = new HashMap<String, Object>();
					resultList = new ArrayList<Object>();
					Elements selectElement = todaylist.get(i).select("td");
					num = 0;
					String code = "";
//					System.out.println(selectElement.size());
					if(selectElement.size() > 5) {
						for(Element elem : selectElement) {
							int idx = elem.elementSiblingIndex();
							idxMap = new HashMap<String, Object>();
							idxMap.put("num", num);
							idxMap.put("text", "");
							idxMap.put("dot", "");
							idxMap.put("dotNm", "");
							idxMap.put("code", "");
							idxMap.put("color", "");
							idxMap.put("blind", "");
//							System.out.println(i+":["+idx+"]:"+(num));
							if("center".equals(elem.attr("class"))) {
								continue;//토론 제외
							}
							if(idx == 0) {
								code = elem.select(".name_area a").attr("href").substring(elem.select(".name_area a").attr("href").lastIndexOf("=")+1);
								String name = elem.select(".name_area a").text();//종목명
								String dot = elem.select(".name_area .dot").text();//코스피 , 코스닥
								String dotGubun = "코스피";
								if(!"".equals(StringUtil.nvl(dot))) {
									dotGubun = "코스닥";
								}
								resultMap.put("parameter"+idx, name);
								resultMap.put("parameter"+idx+"_dot", dot);
								resultMap.put("parameter"+idx+"_dotNm", dotGubun);
								resultMap.put("parameter"+idx+"_code", code);
								resultMap.put("stockname", name);
								
								idxMap.put("text", name);
								idxMap.put("dot", dot);
								idxMap.put("dotNm", dotGubun);
								idxMap.put("code", code);
								
								resultList.add(idx , idxMap);
								
							}else if(idx == 1) {
//								idxMap = new HashMap<String, Object>();
								if("upjong".equals(type)) {
									resultMap.put("parameter"+idx, StringUtil.nvl(elem.text()));
									idxMap.put("text", StringUtil.nvl(elem.text()));
									resultList.add(idx , idxMap);
								}else {
									String einfo = elem.select(".theme_info_area .info_txt").text();//설명
									resultMap.put("parameter"+idx, einfo);
									idxMap.put("text", einfo);
									resultList.add(idx , idxMap);
								}
							}else if(idx == 2) {	
//								idxMap = new HashMap<String, Object>();
								if("upjong".equals(type)) {
									String blind = elem.select(".blind").text().trim();
									String tah = elem.select(".tah").text().trim();
									String blind_color = "#464646";
									if("상승".equals(blind)) {
										blind_color = "#D90400";
									}else if("하락".equals(blind)) {
										blind_color = "#005DDE";
									}
									resultMap.put("parameter"+idx, tah);
									idxMap.put("text", tah);
									idxMap.put("color", blind_color);
									resultList.add(idx , idxMap);
								}else {
									resultMap.put("parameter"+idx, StringUtil.nvl(elem.text()));
									idxMap.put("text", StringUtil.nvl(elem.text()));
									resultList.add(idx , idxMap);
								}
							}else if(idx == 3) {//전일비	, 등락률
//								idxMap = new HashMap<String, Object>();
								if("upjong".equals(type)) {
									String td2_p11 = StringUtil.nvl(elem.select(".p11").text());//전일비
									String blind_color = "#464646";
									if(td2_p11.indexOf("+") > (-1)) {
										blind_color = "#D90400";
									}else if(td2_p11.indexOf("-") > (-1)) {
										blind_color = "#005DDE";
									}
									resultMap.put("parameter"+idx, td2_p11);
									resultMap.put("parameter"+idx+"_color", blind_color);
									
									idxMap.put("text", td2_p11);
									idxMap.put("color", blind_color);
								}else {
									String td2_p11 = StringUtil.nvl(elem.select(".p11").text());//전일비
									String td2_blind = StringUtil.nvl(elem.select(".blind").text().trim());
									String blind_color = "#464646";
									if("상승".equals(td2_blind)) {
										blind_color = "#D90400";
									}else if("하락".equals(td2_blind)) {
										blind_color = "#005DDE";
									}
									resultMap.put("parameter"+idx, td2_p11);
									resultMap.put("parameter"+idx+"_blind", td2_blind);
									resultMap.put("parameter"+idx+"_color", blind_color);
									
									idxMap.put("text", td2_p11);
									idxMap.put("blind", td2_blind);
									idxMap.put("color", blind_color);
								}
								resultList.add(idx , idxMap);
							}else if(idx == 4) {
//								idxMap = new HashMap<String, Object>();
								if("upjong".equals(type)) {
									resultMap.put("parameter"+idx, StringUtil.nvl(elem.text()));
									idxMap.put("text", StringUtil.nvl(elem.text()));
								}else {
									String td3_p11 = StringUtil.nvl(elem.select("span").text());//등락률
									String blind_color = "#464646";
									if(td3_p11.indexOf("+") > (-1)) {
										blind_color = "#D90400";
									}else if(td3_p11.indexOf("-") > (-1)) {
										blind_color = "#005DDE";
									}
									resultMap.put("parameter"+idx, td3_p11);
									resultMap.put("parameter"+idx+"_color", blind_color);
									
									
									idxMap.put("text", td3_p11);
									idxMap.put("color", blind_color);
								}
								resultList.add(idx , idxMap);
							}else {
//								idxMap = new HashMap<String, Object>();
								resultMap.put("parameter"+idx, StringUtil.nvl(elem.text()));
								idxMap.put("text", StringUtil.nvl(elem.text()));
								resultList.add(idx , idxMap);
							}
							num ++;
						}
						String infoURL = naver_domain_url+"/item/main.naver?code="+code; //NAVER 주식 
						Document infodoc = Jsoup.connect(infoURL).get();
						Elements elem = infodoc.select(".date");
						String[] str = elem.text().split(" ");
						Elements summaryInfoList =infodoc.select(".summary_info p");
						String summaryInfo ="";
						int pnum = 0;
						for(Element element : summaryInfoList) {
							pnum++;
							if(!"".equals(summaryInfo))summaryInfo += "\n";
							summaryInfo += pnum+"."+element.text();
						}
						resultMap.put("parameter"+num, summaryInfo);
						idxMap = new HashMap<String, Object>();
						idxMap.put("num", num);
						idxMap.put("text", summaryInfo);
						idxMap.put("dot", "");
						idxMap.put("dotNm", "");
						idxMap.put("color", "");
						idxMap.put("code", "");
						idxMap.put("blind", "");
						resultList.add(num,idxMap);
						resultMap.put("num", (i+1));
						resultMap.put("max_num", (selectElement.size()-1));
						resultMap.put("parameters", resultList);
//						System.out.print(i+".");
//						System.out.println(resultMap);
						
						list.add(resultMap);
						
					}
					
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * 테마 및 업종 정보
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param fieldIds : 검색 할 항목
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getStockUpjongThemeInfo(String no , String type) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>(); 

		String URL = naver_field_del_url.replaceAll("theme", type)+no; //NAVER 주식 
	
		System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements tbody3 = doc.select(".type_1 tbody tr").eq(3);
				String pam0_title = tbody3.select("td").eq(0).select(".info_title").text();
				String pam0_text = tbody3.select("td").eq(0).select(".info_txt").text();
				String pam1 = tbody3.select("td").eq(1).select(".p11").text();
				String pam2 = tbody3.select("td").eq(2).text();
				String pam3 = tbody3.select("td").eq(3).text();
				String pam4 = tbody3.select("td").eq(4).text();
				String pam5 = tbody3.select("td").eq(5).text();
				String pam6 = tbody3.select("td").eq(6).select(".graph_txt").text();
				
				resultMap.put("pam0_title", pam0_title);
				resultMap.put("pam0_text", pam0_text);
				resultMap.put("pam1", pam1);
				resultMap.put("pam2", pam2);
				resultMap.put("pam3", pam3);
				resultMap.put("pam4", pam4);
				resultMap.put("pam5", pam5);
				resultMap.put("pam6", pam6);
			
				System.out.println(resultMap);
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	/**
	 * 테마 및 업종 상세 정보 내역에서 field 검색
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("null")
	public static String [] getStockUpjongThemeFieldS(String no , String type) throws Exception{
		String [] return_fieldIds = null;
		//String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no+p_fieldIds; //NAVER 주식 
		String URL = naver_field_del_url.replaceAll("theme", type)+no; //NAVER 주식 
		System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements theadlist = doc.select("form[name=field_form] table tbody").select(".choice");
				return_fieldIds = new String[theadlist.size()];
				for(int i = 0 ; i < theadlist.size() ; i++) {
					Element fieldIdsInput = theadlist.get(i).selectFirst("input[name=fieldIds]");
					String value="";
					String label = "";
					if(fieldIdsInput != null) {
						value = fieldIdsInput.attr("value");
					}
					if(theadlist.get(i).select("label") != null) {
						label = theadlist.get(i).select("label").text();
					}
					return_fieldIds[i] = value;
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return return_fieldIds;
	}
	
	
	/**
	 * 테마 및 업종 상세 정보 내역 버전 1 타이틀 검색
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param fieldIds : 검색 할 항목
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockUpjongThemeTitleList_V01(String no , String type , String [] fieldIds) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
		String p_fieldIds="";
		
		//String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no+p_fieldIds; //NAVER 주식 
		String URL = naver_field_del_url.replaceAll("theme", type)+no; //NAVER 주식 
		if(!"D".equals(type)) {
			if(fieldIds != null) {
				for(String fieldId : fieldIds) {
					p_fieldIds += "&fieldIds="+fieldId;
				}
			}
			URL = naver_field_submit_url.replaceAll("theme", type)+no+p_fieldIds;
		}
		System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements theadlist = doc.select(".box_type_l").eq(1).select(".type_5 thead tr").eq(0).select("th");
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				int num = 0;
				for(int i = 0 ; i < theadlist.size()-1 ; i++) {
					resultMap = new HashMap<String, Object>();
					String titlenm = theadlist.get(i).text();//타이틀명
					String colspan = StringUtil.nvl(theadlist.get(i).attr("colspan"),"1");
					resultMap.put("num", num);
					resultMap.put("title", titlenm);
					resultMap.put("colspan", colspan);
					resultList.add(resultMap);
					num = i+1;					
				}
				System.out.println(resultList);
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultList;
	}
	
	/**
	 * 종목 상세 정보 내역
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param gubun : 상승(+) , 하락(-) 종목 구분
	 * @param order : 테마, 업종 순번
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockUpjongThemeList(String no , String type , String gubun , int order) throws Exception{
		System.out.println(order+".getStockUpjongThemeList start");
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
		//System.out.println(URL);
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				//Elements elem = doc.select(".date");
				//String[] str = elem.text().split(" ");
				String title = "";
				String info =  "";
				Elements todaylist = doc.select(".box_type_l tr");
				if("upjong".equals(type)) {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).text();
					info = doc.select(".theme_info_area .info_txt").text();
				}else {
					title = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_title").text();
					info = doc.select(".type_1 tr").eq(3).select("td").eq(0).select(".theme_info_area .info_txt").text();					
				}
				Map<String, Object> resultMap = new HashMap<String, Object>();
				
				int num = 1;
				resultMap.put("parameter"+0, order);
				resultMap.put("parameter"+1, title);
				resultMap.put("parameter"+2, "0");
				resultMap.put("parameter"+3, "");
				resultMap.put("parameter"+4, "");
				resultMap.put("parameter"+5, "");
				resultMap.put("parameter"+6, "");
				resultMap.put("parameter"+7, info);
				System.out.println(resultMap);
				list.add(resultMap);
				for(int i = 0 ; i < todaylist.size() ; i++) {
					resultMap = new HashMap<String, Object>();
					if(todaylist.get(i).select("td .name_area a") != null) {
						//기업 내역
						String code = todaylist.get(i).select("td .name_area a").attr("href").substring(todaylist.get(i).select("td .name_area a").attr("href").lastIndexOf("=")+1);
						String name = todaylist.get(i).select("td .name_area a").text();
						String dot = todaylist.get(i).select("td .name_area .dot").text();
						String info1 = todaylist.get(i).select("td .theme_info_area .info_txt").text();
						String red01 = StringUtil.nvl(todaylist.get(i).select("td .red01").text());
						String nv01 = StringUtil.nvl(todaylist.get(i).select("td .nv01").eq(i).text());
						String p11 = StringUtil.nvl(todaylist.get(i).select("td").eq(4).select("span").text());
						String dotGubun = "코스피";
						if(!"".equals(StringUtil.nvl(dot))) {
							dotGubun = "코스닥";
						}
						
						if("".equals(name) || (!"".equals(gubun) && p11.indexOf(gubun) < 0 ))continue;
//						if(num == 1) {
//							resultMap.put("parameter"+0, order);
//							resultMap.put("parameter"+1, title);
//						}else {
//							resultMap.put("parameter"+0, "");
//							resultMap.put("parameter"+1, "");
//						}
						resultMap.put("parameter"+0, "");
						resultMap.put("parameter"+1, "");
						resultMap.put("parameter"+2, (num++));
						resultMap.put("parameter"+3, name);
						resultMap.put("parameter"+4, dot);
						resultMap.put("parameter"+5, code);
						resultMap.put("parameter"+6, p11);
//						if((num-1) == 1) {
//							resultMap.put("parameter"+6, info);
//						}else {
//							resultMap.put("parameter"+6, info1);
//						}
						resultMap.put("parameter"+7, info1);
						
						String infoURL = naver_domain_url+"/item/main.naver?code="+code; //NAVER 주식 
						Document infodoc = Jsoup.connect(infoURL).get();
						Elements elem = infodoc.select(".date");
						String[] str = elem.text().split(" ");
						Elements summaryInfoList =infodoc.select(".summary_info p");//기업개요
						String summaryInfo ="";
						int pnum = 0;
						for(Element element : summaryInfoList) {
							pnum++;
							if(!"".equals(summaryInfo))summaryInfo += "\n";
							summaryInfo += pnum+"."+element.text();
						}
						resultMap.put("parameter"+8, summaryInfo);
						
						//System.out.println((num-1)+"."+resultMap);
						list.add(resultMap);
					
					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		System.out.println(order+".getStockUpjongThemeList end");
		return list;
	}
	
	/**
	 * 종목 정보
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param order : 테마, 업종 순번
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockInfo(String no , String type) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
		String URL = naver_domain_url+"/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements theadlist = doc.select(".box_type_l .type_5 thead tr").eq(0).select("th");
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				int num = 0;
				for(int i = 0 ; i < theadlist.size() ; i++) {
					resultMap = new HashMap<String, Object>();
					String titlenm = theadlist.get(i).text();//타이틀명
					String colspan = StringUtil.nvl(theadlist.get(i).attr("colspan"),"1");
					resultMap.put("num", num);
					resultMap.put("title", titlenm);
					resultMap.put("colspan", colspan);
					resultList.add(resultMap);
					num = i+1;
					
				}
				System.out.println(resultList);
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultList;
	}
	
	
	/**
	 * 네이버 검색
	 * @param search_type : 검색 타입 
	 * @param return_type : 결과 타입
	 * @param display : 조회 건수 => 한 번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
	 * @param start : 검색 시작 위치(기본값: 1, 최댓값: 1000)
	 * @param start : 검색 시작 위치(기본값: 1, 최댓값: 1000)
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
	public static NaverSearchResponseVO getNaverSearchApi(String search_type , String return_type , int display , int start , String searchKeyword) throws Exception{
		NaverSearchResponseVO naverSearchvo = new NaverSearchResponseVO();
		 String encodedKeyword;
	        try {
	            encodedKeyword = URLEncoder.encode(searchKeyword, "UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException("검색어 인코딩 실패", e);
	        }
		StringBuilder apiURL = new StringBuilder(nevar_serach_url)
		        .append("/")
		        .append(search_type)
		        .append(".")
		        .append(return_type)
		        .append("?query=")
		        .append(encodedKeyword)
		        .append("&display=")
		        .append(display)
				.append("&start=")
		        .append(start);
		Map<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("X-Naver-Client-Id", nevar_clientId);
		requestHeaders.put("X-Naver-Client-Secret", nevar_clientSecret);
		try {
		        String responseBody = get(apiURL,requestHeaders);
		        System.out.println(responseBody);
		        if("xml".equals(return_type)) {
		        	naverSearchvo = StringUtil.getXmlStringToNaverSearchResponseVO(responseBody);
		        }else {
		        	   Gson gson = new Gson();
		        	   naverSearchvo = gson.fromJson(responseBody, NaverSearchResponseVO.class);
		        }
		        System.out.println(naverSearchvo.getItems());
		}catch (Exception e) {
			e.printStackTrace();
		} 
		return naverSearchvo;
	}
	
	 private static String get(StringBuilder apiUrl, Map<String, String> requestHeaders){
	        HttpURLConnection con = connect(apiUrl);
	        try {
	            con.setRequestMethod("GET");
	            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	                con.setRequestProperty(header.getKey(), header.getValue());
	            }


	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
	                return readBody(con.getInputStream());
	            } else { // 오류 발생
	                return readBody(con.getErrorStream());
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("API 요청과 응답 실패", e);
	        } finally {
	            con.disconnect();
	        }
	    }
	 
	 private static HttpURLConnection connect(StringBuilder apiUrl){
	        try {
	            URL url = new URL(apiUrl.toString());
	            return (HttpURLConnection)url.openConnection();
	        } catch (MalformedURLException e) {
	            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	        } catch (IOException e) {
	            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	        }
	    }
	 
	 private static String get(String apiUrl, Map<String, String> requestHeaders){
	        HttpURLConnection con = connect(apiUrl);
	        try {
	            con.setRequestMethod("GET");
	            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
	                con.setRequestProperty(header.getKey(), header.getValue());
	            }


	            int responseCode = con.getResponseCode();
	            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
	                return readBody(con.getInputStream());
	            } else { // 오류 발생
	                return readBody(con.getErrorStream());
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("API 요청과 응답 실패", e);
	        } finally {
	            con.disconnect();
	        }
	    }
	  
	  private static HttpURLConnection connect(String apiUrl){
	        try {
	            URL url = new URL(apiUrl);
	            return (HttpURLConnection)url.openConnection();
	        } catch (MalformedURLException e) {
	            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
	        } catch (IOException e) {
	            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
	        }
	    }


	    private static String readBody(InputStream body){
	        InputStreamReader streamReader = new InputStreamReader(body);


	        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
	            StringBuilder responseBody = new StringBuilder();


	            String line;
	            while ((line = lineReader.readLine()) != null) {
	                responseBody.append(line);
	            }


	            return responseBody.toString();
	        } catch (IOException e) {
	            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
	        }
	    }
	
	    /**
		 * 리서치 내역 조회
		 * @param savePath
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String, Object>> getStockResearchList(Map<String, Object> paramMap) throws Exception{
			System.out.println("getStockResearchList START");
			String searchGubun = StringUtil.nvl(paramMap.get("searchGubun"),"company");//리서치 구분
			String searchType = StringUtil.nvl(paramMap.get("searchType"),"");//검색타입
			String keyword = StringUtil.nvl(paramMap.get("keyword"),"");//제목+내용			
			String writeFromDate = StringUtil.nvl(paramMap.get("writeFromDate"),"");//검색시작일
			String writeToDate = StringUtil.nvl(paramMap.get("writeToDate"),"");//검색종료일
			String itemName = StringUtil.nvl(paramMap.get("itemName"),"");//종목명
			String itemCode = StringUtil.nvl(paramMap.get("itemCode"),"");//종목코드
			String brokerCode = StringUtil.nvl(paramMap.get("brokerCode"),"");//증권사
			String upjong = StringUtil.nvl(paramMap.get("upjong"),"");//업종
			String page = StringUtil.nvl(paramMap.get("page"),"1");//페이지
			try {
				keyword = URLEncoder.encode(keyword, "EUC-KR");
				upjong = URLEncoder.encode(upjong, "EUC-KR");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("검색어 인코딩 실패", e);
			}
			
			StringBuilder URL = new StringBuilder(naver_domain_url+"/research/"+searchGubun+"_list.naver?").append("searchType=").append(searchType);
			
			if(!"".equals(keyword)) URL.append("&keyword=").append(keyword);
			if(!"".equals(writeFromDate)) URL.append("&writeFromDate=").append(writeFromDate);
			if(!"".equals(writeToDate)) URL.append("&writeToDate=").append(writeToDate);
			if(!"".equals(itemName)) URL.append("&itemName=").append(itemName);
			if(!"".equals(itemCode)) URL.append("&itemCode=").append(itemCode);
			if(!"".equals(brokerCode)) URL.append("&brokerCode=").append(brokerCode);
			if(!"".equals(upjong)) URL.append("&upjong=").append(upjong);
			
			URL.append("&page=").append(page);
			
//			String URL = naver_domain_url+"/research/"+searchGubun+"_list.naver"; //NAVER 주식 
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
//			String URL = "https://finance.naver.com/research/industry_list.naver?keyword=&brokerCode=&searchType=writeDate&writeFromDate=2025-03-01&writeToDate=2025-03-07&itemName=&itemCode="; //NAVER 주식 
			System.out.println("URL=>"+URL);
			Document doc;
			try {
					doc = Jsoup.connect(URL.toString()).get();
					Elements elem = doc.select("#contentarea_left");
					String title = elem.select(".sub_tlt").text();
					System.out.println("title=>"+title);
					Elements pageElem = elem.select(".Nnavi tr");//페이지 리스트
					String pgLL = StringUtil.nvl(pageElem.select(".pgLL a").attr("href"),"0");//맨앞
					String pgL = StringUtil.nvl(pageElem.select(".pgL a").attr("href"),"0");//이전
					String pgR = StringUtil.nvl(pageElem.select(".pgR a").attr("href"),"0");//다음
					String pgRR = StringUtil.nvl(pageElem.select(".pgRR a").attr("href"),"0");//맨뒤
					String pgOn = StringUtil.nvl(pageElem.select(".on a").attr("href"),"1");//현재 페이지
					
					if(!"0".equals(pgLL))pgLL = pgLL.substring(pgLL.lastIndexOf("=")+1);
					if(!"0".equals(pgL))pgL = pgL.substring(pgL.lastIndexOf("=")+1);
					if(!"0".equals(pgR))pgR = pgR.substring(pgR.lastIndexOf("=")+1);
					if(!"0".equals(pgRR))pgRR = pgRR.substring(pgRR.lastIndexOf("=")+1);
					if(!"1".equals(pgOn))pgOn = pgOn.substring(pgOn.lastIndexOf("=")+1);
					
					System.out.println("pgLL:"+pgLL+", pgL:"+pgL+", pgR:"+pgR+", pgRR:"+pgRR);
					Elements todaylist =elem.select(".type_1 tr");//리스트
					int num = 1;
					int coulnmleng = 0;
					for(int i = 0 ; i < todaylist.size() ; i++) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						if(i == 0) {
							coulnmleng = todaylist.get(i).select("th").size();
							if(coulnmleng > 4) {
							for(int j = 0 ; j < coulnmleng ; j++) {
								resultMap.put("parameter"+j, todaylist.get(i).select("th").get(j).text());
							}
							resultMap.put("columsize", coulnmleng);
							resultMap.put("pgLL", pgLL);
							resultMap.put("pgL", pgL);
							resultMap.put("pgR", pgR);
							resultMap.put("pgRR", pgRR);
							resultMap.put("pgOn", pgOn);
							resultMap.put("title", title);
//							System.out.println(resultMap);
							list.add(resultMap);
							}
						}else {
							coulnmleng = todaylist.get(i).select("td").size();
							if(coulnmleng > 4) {
								for(int j = 0 ; j < coulnmleng ; j++) {
									Element jelem = todaylist.get(i).select("td").get(j);
									String classnm = StringUtil.nvl(jelem.attr("class"),"");
									String aclass = StringUtil.nvl(jelem.select("a").attr("href"),"");
									resultMap.put("parameter"+j, jelem.text());
									resultMap.put("parameter"+j+"_class", classnm);
									resultMap.put("parameter"+j+"_href", aclass);
								}
								System.out.print((num++)+".");
								System.out.println(resultMap);
								list.add(resultMap);
							}
						}
						
					}
					
					System.out.println("getStockResearchList END");
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return list;
		}
		
		/**
		 * 리서치 내역 조회
		 * @param savePath
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String, Object>> getStockResearchList_V01(Map<String, Object> paramMap) throws Exception{
			System.out.println("getStockResearchList_V01 START");

		    // 1. 파라미터 추출
		    String searchGubun = StringUtil.nvl(paramMap.get("searchGubun"), "company"); // 기본값 설정 권장 (예: company)
		    String searchType = StringUtil.nvl(paramMap.get("searchType"), "");
		    String keyword = StringUtil.nvl(paramMap.get("keyword"), "");
		    String writeFromDate = StringUtil.nvl(paramMap.get("writeFromDate"), "");
		    String writeToDate = StringUtil.nvl(paramMap.get("writeToDate"), "");
		    String itemName = StringUtil.nvl(paramMap.get("itemName"), "");
		    String itemCode = StringUtil.nvl(paramMap.get("itemCode"), "");
		    String brokerCode = StringUtil.nvl(paramMap.get("brokerCode"), "");
		    String upjong = StringUtil.nvl(paramMap.get("upjong"), "");
		    String page = StringUtil.nvl(paramMap.get("pageIndex"), "1");
		    if("".equals(searchType)) {
		    	keyword="";
		    	writeFromDate="";
		    	writeToDate="";
		    	itemCode="";
		    	itemName="";
		    	brokerCode="";
		    	upjong="";
		    }

		    // 2. 한글 파라미터 인코딩 (EUC-KR: 네이버 금융 레거시 페이지 호환)
		    try {
		        if (!"".equals(keyword)) keyword = URLEncoder.encode(keyword, "EUC-KR");
		        if (!"".equals(upjong)) upjong = URLEncoder.encode(upjong, "EUC-KR");
		        if (!"".equals(itemName)) itemName = URLEncoder.encode(itemName, "EUC-KR"); // [수정] 종목명도 인코딩 필수
		    } catch (UnsupportedEncodingException e) {
		        throw new RuntimeException("검색어 인코딩 실패", e);
		    }

		    // 3. URL 생성
		    StringBuilder urlBuilder = new StringBuilder(naver_domain_url + "/research/" + searchGubun + "_list.naver?");
		    	urlBuilder.append("searchType=").append(searchType);

		    if (!"".equals(keyword)) urlBuilder.append("&keyword=").append(keyword);
		    if (!"".equals(writeFromDate)) urlBuilder.append("&writeFromDate=").append(writeFromDate);
		    if (!"".equals(writeToDate)) urlBuilder.append("&writeToDate=").append(writeToDate);
		    if (!"".equals(itemName)) urlBuilder.append("&itemName=").append(itemName);
		    if (!"".equals(itemCode)) urlBuilder.append("&itemCode=").append(itemCode);
		    if (!"".equals(brokerCode)) urlBuilder.append("&brokerCode=").append(brokerCode);
		    if (!"".equals(upjong)) urlBuilder.append("&upjong=").append(upjong);
		    urlBuilder.append("&page=").append(page);

		    String requestUrl = urlBuilder.toString();
		    System.out.println("URL=>" + requestUrl);

		    ArrayList<Map<String, Object>> list = new ArrayList<>();

		    try {
		        // 4. Jsoup 연결 (User-Agent 및 Timeout 추가)
		        Document doc = Jsoup.connect(requestUrl)
		                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36") // [수정] 봇 차단 방지
		                .timeout(30000) // [수정] 10초 타임아웃
		                .get();

		        Elements elem = doc.select("#contentarea_left");
		        
		        // 요소가 없는 경우(페이지 구조 변경 혹은 로딩 실패) 방어 로직
		        if(elem.isEmpty()) {
		             System.out.println("컨텐츠 영역을 찾을 수 없습니다.");
		             return list;
		        }

		        String title = elem.select(".sub_tlt").text();
		        System.out.println("title=>" + title);

		        // 5. 페이징 정보 추출
		        Elements pageElem = elem.select(".Nnavi tr");
		        
		        // 헬퍼 메소드 혹은 로직을 사용하여 페이지 번호만 안전하게 추출
		        String pgLL = extractPageNum(pageElem.select(".pgLL a").attr("href"), "0");
		        String pgL = extractPageNum(pageElem.select(".pgL a").attr("href"), "0");
		        String pgR = extractPageNum(pageElem.select(".pgR a").attr("href"), "0");
		        String pgOn = extractPageNum(pageElem.select(".on a").attr("href"), "1");
		        String pgRR = extractPageNum(pageElem.select(".pgRR a").attr("href"), pgOn);

		        System.out.println("pgLL:" + pgLL + ", pgL:" + pgL + ", pgR:" + pgR + ", pgRR:" + pgRR+ ", pgOn:" + pgOn);

		        // 6. 리스트 파싱
		        Elements todaylist = elem.select(".type_1 tr");
		        int num = 1;

		        for (int i = 0; i < todaylist.size(); i++) {
		            Map<String, Object> resultMap = new HashMap<>();
		            Element row = todaylist.get(i);

		            // 헤더 처리 (i==0 일 때만 th가 존재한다고 가정)
		            Elements thCols = row.select("th");
		            if (!thCols.isEmpty()) {
		                 int colSize = thCols.size();
		                 // 컬럼 개수 체크 (네이버 리서치 테이블은 보통 4개 이상)
		                 if (colSize > 2) { 
		                    for (int j = 0; j < colSize; j++) {
		                        resultMap.put("parameter" + j, thCols.get(j).text());
		                    }
		                    resultMap.put("columsize", colSize);
		                    resultMap.put("pgLL", pgLL);
		                    resultMap.put("pgL", pgL);
		                    resultMap.put("pgR", pgR);
		                    resultMap.put("pgRR", pgRR);
		                    resultMap.put("pgOn", pgOn);
		                    resultMap.put("title", title);
		                    list.add(resultMap);
		                 }
		                 continue; // 헤더 처리 후 다음 루프로
		            }

		            // 데이터 행 처리
		            Elements tdCols = row.select("td");
		            int colSize = tdCols.size();
		            
		            // 구분선용 빈 tr 태그 등을 걸러내기 위한 조건 (컬럼 수 확인)
		            if (colSize > 2) {
		                for (int j = 0; j < colSize; j++) {
		                    Element jelem = tdCols.get(j);
		                    String classnm = StringUtil.nvl(jelem.attr("class"), "");
		                    
		                    // a 태그가 있으면 href 가져오기, 없으면 빈값
		                    String aclass = "";
		                    if(!jelem.select("a").isEmpty()){
		                        aclass = StringUtil.nvl(jelem.select("a").attr("href"), "");
		                        if(aclass.indexOf("nid=")>(-1)) {
		                        	resultMap.put("nid", extractNid(aclass , "nid="));
		                        }else  if(aclass.indexOf("code=")>(-1)) {
		                        	resultMap.put("code", extractNid(aclass , "code="));
		                        }
		                    }else {
		                    	if(StringUtil.isValidDate(jelem.text()) && "date".equals(classnm)) {
		                    		resultMap.put("date", jelem.text());
		                    	}
		                    }
		                    
		                    resultMap.put("parameter" + j, jelem.text());
		                    resultMap.put("parameter" + j + "_class", classnm);
		                    resultMap.put("parameter" + j + "_href", aclass);
		                }
		                // System.out.print((num++) + "."); // 로깅 과다 방지
		                 //System.out.println(resultMap);
		                list.add(resultMap);
		            }
		        }

		        System.out.println("getStockResearchList_V01 END");

		    } catch (Exception e) {
		        e.printStackTrace();
		        // 필요시 에러 발생했다는 정보를 리스트에 담거나 throw 처리
		        throw e; // 컨트롤러 등 호출부에서 에러를 인지할 수 있도록 던져주는 것이 좋음
		    }
		    return list;
		}
		
		// [보조 메서드] URL에서 page 파라미터 값만 안전하게 추출
		private static String extractPageNum(String url, String defaultVal) {
		    if (url == null || "".equals(url) || !url.contains("page=")) {
		        return defaultVal;
		    }
		    try {
		        // "page=" 뒤의 숫자만 추출하는 간단한 파싱
		        String[] parts = url.split("page=");
		        if (parts.length > 1) {
		            // 뒤에 &가 붙어있을 경우 제거 (예: page=2&other=val)
		            String pagePart = parts[1];
		            if(pagePart.contains("&")){
		                return pagePart.substring(0, pagePart.indexOf("&"));
		            }
		            return pagePart;
		        }
		    } catch (Exception e) {
		        return defaultVal;
		    }
		    return defaultVal;
		}
		
		// 예시: URL에서 nid 파싱 유틸
		private static String extractNid(String url , String key) {
			String result="";
		    // 간단하게 URL 파라미터 파싱 로직 구현 (또는 라이브러리 사용)
		    if (url.contains(key)) {
		        String[] parts = url.split(key);
		        String tail = parts[1];
		        if (tail.contains("&")) {
		            //return tail.split("&")[0];
		            result = tail.split("&")[0];
		        }else {
		        	result = tail;
		        }
		    }
		    return result; // nid가 없으면 URL 전체를 ID로 사용 (보험용)
		}
		
		
		/**
		 * 상승 , 보합 , 하락 내역 조회
		 * @param savePath
		 * @return
		 * @throws Exception
		 */
		public static List<Map<String, Object>> getStockSiseSearchList(Map<String, Object> paramMap) throws Exception{
			ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
			String searchKeyword = StringUtil.nvl(paramMap.get("searchKeyword"),"");
			String type = StringUtil.nvl(paramMap.get("type"),"");
			String sosok = StringUtil.nvl(paramMap.get("sosok"),"0");
			StringBuilder URL = new StringBuilder(naver_domain_url+"/sise/sise_"+type+".naver?").append("sosok=").append(sosok);
			System.out.println("URL=>"+URL.toString());
			Document doc;
			try {
					doc = Jsoup.connect(URL.toString()).get();

					Elements elem =doc.select("#contentarea");
					Elements todaylist =elem.select(".type_2 tr");//리스트
					int num = 1;
					int coulnmleng = 0;
					System.out.println("todaylist.size("+todaylist.size()+")");
					for(int i = 0 ; i < todaylist.size() ; i++) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						if(i == 0) {
							coulnmleng = todaylist.get(i).select("th").size();
							if(coulnmleng > 4) {
								for(int j = 0 ; j < coulnmleng ; j++) {
									resultMap.put("parameter"+j, todaylist.get(i).select("th").get(j).text());
								}
							}
							resultMap.put("columsize", coulnmleng);
							list.add(resultMap);
						}else {
							coulnmleng = todaylist.get(i).select("td").size();
							String roe ="";
							if(coulnmleng > 4) {
								for(int j = 0 ; j < coulnmleng ; j++) {
									Element jelem = todaylist.get(i).select("td").get(j);
									String color = "";
										if(jelem.text().indexOf("상승") > (-1) || jelem.text().indexOf("상한가") > (-1) || jelem.text().indexOf("+") > (-1)) {
											color = "red";
										}else if(jelem.text().indexOf("하락") > (-1) || jelem.text().indexOf("하한가") > (-1) || jelem.text().indexOf("-") > (-1)) {
											color = "blue";
										}
									String name = jelem.text().trim().replace("상승", "").replace("하락", "").replace("보합", "").replace("상한가", "").replace("하한가", "");
									String classnm = StringUtil.nvl(jelem.attr("class"),"");
									String aclass = StringUtil.nvl(jelem.select("a").attr("href"),"");
									String spanclass = StringUtil.nvl(jelem.select(".tah").attr("class"),"");
									resultMap.put("parameter"+j, name);
									resultMap.put("parameter"+j+"_href", aclass);
									resultMap.put("parameter"+j+"_spanClass", spanclass);	
									resultMap.put("parameter"+j+"_color", color);
									roe = name;
								}
								if(!"N/A".equals(roe)) {									
//									System.out.print((num++)+".");
//									System.out.println(resultMap);
									resultMap.put("columsize", coulnmleng);
									list.add(resultMap);
								}
							}
						}
						
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return list;
		}
		
}
