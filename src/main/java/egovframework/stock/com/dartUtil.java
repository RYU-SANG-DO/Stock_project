package egovframework.stock.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.stock.vo.dart.CorpcodeVO;
import egovframework.stock.vo.dart.FnlttsinglacntallVO;

public class dartUtil{
	
	/**
	 * DART 관련 정보
	 * */
	
	public final static List<Map<String, Object>> dartApiList = new ArrayList<Map<String, Object>>(); 
	
	public final static String dart_key = "0d0847f7d8387063c48a902013299fea898bb50d";
	public final static String save_dir = "D:/eclipse_java/com/dart/";
	
	public final static String dart_api_url = "https://opendart.fss.or.kr/api/";
	
	public static String corp_api_url = "";
	public static String corp_gubun = "";
	public static String corp_out_format = "";
	public static String corp_code = "";
	public static String result_type = "list";
	
	public dartUtil() {
		//고유번호
		corp_api_url = dart_api_url+"corpCode.xml"+"?crtfc_key="+dart_key;
		corp_out_format ="zip";
		corp_gubun = "corpcode";
	}
	
	public dartUtil(String corpGubun , String type , String corpCode) {
		corpGubun = StringUtil.nvl(corpGubun).toLowerCase();
		type = StringUtil.nvl(type).toLowerCase();
		String message="정상";
		corp_gubun = corpGubun;
		corp_out_format = type;
		String apiName="";
		if("corpcode".equals(corpGubun)) {//고유번호(DART에 등록되어있는 공시대상회사의 고유번호,회사명,종목코드, 최근변경일자를 파일로 제공합니다.)
			if("xml".equals(type)) {
				corp_api_url = "corpCode.xml";
				corp_out_format ="zip";
				apiName="공시정보 > 고유번호";
			}else {
				message="고유번호(xml)에는 없음 타입("+type+")";
			}
		}else if("list".equals(corpGubun)) {//공시검색(공시 유형별, 회사별, 날짜별 등 여러가지 조건으로 공시보고서 검색기능을 제공합니다.)
			if("json".equals(type) || "xml".equals(type)) {
				corp_api_url = "list."+type;
				apiName="공시정보 > 공시검색";
			}else {
				message="공시검색(json,xml)에는 없음 타입("+type+")";
			}
		}else if("company".equals(corpGubun)) {//기업개황(DART에 등록되어있는 기업의 개황정보를 제공합니다.)
			if("json".equals(type) || "xml".equals(type)) {
				corp_api_url = "company."+type;
				apiName="공시정보 > 기업개황";
			}else {
				message="기업개황(json,xml)에는 없음 타입("+type+")";
			}
		}else if("fnlttsinglacntall".equals(corpGubun)) {//단일회사 전체 재무제표(상장법인(유가증권, 코스닥) 및 주요 비상장법인(사업보고서 제출대상 & IFRS 적용)이 제출한 정기보고서 내에 XBRL재무제표의 모든계정과목을 제공합니다.)
			if("json".equals(type) || "xml".equals(type)) {
				corp_api_url = "fnlttSinglAcntAll."+type;
				apiName="정기보고서 재무정보 > 단일회사 전체 재무제표";
			}else {
				message="단일회사 전체 재무제표(json,xml)에는 없음 타입("+type+")";
			}
		}
		
		if(!"".equals(corp_api_url)) {
			corp_api_url = dart_api_url+corp_api_url+"?crtfc_key="+dart_key;
			if(!"".equals(corpCode)) {
				corp_api_url += "&corp_code="+corpCode;
			}
 		}
		
		System.out.println(apiName);
		System.out.println(message);
	}
	
	public dartUtil(Map<String, Object> dartInfo , String format , String corpCode) {
		String urlGubun = StringUtil.nvl(dartInfo.get("urlGubun"),"");
		String resultType = StringUtil.nvl(dartInfo.get("resultType"),"list");
		result_type = resultType;
		format = StringUtil.nvl(format).toLowerCase();
		corp_out_format = format;
		corp_gubun = StringUtil.nvl(urlGubun).toLowerCase();
		corp_api_url = dart_api_url+urlGubun+"."+format+"?crtfc_key="+dart_key+"&corp_code="+corpCode;
	}
	
