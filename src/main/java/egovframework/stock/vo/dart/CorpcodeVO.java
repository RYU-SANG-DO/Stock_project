package egovframework.stock.vo.dart;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import egovframework.stock.com.StringUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorpcodeVO extends rtComVO{	

	/**
	 * 고유번호 리턴 결과 VO
	 */
	private String corp_code;	//고유번호:공시대상회사의 고유번호(8자리) ※ ZIP File 안에 있는 XML파일 정보
	private String corp_name;	//정식명칭:정식회사명칭 ※ ZIP File 안에 있는 XML파일 정보
	private String stock_code;	//종목코드:상장회사인 경우 주식의 종목코드(6자리) ※ ZIP File 안에 있는 XML파일 정보
	private String modify_date;	//최종변경일자:기업개황정보 최종변경일자(YYYYMMDD) ※ ZIP File 안에 있는 XML파일 정보
	
	public void setKeyValue(String key , String value) {
		 try {
			Field field =  this.getClass().getDeclaredField(key);
			 field.setAccessible(true);
			 field.set(this, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 }
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String key , Class<T> obj) {
		Object value= null;
		try {
			Field field =  this.getClass().getDeclaredField(key);
			field.setAccessible(true);
			value = field.get(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)value;
	 }
	
	 public String [] toKeys() {
		 Object obj = this;
		 String result [] = new String[obj.getClass().getDeclaredFields().length];
		 try {
				for(int i = 0 ; i < obj.getClass().getDeclaredFields().length ; i++)	{
					Field field = obj.getClass().getDeclaredFields()[i];
					  field.setAccessible(true);
					 
					  result[i] = field.getName();
					  Object value = field.get(obj);
					  System.out.println("field : "+field.getName()+" | value : " + value);
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return result; 
	 }
	 
    public String toString() {
    	
    	String result = this.getClass().getSimpleName()+"{";
    	String keys [] = toKeys();
    	for(String key : keys) {
			result += key+"='"+this.getValue(key , String.class)+'\'' + ",";
		}
    	result += "}";
        return result;
    }
    
    public Map<String, Object> toMap() {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	String keys [] = toKeys();
    	for(String key : keys) {
			resultMap.put(key, this.getValue(key , String.class));
		}
        return resultMap;
    }



}
