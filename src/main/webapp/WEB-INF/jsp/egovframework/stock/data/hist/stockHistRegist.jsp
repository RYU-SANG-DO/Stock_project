<%
 /**
  * @Class Name :  stockHistRegist.jsp
  * @Description :  종목 이력 등록 화면
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
$(function(){
	$("#collectionDate").datepicker( 
	        {dateFormat:'yy-mm-dd'
	         , showOn: 'button'
	         , buttonImage: '<c:url value='/images/egovframework/com/cmm/icon/bu_icon_carlendar.gif'/>'
	         , buttonImageOnly: true
	
	         , showMonthAfterYear: true
	         , showOtherMonths: true
		     , selectOtherMonths: true
				
	         , changeMonth: true // 월선택 select box 표시 (기본은 false)
	         , changeYear: true  // 년선택 selectbox 표시 (기본은 false)
	         , showButtonPanel: true // 하단 today, done  버튼기능 추가 표시 (기본은 false)
	});
	
});
/* ********************************************************
 * 등록처리화면
 ******************************************************** */
function fnInsert(){
	let form = document.egovFrm;
	console.log(quill.root.innerHTML);
	form.userSummary.value= quill.root.innerHTML;
	form.mode.value = "insert";	
	 if($("#title").val() == ""){
  		alert("제목은 필수입니다.");
		return;
  	 }else if($("#collectionDate").val() == ""){
  		alert("수집일자는 필수입니다.");
		return;
  	 }else{
		if(confirm("<spring:message code="common.regist.msg" />")){	
			form.action="/stock/data/hist/saveStockHistInfo.do";	
			form.submit();	
		}
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
label{
	margin-bottom:0px;
}
</style>
</head>
<body>

<!-- 상단타이틀 -->
<form name="egovFrm" method="post"> 
<input type="hidden" name="mode" value="insert">
<input type="hidden" name="stocksCode" value="${stockInfo.stocksCode}">
<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2>${pageTitle} <spring:message code="title.create" /></h2>

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
			<th><label for="stocksCode">종목</label></th>
			<td class="left">
   				<a href="https://finance.naver.com/item/main.naver?code=${stockInfo.stocksCode}" target="_blank"><c:out value="${stockInfo.stocksName}"/></a>
			</td>
		</tr>
		<tr>
			<th><label for="title">제목</label></th>
			<td class="left">
   				<input type="text" name="title"	id="title" size="100" maxlength="100" style="width:auto;" />
			</td>
		</tr>
		<tr>
			<th><label for="collectionDate">수집일자</label></th>
			<td class="left">
   				<input type="text" name="collectionDate"	id="collectionDate" size="10" maxlength="10" style="width:auto;" />
			</td>
		</tr>
		<tr>
			<th><label for="origin">출처</label></th>
			<td class="left">
   				<input type="text" name="origin"	id="origin" size="100" maxlength="100" style="width:auto;" />
			</td>
		</tr>
		<tr>
			<th><label for="evaluation">평가</label></th>
			<td class="left">
   				<input type="text" name="evaluation"	id="evaluation" size="100" maxlength="100" style="width:auto;" />
			</td>
		</tr>
		<tr>
			<th><label for="rm">요약</label></th>
			<td class="nopd">
				<input type="hidden" name="userSummary" id="userSummary">
				<div id="editor" style="height: 300px;"></div>
				<div id="byte-count"></div>
			</td>
		</tr>
	</tbody>
	</table>

	<!-- 하단 버튼 -->
	<div class="btn">
		<span class="btn_s"><a href="#none" onClick="fnInsert(); return false;" title="<spring:message code="title.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
		<a href="#none" onClick="fnParentList();" class="btn_s" title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a>
	</div>
	<div style="clear:both;"></div>
	
</div>

</form>
<script>
//에디터의 HTML 구조를 직접 설정
quill.root.innerHTML = document.getElementById('userSummary').value;
</script>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />