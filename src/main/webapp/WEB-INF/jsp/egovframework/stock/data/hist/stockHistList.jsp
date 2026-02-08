<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : stockHistList.jsp
  * @Description : 종목 이력 내역 화면
  *
  */
  /* Image Path 설정 */
  String imagePath_icon   = "/images/egovframework/com/sym/prm/icon/";
  String imagePath_button = "/images/egovframework/com/sym/prm/button/";
%>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<link href="<c:url value="/css/egovframework/com/button.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-1.4.2.min.js' />" ></script>
<script type="text/javaScript">
<!--
/* ********************************************************
 * 페이징 처리 함수
 ******************************************************** */
function linkPage(pageNo){
	document.stockFrm.pageIndex.value = pageNo;
	document.stockFrm.action = "<c:url value='/stock/data/hist/selectStockHistList.do'/>";
   	document.stockFrm.submit();
}

/* ********************************************************
 * 조회 처리 함수
 ******************************************************** */
function fnSearchList() {	
	document.stockFrm.pageIndex.value = 1;
	document.stockFrm.action = "<c:url value='/stock/data/hist/selectStockHistList.do'/>";
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

//종목 이력 등록
function fnInsertStockHist(){
	document.stockFrm.mode.value="insert";
	document.stockFrm.action = "<c:url value='/stock/data/hist/moveStockHistView.do'/>";
   	document.stockFrm.submit();
}
//종목 이력 상세
function fnStockHistDetail(seq){
	document.stockFrm.seq.value=seq;
	document.stockFrm.action = "<c:url value='/stock/data/hist/selectStockHistDetail.do'/>";
   	document.stockFrm.submit();
}

//종목 이력
function fnStockHist(code){
	document.stockFrm.stocksCode.value=code;
	document.stockFrm.action = "<c:url value='/stock/data/hist/selectStockHistList.do'/>";
   	document.stockFrm.submit();
}

/* ********************************************************
 * 종목 목록 으로 가기
 ******************************************************** */
function fnParentList() {
	document.pStockFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
	document.pStockFrm.submit();
}
-->
</script>
<style>
input[type="number"] {
	width:15%;
    border: 1px solid #d2d2d2;
    color: #727272;
    }
.cssright{
	text-align: right;
}    
.search_box ul{
	margin-bottom: 0px;
}
.search_box ul label{
	margin-bottom: 0px;
}
</style>
</head>
<body>
<form name="pStockFrm" action ="<c:url value='/stock/data/selectStocksList.do'/>" method="post"></form>
<form name="stockFrm" action ="<c:url value='/stock/data/hist/selectStockHistList.do'/>" method="post">
<input type="hidden" name="mode">
<input type="hidden" name="pageIndex" value="<c:out value='${pageIndex}'/>"/>
<input type="hidden" name="stocksCode"	id="stocksCode" value="<c:out value='${stockInfo.stocksCode}'/>"/>
<input type="hidden" name="seq"	id="seq"/>
<div class="board" style="width:100%;">
	<h1><c:out value='${stockInfo.stocksName}'/>[<c:out value='${stockInfo.stocksCode}'/>][<c:out value='${stockInfo.stocksGubunNm}'/>]</h1>

	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />"><!-- 이 레이아웃은 하단 정보를 대한 검색 정보로 구성되어 있습니다. -->
		<ul>
			<li>
				<label for="gubun">구분 : </label>
				<select name="gubun" id="gubun" class="select" title="구분">
					<option value="TITLE" <c:if test="${empty gubun or gubun eq 'TITLE'}">selected="selected"</c:if>>제목</option>
					<option value="ORIGIN" <c:if test="${'ORIGIN' eq gubun}">selected="selected"</c:if>>출처</option>
				</select>
			</li>
			<li>
				<label for="">검색명 : </label>
				<input class="s_input2 vat" name="searchKeyword" type="text"  value='<c:out value="${stocksDataVO.searchKeyword}"/>'  size="30" maxlength="60" title="<spring:message code="title.searchCondition"/>" /><!-- 검색조건 -->
				<input class="s_btn" type="submit" value='<spring:message code="button.inquire" />' title='<spring:message code="title.inquire"/>' onclick="fnSearchList(); return false;" />
			</li>
		</ul>
	</div>
	<div class="button_box">
		<ul style="margin-bottom: 0px;">
			<li style="float:left;">
				페이지 사이즈:
				<select name="pageUnit" class="select" title="페이지">
					<option value="10" <c:if test="${'10' eq pageUnit}">selected="selected"</c:if>>10</option>
					<option value="30" <c:if test="${'30' eq pageUnit}">selected="selected"</c:if>>30</option>
					<option value="60" <c:if test="${'60' eq pageUnit}">selected="selected"</c:if>>60</option>
					<option value="100" <c:if test="${'100' eq pageUnit}">selected="selected"</c:if>>100</option>
				</select>
			</li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="fnParentList()" value="목록" title="목록 <spring:message code="input.button" />" />	
				<input type="button" class="s_btn" onClick="fnInsertStockHist()" value="이력 등록" title="이력 등록 <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	<table class="board_list">
		<caption></caption>
		<colgroup>
			<col style="width:3%"/>
			<col/>
			<col/>
			<col/>
			<col style="width:5%"/>
			<col style="width:5%" />
			<col style="width:5%" />
		</colgroup>
		<thead>
			<tr>
			   <th scope="col">순번</th>
			   <th scope="col">제목</th>
			   <th scope="col">출처</th>
			   <th scope="col">평가</th>
			   <th scope="col">수집일자</th>
			   <th scope="col">등록일자</th>
			   <th scope="col">상세</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(list) == 0}">
				<tr>
					<td colspan="7"><spring:message code="common.nodata.msg" /></td>
				</tr>
			</c:if>
			<c:forEach var="result" items="${list}" varStatus="status">
			  <tr>
			  	<td><c:out value="${result.rn}"/></td>
			    <td style="text-align: left;"><c:out value="${result.title}"/></td>
			    <td><c:out value="${result.origin}"/></td>
			    <td style="text-align: left;"><c:out value="${result.evaluation}"/></td>
			    <td><c:out value="${result.collectionDate}" escapeXml="false"/></td>
			    <td><c:out value="${result.regDate}"/></td>
			    <td>
			    	<div class="button_box">
						<ul style="margin-bottom: 0px;text-align: center;">
							<li style="border: 0px solid #d2d2d2;">
								<input type="button" class="s_btn" onClick="fnStockHistDetail('${result.seq}');" value="<spring:message code="title.update" />" title="<spring:message code="title.update" /> <spring:message code="input.button" />" />
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

