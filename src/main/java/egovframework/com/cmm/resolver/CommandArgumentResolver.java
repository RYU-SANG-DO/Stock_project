package egovframework.com.cmm.resolver;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Map타입 적용 파라미터 복호화를 위한 Custom ArgumentResolver 클래스
 * 
 * @author 표준프레임워크팀 이삼섭
 * @since 2024.07.09
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일          수정자        수정내용
 *  ----------     --------    ---------------------------
 *  2024.07.09     신용호        Map 타입에서 noteId 복호화 적용을 위한 ArgumentResolver 추가
 *
 *      </pre>
 */

public class CommandArgumentResolver implements HandlerMethodArgumentResolver {
	
	protected  Logger log = Logger.getLogger(this.getClass().getName());

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		System.out.println("CommandArgumentResolver supportsParameter type=>"+parameter.getParameterType());
		return parameter.getParameterType().equals(Map.class);
	}
	
	@Override 
	public Object resolveArgument(MethodParameter parameter
									, ModelAndViewContainer mavContainer
									, NativeWebRequest webRequest
									, WebDataBinderFactory binderFactory) throws Exception {
		System.out.println("CommandArgumentResolver resolveArgument");
		String paramName = parameter.getParameterName();
		Class<?> clazz = parameter.getParameterType();
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Enumeration<?> enumeration = request.getParameterNames();
		System.out.println("paramName=>"+paramName);
		System.out.println("clazz=>"+clazz);
		
		//if(clazz.equals(Map.class) && paramName.equals("reqParmMap")){
		if(clazz.equals(Map.class)){
			System.out.println("reqParmMap");
			Map<String, Object> commandMap = new HashMap<String, Object>();
			List<String> keys = new ArrayList<String>();
			while(enumeration.hasMoreElements()){
				String key = (String) enumeration.nextElement();
				String[] values = request.getParameterValues(key);
				if(values != null){
					if(values.length > 1){
						commandMap.put(key, values);
					}else{
						if(values[0] != null && !"".equals(values[0])){
							commandMap.put(key, values[0]);
						}else{
							commandMap.put(key, null);
						}
					}
					//commandMap.put(key, (values.length > 1) ? values:values[0] );

					for(int i = 0; i < values.length ; i++){
						log.info(key+"["+i+"] : "+values[i]);
					}

				}
			}
			return commandMap;
		}
		return null;
	}

}
