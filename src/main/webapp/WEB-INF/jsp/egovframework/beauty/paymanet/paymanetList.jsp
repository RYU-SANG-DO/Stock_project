<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : NAVER 리서치 List 화면
  * @Modification Information
  * @  수정일			수정내용
  * @ -----------		---------------------------
  * @ 2026.01.27		최초 생성
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
	 $("#writeFromDate").datepicker(  
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


		$("#writeToDate").datepicker( 
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
	
		    // 1. 전체 선택 / 해제
		    $('#checkAll').on('change', function() {
		        // 전체 선택 체크박스의 상태를 개별 체크박스에 일괄 적용
		        $('.chk').prop('checked', $(this).is(':checked'));
		    });

		    // 2. 개별 체크박스 클릭 시 '전체 선택' 상태 동기화
		    $('.chk').on('change', function() {
		        const total = $('.chk').length;
		        const checked = $('.chk:checked').length;

		        // 개별 체크박스 전체 개수와 체크된 개수가 같으면 전체 선택 체크
		        $('#checkAll').prop('checked', total === checked);
		    });
});
//검색
function fncSelectList(pageNo){
	let searchType = $("select[name=searchType]").val();
	 if(searchType == "keyword" && $("#keyword").val() == ""){
		alert("제목+내용은 필수입니다.");
		return;
	}else if(searchType == "brokerCode" && $("select[name=brokerCode]").val() == ""){	
		alert("증권사를 선택해 주세요.");
		return;
	}else if(searchType == "writeDate" && ($("#writeFromDate").val() == "" || $("#writeToDate").val() == "")){	
		alert("기간을 선택해 주세요.");
		return;	
	}else if(searchType == "itemCode" && $("input[name=itemCode]").val() == ""){	
		alert("종목을 선택해 주세요.");
		return;
	}else if(searchType == "upjong" && $("select[name=upjong]").val() == ""){	
		alert("업종을 선택해 주세요.");
		return;	
	}
		Loading();
	    document.listForm.pageIndex.value = pageNo;
	    document.listForm.action = "<c:url value='/stock/info/selectMyStockList.do'/>";
	    document.listForm.submit();
}

function linkPage(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/info/selectMyStockList.do'/>";
    document.listForm.submit();
}


function press() {
	//Loading();
    if (event.keyCode==13) {
    	fncSelectList('1');
    }
}

//내역 엑셀 다운로드
function excelDown(gubun){
	$("#downGubun").val(gubun);
	$.ajax({
		type : "POST",
		url : "<c:url value='/stock/info/selectMyStockListExclDownAjax.do'/>",
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


function fnInsert(){
	$("#mode").val("insert");
	document.listForm.action = "<c:url value='/beauty/paymanet/moveBeautyPaymanet.do'/>";
    document.listForm.submit();
}

function fnDetail(seq){
	$("#seq").val(seq);
	$("#mode").val("update");
	document.listForm.action = "<c:url value='/beauty/paymanet/moveBeautyPaymanet.do'/>";
    document.listForm.submit();
}
</script>
<style>
.search_box ul li{vertical-align: middle;}
.mainbtn ul li input.s_btn.nosel{
	color: #232121; 
	background:#f1f1e8;
}
.item_list .button_box ul{
	text-align: center;
}
</style>
</head>
<body>
	<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
	<div class="board">
	<h1>${pageTitle} 내역</h1>
	
	<form name="listForm" method="post">
		<input type="hidden" name="pageIndex"	id="pageIndex"/>
		<input type="hidden" name="seq"	id="seq" />
		<input type="hidden" name="mode"	id="mode" />
		
		<!-- 검색영역11 -->
		<div class="search_box" title="<spring:message code="common.searchCondition.msg" />" style="padding: 10px;">
			<ul style="margin-bottom: 0px;">
				<li><div style="line-height:4px;">&nbsp;</div><div>검색구분 : </div></li>
				<li>
					<select name="searchType" id="searchType" class="select" title="검색구분">
						<%-- <option value="" <c:if test="${empty searchType}">selected="selected"</c:if>>선택</option> --%>
						<option value="stockNm" <c:if test="${'stockNm' eq searchType}">selected="selected"</c:if>>종목</option>
						<option value="account" <c:if test="${'account' eq searchType}">selected="selected"</c:if>>계좌</option>
					</select>
				</li>
				<li class="stype keyword" style="border: 0px solid #d2d2d2;">
					<input class="s_input" name="keyword" id="keyword" type="text"  size="10" style="width: 150px;" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${keyword}"/>'>
				</li>
			
				<li  style="border: 0px solid #d2d2d2;">
					<input type="button" class="s_btn"  onClick="fncSelectList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
				</li>
			</ul>
		</div>
		
		<div class="button_box">
			<ul style="margin-bottom: 0px;">
				<li style="float:left;">
					페이지 사이즈:
					<select name="pageUnit" class="select" title="페이지">
						<option value="10" <c:if test="${'10' eq paramInfo.pageUnit}">selected="selected"</c:if>>10</option>
						<option value="30" <c:if test="${'30' eq paramInfo.pageUnit}">selected="selected"</c:if>>30</option>
						<option value="60" <c:if test="${'60' eq paramInfo.pageUnit}">selected="selected"</c:if>>60</option>
						<option value="100" <c:if test="${'100' eq paramInfo.pageUnit}">selected="selected"</c:if>>100</option>
					</select>
				</li>
				<!-- 검색키워드 및 조회버튼 -->
				<li style="border: 0px solid #d2d2d2;">
					<input type="button" class="s_btn" onClick="fnInsert()" value="등록" title="등록 <spring:message code="input.button" />" />
					<input type="button" class="s_btn" onClick="excelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
				</li>
			</ul>
		</div>
	</form>
	<!-- 목록영역 -->
	<c:set var="unitPriceTotal" value="0"/><!-- 당일합계 -->
	<c:set var="nowPriceTotal" value="0"/><!-- 현재합계 -->
	<c:set var="dyaNowPriceTotal" value="0"/><!-- 합계 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<col width="3%">
		<col>
		<col>
		<col>
		<col>
		<col>
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th>순번</th>
		<th>스타일</th>
		<th>결재일자</th>
		<th>결재타입</th>
		<th>결재금액</th>
		<th>등록일자</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(list) == 0}">
		<tr>
			<td colspan="6"><spring:message code="common.nodata.msg" /></td>
		</tr>
	</c:if>
	<c:forEach var="item" items="${list}" varStatus="status">
		<c:set var="pColor" value="#666"/>
		<c:set var="indeColor" value="#666"/>
	<tr>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	<c:if test="${!empty paginationInfo}">
		<!-- paging navigation -->
		<div class="pagination">
			<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/></ul>
		</div>
	</c:if>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