	/**
	 * DART API
	 * @출력포맷 : json , xml , zip
	 */
	public List<Map<String, Object>> dartApiInfo(Map<String, Object> paramMap) {
		String fileURL = corp_api_url;
        String filePath = save_dir+corp_gubun+"/"+corp_gubun+"."+corp_out_format;               // corp_gubun:api구분 , corp_out_format:확장자
        String xmlFilePath = save_dir+corp_gubun+"/"+corp_gubun+".xml";               // ZIP 파일 경로
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
        System.out.println("fileURL=>"+fileURL);
        System.out.println("filePath=>"+filePath);
        System.out.println("xmlFilePath=>"+xmlFilePath);
        
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	if("zip".equals(corp_out_format)) {
        		FileUtil.downloadZipFile(fileURL,filePath);
        		FileUtil.unzip(filePath,save_dir+"/"+corp_gubun);
        		Document documentInfo = builder.parse(xmlFilePath);
        		resultList = setDartApiResultXml(documentInfo);            	
        	}else if("xml".equals(corp_out_format)) {
        		if(paramMap != null) {
        			String params = "";
        			for(String key : paramMap.keySet()) {
        				params += "&"+key+"="+paramMap.get(key);
        			}
        			fileURL += params;
        		}
        		System.out.println(fileURL);
        		URL apiurl = new URL(fileURL);
        	}else if("json".equals(corp_out_format)) {	
        		if(paramMap != null) {
        			String params = "";
        			for(String key : paramMap.keySet()) {
        				params += "&"+key+"="+paramMap.get(key);
        			}
        			fileURL += params;
        		}
        		System.out.println(fileURL);
        		URL apiurl = new URL(fileURL);
        		
        		try (InputStream is = apiurl.openStream();
    			       JsonReader rdr = Json.createReader(is)) {
    			 
    			      JsonObject obj = rdr.readObject();
    			      String status = StringUtil.nvl(obj.getJsonString("status").toString().replace("\"", ""));
    			      String message = StringUtil.nvl(obj.getJsonString("message").toString().replace("\"", ""));
    			      System.out.println(message+"["+status+"]");
    			      List<FnlttsinglacntallVO> resultVoList = StringUtil.getJsonToVoList(FnlttsinglacntallVO.class, obj, "list");
    			      
    			      for(FnlttsinglacntallVO fvo : resultVoList) {
    			    	  System.out.println(fvo.toString());
    			    	  
    			      }
    			 }
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return resultList;
	}
	
	/**
	 * DART API
	 * @출력포맷 : json , xml , zip
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> dartApiInfo(Map<String, Object> paramMap , Class<T> classType) {
		String fileURL = corp_api_url;
        String filePath = save_dir+corp_gubun+"/"+corp_gubun+"."+corp_out_format;               // corp_gubun:api구분 , corp_out_format:확장자
        String xmlFilePath = save_dir+corp_gubun+"/"+corp_gubun+".xml";               // ZIP 파일 경로
        String path = save_dir+corp_gubun;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<T> resultList = new ArrayList<T>(); 
        System.out.println("fileURL=>"+fileURL);
        System.out.println("filePath=>"+filePath);
        System.out.println("xmlFilePath=>"+xmlFilePath);
        System.out.println("result_type=>"+result_type);

        File Folder = new File(path);
        if (!Folder.exists()) {
    		try{
    		    Folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
    		    System.out.println("폴더가 생성완료.");
    	        } 
    	        catch(Exception e){
    		    e.getStackTrace();
    		}        
         }
        
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
        	if("zip".equals(corp_out_format)) {
        		FileUtil.downloadZipFile(fileURL,filePath);
        		FileUtil.unzip(filePath,save_dir+"/"+corp_gubun);
        		Document documentInfo = builder.parse(xmlFilePath);
        		resultList = (List<T>) setDartApiResultXml(documentInfo, CorpcodeVO.class , result_type);            	
        	}else if("xml".equals(corp_out_format)) {
        		if(paramMap != null) {
        			String params = "";
        			for(String key : paramMap.keySet()) {
        				params += "&"+key+"="+paramMap.get(key);
        			}
        			fileURL += params;
        		}
        		System.out.println(fileURL);
        		 try {
        	            Document documentInfo = getXMLFromURLV01(fileURL);
        	            
        	            // XML 파싱 예제 (루트 노드 출력)
        	            if (documentInfo != null) {
    	                    
    	                    // Root element 출력
    	                    System.out.println("Root Element: " + documentInfo.getDocumentElement().getNodeName());

    	                    resultList = StringUtil.getXmlToVoList(classType, documentInfo, result_type);
        	            }
        	        } catch (Exception e) {
        	            e.printStackTrace();
        	        }
        	}else if("json".equals(corp_out_format)) {	
        		if(paramMap != null) {
        			String params = "";
        			for(String key : paramMap.keySet()) {
        				params += "&"+key+"="+paramMap.get(key);
        			}
        			fileURL += params;
        		}
        		System.out.println(fileURL);
        		URL apiurl = new URL(fileURL);
        		
        		try (InputStream is = apiurl.openStream();
    			       JsonReader rdr = Json.createReader(is)) {
    			 
    			      JsonObject obj = rdr.readObject();
    			      String status = StringUtil.nvl(obj.getJsonString("status"),"").replace("\"", "");
    			      String message = StringUtil.nvl(obj.getJsonString("message"),"").replace("\"", "");
    			      String page_no = StringUtil.nvl(obj.getJsonNumber("page_no"),"1").replace("\"", "");
    			      String page_count = StringUtil.nvl(obj.getJsonNumber("page_count"),"0").replace("\"", "");
    			      String total_count = StringUtil.nvl(obj.getJsonNumber("total_count"),"0").replace("\"", "");
    			      String total_page = StringUtil.nvl(obj.getJsonNumber("total_page"),"0").replace("\"", "");
    			      System.out.println(message+"["+status+"]");
    			      if("000".equals(status)) {
    			    	  resultList = StringUtil.getJsonToVoList(classType, obj, result_type);
    			    	  for(T fvo : resultList) {
    			    		  System.out.println(fvo.toString());
    			    	  }
    			      }
			    	  
    			 }
        		
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return resultList;
	}
	
	public static List<Map<String, Object>> corpCodeInfo() {
		String fileURL = dart_api_url+"corpCode.xml"+"?crtfc_key="+dart_key;
        String zipFilePath = save_dir+"corpcode.zip";               // ZIP 파일 경로
        String xmlFilePath = save_dir+"/corpcode/corpcode.xml";               // ZIP 파일 경로
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
        	FileUtil.downloadZipFile(fileURL,zipFilePath);
        	FileUtil.unzip(zipFilePath,save_dir+"/corpcode");
        	
        	Document documentInfo = builder.parse(xmlFilePath);
        	String status = documentInfo.getElementsByTagName("status").toString();
        	String message = documentInfo.getElementsByTagName("message").toString();
        	System.out.println("status=>"+status);
        	System.out.println("message=>"+message);
        	documentInfo.getDocumentElement().normalize();
        	NodeList nList = documentInfo.getElementsByTagName("list");

        	for(int temp = 0; temp < nList.getLength(); temp++){
				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){					
					Element eElement = (Element) nNode;
//					System.out.println((temp+1)+"######################");
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("corp_code", StringUtil.getTagValue("corp_code", eElement));
					map.put("corp_name", StringUtil.getTagValue("corp_name", eElement));
					map.put("stock_code", StringUtil.getTagValue("stock_code", eElement));
					map.put("modify_date", StringUtil.getTagValue("modify_date", eElement));
					if(!"".equals(StringUtil.getTagValue("stock_code", eElement))) {
					resultList.add(map);
//					System.out.println("고유번호  : " + StringUtil.getTagValue("corp_code", eElement));
//					System.out.println("정식명칭  : " + StringUtil.getTagValue("corp_name", eElement));
//					System.out.println("종목코드 : " + StringUtil.getTagValue("stock_code", eElement));
//					System.out.println("최종변경일자  : " + StringUtil.getTagValue("modify_date", eElement));
					System.out.println((temp+1)+"\t"+StringUtil.getTagValue("corp_code", eElement)+"\t"+StringUtil.getTagValue("corp_name", eElement)+"\t"+StringUtil.getTagValue("stock_code", eElement));
					}
				}	
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return resultList;
	}
	
	public static Map<String, Object> getCorpCode(String corpCode) {
		Map<String, Object> corpCodeMap = new HashMap<String, Object>();
		String fileURL = dart_api_url+"corpCode.xml"+"?crtfc_key="+dart_key;
        String zipFilePath = save_dir+"corpcode.zip";               // ZIP 파일 경로
        String xmlFilePath = save_dir+"/corpcode/corpcode.xml";               // ZIP 파일 경로
        System.out.println("fileURL=>"+fileURL);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
        	FileUtil.downloadZipFile(fileURL,zipFilePath);
        	FileUtil.unzip(zipFilePath,save_dir+"/corpcode");
        	
        	Document documentInfo = builder.parse(xmlFilePath);
        	
        	documentInfo.getDocumentElement().normalize();
        	NodeList nList = documentInfo.getElementsByTagName("list");

        	for(int temp = 0; temp < nList.getLength(); temp++){
				Node nNode = nList.item(temp);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){	
					Element eElement = (Element) nNode;
//					System.out.println((temp+1)+"######################");
					String stock_code = StringUtil.getTagValue("stock_code", eElement).trim();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("corp_code", StringUtil.getTagValue("corp_code", eElement));
					map.put("corp_name", StringUtil.getTagValue("corp_name", eElement));
					map.put("stock_code", stock_code);
					map.put("modify_date", StringUtil.getTagValue("modify_date", eElement));
					resultList.add(map);
					if(corpCode.equals(stock_code)) {
						corpCodeMap = map;
						System.out.println("고유번호  : " + StringUtil.getTagValue("corp_code", eElement));
						System.out.println("정식명칭  : " + StringUtil.getTagValue("corp_name", eElement));
						System.out.println("종목코드 : " + stock_code);
						System.out.println("최종변경일자  : " + StringUtil.getTagValue("modify_date", eElement));
					}
				}	
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return corpCodeMap;
	}
	
	/**
	 * 리턴 결과 셋팅(xml)
	 */
	public static List<Map<String, Object>> setDartApiResultXml(Document documentInfo) {
		 List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>(); 
		 String status = documentInfo.getElementsByTagName("status").toString();
     	String message = documentInfo.getElementsByTagName("message").toString();
     	System.out.println("status=>"+status);
     	System.out.println("message=>"+message);
		documentInfo.getDocumentElement().normalize();
    	NodeList nList = documentInfo.getElementsByTagName("list");

    	for(int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){					
				Element eElement = (Element) nNode;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("corp_code", StringUtil.getTagValue("corp_code", eElement));
				map.put("corp_name", StringUtil.getTagValue("corp_name", eElement));
				map.put("stock_code", StringUtil.getTagValue("stock_code", eElement));
				map.put("modify_date", StringUtil.getTagValue("modify_date", eElement));
				resultList.add(map);
				System.out.println((temp+1)+".["+StringUtil.getTagValue("corp_code", eElement)+"]"+StringUtil.getTagValue("corp_name", eElement)+":"+StringUtil.getTagValue("corp_name", eElement));
			}	
		}	
		
		
		return resultList;
	}
	
