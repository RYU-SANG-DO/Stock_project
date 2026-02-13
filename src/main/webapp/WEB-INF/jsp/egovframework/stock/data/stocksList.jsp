<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : stocksList.jsp
  * @Description : 주식 검색 화면
  *
  */
  /* Image Path 설정 */
  String imagePath_icon   = "/images/egovframework/com/sym/prm/icon/";
  String imagePath_button = "/images/egovframework/com/sym/prm/button/";
%>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<%-- <script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-1.4.2.min.js' />" ></script> --%>
<script type="text/javaScript">
$(function(){	
	//모달 바깥쪽 클릭 시 닫기
	$(window).on('click', function(event) {
	    if ($(event.target).is('#chartModal')) {
	        closeModal();
	    }
	});
});
<!--
/* ********************************************************
 * 페이징 처리 함수
 ******************************************************** */
function linkPage(pageNo){
	document.stockFrm.pageIndex.value = pageNo;
	document.stockFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
   	document.stockFrm.submit();
}

/* ********************************************************
 * 조회 처리 함수
 ******************************************************** */
function selectStocksListSearch() {	
	document.stockFrm.pageIndex.value = 1;
	document.stockFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
	document.stockFrm.submit();
}

/* ********************************************************
 * 프로그램목록 선택 처리 함수
 ******************************************************** */
function choisStocksListSearch(vCode,vName) {
	var parentFrom = parent.document.getElementsByTagName('form');
	parent.$("#stock_code").val(vCode);	
	parent.$("#searchKeyword").val(vName);	
    parent.$('.ui-dialog-content').dialog('close');
}

function fnInsertStock(){
	document.stockFrm.mode.value="insert";
	document.stockFrm.action = "<c:url value='/stock/data/moveStockView.do'/>";
   	document.stockFrm.submit();
}
//종목 정보 수정
function fnUpdateStock(code){
	document.stockFrm.mode.value="update";
	document.stockFrm.stocksCode.value=code;
	document.stockFrm.action = "<c:url value='/stock/data/moveStockView.do'/>";
   	document.stockFrm.submit();
}

//종목 이력
function fnStockHistList(code){
	document.stockFrm.stocksCode.value=code;
	document.stockFrm.searchKeyword.value="";
	document.stockFrm.action = "<c:url value='/stock/data/hist/selectStockHistList.do'/>";
   	document.stockFrm.submit();
}

