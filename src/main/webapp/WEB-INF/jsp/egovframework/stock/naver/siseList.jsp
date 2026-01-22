<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : siseList List 화면
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
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javaScript" defer="defer">
function fncSelectSiseList(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/naver/selectSiseList.do'/>";
    document.listForm.submit();
}

function linkPage(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/naver/selectSiseList.do'/>";
    document.listForm.submit();
}


function press() {
	Loading();
    if (event.keyCode==13) {
    	fncSelectSiseList('1');
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
		url : "<c:url value='/stock/naver/selectThemeListExclDownAjax.do'/>",
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

</script>
</head>
<body>
<!-- javascript warning tag  -->
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" action="${pageContext.request.contextPath}/stock/naver/selectThemeList.do" method="post">
	<input type="hidden" name="downGubun" id="downGubun"/>
	<input type="hidden" name="pageIndex" id="pageIndex"/>
<div class="board">
	<h1>${pageTitle} <spring:message code="title.list" /></h1>
	<!-- 검색영역 -->
	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />">
		<ul>
			<li>
				<select name="gubun" class="gubun" title="구분">
					<option value="0" <c:if test="${'0' eq gubun}">selected="selected"</c:if>>전체</option>
					<option value="1" <c:if test="${'1' eq gubun}">selected="selected"</c:if>>목표주가</option>
				</select>
			</li>
			<li>
			<select name="type" class="type" title="코스피 코스닥 구분">
				<option value="rise" <c:if test="${'rise' ne type}">selected="selected"</c:if>>상승</option>
				<option value="steady" <c:if test="${'steady' eq type}">selected="selected"</c:if>>보합</option>
				<option value="fall" <c:if test="${'fall' eq type}">selected="selected"</c:if>>하락</option>
			</select>
			</li>
			<li>
			<select name="sosok" class="sosok" title="코스피 코스닥 구분">
				<option value="0" <c:if test="${'0' ne sosok}">selected="selected"</c:if>>코스피</option>
				<option value="1" <c:if test="${'1' eq sosok}">selected="selected"</c:if>>코스닥</option>
			</select>
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>명 : </div></li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${param.searchKeyword}"/>'  maxlength="155" >
				<input type="button" class="s_btn"  onClick="fncSelectSiseList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	
	<div class="button_box">
		<ul>
			<li style="float:left;font-size: 14px;">
				<span>검색 : </span><span></span>
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
		<c:forEach var="subject" items="${titleList}" varStatus="substatus">
			<th>${subject.title}</th>
		</c:forEach>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(siseList) == 0}">
	<tr>
		<td colspan="${fn:length(titleList)}"><spring:message code="common.nodata.msg" /></td>
	</tr>
	</c:if>
	<c:forEach var="item" items="${siseList}" varStatus="status">
	<tr>
		<c:forEach var="subject" items="${item.detailList}" varStatus="substatus">
			<td <c:if test="${not empty subject.styleColor}">style="color:${subject.styleColor}"</c:if>>
				<c:choose>
					<c:when test="${not empty subject.ahref}">
							<a href="https://finance.naver.com/${subject.ahref}" target="_blank" <c:if test="${not empty subject.styleColor}">style="color:${subject.styleColor}"</c:if>>
								<c:out value="${subject.dcn}"/>
							</a>
					</c:when>
					<c:when test="${subject.idx eq '0'}">
						<c:out value="${status.count}"/>
					</c:when>
					<c:otherwise>
						<c:out value="${subject.dcn}"/>
						<c:if test="${not empty subject.dcn}">
							<br/>(<c:out value="${subject.percent}"/>%)
							<c:if test="${not empty subject.opinionText}">(<c:out value="${subject.opinionText}"/>)</c:if>
						</c:if>
					</c:otherwise>
				</c:choose>
			</td>
		</c:forEach>
	
	</tr>
	</c:forEach>
	</tbody>
	</table>

</div><!-- end div board -->
</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
