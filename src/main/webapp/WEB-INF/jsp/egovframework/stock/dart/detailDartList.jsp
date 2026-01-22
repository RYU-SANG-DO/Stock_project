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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="detailUrl"><spring:message code="stock.naver.theme.detail.url"/> </c:set>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckTop.jsp" flush="true" />
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/cmm/jqueryui.css' />">
<script src="<c:url value='/js/egovframework/com/cmm/jquery.js' />"></script>
<script src="<c:url value='/js/egovframework/com/cmm/jqueryui.js' />"></script>
<script type="text/javaScript">
$(function(){
	
	 $('#popupStocks').click(function (e) {
     	e.preventDefault();
         //var page = $(this).attr("href");
         var pagetitle = $(this).attr("title");
         var page = "<c:url value='/stock/data/selectStocksList.do'/>";
         var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
         .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
         .dialog({
         	autoOpen: false,
             modal: true,
             width: 510,
             height: 650,
             title: pagetitle
     	});
     	$dialog.dialog('open');
     	
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
	 $("#bgn_de").datepicker(  
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


		$("#end_de").datepicker( 
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
function fncSelectDataList(pageNo){
	//Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/dart/selectDartApiResultList.do'/>";
    document.listForm.submit();
}

function linkPage(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/dart/selectDartApiResultList.do'/>";
    document.listForm.submit();
}


function press() {
	//Loading();
    if (event.keyCode==13) {
    	fncSelectDataList('1');
    }
}

//내역 엑셀 다운로드
function excelDown(gubun){
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
	//Loading();
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
<div class="board">
<h1>DART(${dartInfo.apiGrpNm} > ${dartInfo.apiNm})</h1>
<form name="listForm" method="post">
	<input type="hidden" name="pageIndex"	id="pageIndex"/>
	<input type="hidden" name="apiGrpCd"	id="apiGrpCd"	value="${dartInfo.apiGrpCd}"/>
	<input type="hidden" name="apiId"			id="apiId"		value="${dartInfo.apiId}"/>
	<input type="hidden" name="urlGubun" 	id="urlGubun"	value="${dartInfo.urlGubun}"/>
	<input type="hidden" name="mathod" 		id="mathod"	value="${dartInfo.mathod}"/>
	<input type="hidden" name="encoding" 	id="encoding"	value="${dartInfo.encoding}"/>
	<input type="hidden" name="stock_code" 	id="stock_code" value="${stock_code}"/>
	<!-- 검색영역 -->
	<div class="search_box" title="<spring:message code="common.searchCondition.msg" />" style="padding: 10px;">
		<ul style="margin-bottom: 0px;">
			<li>
				검색기간&nbsp;:&nbsp;
				<input type="text" name="bgn_de" id="bgn_de" size="15" maxlength="10" value="${bgn_de}" title="검색시작일" />"  ~
				<input type="text" name="end_de" id="end_de" size="15" maxlength="10" value="${end_de}" title="검색종료일" >&nbsp;&nbsp;&nbsp;			
			</li>
			<li>
				<select name="pblntf_ty" class="select" title="공시유형"><!-- 공시유형 -->
					<option value="" <c:if test="${empty pblntf_ty}">selected="selected"</c:if>>페이지보기</option>
					<option value="A" <c:if test="${'A' eq pblntf_ty}">selected="selected"</c:if>>전체 보기</option>
				</select>
			</li>
			<li>
				공시유형&nbsp;:&nbsp;
				<select name="listType" class="select" title="검색조건구분"><!-- 검색조건구분 -->
					<option value="" <c:if test="${empty listType}">selected="selected"</c:if>>페이지보기</option>
					<option value="A" <c:if test="${'A' eq listType}">selected="selected"</c:if>>전체 보기</option>
				</select>		
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>페이지 별 건수 : </div></li>
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="page_count" type="text"  size="10" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${page_count}"/>'  maxlength="3">
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>페이지번호 : </div></li>
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="page_no" type="text"  size="10" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${page_no}"/>'  maxlength="5">
			</li>
			<li>
				<select name="resultType" class="resultType" title="결과타입"><!-- 결과타입 -->
					<c:if test="${dartInfo.jsonYn eq 'Y'}">
						<option value="json" <c:if test="${empty resultType || resultType eq 'json'}">selected="selected"</c:if>>JSON</option>
					</c:if>
					<c:if test="${dartInfo.xmlYn eq 'Y'}">
						<option value="xml" <c:if test="${resultType eq 'xml'}">selected="selected"</c:if>>XML</option>
					</c:if>
					<c:if test="${dartInfo.zipYn eq 'Y'}">
						<option value="zip" <c:if test="${resultType eq 'zip'}">selected="selected"</c:if>>ZIP</option>
					</c:if>
				</select>
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>종목 : </div></li>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input class="s_input" name="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${param.searchKeyword}"/>'  maxlength="155" readonly="readonly">
				<a id="popupStocks" href="#none" target="_blank" title="종목 검색" style="selector-dummy:expression(this.hideFocus=false);">
					<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(종목검색)
				</a>
				<input type="button" class="s_btn"  onClick="fncSelectDataList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	
	<div class="button_box">
		<ul style="margin-bottom: 0px;">
			<%-- <li style="float:left;font-size: 14px;">
				<span>${dartInfo.apiGrpNm}</span> > <span>${dartInfo.apiNm}</span>
			</li> --%>
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="excelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	</form>
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<col style="width: 5%;">
		<col style="width: 5%;">
		<col>
		<col>
		<col>
		<col>
		<col>
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th>순번</th>
		<th>법인구분</th>
		<th>종목명</th>
		<th>보고서명</th>
		<th>접수번호</th>
		<th>접수일자</th>
		<th>비고</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(list) == 0}">
		<tr>
			<td colspan="7"><spring:message code="common.nodata.msg" /></td>
		</tr>
	</c:if>
	<c:forEach var="item" items="${list}" varStatus="status">
	<tr>
		<td><c:out value="${status.count}"/></td>
		<td>
			<c:choose>
				<c:when test="${item.corp_cls eq 'Y'}">코스피</c:when>
				<c:when test="${item.corp_cls eq 'K'}">코스닥</c:when>
				<c:when test="${item.corp_cls eq 'N'}">코넥스</c:when>
				<c:otherwise>기타</c:otherwise>
			</c:choose>
		</td>
		<td><c:out value="${item.corp_name}"/></td>
		<td><a href="https://dart.fss.or.kr/dsaf001/main.do?rcpNo=${item.rcept_no}" target="_blank"><c:out value="${item.report_nm}"/></a></td>
		<td><c:out value="${item.rcept_no}"/></td>
		<td>
			<fmt:parseDate value="${item.rcept_dt}" var="dateValue" pattern="yyyyMMdd"/>
			<fmt:formatDate value="${dateValue}" pattern="yyyy-MM-dd"/>
		</td>
		<td><c:out value="${item.rm}"/></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
