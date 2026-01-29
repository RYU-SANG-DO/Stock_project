<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : themeList List 화면
  * @Modification Information
  * @  수정일			수정내용
  * @ -----------		---------------------------
  * @ 2025.01.03		최초 생성
  */
%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javaScript" defer="defer">
$(function(){
	//$('[data-toggle="tooltip"]').tooltip();
	if('${fieldIds}' != 'null'){
		var arrVal = ${fieldIds};
		$("input[name='fieldIds']").prop('checked',false);
		$.each(arrVal, function(index, value) {
			
		    console.log("Index:", index, "Value:", value , $("input[value='"+value+"']").attr("id"));
		    $("input[value='"+value+"']").prop('checked',true);
		});
	}
	
})
//내역 엑셀 다운로드
function naverExcelDown(gubun){
	$("#downGubun").val(gubun);
	$.ajax({
		type : "POST",
		url : "<c:url value='/stock/naver/selectThemeDetailListExclDownAjax.do'/>",
		//data : { "knoTypeCd": $("#knoTypeCd").val() },
		data: $("#listForm").serialize(),
		dataType : 'json',
		success : function(returnData, status) {
			console.log(status, returnData);
			if(status == "success") {
				alert(returnData.fileResultName+"파일로 다운로드가 완료되었습니다.");
				return;
			/* 	if(returnData.checkCount > 0 ) {
					alert("입력하신 지식유형코드는 이미 사용중입니다.");
					return;
				} else {
					alert("입력하신 지식유형코드는 사용하실 수 있습니다.");
					return;
				} */
			} else{
				alert("ERROR!");
				return;
			} 
		}
	});
}

//테마 목록으로 이동
function themeList(){
    document.listForm.action = "<c:url value='/stock/naver/selectThemeList.do'/>";
    document.listForm.submit();
}

function fieldSubmit() {
	var chkcnt = 0;
	
	for(i = 0; i < document.listForm.fieldIds.length ; i++) {
    	if(document.listForm.fieldIds[i].checked == true) {
    		chkcnt++;
    	}
    }
    
    if(chkcnt > 6) {
    	alert('최대 6개까지만 가능합니다.');
    	return;
    }
    $("#field_type").val("");
	document.listForm.action = "<c:url value='/stock/naver/selectThemeDetailList.do'/>";
	document.listForm.submit();
}

function fieldDefault() {
	$("#field_type").val("D");
	$("input[name='fieldIds']").prop('checked',false);
	$(".choice>input[name='fieldIds']").prop('checked',true);
	document.listForm.action = "<c:url value='/stock/naver/selectThemeDetailList.do'/>";
	document.listForm.submit();

}
</script>
</head>
<body>
<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" id="listForm" action="${pageContext.request.contextPath}/stock/naver/selectThemeDetailList.do" method="post">
	<input type="hidden" name="downGubun" id="downGubun"/>
	<input type="hidden" name="type" id="type" value="theme"/>
	<input type="hidden" name="no" id="no" value="${no}"/>
	<input type="hidden" name="field_type" id="field_type" />
	<input type="hidden" name="pageIndex" id="pageIndex" value="${pageIndex}"/>
	<input type="hidden" name="gubun" value="${gubun}"/>
	<input type="hidden" name="searchKeyword" value="${searchKeyword}"/>