	/**
	 * 제네릭을 이용한 리턴 결과 셋팅(xml)
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> setDartApiResultXml(Document documentInfo , Class<T> classType , String tagName) {
		 List<T> resultList = new ArrayList<T>(); 
		NodeList status = documentInfo.getElementsByTagName("status");
     	NodeList message = documentInfo.getElementsByTagName("message");
     	System.out.println("status=>"+status.item(0));
     	System.out.println("message=>"+message.item(0));
		documentInfo.getDocumentElement().normalize();
    	NodeList nList = documentInfo.getElementsByTagName(tagName);
    	T objVo = null;
    	for(int temp = 0; temp < nList.getLength(); temp++){
			Node nNode = nList.item(temp);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){	
				Element eElement = (Element) nNode;
				
				if(classType.equals(CorpcodeVO.class)) {
					CorpcodeVO codeVO = new CorpcodeVO();
					String [] keys = codeVO.toKeys();
					for(String key : keys) {
						codeVO.setKeyValue(key, StringUtil.getTagValue(key, eElement));
					}
					System.out.println(codeVO.toString());
					resultList.add((T)codeVO);
				}
			}	
		}	
		
		
		return resultList;
	}
	
	 public static Document getXMLFromURL(String fileURL) throws Exception {
	        // URL 객체 생성
	        URL url = new URL(fileURL);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        // 응답 코드 확인
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	            // InputStream을 Document 객체로 변환
	            InputStream inputStream = connection.getInputStream();
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            return builder.parse(inputStream);
	        } else {
	            throw new Exception("HTTP Response Code: " + responseCode);
	        }
	    }
	 
	 public static Document getXMLFromURLV01(String fileURL) throws Exception {
		 URL url = new URL(fileURL);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        // 응답 코드 확인
	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) {
	        	
	        	  InputStream inputStream = connection.getInputStream();
	              BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	              // 전체 XML 내용을 String으로 변환
	              String xmlString = reader.lines().collect(Collectors.joining("\n"));
	              System.out.println(xmlString);
	             
	  	        return convertStringToXML(xmlString);
	        } else {
	            throw new Exception("HTTP Response Code: " + responseCode);
	        }
	    }
	 
	 public static Document convertStringToXML(String xmlString) throws Exception {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        return builder.parse(new InputSource(new StringReader(xmlString)));
	    }
	
	/**
	 * 리턴 결과 셋팅(json)
	 */
	public static Map<String, Object> setDartApiResultJson(String corpName) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		
		return resultMap;
	}
}
