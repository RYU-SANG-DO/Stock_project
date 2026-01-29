<%
 /**
  * @Class Name : StockDartController.java
  * @Description : dartMain List 화면
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
<script type="text/javaScript">
$(function(){
	$("#sel_"+"${apiGrpCd}").css({"color":"#fff","background":"#4688d2"});
});
//
function menuMove(p_apiGrpCd){
	//location.href="<c:url value='/stock/naver/"+menuKey+".do'/>?type="+param;
	$("#apiGrpCd").val(p_apiGrpCd);
    document.listForm.action = "<c:url value='/stock/dart/selectDartList.do'/>";
    document.listForm.submit();
}

function selectDetailData(p_apiGrpCd, p_apiId){
	$("#apiGrpCd").val(p_apiGrpCd);
	$("#apiId").val(p_apiId);
    document.listForm.action = "<c:url value='/stock/dart/selectDetailDartList.do'/>";
    document.listForm.submit();
}
</script>
</head>
<style>
.mainbtn{margin-bottom: 0px;}
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
	<input type="hidden" name="apiGrpCd" id="apiGrpCd" value="${apiGrpCd}"/>
	<input type="hidden" name="apiId" id="apiId"/>
</form>
<div class="board">
	<h1>DART 메인</h1>
	<!-- 검색영역 -->
	<div style="padding: 10px; border: 1px solid #b1bbcb;">
	<table summary="원하시는 항목을 선택하여 결과를 보실 수 있습니다." class="item_list">
		<caption></caption>
			<colgroup>
				<col>
				<col>
				<col>
				<col>
				<col>
				<col>
			</colgroup>
			<tbody>
			<tr>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS001" onClick="menuMove('DS001')" value="공시정보"/></li></ul></div></td>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS002" onClick="menuMove('DS002')" value="정기보고서 주요정보"/></li></ul></div></td>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS003" onClick="menuMove('DS003')" value="정기보고서 재부정보"/></li></ul></div></td>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS004" onClick="menuMove('DS004')" value="지분공시 종합정보"/></li></ul></div></td>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS005" onClick="menuMove('DS005')" value="주요사항보고서 주요정보"/></li></ul></div></td>
				<td class="tab"><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" id="sel_DS006" onClick="menuMove('DS006')" value="증권신고서 주요정보"/></li></ul></div></td>
			</tr>	
		</tbody>
		</table>
		</div>
	
	
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="DART 메인" />">
	<caption>DART 메인 <spring:message code="title.list" /></caption>
	<colgroup>
		<col style="width: 5%;">
		<col style="width: 25%;">
		<col>
		<col style="width: 5%;">
	</colgroup>
	<thead>
	<c:if test="${fn:length(list) == 0}">
	<tr>
		<td colspan="4"><spring:message code="common.nodata.msg" /></td>
	</tr>
	</c:if>
	<tr>
		<th>번호</th>
		<th>API명</th>
		<th>상세기능</th>
		<th>바로가기</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:forEach var="item" items="${list}" varStatus="status">
		<tr class="${item.apiGrpCd}_${item.apiId}">
			<td>${item.apiNum}</td>
			<td style="text-align: left;"><a href="#LINK" onclick="selectDetailData('${item.apiGrpCd}','${item.apiId}');">${item.apiNm}</a></td>
			<td style="text-align: left;">${item.detail}</td>
			<td><a href="#LINK" onclick="selectDetailData('${item.apiGrpCd}','${item.apiId}');">바로가기</a>
			</td>
			<%-- <td>
				<c:if test="${item.xmlYn eq 'Y'}">
					<a href="#LINK">조회</a>
				</c:if>
			</td>
			<td>
				<c:if test="${item.zipYn eq 'Y'}">
					<a href="#LINK">조회</a>
				</c:if>
			</td> --%>
		</tr>
	</c:forEach>
	</tbody>
	</table>
</div><!-- end div board -->
</body>
</html>
