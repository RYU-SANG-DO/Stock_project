package egovframework.stock.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import egovframework.stock.vo.StockDefaultVO;

public class stockUtil extends StockDefaultVO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5564804359390428545L;
	
	public final static String dart_key = "0d0847f7d8387063c48a902013299fea898bb50d";
	public final static String dart_save_dir = "D:/eclipse_java/com/dart/";
	
	public final static String dart_api_url = "https://opendart.fss.or.kr/api/";
	
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
		String URL = "https://finance.naver.com/item/main.naver?code="+stockCode; //NAVER 주식 
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
			//}
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
		System.out.println("getStockInfo start");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String [] stockCodes = {"005930","035720","012450","034020","373220","000660"};
		String URL = "https://finance.naver.com/item/main.naver?code="+stockCode; //NAVER 주식 
		Document doc;
		int start = startnum;
		String excelpath = "D:/eclipse_java/com/stock/cospi_20220311.xlsx";
		StockDefaultVO vo = new StockDefaultVO();
		vo.setStockTitles("기타");
		System.out.println(vo.getStockTitles());
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
		System.out.println("getStockInfo end");
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
		String URL = "https://finance.naver.com/item/main.naver?code="+stockCode; //NAVER 주식 
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
		String URL = "https://finance.naver.com/item/main.naver?code="+stockCode; //NAVER 주식 
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
	 * 종목입체분석(종목검색)
	 * 코스피 종목 내역(http://www.paxnet.co.kr/)
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockInfo_paxnet(int currentPageNo , String searchExchange, int period) throws Exception{
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		//삼성전자[005930] , 카카오[035720], 한화에어로스페이스[012450],두산중공업[034020],LG에너지솔루션[373220],SK하이닉스[000660]
		String [] stockCodes = {"005930","035720","012450","034020","373220","000660"};
		//가건 누적
		Map<String, Object> periods =  new HashMap<String, Object>(){
			{
			 put("3", "0");//3일
              put("5", "1");//5일
              put("10", "2");//10일
              put("20", "3");//20일
              put("60", "4");//60일
              put("90", "5");//90일
              put("120", "6");//120일
			}
		};
		String URL = "http://www.paxnet.co.kr/stock/sise/jongmokSearch?currentPageNo="+currentPageNo+"&searchExchange="+searchExchange+"&period="+period;
		Document doc;
		//String excelpath = "D:/eclipse_java/com/stock/cospi_20220311.xlsx";
		System.out.println(URL);
		try {
			//System.out.println("종목\t주가\t등락률\t시가\t고가\t저가\t거래량\t타입\t전일대비\t가져오는 시간");
			//for(int i = 0 ; i < stockCodes.length ; i++) {
				//String code = StringUtil.nvl(stockCodes[i],"");
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".table-scroll table");
				Elements theadlist =elem.select("thead tr");
				Elements todaylist =elem.select("tbody tr");
				
				//System.out.println("size=>"+todaylist.size());
				String summaryInfo ="";
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					Elements tds =todaylist.get(i).select("td");
					for(int j = 0 ; j < tds.size() ; j++) {
						String text =tds.get(j).text().replace("+ ", "+");
						String val = "";
						if(j >= 6) {
							String [] texts = text.split(" ");
							//System.out.print("("+texts.length+")");
							//System.out.print(text);
							String result = texts[Integer.parseInt(periods.get(period+"")+"")];
							val = result;
						}else {
							val = text;
						}
						//System.out.print("[parameter"+j+":"+val+"]");
						resultMap.put("parameter"+j, val);
					}
					resultList.add(resultMap);
				}
		
//			Elements tradecompares = doc.select(".trade_compare tbody tr");
//			if(tradecompares != null && tradecompares.size() > 0) {
//				for(int i =0 ; i < tradecompares.size() ; i++) {
//					String title =tradecompares.get(i).select("th span").text();
//					String text = tradecompares.get(i).select("td").get(0).text().replace("하향", "").replace("%", "");
//					resultMap.put("parameter"+(startnum++), text);					
//				}
//				
//			}else {
//				
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultList;
	}
	
	/**
	 * 업종 내역 조회
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockSectorsList() throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = "https://finance.naver.com/sise/sise_group.naver?type=upjong"; //NAVER 주식 
		Document doc;
		try {
				doc = Jsoup.connect(URL).get();
				Elements elem = doc.select(".date");
				String[] str = elem.text().split(" ");

				Elements todaylist =doc.select("#contentarea_left td>a");
				
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String href = todaylist.get(i).attr("href").substring(todaylist.get(i).attr("href").lastIndexOf("=")+1);
					String text = todaylist.get(i).text();
					resultMap.put("parameter"+0, (i+1));
					resultMap.put("parameter"+1, text);
					resultMap.put("parameter"+2, href);
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
		String URL = "https://finance.naver.com/sise/theme.naver?&page="+page; //NAVER 주식 
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
		String URL = "https://finance.naver.com/sise/theme.naver?&page="+page; //NAVER 주식 
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
						if(!"".equals(searchKeyword)) {
							String search = StringUtil.nvl(resultMap.get("parameter1"),"");
							if(search.indexOf(searchKeyword) > (-1)) {
								System.out.println((num-1)+"."+resultMap);
								list.add(resultMap);
							}else {
								num--;
							}
						}else {
							System.out.println((num-1)+"."+resultMap);
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
	 * 종목 건수 조회
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param stockname : 검색 할 종목명
	 * @return
	 * @throws Exception
	 */
	public static int getStockUpjongThemeCnt(String no , String type , String stockname) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		int resultcnt = 0;
		String URL = "https://finance.naver.com/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
