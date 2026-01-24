<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : themeList List 화면
  * @Modification Information
  * @  수정일			수정내용
  * @ -----------		---------------------------
  * @ 2025.01.03		최초 생성
  */
%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"		uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui"		uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn"		uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt"	uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<script type="text/javaScript" defer="defer">
$(function(){
	$("#display, #start").keyup(function() {
		$(this).val($(this).val().replace(/[^0-9]/g,""));
	});
	
	$(".s_input").on("keyup",function(event){
		 if (event.keyCode==13) {
		    	fncSelectList('1');
		    }
	});
	
	 $.datepicker.setDefaults({
		  dateFormat: 'yy-mm-dd',
		  closeText: "닫기",
		  currentText: "오늘",
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
	 $("#search_date").datepicker(  
		        {dateFormat:'yy-mm-dd'
		         , showOn: 'button'
		         , buttonImage: '<c:url value='/images/egovframework/com/cmm/icon/bu_icon_carlendar.gif'/>'
		         , buttonImageOnly: true
		         
		         , showMonthAfterYear: true
		         , showOtherMonths: true
			     , selectOtherMonths: true
					
		         , changeMonth: true // 월선택 select box 표시 (기본은 false)
		         , changeYear: true  // 년선택 selectbox 표시 (기본은 false)
		         , showButtonPanel: true // 하단 today, done  버튼기능 추가 표시 (기본은 false)
		});

	
});

function fncSelectList(pageNo){
	if($("#searchKeyword").val() == ""){
		alert("검색어는 필수입니다.");
		return;
	}else if(parseInt($("#start").val()) < 1  || parseInt($("#start").val()) > 100){
		alert("페이지수는 1에서 100사이만 가능합니다.");
		return;
	}else if(parseInt($("#display").val()) < 1  || parseInt($("#display").val()) > 100){	
		alert("조회건수는 1에서 100사이만 가능합니다.");
		return;
	}else{
		Loading();
	    document.listForm.pageIndex.value = pageNo;
	    document.listForm.action = "<c:url value='/stock/naver/selectSearchApi.do'/>";
	    document.listForm.submit();
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
			} else{
				alert("ERROR!");
				return;
			} 
		}
	});
}

</script>
<style>
.board_list .ov b{color: #4160d5;}
</style>
</head>
<body>
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<form name="listForm" action="${pageContext.request.contextPath}/stock/naver/selectSearchApi.do" method="post">
	<input type="hidden" name="downGubun" id="downGubun"/>
	<input type="hidden" name="type" id="type" value="theme"/>
	<input type="hidden" name="no" id="no" value=""/>
<div class="board">
	<h1>네이버 검색 <spring:message code="title.list" /></h1>
	<!-- 검색영역 -->
	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />">
		<ul>
			<li><div style="line-height:4px;">&nbsp;</div><div>일자 : </div></li>
			<li>
				<input type="text" name="search_date" id="search_date" size="15" maxlength="10" value="${search_date}" style="width: 80px; text-align: center;" title="검색일" />
			</li>
			<li>
			<select name="serchType" class="select" title="검색타입">
				<%-- <option value=""				<c:if test="${empty  serchType}">selected="selected"</c:if>>검색 타입</option> --%>
				<option value="news"		<c:if test="${'news' eq serchType}">selected="selected"</c:if>>뉴스</option>
				<option value="webkr"		<c:if test="${'webkr' eq serchType}">selected="selected"</c:if>>웹문서</option>
				<option value="doc"			<c:if test="${'doc' eq serchType}">selected="selected"</c:if>>전문자료</option>
				<option value="blog"			<c:if test="${'blog' eq serchType}">selected="selected"</c:if>>블로그</option>
				<option value="book_adv" <c:if test="${'book_adv' eq serchType}">selected="selected"</c:if>>책</option>
				<option value="encyc"		<c:if test="${'encyc' eq serchType}">selected="selected"</c:if>>백과사전</option>
				<option value="kin"			<c:if test="${'kin' eq serchType}">selected="selected"</c:if>>지식IN</option>
			</select>
			</li>
			<li>
			<select name="returnType" class="select" title="리턴 타입">
				 <%-- <option value="" <c:if test="${empty returnType}">selected="selected"</c:if>>데이터 타입</option> --%>
				<option value="json" <c:if test="${'json' eq returnType}">selected="selected"</c:if>>JSON</option>
				<option value="xml" <c:if test="${'xml' eq returnType}">selected="selected"</c:if>>XML</option>
			</select>
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>페이지수 : </div></li>
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="start" id="start" type="text" style="width: 30px;"  title="페이지수" value="${start == null ? 1 : start}"  maxlength="3" min="1" max="100">
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>조회건수 : </div></li>
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="display" id="display" type="text" style="width: 50px;"  title="건수" value="${display == null ? 10 : display}"  maxlength="3" min="1" max="100">
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>검색어 : </div></li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="searchKeyword" id="searchKeyword" type="text"  size="40" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${searchKeyword}"/>'  maxlength="155" >
				<input type="button" class="s_btn"  onClick="fncSelectList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	
	<div class="button_box">
		<ul>
			<li style="float:left;font-size: 14px;">
				<span>전체건수 : </span><span><fmt:formatNumber value="${fn:length(searchList)}" pattern="#,###" /></span>
			</li>
			<%-- <li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="naverExcelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
				<input type="button" class="s_btn" onClick="naverExcelDown('D')" value="<spring:message code="stock.com.excelDown.title" />(상세포함)" title="<spring:message code="stock.com.excelDown.title" />(상세포함) <spring:message code="input.button" />" />
			</li> --%>
		</ul>
	</div>
	
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<col style="width: 5%;">
		<col style="width: 30%;">
		<col>
		<col style="width: 10%;">
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th>순번</th>
		<th>제목</th>
		<th>설명</th>
		<th>일자</th>
	</tr>
	</thead>
	<tbody class="ov">
		<c:if test="${fn:length(searchList) == 0}">
		<tr>
			<td colspan="4"><spring:message code="common.nodata.msg" /></td>
		</tr>
		</c:if>
		<c:forEach var="item" items="${searchList}" varStatus="status">
		<tr>
			<td><c:out value="${status.count}"/></td>
			<td style="text-align: left;"><a href="${item.link}" target="_blank"><c:out value="${item.title}" escapeXml="false"/></a></td>
			<td style="text-align: left;"><c:out value="${item.description}" escapeXml="false"/></td>
			<td><c:out value="${item.pubDateFormat}"/></td>
		</tr>
		</c:forEach>
	</tbody>
	</table>
</div><!-- end div board -->
<input type="hidden" name="authorCode"/>
<input type="hidden" name="authorCodes"/>
<input type="hidden" name="pageIndex" value="<c:out value='${naverThemeVO.pageIndex}'/>"/>

</form>
<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />

