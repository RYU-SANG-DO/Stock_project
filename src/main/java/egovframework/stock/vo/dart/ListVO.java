package egovframework.stock.vo.dart;

import org.w3c.dom.Element;

import egovframework.stock.com.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListVO extends rtComVO{	

	/**
	 * 공시정보 > 공시검색 리턴 결과 VO
	 */
	private String corp_cls;	//법인구분:법인구분 : Y(유가), K(코스닥), N(코넥스), E(기타)
	private String corp_name;	//종목명(법인명):공시대상회사의 종목명(상장사) 또는 법인명(기타법인)
	private String corp_code;	//고유번호:공시대상회사의 고유번호(8자리)
	private String stock_code;	//종목코드:상장회사의 종목코드(6자리)
	private String report_nm;	//보고서명:공시구분+보고서명+기타정보
	private String rcept_no;	//접수번호:접수번호(14자리)
	private String flr_nm;	//공시 제출인명:공시 제출인명
	private String rcept_dt;	//접수일자:공시 접수일자(YYYYMMDD)
	private String rm;	//비고:조합된 문자로 각각은 아래와 같은 의미가 있음
	
	// 생성자
    public ListVO(String corpCode, String corpName, String stockCode, String corpCls, String reportName, String rceptNo, String flrNm, String rceptDt) {
        this.corp_code = corpCode;
        this.corp_name = corpName;
        this.stock_code = stockCode;
        this.corp_cls = corpCls;
        this.report_nm = reportName;
        this.rcept_no = rceptNo;
        this.flr_nm = flrNm;
        this.rcept_dt = rceptDt;
    }

    // 생성자
    public ListVO(Element element) {
        this.corp_code = StringUtil.getTagValue("corp_code", element);
        this.corp_name = StringUtil.getTagValue("corp_name", element);
        this.stock_code = StringUtil.getTagValue("stock_code", element);
        this.corp_cls = StringUtil.getTagValue("corp_cls", element);
        this.report_nm = StringUtil.getTagValue("report_nm", element);
        this.rcept_no = StringUtil.getTagValue("rcept_no", element);
        this.flr_nm = StringUtil.getTagValue("flr_nm", element);
        this.rcept_dt = StringUtil.getTagValue("rcept_dt", element);
    }


    public String toString() {
    	return "ListVO{" +
        		"corp_cls='"+corp_cls+'\'' +"," +
        		"corp_name='"+corp_name+'\'' +"," +
        		"corp_code='"+corp_code+'\'' +"," +
        		"stock_code='"+stock_code+'\'' +"," +
        		"report_nm='"+report_nm+'\'' +"," +
        		"rcept_no='"+rcept_no+'\'' +"," +
        		"flr_nm='"+flr_nm+'\'' +"," +
        		"rcept_dt='"+rcept_dt+'\'' +"," +
        		"rm='"+rm+'\'' +
                '}';
    }



}