-->
</script>
<style>
label {
    margin-bottom: 0px;
}
</style>
</head>
<body>
<form name="stockFrm" action ="<c:url value='/stock/data/selectStocksList.do'/>" method="post">
<input name="pageIndex" type="hidden" value="<c:out value='${stocksDataVO.pageIndex}'/>"/>
<input type="hidden" name="mode"	id="mode" />
<input type="hidden" name="stocksCode"	id="stocksCode" />
<div class="board" style="width:100%;">
	<h1>종목 검색</h1>

	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />"><!-- 이 레이아웃은 하단 정보를 대한 검색 정보로 구성되어 있습니다. -->
		<ul>
			<li>
				<label for="gubun">구분 : </label>
				<select name="gubun" id="gubun" class="select" title="구분">
					<option value="" <c:if test="${empty gubun}">selected="selected"</c:if>>전체</option>
					<option value="KOSPI" <c:if test="${'KOSPI' eq gubun}">selected="selected"</c:if>>코스피</option>
					<option value="KOSDAQ" <c:if test="${'KOSDAQ' eq gubun}">selected="selected"</c:if>>코스닥</option>
				</select>
			</li>
			<li>
				<label for="gubun">분류 : </label>
				<select name="cl" id="cl" class="select" title="분류">
					<option value="NAME" <c:if test="${empty cl || 'NAME' eq cl}">selected="selected"</c:if>>종목명</option>
					<option value="UPJONG" <c:if test="${'UPJONG' eq cl}">selected="selected"</c:if>>업종</option>
					<option value="PRDUCT" <c:if test="${'PRDUCT' eq cl}">selected="selected"</c:if>>주요제품</option>
				</select>
			</li>
			<li>
				<label for="">검색명 : </label><!-- 프로그램명 -->
				<input class="s_input2 vat" name="searchKeyword" type="text"  value='<c:out value="${stocksDataVO.searchKeyword}"/>'  size="30" maxlength="60" title="<spring:message code="title.searchCondition"/>" /><!-- 검색조건 -->
				<input class="s_btn" type="submit" value='<spring:message code="button.inquire" />' title='<spring:message code="title.inquire"/>' onclick="selectStocksListSearch(); return false;" />
			</li>
		</ul>
	</div>
	<div class="button_box">
		<ul style="margin-bottom: 0px;">
			<li style="float:left;">
				페이지 사이즈:
				<select name="pageUnit" class="select" title="페이지">
					<option value="10" <c:if test="${'10' eq stocksDataVO.pageUnit}">selected="selected"</c:if>>10</option>
					<option value="30" <c:if test="${'30' eq stocksDataVO.pageUnit}">selected="selected"</c:if>>30</option>
					<option value="60" <c:if test="${'60' eq stocksDataVO.pageUnit}">selected="selected"</c:if>>60</option>
					<option value="100" <c:if test="${'100' eq stocksDataVO.pageUnit}">selected="selected"</c:if>>100</option>
				</select>
			</li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="fnInsertStock()" value="종목 등록" title="종목 등록 <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	<table class="board_list">
		<caption></caption>
		<colgroup>
			<col style="width:3%"/>
			<col style="width:5%"/>
			<col style="width:20%" />
			<col style="width:5%"/>
			<col style="width:5%"/>
			<col/>
			<col/>
			<col style="width:5%" />
			<col style="width:5%" />
		</colgroup>
		<thead>
			<tr>
			   <th scope="col">순번</th>
			   <th scope="col">종목코드</th>
			   <th scope="col">종목명</th>
			   <th scope="col">주가</th>
			   <th scope="col">등락률</th>
			   <th scope="col">업종</th>
			   <th scope="col">주요제품</th>
			   <th scope="col">이력건수</th>
			   <th scope="col">수정</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(resultList) == 0}">
				<tr>
					<td colspan="9"><spring:message code="common.nodata.msg" /></td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${resultList}" varStatus="status">
			<c:set var="pClassColore" value="#666"/>
			<c:choose>
				<c:when test="${result.risigHnl eq 'H'}"><c:set var="pClassColore" value="red"/></c:when>
				<c:when test="${result.risigHnl eq 'L'}"><c:set var="pClassColore" value="#007bff"/></c:when>
			</c:choose>
			  <tr>
			  	<td><c:out value="${result.rn}"/></td>
			    <td>
			      <span class="link">
			      	<a href="#none" onclick="stockOpenPopup('https://finance.naver.com/item/main.naver?code=${result.stocksCode}','${result.stocksName}','1000','600'); return false;" title="네이버 종합정보 이동"><c:out value="${result.stocksCode}"/></a>
			      </span>
			    </td>
			    <td>
			    	<span class="link">
			      	<a href="#none" onclick="stockOpenPopup('https://finance.naver.com/item/main.naver?code=${result.stocksCode}','${result.stocksName}','1000','600'); return false;" title="네이버 종합정보 이동"><c:out value="${result.stocksName}"/></a>
			      	<a href="#none" onclick="stockOpenPopup('${result.homepage}','${result.stocksName}','1000','600'); return false;" title="${result.stocksName} 홈페이지"><img src="/images/egovframework/stock/icon_home.gif"></a>
			      	<a href="#none" onclick="stockOpenPopup('https://finance.naver.com/item/fchart.naver?code=${result.stocksCode}','${result.stocksName}','1000','600'); return false;" title="네이버 챠트보기 이동"><img src="/images/egovframework/stock/chart.png" style="width: 14px;"></a>
			      	<span class="chart-icons" style="cursor:pointer;">
				        <i class="fa fa-calendar-day" onclick="openChartModal('${result.stocksCode}' , '${result.stocksName}', 'day')" title="일봉차트 이미지팝업">[일]</i>
				        <i class="fa fa-calendar-week" onclick="openChartModal('${result.stocksCode}' , '${result.stocksName}' , 'week')" title="주봉차트 이미지팝업">[주]</i>
				        <i class="fa fa-calendar-alt" onclick="openChartModal('${result.stocksCode}' , '${result.stocksName}' , 'month')" title="월봉차트 이미지팝업">[월]</i>
				    </span>
			      </span>
			    </td>
			    <td style="color: ${pClassColore}"><c:out value="${result.nowPrice}"/>원</td>
			    <td style="color: ${pClassColore}"><c:out value="${result.indepercent}"/></td>
			    <td style="text-align: left;"><c:out value="${result.upjong}"/></td>
			    <td style="text-align: left;"><c:out value="${result.mainPrduct}"/></td>
			    <td>
			    	<div class="button_box">
						<ul style="margin-bottom: 0px;text-align: center;">
							<li style="border: 0px solid #d2d2d2;">
								<input type="button" class="s_btn" onClick="fnStockHistList('<c:out value="${result.stocksCode}"/>');" value="<c:out value="${result.histCnt}"/>" title="이력" />
							</li>
						</ul>
					</div>
			    </td>
			     <td>
			    	<div class="button_box">
						<ul style="margin-bottom: 0px;text-align: center;">
							<li style="border: 0px solid #d2d2d2;">
								<input type="button" class="s_btn" onClick="fnUpdateStock('${result.stocksCode}');" value="<spring:message code="title.update" />" title="<spring:message code="title.update" /> <spring:message code="input.button" />" />
							</li>
						</ul>
					</div>
			    </td>
			  </tr>
			 </c:forEach>
		</tbody>
	</table>

	<!-- paging navigation -->
	<div class="pagination">
		<ul>
			<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/>
		</ul>
	</div>
</div>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />

