package egovframework.stock.vo.dart;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyVO extends rtComVO{	

	/**
	 * 공시정보 > 기업개황 리턴 결과 VO
	 */
//	private String status;	//에러 및 정보 코드:(※메시지 설명 참조)
//	private String message;	//에러 및 정보 메시지:(※메시지 설명 참조)
	private String corp_name;	//정식명칭:정식회사명칭
	private String corp_name_eng;	//영문명칭:영문정식회사명칭
	private String stock_name;	//종목명(상장사) 또는 약식명칭(기타법인):종목명(상장사) 또는 약식명칭(기타법인)
	private String stock_code;	//상장회사인 경우 주식의 종목코드:상장회사의 종목코드(6자리)
	private String ceo_nm;	//대표자명:대표자명
	private String corp_cls;	//법인구분:법인구분 : Y(유가), K(코스닥), N(코넥스), E(기타)
	private String jurir_no;	//법인등록번호:법인등록번호
	private String bizr_no;	//사업자등록번호:사업자등록번호
	private String adres;	//주소:주소
	private String hm_url;	//홈페이지:홈페이지
	private String ir_url;	//IR홈페이지:IR홈페이지
	private String phn_no;	//전화번호:전화번호
	private String fax_no;	//팩스번호:팩스번호
	private String induty_code;	//업종코드:업종코드
	private String est_dt;	//설립일(YYYYMMDD):설립일(YYYYMMDD)
	private String acc_mt;	//결산월(MM):결산월(MM)

    public String toString() {
        return "CompanyVO{" +
        		"status='"+this.getStatus()+'\''+"," +
        		"message='"+this.getMessage()+'\''+"," +
        		"corp_name='"+corp_name+'\''+"," +
        		"corp_name_eng='"+corp_name_eng+'\''+"," +
        		"stock_name='"+stock_name+'\''+"," +
        		"stock_code='"+stock_code+'\''+"," +
        		"ceo_nm='"+ceo_nm+'\''+"," +
        		"corp_cls='"+corp_cls+'\''+"," +
        		"jurir_no='"+jurir_no+'\''+"," +
        		"bizr_no='"+bizr_no+'\''+"," +
        		"adres='"+adres+'\''+"," +
        		"hm_url='"+hm_url+'\''+"," +
        		"ir_url='"+ir_url+'\''+"," +
        		"phn_no='"+phn_no+'\''+"," +
        		"fax_no='"+fax_no+'\''+"," +
        		"induty_code='"+induty_code+'\''+"," +
        		"est_dt='"+est_dt+'\''+"," +
        		"acc_mt='"+acc_mt+'\'' +
                '}';
    }



}
