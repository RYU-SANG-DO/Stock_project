<%
 /**
  * @Class Name :  stockHistUpdt.jsp
  * @Description :  종목 이력 수정하는 화면
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
	
	$("#editor").change(function(){
		console.log(11);
	});
});
/* ********************************************************
 * 수정처리화면
 ******************************************************** */
function fnUpdate(){
	let form = document.egovFrm;
	console.log(quill.root.innerHTML);
	form.userSummary.value= quill.root.innerHTML;
	form.mode.value = "update";	
	 if($("#title").val() == ""){
  		alert("제목은 필수입니다.");
		return;
  	 }else if($("#collectionDate").val() == ""){
  		alert("수집일자는 필수입니다.");
		return;
  	 }else{
		if(confirm("<spring:message code="common.update.msg" />")){	
			form.action="/stock/data/hist/saveStockHistInfo.do";	
			form.submit();	
		}
  	 }
}

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
<input type="hidden" name="stocksCode" value="${histInfo.stocksCode}">
<input type="hidden" name="seq" value="${histInfo.seq}">
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
			<th><label for="stocksCode">종목</label></th>
			<td class="left">
   				<a href="https://finance.naver.com/item/main.naver?code=${histInfo.stocksCode}" target="_blank"><c:out value="${histInfo.stocksName}"/></a>
			</td>
		</tr>
		<tr>
			<th><label for="title">제목</label></th>
			<td class="left">
   				<input type="text" name="title"	id="title" size="10" maxlength="10" value="<c:out value="${histInfo.title}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="collectionDate">수집일자</label></th>
			<td class="left">
   				<input type="text" name="collectionDate"	id="collectionDate" size="10" maxlength="10" style="width:auto;" value="<c:out value="${histInfo.collectionDate}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="origin">출처</label></th>
			<td class="left">
   				<input type="text" name="origin"	id="origin" size="10" maxlength="80"  value="<c:out value="${histInfo.origin}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="evaluation">평가</label></th>
			<td class="left">
   				<input type="text" name="evaluation"	id="evaluation" size="10" maxlength="200"  value="<c:out value="${histInfo.evaluation}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="userSummary">요약</label></th>
			<td class="nopd">
				<input type="hidden" name="userSummary" id="userSummary" value="<c:out value="${histInfo.userSummary}" escapeXml="false"/>">
				<div id="editor" style="height: 300px;"></div>
				<div id="byte-count"></div>
			</td>
		</tr>
	</tbody>
	</table>

	<!-- 하단 버튼 -->
	<div class="btn">
		<span class="btn_s"><a href="#none" onClick="fnUpdate(); return false;" title="<spring:message code="title.update" /> <spring:message code="input.button" />"><spring:message code="button.update" /></a></span>
		<span class="btn_s"><a href="#none" onClick="fnDelete(); return false;" title="<spring:message code="title.delete" /> <spring:message code="input.button" />"><spring:message code="button.delete" /></a></span>
		<span class="btn_s"><a href="#none" onClick="fnParentList();" title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div>
	<div style="clear:both;"></div>
	
</div>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
<script>
$(function(){
    if (quill && document.getElementById('userSummary')) {
        quill.root.innerHTML = document.getElementById('userSummary').value || '';
    }
});
</script>