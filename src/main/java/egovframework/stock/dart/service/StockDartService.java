package egovframework.stock.dart.service;

import java.util.List;
import java.util.Map;

/**
 *  DART
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2009.03.12		최초 생성
 *
 * </pre>
 */
public interface StockDartService{

	List<Map<String, Object>> selectDartList(Map<String, Object> commandMap) throws Exception;

	Map<String, Object> selectDetailDartMap(Map<String, Object> commandMap) throws Exception;
  
}
