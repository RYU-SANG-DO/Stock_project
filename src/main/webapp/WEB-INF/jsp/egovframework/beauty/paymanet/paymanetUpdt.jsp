<%
 /**
  * @Class Name :  paymanetUpdt.jsp
  * @Description :  My 거래내역 수정하는 화면
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
	$("#pmDate").datepicker( 
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
})
/* ********************************************************
 * 등록처리화면
 ******************************************************** */
function fn_egov_insert(){
	let form = document.egovFrm;
	if($("#pmDate").val() == ""){
  		alert("매출일자는 필수입니다.");
		return;
  	 }else if($("#pmPrice").val() == ""){
  		alert("결재금액은 필수입니다.");
		return;
  	 }
	
	form.mode.value = "insert";	
	$('#pmPrice').val($('#pmPrice').val().replace(/,/g, ""));
	if(confirm("<spring:message code="common.regist.msg" />")){	
		form.action="/beauty/paymanet/saveBeautyPaymanet.do";	
		form.submit();	
	}
}
/* ********************************************************
 * 수정처리화면
 ******************************************************** */
function fn_egov_update(){
	let form = document.egovFrm;
	if($("#pmDate").val() == ""){
  		alert("매출일자는 필수입니다.");
		return;
  	 }else if($("#pmPrice").val() == ""){
  		alert("결재금액은 필수입니다.");
		return;
  	 }
	form.mode.value = "update";	
	$('#pmPrice').val($('#pmPrice').val().replace(/,/g, ""));
	if(confirm("<spring:message code="common.update.msg" />")){	
		form.action="/beauty/paymanet/saveBeautyPaymanet.do";	
		form.submit();	
	}
}

/* ********************************************************
 * 삭제처리
 ******************************************************** */
 function fn_egov_delete(){
	if(confirm("<spring:message code="common.delete.msg" />")){	
		// Delete하기 위한 키값을 셋팅
		document.egovFrm.mode.value = "delete";	
		document.egovFrm.action = "<c:url value='/beauty/paymanet/saveBeautyPaymanet.do'/>";
		document.egovFrm.submit();
	}	
}	
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/beauty/paymanet/selectBeautyPaymanetList.do'/>";
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
<input type="text" name="mode" value="${move}">
<input type="text" name="rpId" value="${beautyInfo.seq}">

<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2>${pageTitle} <spring:message code="title.update" /></h2>

	<!-- 등록폼 -->
	<table class="wTable" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle } <spring:message code="title.update" /></caption>
	<colgroup>
		<col style="width: 10%;">
		<col>
	</colgroup>
	<tbody>
		<tr>
			<th><label for="pmDate">매출일자</label></th>
			<td class="left">
   				<input type="text" name="pmDate" id="pmDate" size="10" maxlength="15" readonly="readonly" style="text-align:cneter; width: auto;" value="<c:out value="${beautyInfo.pmDate}"/>"/>
			</td>
		</tr>
		<tr>
			<th>스타일 <span class="styleType">*</span></th>
			<td class="left">
				<select name="styleType" id="styleType" title="스타일">
				<c:forEach var="item" items="${styleList}" varStatus="status">
					<option value="${item.code}" <c:if test="${beautyInfo.styleType eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th>결재타입 <span class="pmType">*</span></th>
			<td class="left">
				<select name="pmType" id="pmType" title="결재타입">
				<c:forEach var="item" items="${pmtypeList}" varStatus="status">
					<option value="${item.code}" <c:if test="${beautyInfo.pmType eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><label for="pmPrice">결재금액</label></th>
			<td class="left">
   				<input type="text" name="pmPrice"	id="pmPrice" size="12" maxlength="10" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${beautyInfo.pmPrice}" pattern="#,###" />"/>원
			</td>
		</tr>		
	</tbody>
	</table>

	<!-- 하단 버튼 -->
	<div class="btn">
		<c:if test="${move eq 'update'}">
			<span class="btn_s"><a href="#none" onClick="fn_egov_update(); return false;" title="<spring:message code="title.update" /> <spring:message code="input.button" />"><spring:message code="button.update" /></a></span>
			<span class="btn_s"><a href="#none" onClick="fn_egov_delete(); return false;" title="<spring:message code="title.delete" /> <spring:message code="input.button" />"><spring:message code="button.delete" /></a></span>
		</c:if>
		<c:if test="${move ne 'update'}">
			<span class="btn_s"><a href="#none" onClick="fn_egov_insert(); return false;" title="<spring:message code="title.create" /> <spring:message code="input.button" />"><spring:message code="button.create" /></a></span>
		</c:if>>
		<span class="btn_s"><a href="#none" onClick="fn_egov_list(); return false;" title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div>
	<div style="clear:both;"></div>
	
</div>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
<script>
$(function(){
    //if (quill && document.getElementById('userSummary')) {
    //    quill.root.innerHTML = document.getElementById('userSummary').value || '';
    //}
});
</script>