<div class="board">
	<h1><spring:message code="stock.${stockSite}.title"/> <spring:message code="stock.${stockSite}.theme.title"/> <spring:message code="title.detail" /> (${themeNm})</h1>
	<div style="padding: 10px; border: 1px solid #b1bbcb; background-color:#f5f5f2; margin-bottom: 7px;">
		${themeDetailInfo.pam0_text}
	</div>
	<!-- 검색영역 -->
	<div style="padding: 10px; border: 1px solid #b1bbcb; margin-bottom: 7px;">
	<table summary="원하시는 항목을 선택하여 결과를 보실 수 있습니다." class="item_list">
		<caption></caption>
		<colgroup><col width="15%"><col width="15%"><col width="15%"><col width="15%"><col width="15%"><col width="15%">		
			</colgroup>
			<tbody><tr>
				<td class="choice"><input type="checkbox" id="option1" name="fieldIds" value="quant" checked=""> <label for="option1">거래량</label></td>
				<td class="choice"><input type="checkbox" id="option2" name="fieldIds" value="ask_buy" checked=""> <label for="option2">매수호가</label></td>
				<td class="choice"><input type="checkbox" id="option3" name="fieldIds" value="amount" checked=""> <label for="option3">거래대금</label>(백만)</td>
				<td><input type="checkbox" id="option4" name="fieldIds" value="market_sum"> <label for="option4">시가총액</label>(억)</td>
				<td><input type="checkbox" id="option5" name="fieldIds" value="operating_profit"> <label for="option5">영업이익</label>(억)</td>
				<td><input type="checkbox" id="option6" name="fieldIds" value="per"> <label for="option6">PER</label>(배)</td>			
			</tr>		
			<tr>
				<td><input type="checkbox" id="option7" name="fieldIds" value="open_val"> <label for="option7">시가</label></td>
				<td class="choice"><input type="checkbox" id="option8" name="fieldIds" value="ask_sell" checked=""> <label for="option8">매도호가</label></td>
				<td class="choice"><input type="checkbox" id="option9" name="fieldIds" value="prev_quant" checked=""> <label for="option9">전일거래량</label></td>
				<td><input type="checkbox" id="option10" name="fieldIds" value="property_total"> <label for="option10">자산총계</label>(억)</td>
				<td><input type="checkbox" id="option11" name="fieldIds" value="operating_profit_increasing_rate"> <label for="option11">영업이익증가율</label></td>
				<td><input type="checkbox" id="option12" name="fieldIds" value="roe"> <label for="option12">ROE</label>(%)</td>			
			</tr>		
			<tr>	
				<td><input type="checkbox" id="option13" name="fieldIds" value="high_val"> <label for="option13">고가</label></td>
				<td><input type="checkbox" id="option14" name="fieldIds" value="buy_total"> <label for="option14">매수총잔량</label></td>
				<td><input type="checkbox" id="option15" name="fieldIds" value="frgn_rate"> <label for="option15">외국인비율</label></td>				
				<td><input type="checkbox" id="option16" name="fieldIds" value="debt_total"> <label for="option16">부채총계</label>(억)</td>	
				<td><input type="checkbox" id="option17" name="fieldIds" value="net_income"> <label for="option17">당기순이익</label>(억)</td>
				<td><input type="checkbox" id="option18" name="fieldIds" value="roa"> <label for="option18">ROA</label>(%)</td>		
			</tr>		
			<tr>	
				<td><input type="checkbox" id="option19" name="fieldIds" value="low_val"> <label for="option19">저가</label></td>				
				<td><input type="checkbox" id="option20" name="fieldIds" value="sell_total"> <label for="option20">매도총잔량</label></td>
				<td><input type="checkbox" id="option21" name="fieldIds" value="listed_stock_cnt"> <label for="option21">상장주식수</label>(천주)</td>
				<td><input type="checkbox" id="option22" name="fieldIds" value="sales"> <label for="option22">매출액</label>(억)</td>
				<td><input type="checkbox" id="option23" name="fieldIds" value="eps"> <label for="option23">주당순이익</label>(원)</td>
				<td><input type="checkbox" id="option24" name="fieldIds" value="pbr"> <label for="option24">PBR</label>(배)</td>			
			</tr>		
			<tr>		
			<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>				
				<td><input type="checkbox" id="option25" name="fieldIds" value="sales_increasing_rate"> <label for="option25">매출액증가율</label></td>
				<td><input type="checkbox" id="option26" name="fieldIds" value="dividend"> <label for="option26">보통주배당금</label>(원)</td>
				<td><input type="checkbox" id="option27" name="fieldIds" value="reserve_ratio"> <label for="option27">유보율</label>(%)</td>			
			</tr>	

			<tr>		
			<td colspan="6">
				<div class="button_box">
		<ul>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 1px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="fieldSubmit()" value="적용하기" title="적용하기 <spring:message code="input.button" />" style="color: #232121; background:#f1f1e8"/>
			</li>
			<li style="border: 1px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="fieldDefault()" value="초기 항목으로" title="초기 항목으로 <spring:message code="input.button" />" style="color: #232121; background:#f1f1e8"/>
			</li>
		</ul>
	</div>
			</td>				
			</tr>		
		</tbody>
		</table>
		</div>
		<div class="button_box">
		<ul>
			<li style="float:left;font-size: 14px;">
				<span>검색 : </span><span>${searchKeyword}</span>
			</li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="naverExcelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
				<input type="button" class="s_btn" onClick="themeList()" value="<spring:message code="stock.${stockSite}.theme.title"/><spring:message code="title.list" />" title="<spring:message code="stock.${stockSite}.theme.title"/><spring:message code="title.list" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<c:forEach var="item" items="${themeDetailTitleList}" varStatus="status">
			<c:choose>
				<c:when test="${status.count eq 2}"><col style="width: 10%;"></c:when>
				<c:otherwise><col></c:otherwise>
			</c:choose>
		</c:forEach>
		<col>
	</colgroup>
	<thead>
	<tr class="algin-center">
		<c:forEach var="item" items="${themeDetailTitleList}" varStatus="status">
			<th colspan="${item.colspan}"><c:out value="${item.title}"/></th>
		</c:forEach>
		<th>기업설명</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(themeDetailList) == 0}">
	<tr>
		<td colspan="${fn:length(themeDetailTitleList)+1}"><spring:message code="common.nodata.msg" /></td>
	</tr>
	</c:if>
	<c:forEach var="item" items="${themeDetailList}" varStatus="status">
		<tr>
		<c:forEach var="t_item" items="${item.parameters}" varStatus="t_status">
			<c:if test="${not empty t_item.text}">
			<td>
				<span data-toggle="tooltip" tabindex="0" title="${t_item.text}" style="cursor: pointer;<c:if test="${not empty t_item.color}">color:${t_item.color};</c:if>">
					<c:out value="${t_item.text}"/>
					<c:if test="${not empty t_item.dotNm}">
						<br/>(<c:out value="${t_item.dotNm}"/>)	
					</c:if>
				</span>
			</c:if>
		</c:forEach>
		</tr>
	</c:forEach>
	</tbody>
	</table>

</div><!-- end div board -->

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
