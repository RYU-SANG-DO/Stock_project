package egovframework.stock.vo.dart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class rtComVO{	

	/**
	 * 리턴 결과 공통 VO
	 */
	private String status;	//에러 및 정보 코드(※메시지 설명 참조)
	private String message;	//에러 및 정보 메시지(※메시지 설명 참조)
	private String page_no;	//페이지 번호페이지 번호
	private String page_count;	//페이지 별 건수페이지 별 건수
	private String total_count;	//총 건수총 페이지 수
	private String total_page;	//총 페이지 수총 페이지 수

}
