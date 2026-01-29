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
<c:set var="pageTitle"/>
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>
<c:if test="${not empty stockSite}">
	<c:set var="pageTitle">
		<spring:message code="stock.${stockSite}.title"/> 메인
	</c:set>
</c:if>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle}</title><!-- 테마 목록 -->
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
<script type="text/javaScript" defer="defer">
//메뉴이동
function menuMove(menuKey, param){
		location.href="<c:url value='/stock/naver/select"+menuKey+"List.do.do'/>?type="+param;
    //document.listForm.action = "<c:url value='/stock/naver/selectThemeList.do'/>";
    //document.listForm.submit();
}
</script>
</head>
<style>
.mainbtn ul{text-align: center;}
.mainbtn ul li{border: 1px solid #d2d2d2;}
.mainbtn ul li input.s_btn{
	color: #232121; 
	background:#f1f1e8;
}
</style>
<body>
<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" method="post">
<div class="board">
	<h1>${pageTitle}</h1>
	<!-- 검색영역 -->
	<div style="padding: 10px; border: 1px solid #b1bbcb; margin-bottom: 7px;">
	<table summary="원하시는 항목을 선택하여 결과를 보실 수 있습니다." class="item_list">
		<caption></caption>
			<colgroup>
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
			</colgroup>
			<tbody>
			<tr>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Theme','theme')" value="테마"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Upjong','upjong')" value="업종"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Sise','rise')" value="상승"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Sise','steady')" value="보함"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Sise','fall')" value="하락"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('Dividend', 'dividend_list')" value="배당"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_trans_style','')" value="투자자별 매매 동향"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_deal_rank','')" value="외국인 순매매 상위"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_deal_rank','')" value="기관 순매매 상위"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_program','')" value="프로그램매매 동향"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_deposit','')" value="증시자금동향"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('sise_foreign_hold','')" value="외국인보유현황"/></li></ul></div></td>	
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('item_gold','')" value="콜든크로스"/></li></ul></div></td>
			</tr>	
		</tbody>
		</table>
		</div>
	
	
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<col style="width: 5%;">
		<col>
		<col>
		<col>
	</colgroup>
	<thead>
	<tr>
		<th>순번</th><!-- 0 -->
		<th>테마명</th><!-- 1 -->
		<th>전일대비</th><!-- 3 -->
		<th>종목명1</th><!-- 4 -->
		<th>종목명2</th><!-- 6 -->
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(themeList) == 0}">
	<tr>
		<td colspan="5"><spring:message code="common.nodata.msg" /></td>
	</tr>
	</c:if>
	<c:forEach var="item" items="${themeList}" varStatus="status">
	<tr>
		<td><c:out value="${item.parameter0}"/></td>
		<td><a href="#LINK" onclick="javascript:fncSelectThemeDetailList('<c:out value="${item.parameter2}"/>','<c:out value="${item.parameter1}"/>')"><c:out value="${item.parameter1}"/></a></td>
		<td style="color: ${item.parameter8}"><c:out value="${item.parameter3}"/></td>
		<td><a href="#LINK" onclick="javascript:fncSelectStocksInfo('<c:out value="${item.parameter5}"/>')"><c:out value="${item.parameter4}"/></a></td>
		<td><a href="#LINK" onclick="javascript:fncSelectStocksInfo('<c:out value="${item.parameter7}"/>')"><c:out value="${item.parameter6}"/></a></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	<c:if test="${!empty naverThemeVO.pageIndex && empty listType}">
		<!-- paging navigation -->
		<div class="pagination">
			<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/></ul>
		</div>
	</c:if>

</div><!-- end div board -->
<input type="hidden" name="authorCode"/>
<input type="hidden" name="authorCodes"/>
<input type="hidden" name="pageIndex" value="<c:out value='${naverThemeVO.pageIndex}'/>"/>

</form>
</body>
</html>
