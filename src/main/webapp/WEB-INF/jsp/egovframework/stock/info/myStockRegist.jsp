<%
 /**
  * @Class Name : myStockRegist.jsp
  * @Description : 공통분류코드 등록 화면
  * @Modification Information
  * @
  * @  수정일             수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.02.01   박정규              최초 생성
  *  @see
  *  
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
	
	 $('#popupStocks').click(function (e) {
	     	e.preventDefault();
	         //var page = $(this).attr("href");
	         var pagetitle = $(this).attr("title");
	         var page = "<c:url value='/stock/data/selectStocksPopList.do'/>";
	         var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
	         .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
	         .dialog({
	         	autoOpen: false,
	             modal: true,
	             width: 820,
	             height: 700,
	             title: pagetitle
	     	});
	     	$dialog.dialog('open');
	     	
	 	});
})
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function fn_egov_regist(form){
  	 if($("#stock_code").val() == ""){
  		alert("종목은 필수입니다.");
		return;
  	 }else if($("#qy").val() == ""){
  		alert("수량은 필수입니다.");
		return;
  	 }else if($("#unitPrice").val() == ""){
  		alert("단가는 필수입니다.");
		return;
  	// }else if($("#delngDe").val() == ""){
  	//	alert("거래일자는 필수입니다.");
	//	return;
  	 }else{
  		if(confirm("<spring:message code="common.regist.msg" />")){	
  			form.action="/stock/info/saveMyStock.do";	
			form.submit();	
		}
  	 }
}

/* ********************************************************
* 서버 처리 후 메세지 화면에 보여주기
******************************************************** */
function fncShowMessg(){
	if("<c:out value='${message}'/>" != ''){
	alert("<c:out value='${message}'/>");
	}
}
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/stock/info/selectMyStockList.do'/>";
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
<body onLoad="fncShowMessg();">

<form name="egovFrm" method="post" onSubmit="fn_egov_regist(document.forms[0]); return false;"> 
<input type="hidden" name="mode" value="insert">
<div class="wTableFrm">
	<!-- 타이틀 -->
	<h2>${pageTitle} <spring:message code="title.create" /></h2>

	<!-- 등록폼 -->
	<table class="wTable" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle } <spring:message code="title.create" /></caption>
	<colgroup>
		<col style="width: 10%;">
		<col>
	</colgroup>
	<tbody>
		<!-- 입력 -->
		<tr>
			<th><label for="code">종목코드 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="text" name="code"	id="stock_code"/>
   				<input type="text" name="codeNm" id="searchKeyword" size="30" maxlength="100" style="width: auto;" readonly="readonly"/>
   				<a id="popupStocks" href="#none" target="_blank" title="종목 검색" style="selector-dummy:expression(this.hideFocus=false);">
						<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(종목검색)
					</a>
			</td>
		</tr>
		
		<tr>
			<th><label for="qy">수량 <span class="pilsu">*</span></label></th>
			<td class="left">
   				<input type="number" name="qy"	id="qy" class="cssright" size="10" maxlength="10" style="text-align: right;"/>
			</td>
		</tr>
		
		<tr>
			<th>구분 <span class="pilsu">*</span></th>
			<td class="left">
				<select name="gubun" id="gubun" title="구분" cssClass="txt">
					<option value="BUY">매수</option> 
					<option value="SELL">매도</option> 
				</select>
			</td>
		</tr>
		
		<tr>
			<th><label for="fee">수수료</label></th>
			<td class="left">
   				<input type="number" name="fee"	id="fee" size="10" maxlength="10" class="cssright"/>원
			</td>
		</tr>
		
		<tr>
			<th><label for="trftax">거래세/농특세</label></th>
			<td class="left">			
   				<input type="number" name="trftax"	id="trftax" size="10" maxlength="10" class="cssright"/>원
			</td>
		</tr>
		<tr>
			<th><label for="incmtax">소득세/주민세</label></th>
			<td class="left">			
   				<input type="number" name="incmtax"	id="incmtax" size="10" maxlength="10" class="cssright"/>원
			</td>
		</tr>
		<tr>
			<th><label for="unitPrice">단가 <span class="pilsu">*</span></label></th>
			<td class="left">			
   				<input type="number" name="unitPrice"	id="unitPrice" size="10" maxlength="10" class="cssright"/>원
			</td>
		</tr>
		<tr>
			<th><label for="delngDe">거래일자</label></th>
			<td class="left">
   				<input type="text" name="delngDe"	id="delngDe" size="10" maxlength="15" readonly="readonly" style="text-align:cneter; width: auto; margin-right:5px;"/>
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
		<input type="submit" class="s_submit" value="<spring:message code="button.create" />" title="<spring:message code="button.create" /> <spring:message code="input.button" />" />
		<span class="btn_s"><a href="#none" onClick="fn_egov_list();" title="<spring:message code="button.list" />  <spring:message code="input.button" />"><spring:message code="button.list" /></a></span>
	</div><div style="clear:both;"></div>
	
</div>

</form>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
