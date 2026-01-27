package egovframework.stock.com;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.stock.vo.dart.ListVO;
import egovframework.stock.vo.naver.NaverSearchResponseVO;

public class StringUtil {
	@Resource(name="egovMessageSource")
	static
    EgovMessageSource egovMessageSource;
	/**
	 * 대상 String이 null일 경우 대체 String을, null이 아닐 경우 대상 String을 return
	 * @param str
	 *            대상 문자
	 * @param val
	 *            null일 경우 대체될 문자
	 */
	public static String nvl( Object obj, String val ) {
		if( obj == null )
			return val;
		else if( obj.toString().equals( "" ) )
			return val;
		else
			return obj.toString();
	}

	/**
	 * 대상 String이 null일 경우 ""을, null이 아닐 경우 대상 String을 return
	 * @param str
	 *            대상 스트링
	 */
	public static String nvl( String str ) {
		if( str == null )
			return "";
		else
			return str;
	}

	/**
	 * 대상 String이 null일 경우 ""을, null이 아닐 경우 대상 String을 trim한 후 return
	 * @param str
	 *            trim한 대상 스트링
	 */
	public static String trim( String str ) throws Exception {
		String sTmp = str;

		if( str == null ) {
			sTmp = "";
		} else if( !"".equals( str ) && str.length() > 0 ) {
			sTmp = str.trim();
		}

		return sTmp;
	}

	/**
	 * 대상 String이 null일 경우 ""을, null이 아닐 경우 대상 String을 trim한 후 return
	 * @param str
	 *            trim한 대상 스트링
	 * @param value
	 *            null일 경우 대체 string
	 */
	public static String trim( String str, String value ) throws Exception {
		String sTmp = str;

		if( str == null ) {
			sTmp = value;
		} else if( !"".equals( str ) && str.length() > 0 ) {
			sTmp = str.trim();
		}

		return sTmp;
	}

	 public static boolean isValidEmail(String email) {
		  boolean err = false;
		  String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(email);
		  if(m.matches()) {
		   err = true;
		  }
		  return err;
		 }
	 
	 public static String getTagValue(String tag, Element eElement) {
		    NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
		    Node nValue = (Node) nlList.item(0);
		    if(nValue == null) {
		    	return null;
		    }
		    return nValue.getNodeValue();
		}
	 
	  public static String getTagValueV01(String tag, Element element) {
	        NodeList nodeList = element.getElementsByTagName(tag);
	        return (nodeList.getLength() > 0) ? nodeList.item(0).getTextContent() : "";
	    }
	 
