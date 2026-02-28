package egovframework.beauty.service;

import java.util.List;
import java.util.Map;

/**
 *  미용실
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일				수정내용
 *  -----------		---------------------------
 *   2026.02.28		최초 생성
 *
 * </pre>
 */
public interface BeautyService{

	int selectBeautyPaymanetListTotCnt(Map<String, Object> commandMap) throws Exception;

	List<Map<String, Object>> selectBeautyPaymanetList(Map<String, Object> commandMap) throws Exception;

	int updateBeautyPaymanet(Map<String, Object> commandMap) throws Exception;

	int deleteBeautyPaymanet(Map<String, Object> commandMap) throws Exception;

	int insertBeautyPaymanet(Map<String, Object> commandMap) throws Exception;

	Map<String, Object> selectBeautyPaymanetInfo(Map<String, Object> commandMap) throws Exception;


}
