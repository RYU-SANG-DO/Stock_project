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

<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/com.css' />">
<%-- <link href="<c:url value="/css/egovframework/com/button.css"/>" rel="stylesheet" type="text/css"> --%>
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/bootstrap-4.0.0/css/bootstrap.min.css' />">
<link type="text/css" rel="stylesheet" href="<c:url value='/js/egovframework/com/cmm/jquery-ui-1.14.0.css' />">
<link href="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.snow.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/quill@2.0.3/dist/quill.js"></script>

<script type="text/javascript" src="<c:url value='/js/egovframework/com/util.js'/>" ></script>

<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<script>
$.datepicker.setDefaults({
	  dateFormat: 'yy-mm-dd',
	  prevText: '이전 달',
	  nextText: '다음 달',
	  monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	  monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	  dayNames: ['일', '월', '화', '수', '목', '금', '토'],
	  dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
	  dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	  showMonthAfterYear: true,
	  yearSuffix: '년'
	});
$(function() {
	 /*  $("#datepicker").datepicker({
	    dateFormat: 'yy-mm-dd', // 날짜 형식
	    prevText: '이전 달',
	    nextText: '다음 달',
	    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
	    dayNames: ['일', '월', '화', '수', '목', '금', '토'],
	    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
	    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
	    showMonthAfterYear: true, // 년도 뒤에 월 표시
	    yearSuffix: '년' // 년도 뒤에 붙는 텍스트
	  }); */
	});
</script>
<style>
body{
	line-height: 1;
}
a{
	color: #7854ba;
	font-weight: bold;
}
</style>