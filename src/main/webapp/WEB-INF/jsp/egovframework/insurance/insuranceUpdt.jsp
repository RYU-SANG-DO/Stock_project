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
<%@ taglib prefix="egovc" uri="/WEB-INF/tlds/egovc.tld" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/fms/EgovMultiFiles.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/utl/EgovCmmUtl.js'/>" ></script>
<script type="text/javascript">
$(function(){
	 $.datepicker.setDefaults({
		  dateFormat: 'yy-mm-dd',
		  closeText: "닫기",
		  currentText: "오늘",
		  prevText: '이전 달',
		  nextText: '다음 달',
		  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
		  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
		  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
		  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
		  showMonthAfterYear: true,
		  yearSuffix: '년'
		});
	$("#cttStartDate,#tmntDate").datepicker( 
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
	         , yearRange: "c-100:c+1"
	});
})
/* ********************************************************
 * 등록처리화면
 ******************************************************** */
function fn_egov_insert(){
	let form = document.egovFrm;
	console.log(quill.root.innerHTML);
	form.etc.value= quill.root.innerHTML;
	if($("#insCpy").val() == ""){
  		alert("보험사는 필수입니다.");
		return;
  	 }else if($("#ctfcNum").val() == ""){
  		alert("증권번호는 필수입니다.");
		return;
  	 }else if($("#cttStartDate").val() == ""){
  		alert("계약일자는 필수입니다.");
		return;
  	 }else if($("#payPeod").val() == ""){
  		alert("납입기간은 필수입니다.");
		return;
  	 }else if($("#insAmt").val() == ""){
  		alert("보험료는 필수입니다.");
		return;
  	 }
	
	form.mode.value = "insert";	
	$('#insAmt').val($('#insAmt').val().replace(/,/g, ""));
	if(confirm("<spring:message code="common.regist.msg" />")){	
		form.action="/insurance/saveInsurance.do";	
		form.submit();	
	}
}
/* ********************************************************
 * 수정처리화면
 ******************************************************** */
