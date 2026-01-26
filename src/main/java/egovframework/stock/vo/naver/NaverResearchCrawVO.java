package egovframework.stock.vo.naver;

import java.util.Arrays;
import java.util.LinkedList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.ComDefaultVO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SuppressWarnings("serial")
public class NaverResearchCrawVO extends ComDefaultVO{
	

	private int seq;            // DB 순번
    private String title;       // 제목
    private String writer;      // 작성자/증권사
    private String regDate;     // 작성일
    private String linkUrl;     // 링크
    private int viewCount;      // 조회수
    private String researchType; // 구분 (market:시황, industry:산업, company:종목)
	
	public String toString() {
        return "NaverResearchCrawVO{" +
        		"seq='"+seq+'\''+"," +
        		"title='"+title+'\''+"," +
        		"writer='"+writer+'\''+"," +
        		"regDate='"+regDate+'\''+"," +
        		"linkUrl='"+linkUrl+'\''+"," +
        		"viewCount='"+viewCount+'\''+"," +
        		"researchType='"+researchType+'\''+"," +
        		"pageIndex='"+this.getPageIndex()+'\''+"," +
        		"firstIndex='"+this.getFirstIndex()+'\''+"," +
        		"pageSize='"+this.getPageSize()+'\''+"," +
        		"pageUnit='"+this.getPageUnit()+'\''+"," +
        		"recordCountPerPage='"+this.getRecordCountPerPage()+'\'' +
        		"stockSite='"+this.getStockSite()+'\''+"," +
                '}';
    }
}
