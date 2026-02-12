<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : upjongList List 화면
  * @Modification Information
  * @  수정일			수정내용
  * @ -----------		---------------------------
  * @ 2025.02.03		최초 생성
  */
%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javaScript" defer="defer">
function fncSelectUpjongList(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/naver/selectUpjongList.do'/>";
    document.listForm.submit();
}

function linkPage(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/naver/selectUpjongList.do'/>";
    document.listForm.submit();
}


function press() {
	Loading();
    if (event.keyCode==13) {
    	fncSelectThemeList('1');
    }
}

//내역 엑셀 다운로드
function naverExcelDown(gubun){
	if("D" == gubun){		
		if(!confirm("엑셀 다운로드(상세포함)는 다운로드 시간이 오래 걸립니다. 그래도 다운로드 하시겠습니까?")){	return;}
	}

	$("#downGubun").val(gubun);
	$.ajax({
		type : "POST",
		url : "<c:url value='/stock/naver/selectUpjongListExclDownAjax.do'/>",
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

//테마 상세 정보
function fncSelectDetailList(upjongNo , upjongNm){
	Loading();
	$("#no").val(upjongNo);
	$("#upjongNm").val(upjongNm);
    document.listForm.action = "<c:url value='/stock/naver/selectUpjongDetailList.do'/>";
    document.listForm.submit();
}
</script>
</head>
<body>
<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" action="${pageContext.request.contextPath}/stock/naver/selectUpjongList.do" method="post">
	<input type="hidden" name="downGubun" id="downGubun"/>
	<input type="hidden" name="type" id="type" value="upjong"/>
	<input type="hidden" name="no" id="no" value=""/>
	<input type="hidden" name="upjongNm" id="upjongNm" value=""/>
<div class="board">
	<h1>${pageTitle} <spring:message code="title.list" /></h1>
	<!-- 검색영역 -->
	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />">
		<ul>
			<li>
			<select name="listType" class="select" title="검색조건구분"><!-- 검색조건구분 -->
				<option value="" <c:if test="${empty listType}">selected="selected"</c:if>>페이지보기</option>
				<option value="A" <c:if test="${'A' eq listType}">selected="selected"</c:if>>전체 보기</option>
			</select>
			</li>
			<li>
			<select name="gubun" class="select" title="테마,종목 구분"><!-- 테마,종목 구분 -->
				<option value="" <c:if test="${'S' ne gubun}">selected="selected"</c:if>>업종</option>
				<option value="S" <c:if test="${'S' eq gubun}">selected="selected"</c:if>>종목</option>
			</select>
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>명 : </div></li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${param.searchKeyword}"/>'  maxlength="155" >
				<input type="button" class="s_btn"  onClick="fncSelectUpjongList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	
	<div class="button_box">
		<ul style="margin-bottom: 0px;">
			<li style="float:left;">
				페이지 사이즈:
				<select name="pageUnit" class="select" title="페이지">
					<option value="10" <c:if test="${'10' eq naverThemeVO.pageUnit}">selected="selected"</c:if>>10</option>
					<option value="30" <c:if test="${'30' eq naverThemeVO.pageUnit}">selected="selected"</c:if>>30</option>
					<option value="60" <c:if test="${'60' eq naverThemeVO.pageUnit}">selected="selected"</c:if>>60</option>
					<option value="100" <c:if test="${'100' eq naverThemeVO.pageUnit}">selected="selected"</c:if>>100</option>
				</select>
			</li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="naverExcelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
				<input type="button" class="s_btn" onClick="naverExcelDown('D')" value="<spring:message code="stock.com.excelDown.title" />(상세포함)" title="<spring:message code="stock.com.excelDown.title" />(상세포함) <spring:message code="input.button" />" />
			</li>
		</ul>
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
	<tr class="algin-center">
		<th>순번</th><!-- 0 -->
		<th>업종명</th><!-- 1 -->
		<th>전일대비</th><!-- 3 -->
		<th>전체</th>
		<th>상승</th>
		<th>보합</th>
		<th>하락</th>
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
		<td><a href="#LINK" onclick="javascript:fncSelectDetailList('<c:out value="${item.parameter2}"/>','<c:out value="${item.parameter1}"/>')"><c:out value="${item.parameter1}"/></a></td>
		<td style="color: ${item.parameter8}"><c:out value="${item.parameter3}"/></td>
		<td><c:out value="${item.parameter4}"/></td>
		<td><c:out value="${item.parameter5}"/></td>
		<td><c:out value="${item.parameter6}"/></td>
		<td><c:out value="${item.parameter7}"/></td>
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
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
