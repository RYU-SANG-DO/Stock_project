<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : stocksList.jsp
  * @Description : 주식 검색 화면
  *
  */
  /* Image Path 설정 */
  String imagePath_icon   = "/images/egovframework/com/sym/prm/icon/";
  String imagePath_button = "/images/egovframework/com/sym/prm/button/";
%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="comSymPrm.fileNmSearch.title"/></title><!-- 프로그램파일명 검색 -->
<link href="<c:url value="/css/egovframework/com/com.css"/>" rel="stylesheet" type="text/css">
<link href="<c:url value="/css/egovframework/com/button.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-1.4.2.min.js' />" ></script>
<script type="text/javaScript">
<!--
/* ********************************************************
 * 페이징 처리 함수
 ******************************************************** */
function linkPage(pageNo){
	document.stockFrm.pageIndex.value = pageNo;
	document.stockFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
   	document.stockFrm.submit();
}

/* ********************************************************
 * 조회 처리 함수
 ******************************************************** */
function selectStocksListSearch() {	
	document.stockFrm.pageIndex.value = 1;
	document.stockFrm.action = "<c:url value='/stock/data/selectStocksList.do'/>";
	document.stockFrm.submit();
}

/* ********************************************************
 * 프로그램목록 선택 처리 함수
 ******************************************************** */
function choisStocksListSearch(vCode,vName) {
	var parentFrom = parent.document.getElementsByTagName('form');
	parent.$("#stock_code").val(vCode);	
	parent.$("#searchKeyword").val(vName);	
	//parentFrom[0].stock_code.value = vCode;
	//parentFrom[0].searchKeyword.value = vName;	
    parent.$('.ui-dialog-content').dialog('close');
}
-->
</script>
</head>
<body>
<form name="stockFrm" action ="<c:url value='/stock/data/selectStocksList.do'/>" method="post">
<input name="pageIndex" type="hidden" value="<c:out value='${stocksDataVO.pageIndex}'/>"/>
<div class="board" style="width:100%;">
	<h1>종목 검색</h1><!-- 프로그램파일명 검색 -->

	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />"><!-- 이 레이아웃은 하단 정보를 대한 검색 정보로 구성되어 있습니다. -->
		<ul>
			<li>
				<label for="gubun">구분 : </label><!-- 프로그램명 -->
				<select name="gubun" id="gubun" class="select" title="구분">
					<option value="" <c:if test="${empty gubun}">selected="selected"</c:if>>전체</option>
					<option value="KOSPI" <c:if test="${'KOSPI' eq gubun}">selected="selected"</c:if>>유가</option>
					<option value="KOSDAQ" <c:if test="${'KOSDAQ' eq gubun}">selected="selected"</c:if>>코스닥</option>
				</select>
			</li>
			<li>
				<label for="">종목명 : </label><!-- 프로그램명 -->
				<input class="s_input2 vat" name="searchKeyword" type="text"  value='<c:out value="${stocksDataVO.searchKeyword}"/>'  size="30" maxlength="60" title="<spring:message code="title.searchCondition"/>" /><!-- 검색조건 -->
				
				<input class="s_btn" type="submit" value='<spring:message code="button.inquire" />' title="<spring:message code="title.inquire"/>" onclick="selectStocksListSearch(); return false;" /><!-- 조회 -->
			</li>
		</ul>
	</div>

	<table class="board_list">
		<caption></caption>
		<colgroup>
			<col style="width:3%"/>
			<col style="width:5%"/>
			<col style="width:10%" />
			<col/>
			<col/>
		</colgroup>
		<thead>
			<tr>
			   <th scope="col">순번</th>
			   <th scope="col">종목코드</th>
			   <th scope="col">종목명</th>
			   <th scope="col">업종</th>
			   <th scope="col">주요제품</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="result" items="${resultList}" varStatus="status">
			  <tr>
			  	<td>
			    	<c:out value="${result.rn}"/>
			    </td>
			    <td>
			      <span class="link">
			      	<a href="#LINK" onclick="choisStocksListSearch('<c:out value="${result.stocksCode}"/>','<c:out value="${result.stocksName}"/>'); return false;"><c:out value="${result.stocksCode}"/></a>
			      </span>
			    </td>
			    <td>
			    	<span class="link">
			      	<a href="#LINK" onclick="choisStocksListSearch('<c:out value="${result.stocksCode}"/>','<c:out value="${result.stocksName}"/>'); return false;"><c:out value="${result.stocksName}"/></a>
			      </span>
			    </td>
			     <td>
			    	<c:out value="${result.upjong}"/>
			    </td>
			    <td style="text-align: left;">
			    	<c:out value="${result.mainPrduct}"/>
			    </td>
			  </tr>
			 </c:forEach>
		</tbody>
	</table>

	<!-- paging navigation -->
	<div class="pagination">
		<ul>
			<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/>
		</ul>
	</div>
</div>

</form>
</body>
</html>