//		System.out.println(URL);
//		System.out.println("stockname=>"+stockname);
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
						if("".equals(name)) continue;
						
						if(name.indexOf(stockname) > (-1)) {
							resultcnt += 1;
						}
					
					}
				}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultcnt;
	}
	
	/**
	 * 종목 상세 정보 내역
	 * @param no : 테마, 업종 번호
	 * @param 테마(type=theme) 및 업종(type=upjong) 
	 * @param order : 테마, 업종 순번
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getStockUpjongThemeList(String no , String type , int order) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String URL = "https://finance.naver.com/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
		System.out.println(URL);
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
				resultMap.put("parameter"+9, info);
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
						resultMap.put("parameter"+6, td2);//현재가
						resultMap.put("parameter"+7, td3_p11);//전일비
						resultMap.put("parameter"+8, td4_p11);//등락률
//						if((num-1) == 1) {
//							resultMap.put("parameter"+6, info);
//						}else {
//							resultMap.put("parameter"+6, info1);
//						}
						resultMap.put("parameter"+9, info1);
						
						String infoURL = "https://finance.naver.com/item/main.naver?code="+code; //NAVER 주식 
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
						resultMap.put("parameter"+10, summaryInfo);
						
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
		String URL = "https://finance.naver.com/sise/sise_group_detail.naver?type="+type+"&no="+no; //NAVER 주식 
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
						
						String infoURL = "https://finance.naver.com/item/main.naver?code="+code; //NAVER 주식 
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
	 * DART 개발 가이드
	 * dart_key = "0d0847f7d8387063c48a902013299fea898bb50d";
	 * dart_save_dir = "D:/eclipse_java/com/dart/";
	 * dart_api_url = "https://opendart.fss.or.kr/api/";
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getDartApiLv1List(String apiGrpCd) throws Exception{
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
		String api_url = "https://opendart.fss.or.kr";
		String apiMainUrl = "https://opendart.fss.or.kr/guide/main.do?apiGrpCd="+apiGrpCd;
		Document doc;
		try {
				doc = Jsoup.connect(apiMainUrl).get();
				Elements elem = doc.select(".list_area");

				Elements todaylist =elem.select(".tb01>tbody>tr");
				
				for(int i = 0 ; i < todaylist.size() ; i++) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					String apiName = todaylist.get(i).select("td").eq(1).text();
					System.out.println(apiName);
					resultMap.put("apiLv", todaylist.get(i).select("td").eq(0).text());
					resultMap.put("apiName", apiName);
					resultMap.put("apiExplanation", todaylist.get(i).select("td").eq(2).text());
					String href = api_url+""+todaylist.get(i).select("td").eq(3).select("a").attr("href");
					resultMap.put("detilUrl", href);
					list.add(resultMap);
				}
				System.out.println(list);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return list;
	}
	
	/**
	 * DART 개발 가이드 상세
	 * dart_key = "0d0847f7d8387063c48a902013299fea898bb50d";
	 * dart_save_dir = "D:/eclipse_java/com/dart/";
	 * dart_api_url = "https://opendart.fss.or.kr/api/";
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getDartApiLv2List(String apiGrpCd , String apiId) throws Exception{
		List<Map<String, Object>> baslist = new ArrayList<Map<String, Object>>(); 
		List<Map<String, Object>> reqlist = new ArrayList<Map<String, Object>>(); 
		List<Map<String, Object>> replist = new ArrayList<Map<String, Object>>(); 
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String detailUrl = "https://opendart.fss.or.kr/guide/detail.do?apiGrpCd="+apiGrpCd+"&apiId="+apiId;
		Document doc;
		try {
				doc = Jsoup.connect(detailUrl).get();
				Elements elem = doc.select("#contents");
				//제목 : .DGTopTitle p text
				String title = elem.select(".DGTopTitle p").text();
				System.out.println(title);
				Elements basiclist = elem.select(".DGCont").eq(0).select(".contWrapToggle>.tb02 tbody>tr");//기본정보
				Elements requestlist = elem.select(".DGCont").eq(1).select(".listFull_area>.tb02 tbody>tr");//요청정보
				String title2 = elem.select(".DGCont").eq(2).select(".titleWrapToggle>h5").text().replaceAll(" ", "").trim();
				Elements reponsetlist = elem.select(".DGCont").eq(2).select(".contWrapToggle>.tb02 tbody>tr");//응답정보
				if("상세유형".equals(title2)) {
					reponsetlist = elem.select(".DGCont").eq(3).select(".contWrapToggle>.tb02 tbody>tr");//응답정보
				}
				title2 = elem.select(".DGCont").eq(3).select(".titleWrapToggle>h5").text().replaceAll(" ", "").trim();
				System.out.println("기본정보 건수=>"+basiclist.size());
				System.out.println("요청 인자 건수=>"+requestlist.size());
				System.out.println("응답 결과 건수=>"+reponsetlist.size());
				//기본정보
				for(int i = 0 ; i < basiclist.size() ; i++) {
					Map<String, Object> msp = new HashMap<String, Object>();
					msp.put("apiGrpCd", apiGrpCd);
					msp.put("apiId", apiId);
					msp.put("apiName", title);
					msp.put("method", basiclist.get(i).select("td").eq(0).text());
					msp.put("url", basiclist.get(i).select("td").eq(1).text());
					msp.put("encode", basiclist.get(i).select("td").eq(2).text());
					msp.put("outFormat", basiclist.get(i).select("td").eq(3).text());
					String gubun = basiclist.get(i).select("td").eq(1).text();
					gubun = gubun.substring(gubun.lastIndexOf("/")+1 , gubun.lastIndexOf("."));
					msp.put("gubun", gubun);
					baslist.add(msp);
				}
				System.out.println(baslist);
				resultMap.put("basic", baslist);
				
				//요청인자
				for(int i = 0 ; i < requestlist.size() ; i++) {
					Map<String, Object> msp = new HashMap<String, Object>();					
					msp.put("apiGrpCd", apiGrpCd);
					msp.put("apiId", apiId);
					msp.put("key", requestlist.get(i).select("td").eq(0).text());
					msp.put("name", requestlist.get(i).select("td").eq(1).text());
					msp.put("type", requestlist.get(i).select("td").eq(2).text());
					msp.put("required", requestlist.get(i).select("td").eq(3).text());
					msp.put("explanation", requestlist.get(i).select("td").eq(4).text());					
					reqlist.add(msp);
				}
				System.out.println(reqlist);
				resultMap.put("request", reqlist);
				
				//응답 결과
				boolean listYn = false;
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 
				for(int i = 1 ; i < reponsetlist.size() ; i++) {
					Map<String, Object> map = new HashMap<String, Object>();					
					map.put("apiGrpCd", apiGrpCd);
					map.put("apiId", apiId);
					String text0 =  reponsetlist.get(i).select("td").eq(0).text().replaceAll("\"", "").replaceAll(" ", "").trim();
					String text1 =  reponsetlist.get(i).select("td").eq(1).text().trim();
					String text2 =  reponsetlist.get(i).select("td").eq(2).text().trim();
					String classGubun = reponsetlist.get(i).select("td").eq(0).select("i").attr("class");
					int sapnGubun = Integer.parseInt(StringUtil.nvl(reponsetlist.get(i).select("td").eq(0).select("span").attr("class").replace("mgl", ""),"0"));
//					System.out.println(text0+":"+classGubun+":"+sapnGubun);
					map.put("key", text0);
					map.put("name", text1);
					map.put("explanation", text2);					
					if("iconArrow".equals(classGubun)) {
						listYn = true;
					}else {
						if(!listYn) {
							replist.add(map);
						}else {
							list.add(map);
						}
					}
					
				}
				if(list != null && list.size() > 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("list", list);
					replist.add(map);	
				}
				System.out.println(replist);
				resultMap.put("response", replist);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return resultMap;
	}
	
	
}