function fn_egov_update(){
	let form = document.egovFrm;
	console.log(quill.root.innerHTML);
	form.etc.value= quill.root.innerHTML;
	if($("#insCpy").val() == ""){
  		alert("보험사는 필수입니다.");
		return;
  	 }else if($("#ctfcNum").val() == ""){
  		alert("증권번호는 필수입니다.");
		return;
  	 }else if($("#cttStartDate").val() == ""){
  		alert("계약일자는 필수입니다.");
		return;
  	 }else if($("#payPeod").val() == ""){
  		alert("납입기간은 필수입니다.");
		return;
  	 }else if($("#insAmt").val() == ""){
  		alert("보험료는 필수입니다.");
		return;
  	 }
	
	form.mode.value = "update";	
	$('#insAmt').val($('#insAmt').val().replace(/,/g, ""));
	if(confirm("<spring:message code="common.update.msg" />")){	
		form.action="/insurance/saveInsurance.do";	
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
		document.egovFrm.action = "<c:url value='/insurance/saveInsurance.do'/>";
		document.egovFrm.submit();
	}	
}	
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/insurance/selectInsuranceList.do'/>";
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
<form name="egovFrm" method="post" enctype="multipart/form-data"> 
<input type="text" name="mode" value="${move}">

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
			<th>보험사 <span class="insCpy">*</span></th>
			<td class="left">
				<select name="insCpy" id="insCpy" title="보험사">
				<c:forEach var="item" items="${inscpyList}" varStatus="status">
					<option value="${item.code}" <c:if test="${insuranceInfo.insCpy eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><label for="pmDate">증권번호</label></th>
			<td class="left">
   				<input type="text" name="ctfcNum" id="ctfcNum" size="25" maxlength="15" style="text-align:cneter; width: auto;" value="<c:out value="${insuranceInfo.ctfcNum}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="cttNm">계약명</label></th>
			<td class="left">
   				<input type="text" name="cttNm" id="cttNm" size="80" maxlength="15" style="width: auto;" value="<c:out value="${insuranceInfo.cttNm}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="cttUser">계약자</label></th>
			<td class="left">
   				<input type="text" name="cttUser" id="cttUser" size="10" maxlength="10" style="width: auto;" value="<c:out value="${insuranceInfo.cttUser}"/>"/>
			</td>
		</tr>
		<tr>
			<th>상태 <span class="stats">*</span></th>
			<td class="left">
				<select name="stats" id="stats" title="상태">
				<c:forEach var="item" items="${statsList}" varStatus="status">
					<option value="${item.code}" <c:if test="${insuranceInfo.stats eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><label for="cttStartDate">계약일자 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="text" name="cttStartDate"	id="cttStartDate" size="10" maxlength="15" readonly="readonly" style="text-align:cneter; width: auto; margin-right:5px;" value="<c:out value="${insuranceInfo.cttStartDate}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="payPeod">납입기간</label></th>
			<td class="left">
   				<input type="text" name="payPeod" id="payPeod" size="10" maxlength="3" style="text-align:cneter; width: auto;" value="<c:out value="${insuranceInfo.payPeod}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="tnsfDate">이체일</label></th>
			<td class="left">
   				<input type="text" name="tnsfDate" id="tnsfDate" size="10" maxlength="3" style="text-align:cneter; width: auto;" value="<c:out value="${insuranceInfo.tnsfDate}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="insAmt">보험료</label></th>
			<td class="left">
   				<input type="text" name="insAmt" id="insAmt" size="20" maxlength="10" style="text-align:right; width: auto;" value="<c:out value="${insuranceInfo.insAmt}"/>"/>
			</td>
		</tr>
		<tr>
			<th>결재은행 <span class="bank">*</span></th>
			<td class="left">
				<select name="bank" id="bank" title="결재은행">
				<c:forEach var="item" items="${bankList}" varStatus="status">
					<option value="${item.code}" <c:if test="${insuranceInfo.bank eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<th><label for="tmntDate">해지일자 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="text" name="tmntDate"	id="tmntDate" size="10" maxlength="15" readonly="readonly" style="text-align:cneter; width: auto; margin-right:5px;" value="<c:out value="${insuranceInfo.tmntDate}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="manager">담당자</label></th>
			<td class="left">
   				<input type="text" name="manager" id="manager" size="10" maxlength="15" style="width: auto;" value="<c:out value="${insuranceInfo.manager}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="phoneNum">전화번호</label></th>
			<td class="left">
   				<input type="text" name="phoneNum" id="phoneNum" size="25" maxlength="15" style="text-align:cneter; width: auto;" value="<c:out value="${insuranceInfo.phoneNum}"/>"/>
			</td>
		</tr>
		<c:set var="title"><spring:message code="comCopBbs.articleVO.updt.atchFile"/></c:set>
		<c:if test="${not empty insuranceInfo.atchFileId}">
			<tr>
				<th>${title}</th>
				<td class="nopd">
					<c:import url="/cmm/fms/selectFileInfsForUpdate.do" charEncoding="utf-8">
						<c:param name="pageIndex" value="1" />
						<c:param name="param_atchFileId" value="FILE_000000000000001" />
					</c:import>
				</td>
			</tr>
		</c:if>
		<!-- 첨부파일 추가 시작 -->
		<c:set var="title"><spring:message code="comCopBbs.articleVO.updt.atchFileAdd"/></c:set>
		<tr>
			<th><label for="file_1">${title}</label> </th>
			<td class="nopd" colspan="3">
				<input type="file" name="file_1" id="egovComFileUploader" title="<spring:message code="comCopBbs.articleVO.updt.atchFile"/>" multiple/><!-- 첨부파일 -->
			    <div id="egovComFileList"></div>
			</td>
		</tr>
		<!-- 첨부파일 추가 끝 -->
		<tr>
			<th><label for="etc">비고</label></th>
			<td class="nopd">
				<input type="hidden" name="etc" id="etc" value="<c:out value="${insuranceInfo.etc}"/>">
				<div id="editor" style="height: 300px;"></div>
				<div id="byte-count"></div>
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
		</c:if>
		<span class="btn_s"><a href="#none" onClick="fn_egov_list(); return false;" title="<spring:message code="button.list" /> <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div>
	<div style="clear:both;"></div>
	
</div>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
<script>
$(function(){
    if (quill && document.getElementById('etc')) {
        quill.root.innerHTML = document.getElementById('etc').value || '';
    }
    
    var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), 10 );
    multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
});
</script>