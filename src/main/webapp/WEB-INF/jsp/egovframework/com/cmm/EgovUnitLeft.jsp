<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>eGovFrame 공통 컴포넌트</title>
<link href="<c:url value='/css/egovframework/com/cmm/main.css' />" rel="stylesheet" type="text/css">
<style type="text/css">
link { color: #666666; text-decoration: none; }
link:hover { color: #000000; text-decoration: none; }
</style>
</head>
<body>
<div id="lnb">
<c:set var="isMai" value="false"/>
<c:set var="isUat" value="false"/>
<c:set var="isSec" value="false"/>
<c:set var="isSts" value="false"/>
<c:set var="isCop" value="false"/>
<c:set var="isUss" value="false"/>
<c:set var="isSym" value="false"/>
<c:set var="isDam" value="false"/>
<c:set var="isCom" value="false"/>
<c:set var="isExt" value="false"/>
<c:set var="isStock" value="false"/>
<ul class="lnb_title">
	<c:forEach var="result" items="${resultList}" varStatus="status">
	
		<c:if test="${isMai == 'false' && result.gid == '0'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.mai.title"/></strong></strong><!-- 포털(예제) 메인화면 -->
			</li>
			<c:set var="isMai" value="true"/>
		</c:if>
		<c:if test="${isUat == 'false' && result.gid == '10'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.uat.title"/></strong></strong><!-- 사용자디렉토리/통합인증 -->
			</li>
			<c:set var="isUat" value="true"/>			
		</c:if>
		
		<c:if test="${isSec == 'false' && result.gid == '20'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.sec.title"/></strong></strong><!-- 보안 -->
			</li>
			<c:set var="isSec" value="true"/>
		</c:if>
		<c:if test="${isCop == 'false' && result.gid == '40'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.cop.title"/></strong></strong><!-- 협업 -->
			</li>
			<c:set var="isCop" value="true"/>
		</c:if>
		<c:if test="${isUss == 'false' && result.gid == '50'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.uss.title"/></strong></strong><!-- 사용자지원 -->
			</li>
			<c:set var="isUss" value="true"/>
		</c:if>
		<c:if test="${isSym == 'false' && result.gid == '60'}">
			<li>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="comCmm.sym.title"/></strong></strong><!-- 시스템관리 -->
			</li>
			<c:set var="isSym" value="true"/>
		</c:if>
		<c:if test="${isStock == 'false' && result.gid == '200'}">
			<li>
			<c:set var="mess_code" value="comCmm.${result.keyL1}.title"/>
				<strong class="left_title_strong"><strong class="top_title_strong"><spring:message code="${mess_code}"/></strong></strong><!-- 주식 정보 컴포넌트 -->
			</li>
			<c:set var="isStock" value="true"/>
		</c:if>
		
	
		<%-- <c:set var="componentMsgKey">comCmm.left.${result.order}</c:set> --%>
		<c:set var="margin_left" value="${result.lv * 20}"/>
		<c:choose>
			<c:when test="${result.gid eq '200'}">
				<ul class="2depth" style="margin-left: ${margin_left}px;">
					<li><a href="${pageContext.request.contextPath}<c:out value="${result.listUrl}"/>" target="_content" class="link" title="${result}"> <c:out value="${result.order}"/>. <c:out value="${result.name}"/></a></li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="2depth" style="margin-left: ${margin_left}px;">
					<li><a href="${pageContext.request.contextPath}<c:out value="${result.listUrl}"/>" target="_content" class="link" title="${result}"> <c:out value="${result.order}"/>. <c:out value="${result.name}"/></a></li>
				</ul>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>

</body>
</html>
