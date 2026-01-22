<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%-- <c:set var="pageTitle"/>
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>
<c:if test="${not empty stockSite}">
	<c:set var="pageTitle">
		<spring:message code="stock.${stockSite}.title"/> <spring:message code="stock.${stockSite}.theme.title"/> 
	</c:set>
</c:if> --%>
<!DOCTYPE html>
<html>
<head>
<title>${pageTitle} <spring:message code="title.list" /></title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-3.7.1.min.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-ui-1.14.0.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/js/bootstrap.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/js/bootstrap.bundle.min.js'/>" ></script>

<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/css/bootstrap.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/jquery-ui-1.14.0.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">

<script type="text/javascript" src="<c:url value='/js/egovframework/com/util.js'/>" ></script>