	 /*
	  * paramkey : 리턴값중에 가져올 키
	  * */
	 public static <T> List<T> getJsonToVoList(Class<T> typeobj ,JsonObject jsonobj, String paramkey) throws Exception {
			List<T> resultVoList = new ArrayList<T>(); 
		
			 try {
				 if("list".equals(paramkey)) {
					 JsonArray results = jsonobj.getJsonArray(paramkey);
					 for (JsonObject result : results.getValuesAs(JsonObject.class)) {
						 T objVo = new Gson().fromJson(result.toString(), typeobj);
						 resultVoList.add(objVo);
					 }
				 }else {
					 T objVo = new Gson().fromJson(jsonobj.toString(), typeobj);
			          resultVoList.add(objVo);
				 }
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			return resultVoList;
		}
	 
	 /*
	  * paramkey : 리턴값중에 가져올 키
	  * */
	 @SuppressWarnings("unchecked")
	public static <T> List<T> getXmlToVoList(Class<T> typeobj ,Document documentInfo, String paramkey) throws Exception {
			List<T> resultVoList = new ArrayList<T>(); 
		
			 try {
				 if("list".equals(paramkey)) {
					 NodeList nodeList = documentInfo.getElementsByTagName(paramkey);
					 for (int i = 0; i < nodeList.getLength(); i++) {
						 Node node = nodeList.item(i);
						 if (node.getNodeType() == Node.ELEMENT_NODE) {
							 Element element = (Element) node;
							 T report = (T) new ListVO(element);
							 
							 resultVoList.add(report);
						 }
					 }
				 }else {
					 T report = (T) new ListVO(documentInfo.getDocumentElement());

	                 resultVoList.add(report);
				 }
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
			return resultVoList;
		}
	 
	 
	 public static <T> T fromJson(String json, Type typeOfT) {
		 	Gson gson = new Gson();
	        return gson.fromJson(json, typeOfT);
	    }

	 public static Map<String, Object> voToMap(Object vo) {
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        try {
	            Class<?> clazz = vo.getClass();
	            while (clazz != null) { // 상속 계층 탐색
	            	Field[] fields = clazz.getDeclaredFields();
	            	for (Field field : fields) {
	            		field.setAccessible(true); // private 필드 접근 가능 설정
	            		resultMap.put(field.getName(), field.get(vo));
	            	}
	            	clazz = clazz.getSuperclass();
	            }
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	        return resultMap;
	    }
	
	 //Map<String, String []> => Map<String, Object>
	 public static Map<String, Object> mapToMap(HttpServletRequest request) {
	        Map<String, Object> resultMap = new HashMap<String, Object>();
	        try {
	        	
	        	String requestURL = request.getRequestURI();
	        	String [] url = requestURL.split("/");
				System.out.println(Arrays.toString(url));
	        	Map<String, String []> reqMap = request.getParameterMap();
	        	Enumeration<?> enumeration = request.getParameterNames();
	        	
	        	while(enumeration.hasMoreElements()){
					String key = (String) enumeration.nextElement();
					String[] values = request.getParameterValues(key);
					if(values != null){
						if(values.length > 1){
							resultMap.put(key, values);
						}else{
							if(values[0] != null && !"".equals(values[0])){
								resultMap.put(key, values[0]);
							}else{
								resultMap.put(key, null);
							}
						}
						//commandMap.put(key, (values.length > 1) ? values:values[0] );

//						for(int i = 0; i < values.length ; i++){
//							System.out.println(key+"["+i+"] : "+values[i]);
//						}

					}
				}
	        	if(url.length >= 3) {
					System.out.println("stockGubun=>"+url[1].trim());
					System.out.println("stockSite=>"+url[2].trim());
					// 요청 파라미터 추가
					resultMap.put("stockGubun", url[1].trim());
					resultMap.put("stockSite", url[2].trim());
				}
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return resultMap;
	    }
	 
	 /*
		 * JsonString => map 변환
		 * */
		@SuppressWarnings("unchecked")
		public static Map<String, Object> getJsonString_to_map( String sourceJson) {
			Map<String , Object> resultMap = new HashMap<String, Object>();
			ObjectMapper objectMapper = new ObjectMapper();		
			try {
				resultMap = objectMapper.readValue(sourceJson, Map.class);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return resultMap;
		}
		
		/*
		 * map => json(String) 변환
		 * */
		public static String getMap_to_jsonString( Map<String , Object> sourceMap) {
			 String mapTojson = "";
			try {
				mapTojson = new ObjectMapper().writeValueAsString(sourceMap);//jsonString
			}catch (Exception e) {
				e.printStackTrace();
			}
			return mapTojson;
		}
		
		/*
		 * json(String) => json(Gson) 변환
		 * */
		@SuppressWarnings("unchecked")
		public static com.google.gson.JsonObject getJsonString_to_json( String sourceJson) {
			ObjectMapper objectMapper = new ObjectMapper();
			 Gson gson = new Gson();
			 com.google.gson.JsonObject json = null;
			try {
				Map<String, Object> map = objectMapper.readValue(sourceJson, Map.class);
				json = gson.toJsonTree(map).getAsJsonObject();
			} catch (Exception e) {
				e.printStackTrace();
			} 		
			return json;
		}
		
		/*
		 * xml(String) => VO 변환
		 * */
		public static NaverSearchResponseVO getXmlStringToNaverSearchResponseVO( String sourceXml) {
			 NaverSearchResponseVO resultVo = new NaverSearchResponseVO();
			
			try {
				Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(sourceXml.getBytes()));
				xml.getDocumentElement().normalize();
	            XPath xpath = XPathFactory.newInstance().newXPath();
	            String title = StringUtil.nvl(xpath.evaluate("//channel//title", xml, XPathConstants.STRING),"");
	            String link = StringUtil.nvl(xpath.evaluate("//channel//link", xml, XPathConstants.STRING),"");
	            String description = StringUtil.nvl(xpath.evaluate("//channel//description", xml, XPathConstants.STRING),"");
	            String lastBuildDate = StringUtil.nvl(xpath.evaluate("//channel//lastBuildDate", xml, XPathConstants.STRING),"");
	            int total = Integer.parseInt(StringUtil.nvl(xpath.evaluate("//channel//total", xml, XPathConstants.STRING),"0"));
	            int start = Integer.parseInt(StringUtil.nvl(xpath.evaluate("//channel//start", xml, XPathConstants.STRING),"0"));
	            int display = Integer.parseInt(StringUtil.nvl(xpath.evaluate("//channel//display", xml, XPathConstants.STRING),"0"));
	            resultVo.setTitle(title);
	            resultVo.setLink(link);
	            resultVo.setDescription(description);
	            resultVo.setLastBuildDate(lastBuildDate);
	            resultVo.setTotal(total);
	            resultVo.setStart(start);
	            resultVo.setDisplay(display);
	            
	            NodeList nodeList = (NodeList) xpath.evaluate("//channel//item", xml, XPathConstants.NODESET); 
	            Map<String, Object> xmlMap = new HashMap<>();
	            List<Map<String, Object>> itemList = new ArrayList<>();
				 for (int index = 0; index < nodeList.getLength(); index++) {
					 Element eElement = (Element) nodeList.item(index);
					 /**xmlMap 생성자 초기화*/
	                    xmlMap = new HashMap<String, Object>();
	                    /**각 값을 적재*/
	                   String p_title = eElement.getElementsByTagName("title").item(0).getTextContent();
	                   String p_originallink = eElement.getElementsByTagName("originallink").item(0).getTextContent();
	                   String p_link = eElement.getElementsByTagName("link").item(0).getTextContent();
	                   String p_description = eElement.getElementsByTagName("description").item(0).getTextContent();
	                   String p_pubDate = eElement.getElementsByTagName("pubDate").item(0).getTextContent();
	                   xmlMap.put("title", p_title);
	                   xmlMap.put("originallink", p_originallink);
	                   xmlMap.put("link", p_link);
	                   xmlMap.put("description", p_description);
	                   xmlMap.put("pubDate", p_pubDate);
	                   itemList.add(xmlMap);
				 }
				 resultVo.setItems(itemList);
				
			} catch (Exception e) {
				e.printStackTrace();
			} 		
			return resultVo;
		}
	    
}
