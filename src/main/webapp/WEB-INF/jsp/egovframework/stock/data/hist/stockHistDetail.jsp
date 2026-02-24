<%
 /**
  * @Class Name : stockHistDetail.jsp
  * @Description : 종목 이력 상세조회 화면
  * @Modification Information
  * @
  * @  수정일			수정내용
  * @ -----------		---------------------------
  * @ 2026.02.08		최초 생성
  *  
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%pageContext.setAttribute("crlf", "\r\n"); %>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javascript">
 /* ********************************************************
  * 삭제처리
  ******************************************************** */
 function fnDelete(){
 	if(confirm("<spring:message code="common.delete.msg" />")){	
 		// Delete하기 위한 키값을 셋팅
 		document.egovFrm.mode.value = "delete";	
 		document.egovFrm.action = "<c:url value='/stock/data/hist/saveStockHistInfo.do'/>";
 		document.egovFrm.submit();
 	}	
 }	

 /* ********************************************************
  * 목록 으로 가기
  ******************************************************** */
 function fnParentList() {
 	document.egovFrm.action = "<c:url value='/stock/data/hist/selectStockHistList.do'/>";
 	document.egovFrm.mode.value="list";
 	document.egovFrm.submit();
 }
 
 /* ********************************************************
  * 수정 화면으로 가기
  ******************************************************** */
 function fnUpdateMove() {
 	document.egovFrm.action = "<c:url value='/stock/data/hist/moveStockHistView.do'/>";
 	document.egovFrm.mode.value="update";
 	document.egovFrm.submit();
 }
</script>
<style>
p{
	margin-bottom:0px;
}
</style>
</head>
<body>

<form name="egovFrm" method="post">
<input type="hidden" name="mode" value="update">
<input type="hidden" name="stocksCode" value="${histInfo.stocksCode}">
<input type="hidden" name="seq" value="${histInfo.seq}">
<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2><c:out value="${histInfo.stocksName}"/> ${pageTitle} <spring:message code="title.detail" /></h2>

	<!-- 상세조회 -->
	<table class="wTable" summary="">
	<caption>종목 이력 <spring:message code="title.detail" /></caption>
	<colgroup>
		<col style="width: 10%;">
		<col>
	</colgroup>
	<tbody>
		<tr>
			<th>제목</th>
			<td class="left"><c:out value="${histInfo.title}"/></td>
		</tr>
		<tr>
			<th>수집일자</th>
			<td class="left"><c:out value="${histInfo.collectionDate}"/></td>
		</tr>
		<tr>
			<th>출처</th>
			<td class="left"><c:out value="${histInfo.origin}"/></td>
		</tr>
		<tr>
			<th>평가</th>
			<td class="left"><c:out value="${histInfo.evaluation}"/></td>
		</tr>
		<tr>
			<th class="vtop">요약</th>
			<td class="cnt" id="summary-container">
				<c:out value="${fn:replace(histInfo.userSummary , crlf , '<br/>')}" escapeXml="false" />
			</td>
		</tr>
		<tr>
			<th>등록일자</th>
			<td class="left"><c:out value="${histInfo.regDate}"/></td>
		</tr>
		
		
	</tbody>
	</table>
	<!-- 하단 버튼 -->
	<div class="btn">
		<span class="btn_s"><a href="#none" onClick="fnUpdateMove(); return false;" title="<spring:message code="title.update" /> <spring:message code="input.button" />"><spring:message code="button.update" /></a></span>
		<span class="btn_s"><a href="#none" onClick="fnDelete(); return false;" title="<spring:message code="title.delete" /> <spring:message code="input.button" />"><spring:message code="button.delete" /></a></span>
		<span class="btn_s"><a href="#none" onclick="fnParentList(); return false;"  title="<spring:message code="title.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div><div style="clear:both;"></div>
	
</div>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
<script>
$(function(){
});
</script>
