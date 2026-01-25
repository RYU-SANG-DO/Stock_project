<%
 /**
  * @Class Name : EgovCcmCmmnCodeList.jsp
  * @Description : 공통코드 목록 화면
  * @Modification Information
  * @
  * @  수정일             수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.02.01   박정규              최초 생성
  *   2017.08.08   이정은              표준프레임워크 v3.7 개선
  *  @author 공통서비스팀
  *  @since 2009.02.01
  *  @version 1.0
  *  @see
  *
  */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="pageTitle"><spring:message code="comSymCcmCca.cmmnCodeVO.title"/></c:set>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.list" /></title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
<script type="text/javascript">
/*********************************************************
 * 초기화
 ******************************************************** */
function fn_egov_init(){
	// 첫 입력란에 포커스..
	document.stockForm.searchCondition.focus();
}

/*********************************************************
 * 페이징 처리 함수
 ******************************************************** */
function fn_linkPage(pageNo){
	document.stockForm.pageIndex.value = pageNo;
	document.stockForm.action = "<c:url value='/stock/com/selectComThemeCodeList.do'/>";
   	document.stockForm.submit();
}
/*********************************************************
 * 조회 처리 함수
 ******************************************************** */
function fn_search(){
	document.stockForm.pageIndex.value = 1;
	document.stockForm.submit();
}
/* ********************************************************
 * 상세회면 처리 함수
 ******************************************************** */
function fn_codedetail(codeId) {
	// 사이트 키값(siteId) 셋팅.
	document.stockForm.codeId.value = codeId;
  	document.stockForm.action = "<c:url value='/stock/com/selectComThemeCodeDetail.do'/>";
  	document.stockForm.submit();
}
</script>
</head>
<body onload="fn_egov_init()">

<form name="stockForm" action="<c:url value='/stock/com/selectComThemeCodeList.do'/>" method="post" onSubmit="fn_search(); return false;"> 
<div class="board">
	<h1>${pageTitle} <spring:message code="title.list" /></h1>
	
	<!-- 검색영역 -->
			<!-- 검색조건선택 -->
	<div class="search_box" title="이 레이아웃은 하단 정보를 대한 검색 정보로 구성되어 있습니다.">
		<ul>
			<li>
				<select name="searchCondition" title="검색조건">
					<%-- <option <c:if test="${searchVO.searchCondition == ''}">selected="selected"</c:if>><spring:message code="input.select" /></option><!-- 선택하세요 --> --%>
					<option selected value=''>선택하세요</option><!-- 선택하세요 -->
					<option value="1"  <c:if test="${searchVO.searchCondition == '1'}">selected="selected"</c:if> >Code ID</option><!-- 코드ID -->
					<option value="2"  <c:if test="${searchVO.searchCondition == '2'}">selected="selected"</c:if> >Code ID Name</option><!-- 코드ID명 -->
				</select>
			</li>
			<!-- 검색키워드 및 조회버튼 -->
			<li>
				<input class="s_input" name="searchKeyword" type="text"  size="35" title="검색 입력" value='<c:out value="${searchVO.searchKeyword}"/>'  maxlength="155" >
				<input type="submit" class="s_btn" value="조회" title="조회 버튼" />
				<span class="btn_b"><a href="<c:url value='/sym/ccm/cca/RegistCcmCmmnCodeView.do' />"  title="등록 버튼">등록</a></span>
			</li>
		</ul>
	</div>
	
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle}<spring:message code="title.list" /></caption>
	<colgroup>
		<col style="width: 9%;">
		<col style="width: 40%;">
		<col style="width: 13%;">
		<col style="width: 40%;">
		<col style="width: 13%;">
	</colgroup>
	<thead>
	<tr>
		<th>번호</th><!-- 번호 -->
		<th>분류코드</th><!-- 분류코드명 -->
		<th class="board_th_link">코드ID</th><!-- 코드ID -->
		<th>코드명</th><!-- 코드ID -->
		<th>사용여부</th><!-- 사용여부 -->
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(resultList) == 0}">
	<tr>
		<td colspan="5"><spring:message code="common.nodata.msg" /></td>
	</tr>
	</c:if>
	<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
	<tr>
		<td><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></td>
		<td><c:out value='${resultInfo.themeName}'/></td>
		<td><a href="#none" onClick="fn_codedetail('<c:out value="${resultInfo.themeId}"/>');return false;"><c:out value='${fn:substring(resultInfo.themeId, 0, 40)}'/></a></td>
		<td><a href="#none" onClick="fn_codedetail('<c:out value="${resultInfo.themeId}"/>');return false;"><c:out value='${fn:substring(resultInfo.themeName, 0, 40)}'/></a></td>
		<td><c:out value='${resultInfo.useAt}'/></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	
	<!-- paging navigation -->
	<div class="pagination">
		<ul>
		<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fn_linkPage"/>
		</ul>
	</div>
	 
</div>

<input name="codeId" type="hidden" value="">
<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>">
</form>

</body>
</html>