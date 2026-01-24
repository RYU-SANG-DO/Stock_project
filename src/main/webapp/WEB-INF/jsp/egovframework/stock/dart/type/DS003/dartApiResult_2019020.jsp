<%
 /**
  * @Class Name : StockDartController.java
  * @Description : 정기보고서 재무정보 > 단일회사 전체 재무제표 List 화면
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
             width: 620,
             height: 700,
             title: pagetitle
     	});
     	$dialog.dialog('open');
     	
 	});
	 $.datepicker.setDefaults({
		  dateFormat: 'yy',
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
	 $("#bsns_year").datepicker(  
		        {dateFormat:'yy'
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
//검색
function fncSelectDataList(pageNo){
	/* if($("#bgn_de").val() == ""){
		alert("검색기간에 시작일을 선택해 주세요.");
		return;
	}else if($("#end_de").val() == ""){	
		alert("검색기간에 종료일을 선택해 주세요.");
		return;
	}else if($("#stock_code").val() == ""){	
		alert("종목을 선택해 주세요.");
		return;
	} */
		Loading();
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
	$("#downGubun").val(gubun);
	$.ajax({
		type : "POST",
		url : "<c:url value='/stock/dart/selectDartApiResultListExclDownAjax.do'/>",
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

function fncSelectList(){
	$("#apiId").val("");
    document.listForm.action = "<c:url value='/stock/dart/selectDartList.do'/>";
    document.listForm.submit();
}
</script>
<style>
.search_box ul li{vertical-align: middle;}
</style>
</head>
<body>
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
			<li><div style="line-height:4px;">&nbsp;</div><div>사업연도 : </div></li>
			<li>
				<input type="text" name="bsns_year" id="bsns_year" size="15" maxlength="10" value="${bsns_year}" style="width: 80px; text-align: center;" title="사업연도" />
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>보고서 : </div></li>
			<li>
				<select name="reprt_code" class="select" title="보고서">
					<option value="11013" <c:if test="${'11013' eq reprt_code}">selected="selected"</c:if>>1분기보고서</option>
					<option value="11012" <c:if test="${'11012' eq reprt_code}">selected="selected"</c:if>>반기보고서</option>
					<option value="11014" <c:if test="${'11014' eq reprt_code}">selected="selected"</c:if>>3분기보고서</option>
					<option value="11011" <c:if test="${'11011' eq reprt_code}">selected="selected"</c:if>>사업보고서</option>
				</select>
			</li>
			<li><div style="line-height:4px;">&nbsp;</div><div>개별/연결구분 : </div></li>
			<li>
				<select name="fs_div" class="select" title="개별/연결구분">
					<option value="OFS" <c:if test="${'OFS' eq fs_div}">selected="selected"</c:if>>재무제표</option>
					<option value="CFS" <c:if test="${'CFS' eq fs_div}">selected="selected"</c:if>>연결재무제표</option>
				</select>
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
				<input class="s_input" name="searchKeyword" id="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${param.searchKeyword}"/>'  maxlength="155" readonly="readonly">
				<a id="popupStocks" href="#none" target="_blank" title="종목 검색" style="selector-dummy:expression(this.hideFocus=false);">
					<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(종목검색)
				</a>
				<input type="button" class="s_btn"  onClick="fncSelectDataList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
			</li>
		</ul>
	</div>
	
	<div class="button_box">
		<ul style="margin-bottom: 0px;">
			<!-- 검색키워드 및 조회버튼 -->
			<li style="border: 0px solid #d2d2d2;">
				<input type="button" class="s_btn" onClick="fncSelectList()" value="목록" title="목록 <spring:message code="input.button" />" />
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
		<th>재무제표</th>
		<th>계정명</th>
		<th>계정상세</th>
		<th>당기명</th>
		<th>당기금액</th>
		<th>전기명</th>
		<th>전기금액</th>
		<th>전기명(분/반기)</th>
		<th>전기금액(분/반기)</th>
		<th>전기누적금액</th>
		<th>전전기명</th>
		<th>전전기금액</th>
		<th>정렬순서</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(list) == 0}">
		<tr>
			<td colspan="14"><spring:message code="common.nodata.msg" /></td>
		</tr>
	</c:if>
	<c:forEach var="item" items="${list}" varStatus="status">
	<tr>
		<td><c:out value="${status.count}"/></td>
		<td><c:out value="${item.sj_div}"/></td>
		<td><c:out value="${item.account_nm}"/></td>
		<td><c:out value="${item.account_detail}"/></td>
		<td><c:out value="${item.thstrm_nm}"/></td>
		<td><c:out value="${item.thstrm_amount}"/></td>
		<td><c:out value="${item.frmtrm_nm}"/></td>
		<td><c:out value="${item.frmtrm_amount}"/></td>
		<td><c:out value="${item.frmtrm_q_nm}"/></td>
		<td><c:out value="${item.frmtrm_q_amount}"/></td>
		<td><c:out value="${item.frmtrm_add_amount}"/></td>
		<td><c:out value="${item.bfefrmtrm_nm}"/></td>
		<td><c:out value="${item.bfefrmtrm_amount}"/></td>
		<td><c:out value="${item.ord}"/></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
