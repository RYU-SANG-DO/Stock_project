package egovframework.stock.vo.dart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FnlttsinglacntallVO extends rtComVO{	

	/**
	 * 정기보고서 재무정보 > 단일회사 전체 재무제표 개발가이드 리턴 결과 VO
	 */
	private String rcept_no;				//접수번호:접수번호(14자리),공시뷰어 연결에 이용예시(PC용 : https://dart.fss.or.kr/dsaf001/main.do?rcpNo=접수번호)
	private String reprt_code;				//보고서 코드:1분기보고서 : 11013 반기보고서 : 11012 3분기보고서 : 11014 사업보고서 : 11011
	private String bsns_year;				//사업 연도:2018
	private String corp_code;				//고유번호:공시대상회사의 고유번호(8자리)
	private String sj_div;					//재무제표구분:BS : 재무상태표 IS : 손익계산서 CIS : 포괄손익계산서 CF : 현금흐름표 SCE : 자본변동표
	private String sj_nm;					//재무제표명:ex) 재무상태표 또는 손익계산서 출력
	private String account_id;			//계정ID:XBRL 표준계정ID ※ 표준계정ID가 아닐경우 -표준계정코드 미사용- 표시
	private String account_nm;			//계정명:계정명칭 ex) 자본총계
	private String account_detail;		//계정상세:※ 자본변동표에만 출력 ex) 계정 상세명칭 예시 - 자본 [member]|지배기업 소유주지분 - 자본 [member]|지배기업 소유주지분|기타포괄손익누계액 [member]
	private String thstrm_nm;				//당기명:ex) 제 13 기
	private String thstrm_amount;		//당기금액:9,999,999,999 ※ 분/반기 보고서이면서 (포괄)손익계산서 일 경우 [3개월] 금액
	private String thstrm_add_amount;	//당기누적금액:9999999999
	private String frmtrm_nm;				//전기명:ex) 제 12 기말
	private String frmtrm_amount;		//전기금액:9999999999
	private String frmtrm_q_nm;			//전기명(분/반기):ex) 제 18 기 반기
	private String frmtrm_q_amount;	//전기금액(분/반기):9,999,999,999 ※ 분/반기 보고서이면서 (포괄)손익계산서 일 경우 [3개월] 금액
	private String frmtrm_add_amount;	//전기누적금액:9999999999
	private String bfefrmtrm_nm;			//전전기명:ex) 제 11 기말(※ 사업보고서의 경우에만 출력)
	private String bfefrmtrm_amount;	//전전기금액:9,999,999,999(※ 사업보고서의 경우에만 출력)
	private String ord;						//계정과목 정렬순서:계정과목 정렬순서
	private String currency;				//통화 단위:통화 단위
	

    public String toString() {
    	return "FnlttsinglacntallVO{" +
    			"rcept_no='"+rcept_no+'\'' +"," +
    			"reprt_code='"+reprt_code+'\'' +"," +
    			"bsns_year='"+bsns_year+'\'' +"," +
    			"corp_code='"+corp_code+'\'' +"," +
    			"sj_div='"+sj_div+'\'' +"," +
    			"sj_nm='"+sj_nm+'\'' +"," +
    			"account_id='"+account_id+'\'' +"," +
    			"account_nm='"+account_nm+'\'' +"," +
    			"account_detail='"+account_detail+'\'' +"," +
    			"thstrm_nm='"+thstrm_nm+'\'' +"," +
    			"thstrm_amount='"+thstrm_amount+'\'' +"," +
    			"thstrm_add_amount='"+thstrm_add_amount+'\'' +"," +
    			"frmtrm_nm='"+frmtrm_nm+'\'' +"," +
    			"frmtrm_amount='"+frmtrm_amount+'\'' +"," +
    			"frmtrm_q_nm='"+frmtrm_q_nm+'\'' +"," +
    			"frmtrm_q_amount='"+frmtrm_q_amount+'\'' +"," +
    			"frmtrm_add_amount='"+frmtrm_add_amount+'\'' +"," +
    			"bfefrmtrm_nm='"+bfefrmtrm_nm+'\'' +"," +
    			"bfefrmtrm_amount='"+bfefrmtrm_amount+'\'' +"," +
    			"ord='"+ord+'\'' +"," +
    			"currency='"+currency+'\'' +
                '}';
    }



}
