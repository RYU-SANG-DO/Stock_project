package egovframework.com.cmm;

/**
 * IncludedCompInfoVO 클래스
 * 
 * <p>
 *  Description : IncludedInfo annotation을 바탕으로 화면에 표시할 정보를 구성하기 위한 VO 클래스
 * </p>
 * 
 * @author 공통컴포넌트 정진오
 * @since 2011.08.26
 * @version 2.0.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일		수정자		수정내용
 *  -------    	--------    ---------------------------
 *  2011.08.26	정진오 		최초 생성
 * </pre>
 * 
 */
public class IncludedCompInfoVO {
	
	private String name;
	private String listUrl;
	private int order;
	private int gid;
	private int lv;
	private String keyL1;
	private String keyL2;
	private String order_title;
<<<<<<< HEAD
	private String useYn = "Y";	//메뉴 사용유무
=======
>>>>>>> 9e8d188b980609abd0d5b2c40af6feb1d3595fca
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getListUrl() {
		return listUrl;
	}
	public void setListUrl(String listUrl) {
		this.listUrl = listUrl;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public String getKeyL1() {
		return keyL1;
	}
	public void setKeyL1(String keyL1) {
		this.keyL1 = keyL1;
	}
	public String getKeyL2() {
		return keyL2;
	}
	public void setKeyL2(String keyL2) {
		this.keyL2 = keyL2;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public String getOrder_title() {
		return order_title;
	}
	public void setOrder_title(String order_title) {
		this.order_title = order_title;
	}
<<<<<<< HEAD
	public String isUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
=======
>>>>>>> 9e8d188b980609abd0d5b2c40af6feb1d3595fca

}
