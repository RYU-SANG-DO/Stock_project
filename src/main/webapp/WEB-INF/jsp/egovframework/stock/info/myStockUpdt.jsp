<%
 /**
  * @Class Name :  myStockUpdt.jsp
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
	$("#delngDe").datepicker( 
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
 * 수정처리화면
 ******************************************************** */
function fn_egov_updt(){
	let form = document.egovFrm;
	form.mode.value = "update";	
	 if($("#stock_code").val() == ""){
	  		alert("종목은 필수입니다.");
			return;
	  	 }else if($("#qy").val() == ""){
	  		alert("수량은 필수입니다.");
			return;
	  	 }else if($("#unitPrice").val() == ""){
	  		alert("단가는 필수입니다.");
			return;
	  	 //}else if($("#delngDe").val() == ""){
	  	//	alert("거래일자는 필수입니다.");
		//	return;
	  	 }else{
	  		$('#qy').val($('#qy').val().replace(/,/g, ""));
	  		$('#fee').val($('#fee').val().replace(/,/g, ""));
	  		$('#trftax').val($('#trftax').val().replace(/,/g, ""));
	  		$('#incmtax').val($('#incmtax').val().replace(/,/g, ""));
	  		$('#unitPrice').val($('#unitPrice').val().replace(/,/g, ""));
	  		if(confirm("<spring:message code="common.update.msg" />")){	
	  			form.action="/stock/info/saveMyStock.do";	
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
		document.egovFrm.action = "<c:url value='/stock/info/saveMyStock.do'/>";
		document.egovFrm.submit();
	}	
}	
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/stock/info/selectMyStockList.do'/>";
	document.egovFrm.mode.value="list";
	document.egovFrm.seq.value="";
	document.egovFrm.submit();
}
</script>
<style>
.cssright{
	text-align: right;
}    
</style>
</head>
<body>

<!-- 상단타이틀 -->
<form name="egovFrm" method="post"> 
<input type="hidden" name="mode" value="update">
<input type="hidden" name="seq" value="${seq}">
<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2>${pageTitle} <spring:message code="title.create" /></h2>

	<!-- 등록폼 -->
	<table class="wTable" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle } <spring:message code="title.create" /></caption>
	<colgroup>
		<col style="width: 20%;">
		<col style="width: ;">
	</colgroup>
	<tbody>
		<!-- 입력 -->
		<tr>
			<th><label for="code">종목코드 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="hidden" name="code"	id="stock_code" value="<c:out value="${infoMap.stocksCode}"/>"/>
   				<input type="text" name="codeNm" id="searchKeyword" size="30" maxlength="100" style="width: auto;" readonly="readonly" value="<c:out value="${infoMap.stocksName}"/>"/>
   				<%-- <a id="popupStocks" href="#none" target="_blank" title="종목 검색" style="selector-dummy:expression(this.hideFocus=false);">
						<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(종목검색)
					</a> --%>
			</td>
		</tr>
		
		<tr>
			<th><label for="qy">수량 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="text" name="qy"	id="qy" class="cssright" size="10" maxlength="10" style="width:auto; text-align: right;" value="<c:out value="${infoMap.qy}"/>"/>
			</td>
		</tr>
		
		<tr>
			<th>구분 <span class="pilsu">*</span></th>
			<td class="left">
				<select name="gubun" id="gubun" title="구분">
					<option value="BUY" <c:if test="${infoMap.gubun eq 'BUY'}">selected="selected"</c:if>>매수</option> 
					<option value="SELL" <c:if test="${infoMap.gubun eq 'SELL'}">selected="selected"</c:if>>매도</option> 
				</select>
			</td>
		</tr>
		
		<tr>
			<th><label for="fee">수수료</label></th>
			<td class="left">
   				<input type="text" name="fee"	id="fee" size="10" maxlength="5" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.fee}" pattern="#,###" />"/>원
			</td>
		</tr>
		
		<tr>
			<th><label for="trftax">거래세/농특세</label></th>
			<td class="left">			
   				<input type="text" name="trftax"	id="trftax" size="10" maxlength="5" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.trftax}" pattern="#,###" />"/>원
			</td>
		</tr>
		<tr>
			<th><label for="incmtax">소득세/주민세</label></th>
			<td class="left">			
   				<input type="text" name="incmtax"	id="incmtax" size="10" maxlength="5" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.incmtax}" pattern="#,###" />"/>원
			</td>
		</tr>
		<tr>
			<th><label for="unitPrice">단가 <span class="pilsu">*</span></label></th>
			<td class="left">			
   				<input type="text" name="unitPrice"	id="unitPrice" size="10" maxlength="5" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.unitPrice}" pattern="#,###" />"/>원
			</td>
		</tr>
		<tr>
			<th><label for="delngDe">거래일자 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="text" name="delngDe"	id="delngDe" size="10" maxlength="15" readonly="readonly" style="text-align:cneter; width: auto; margin-right:5px;"/>
			</td>
		</tr>
		<tr>
			<th><label for="account">계좌</label></th>
			<td class="left">			
   				<input type="text" name="account"	id="account" size="10" maxlength="9" style="width:auto;" value="<c:out value="${infoMap.account}"/>"/>
			</td>
		</tr>
		<tr>
			<th><label for="rm">비고</label></th>
			<td class="nopd">
				<textarea name="rm" title="비고" cols="300" rows="10" style="height:auto;"></textarea>   
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

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />