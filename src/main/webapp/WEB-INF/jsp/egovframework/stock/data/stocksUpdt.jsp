<%
 /**
  * @Class Name :  stocksRegist.jsp
  * @Description :  종목 등록 화면
  * @Modification Information
  * @
  * @  수정일				수정내용
  * @ --------------	---------------------------
  * @ 2026.01.30			최초 생성
  *  
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javascript">
/* ********************************************************
 * 수정처리화면
 ******************************************************** */
function fn_egov_updt(){
	let form = document.egovFrm;
	 if($("#stocksCode").val() == ""){
	  		alert("종목 코드는 필수입니다.");
			return;
	  	 }else if($("#stocksName").val() == ""){
	  		alert("종목 명은 필수입니다.");
			return;
	  	 }else if($("#unitPrice").val() == ""){
	  		alert("단가는 필수입니다.");
			return;
	  	 }else{
				if(confirm("<spring:message code="common.update.msg" />")){	
					form.action="/stock/data/saveStocksInfo.do";	
					form.submit();	
				}
	  	 }
}

/* ********************************************************
 * 삭제처리
 ******************************************************** */
 function fn_egov_delete(){
	if(confirm("<spring:message code="common.delete.msg" />")){	
		// Delete하기 위한 키값을 셋팅
		document.egovFrm.mode.value = "delete";	
		document.egovFrm.action = "<c:url value='/stock/data/saveStocksInfo.do'/>";
		document.egovFrm.submit();
	}	
}	
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
	document.egovFrm.mode.value="list";
	document.egovFrm.submit();
}
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
</style>
</head>
<body>

<!-- 상단타이틀 -->
<form name="egovFrm" method="post"> 
<input type="hidden" name="mode" value="update">
<input type="hidden" name="gubun" value="${gubun}">
<input type="hidden" name="cl" value="${cl}">
<input type="hidden" name="searchKeyword" value="${searchKeyword}">
<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2>${pageTitle} <spring:message code="title.update" /></h2>

	<!-- 등록폼 -->
	<table class="wTable" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle } <spring:message code="title.update" /></caption>
	<colgroup>
		<col style="width: 20%;">
		<col style="width: ;">
	</colgroup>
	<tbody>
		<!-- 입력 -->
		<tr>
			<th><label for="relStockCode">종목코드</label></th>
			<td class="left">
   				<input type="text" name="stocksCode"	id="stocksCode" size="10" maxlength="10" style="width:auto;" readonly="readonly" value="<c:out value="${stockInfo.stocksCode}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="relStockCode">종목 명</label></th>
			<td class="left">
   				<input type="text" name="stocksName"	id="stocksName" size="80" maxlength="80" style="width:auto;" value="<c:out value="${stockInfo.stocksName}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="stocksGubun">종목구분</label></th>
			<td class="left">
				<select name="stocksGubun" id="stocksGubun" title="종목구분">
					<option value="KOSPI" <c:if test="${stockInfo.stocksGubun eq 'KOSPI'}">selected="selected"</c:if>>코스피</option> 
					<option value="KOSDAQ" <c:if test="${stockInfo.stocksGubun eq 'KOSDAQ'}">selected="selected"</c:if>>코스닥</option> 
				</select>
			</td>
		</tr>
		<tr>
			<th><label for="homepage">홈페이지</label></th>
			<td class="left">
   				<input type="text" name="homepage"	id="homepage" size="80" maxlength="80" style="width:auto;" value="<c:out value="${stockInfo.homepage}"/>" />
			</td>
		</tr>
		<tr>
			<th><label for="upjong">업종</label></th>
			<td class="left">
   				<input type="text" name="upjong"	id="upjong" size="80" maxlength="80" style="width:auto;" value="<c:out value="${stockInfo.upjong}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="mainPrduct">주요제품</label></th>
			<td class="left">
   				<input type="text" name="mainPrduct" id="mainPrduct" size="100" maxlength="350" style="width:auto;" value="<c:out value="${stockInfo.mainPrduct}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="country">국가</label></th>
			<td class="left">
				<select name="country" id="country" title="국가">
					<option value="KOR" <c:if test="${stockInfo.country eq 'KOR'}">selected="selected"</c:if>>한국</option> 
					<option value="USA" <c:if test="${stockInfo.country eq 'USA'}">selected="selected"</c:if>>미국</option> 
				</select>
			</td>
		</tr>
	</tbody>
	</table>

	<!-- 하단 버튼 -->
	<div class="btn">
		<span class="btn_s"><a href="#none" onClick="fn_egov_updt(); return false;" title="<spring:message code="title.update" /> <spring:message code="input.button" />"><spring:message code="button.update" /></a></span>
		<span class="btn_s"><a href="#none" onClick="fn_egov_delete(); return false;" title="<spring:message code="title.delete" /> <spring:message code="input.button" />"><spring:message code="button.delete" /></a></span>
		<a href="#none" onClick="fn_egov_list();" class="btn_s" title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a>
	</div>
	<div style="clear:both;"></div>
	
</div>

</form>
<script>

</script>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />