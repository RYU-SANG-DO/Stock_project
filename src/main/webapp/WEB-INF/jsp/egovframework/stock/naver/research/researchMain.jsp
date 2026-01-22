<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : researchMain List 화면
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

<!DOCTYPE html>
<html>
<head>
<title>NAVER 리서치</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery.js'/>" ></script>
<script type="text/javaScript" defer="defer">
//메뉴이동
function menuMove(param , name){
	document.listForm.searchGubun.value = param;
	document.listForm.searchGubunNm.value = name;
    document.listForm.action = "<c:url value='/stock/naver/selectNaverResearchList.do'/>";
    document.listForm.submit();
}
</script>
<style>
.mainbtn ul{text-align: center;}
.mainbtn ul li{border: 1px solid #d2d2d2;}
.mainbtn ul li input.s_btn{
	color: #232121; 
	background:#f1f1e8;
}
</style>
</head>
<body>
<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" method="post">
<input type="hidden" name="searchGubun"	id="searchGubun"/>
<input type="hidden" name="searchGubunNm"	id="searchGubunNm"/>
<div class="board">
	<h1>NAVER 리서치</h1>
	<!-- 검색영역 -->
	<div style="padding: 10px; border: 1px solid #b1bbcb; margin-bottom: 7px;">
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
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('market_info','시황정보')" value="시황정보"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('invest','투자정보')" value="투자정보"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('company','종목분서')" value="종목분석"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('industry','산업분석')" value="산업분석"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('economy','경제분석')" value="경제분석"/></li></ul></div></td>
				<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn" onClick="menuMove('debenture','채권분석')" value="채권분석"/></li></ul></div></td>
			</tr>	
		</tbody>
		</table>
		</div>
	
</div><!-- end div board -->
</form>
</body>
</html>
