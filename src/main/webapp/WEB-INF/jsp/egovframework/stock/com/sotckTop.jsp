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
<title>${pageTitle}</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-3.7.1.min.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/jquery-ui-1.14.0.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/js/bootstrap.js'/>" ></script>
<script type="text/javascript" src="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/js/bootstrap.bundle.min.js'/>" ></script>

<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/css/bootstrap.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/jquery-ui-1.14.0.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
<link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>

<script type="text/javascript" src="<c:url value='/js/egovframework/com/util.js'/>" ></script>

<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<script>
var toolbarOptions = [
    [{ 'font': [] }],                                 	// 폰트 설정
    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],	// 텍스트 크기 설정
    ['bold', 'italic', 'underline', 'strike'],        	// toggled buttons
    [{ 'color': [] }, { 'background': [] }],       // dropdown with defaults from theme
    [{ 'align': [] }],                                	// 텍스트 정렬
    [{ 'list': 'ordered'}, { 'list': 'bullet' }],     	// 리스트 항목
    [{ 'indent': '-1'}, { 'indent': '+1' }],         // outdent/indent
    ['blockquote', 'code-block'],                   // 코드블럭, 인용 문구
    ['link', 'image'],                                		// 링크, 이미지 추가
    ['clean']                                         		// remove formatting button
];
</script>
