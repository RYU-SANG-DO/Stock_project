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
/* ********************************************************
 * 수정처리화면
 ******************************************************** */
function fn_egov_updt(){
	let form = document.egovFrm;
	form.mode.value = "update";	
	$('#dayPrice').val($('#dayPrice').val().replace(/,/g, ""));
		$('#targetPrice').val($('#targetPrice').val().replace(/,/g, ""));
		if(confirm("<spring:message code="common.update.msg" />")){	
			form.action="/stock/naver/saveNaverMyResearch.do";	
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
		document.egovFrm.action = "<c:url value='/stock/naver/saveNaverMyResearch.do'/>";
		document.egovFrm.submit();
	}	
}	
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list() {
	document.egovFrm.action = "<c:url value='/stock/naver/selectNaverMyResearchList.do'/>";
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
<input type="hidden" name="rpId" value="${infoMap.rpId}">
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
			<th><label for="relStockCode">종목</label></th>
			<td class="left">
   				<%-- <input type="hidden" name="relStockCode" id="relStockCode" value="<c:out value="${infoMap.relStockCode}"/>"/> --%>
   				<c:out value="${infoMap.stocksName}"/>
			</td>
		</tr>
		<tr>
			<th><label for="rpTitle">리포트 제목</label></th>
			<td class="left">
   				<%-- <input type="text" name="rpTitle" id="rpTitle" size="10" maxlength="10" value="<c:out value="${infoMap.rpTitle}"/>"/> --%>
   				<c:out value="${infoMap.rpTitle}"/>
			</td>
		</tr>
		<tr>
			<th><label for="rpDate">리포트 발행일</label></th>
			<td class="left">
   				<%-- <input type="text" name="rpDate" id="rpDate" size="10" maxlength="10" value="<c:out value="${infoMap.rpDate}"/>"/> --%>
   				<c:out value="${infoMap.rpDate}"/>
			</td>
		</tr>
		
		<tr>
			<th>투자의견 <span class="stockRecommendation">*</span></th>
			<td class="left">
				<select name="stockRecommendation" id="stockRecommendation" title="투자의견">
				<c:forEach var="item" items="${clCodeList}" varStatus="status">
					<option value="${item.code}" <c:if test="${infoMap.stockRecommendation eq item.code}">selected="selected"</c:if>><c:out value="${item.codeNm}"/></option> 
				</c:forEach>
					<%-- <option value="BUY" <c:if test="${infoMap.stockRecommendation eq 'BUY'}">selected="selected"</c:if>>매수</option> 
					<option value="SELL" <c:if test="${infoMap.stockRecommendation eq 'SELL'}">selected="selected"</c:if>>매도</option>
					<option value="NEUTRAL" <c:if test="${infoMap.stockRecommendation eq 'NEUTRAL'}">selected="selected"</c:if>>중립</option>
					<option value="HOLD" <c:if test="${infoMap.stockRecommendation eq 'HOLD'}">selected="selected"</c:if>>보유</option> --%>
				</select>
			</td>
		</tr>
		
		<tr>
			<th><label for="dayPrice">현재가</label></th>
			<td class="left">
   				<input type="text" name="dayPrice"	id="dayPrice" size="10" maxlength="10" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.dayPrice}" pattern="#,###" />"/>원
			</td>
		</tr>
		
		<tr>
			<th><label for="targetPrice">목표가</label></th>
			<td class="left">			
   				<input type="text" name="targetPrice"	id="targetPrice" size="10" maxlength="10" class="cssright" style="width:auto;" value="<fmt:formatNumber value="${infoMap.targetPrice}" pattern="#,###" />"/>원
			</td>
		</tr>
		<tr>
			<th><label for="rm">요약</label></th>
			<td class="nopd">
				<textarea name="userSummary" title="요약" cols="300" rows="10" style="height:auto; text-align: left;"><c:out value="${infoMap.userSummary}"/></textarea>
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

</body>
</html>