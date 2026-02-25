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
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/cmm/jqueryui.css' />">
<script src="<c:url value='/js/egovframework/com/cmm/jquery.js' />"></script>
<script src="<c:url value='/js/egovframework/com/cmm/jqueryui.js' />"></script>
<script type="text/javaScript">
$(function(){	
	$(".stype").hide();
	if("${searchType}" == ""){
		$(".keyword").show();		
	}else{
		$(".${searchType}").show();
	}
	 $('#popupStocks').click(function (e) {
     	e.preventDefault();
         //var page = $(this).attr("href");
         var pagetitle = $(this).attr("title");
         var page = "<c:url value='/stock/data/selectStocksPopList.do'/>";
         var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
         .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
         .dialog({
         	autoOpen: false,
             modal: true,
             width: 820,
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
		
		$("#searchType").on("change",function(){
			let type = $(this).val();
			$(".stype").hide();
			$("."+type).show();
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
		alert("제목은 필수입니다.");
		return;
	}else if(searchType == "writeDate" && ($("#writeFromDate").val() == "" || $("#writeToDate").val() == "")){	
		alert("기간을 선택해 주세요.");
		return;	
	}else if(searchType == "itemCode" && $("input[name=itemCode]").val() == ""){	
		alert("종목을 선택해 주세요.");
		return;
	}
		Loading();
	    document.listForm.pageIndex.value = pageNo;
	    document.listForm.action = "<c:url value='/stock/naver/selectNaverMyResearchList.do'/>";
	    document.listForm.submit();
}

function linkPage(pageNo){
	Loading();
    document.listForm.pageIndex.value = pageNo;
    document.listForm.action = "<c:url value='/stock/naver/selectNaverMyResearchList.do'/>";
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


function fnDetail(rpId){
	$("#rpId").val(rpId);
	$("#mode").val("update");
	document.listForm.action = "<c:url value='/stock/naver/selectNaverMyResearchDetail.do'/>";
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
		<input type="hidden" name="stockSite"	value="${stockSite}"/>
		<input type="hidden" name="rpId"	id="rpId" />
		<input type="hidden" name="mode"	id="mode" />
		
		<!-- 검색영역11 -->
		<div class="search_box" title="<spring:message code="common.searchCondition.msg" />" style="padding: 10px;">
			<ul style="margin-bottom: 0px;">
				<li><div style="line-height:4px;">&nbsp;</div><div>분류 : </div></li>
				<li>
					<select name="searchGubun" id="searchGubun" class="select" title="검색구분">
						<option value="company" <c:if test="${'company' eq searchGubun}">selected="selected"</c:if>>종목분석</option>
						<option value="industry" <c:if test="${'industry' eq searchGubun}">selected="selected"</c:if>>산업분석</option>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>투자의견 : </div></li>
				<li>
					<select name="searchStockRecommendation" id="searchStockRecommendation" class="select" title="투자의견">
						<option value="" <c:if test="${empty searchStockRecommendation}">selected="selected"</c:if>>선택</option>
							<c:forEach items="${streCodeList}" var="info" varStatus="status">
								<option value="${info.code}" <c:if test="${searchStockRecommendation eq info.code}">selected="selected"</c:if>>${info.codeNm}</option>
							</c:forEach>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>검색구분 : </div></li>
				<li>
					<select name="searchType" id="searchType" class="select" title="검색구분">
						<option value="" <c:if test="${empty searchType}">selected="selected"</c:if>>선택</option>
						<option value="keyword" <c:if test="${'keyword' eq searchType}">selected="selected"</c:if>>제목</option>
						<option value="writeDate" <c:if test="${'writeDate' eq searchType}">selected="selected"</c:if>>기간</option>
						<c:if test="${searchGubun eq 'company'}">
							<option value="itemCode" <c:if test="${'itemCode' eq searchType}">selected="selected"</c:if>>종목</option>
						</c:if>
						<c:if test="${'industry' eq searchGubun}"><!-- 산업분석 리포트 -->
							<option value="upjong" <c:if test="${'upjong' eq searchType}">selected="selected"</c:if>>업종</option>
						</c:if>
					</select>
				</li>
				<li class="stype keyword" style="border: 0px solid #d2d2d2;">
					<input class="s_input" name="keyword" id="keyword" type="text"  size="10" style="width: 150px;" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${keyword}"/>'>
				</li>
				<c:if test="${'industry' eq searchGubun}"><!-- 산업분석 리포트 -->
					<li class="stype upjong"><div style="line-height:4px;">&nbsp;</div><div>업종 : </div></li>
					<li class="stype upjong">
						<select name="upjong" class="select" title="업종 선택">
							<option value="" <c:if test="${empty upjong}">selected="selected"</c:if>>선택</option>
							<c:forEach items="${upjoingCodeList}" var="upjongInfo" varStatus="status">
								<option value="${upjongInfo.codeNm}" <c:if test="${upjong eq upjongInfo.codeNm}">selected="selected"</c:if>>${upjongInfo.codeNm}</option>
							</c:forEach>
						</select>
					</li>	
				</c:if>
			
				<li class="stype writeDate"><div style="line-height:4px;">&nbsp;</div><div>기간 : </div></li>
				<li class="stype writeDate">
					<input type="text" name="writeFromDate" id="writeFromDate" size="15" maxlength="10" value="${writeFromDate == null ? today : writeFromDate}" style="width: 80px; text-align: center;" title="시작 기간 입력" />  ~
					<input type="text" name="writeToDate" id="writeToDate" size="15" maxlength="10" value="${writeToDate == null ? today : writeToDate}" style="width: 80px; text-align: center;" title="끝나는 기간 입력" >	
				</li>
				<c:if test="${searchGubun eq 'company'}">
					<li class="stype itemCode"><div style="line-height:4px;">&nbsp;</div><div>종목 : </div></li>
					<!-- 검색키워드 및 조회버튼 -->
					<li class="stype itemCode" style="border: 0px solid #d2d2d2;">
						<input type="hidden" name="itemCode" 	id="stock_code" value="${itemCode}"/>
						<input class="s_input" name="itemName" id="searchKeyword" type="text"  size="35" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${param.itemName}"/>'  maxlength="155" readonly="readonly">
						<a id="popupStocks" href="#none" target="_blank" title="종목 검색" style="selector-dummy:expression(this.hideFocus=false);">
							<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(종목검색)
						</a>
					</li>
				</c:if>
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
						<option value="10" <c:if test="${'10' eq naverResearchVO.pageUnit}">selected="selected"</c:if>>10</option>
						<option value="30" <c:if test="${'30' eq naverResearchVO.pageUnit}">selected="selected"</c:if>>30</option>
						<option value="60" <c:if test="${'60' eq naverResearchVO.pageUnit}">selected="selected"</c:if>>60</option>
						<option value="100" <c:if test="${'100' eq naverResearchVO.pageUnit}">selected="selected"</c:if>>100</option>
					</select>
				</li>
				<!-- 검색키워드 및 조회버튼 -->
				<li style="border: 0px solid #d2d2d2;">
					<%-- <input type="button" class="s_btn" onClick="fnInsert()" value="등록" title="등록 <spring:message code="input.button" />" /> --%>
					<input type="button" class="s_btn" onClick="excelDown('L')" value="<spring:message code="stock.com.excelDown.title" />" title="<spring:message code="stock.com.excelDown.title" /> <spring:message code="input.button" />" />
				</li>
			</ul>
		</div>
	</form>
	<!-- 목록영역 -->
	<table class="board_list" summary="<spring:message code="common.summary.list" arguments="${pageTitle}" />">
	<caption>${pageTitle} <spring:message code="title.list" /></caption>
	<colgroup>
		<col width="3%">
		<col width="4%"><!-- 순번 -->
		<c:if test="${searchGubun eq 'company'}">
			<col><!-- 종목 -->
		</c:if>
		<c:if test="${searchGubun eq 'industry'}">
			<col width="10%"><!-- 분류 -->
		</c:if>
		<col><!-- 제목 -->
		<c:if test="${searchGubun eq 'company'}">
			<col width="5%"><!-- 투자의견 -->
		</c:if>
			<col width="3%"><!-- 첨부 -->
			<col width="5%"><!-- 발행일 -->
		<c:if test="${searchGubun eq 'company'}">
			<col width="5%"><!-- 당일가 -->
			<col width="5%"><!-- 목표가 -->
			<col width="5%"><!-- 현재가 -->
			<col width="5%"><!-- 증감률 -->
			<col><!-- 반등시기 -->
		</c:if>
		<col width="5%"><!-- 수정일자 -->
		<col width="5%"><!-- 등록일자 -->
		<col width="5%"><!-- 수정 -->
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th><input type="checkbox" name="checkAll" id="checkAll" class="check2" title="전체선택"/></th>
		<th>순번</th>
		<c:if test="${searchGubun eq 'company'}">
			<th>종목</th>
		</c:if>
		<c:if test="${searchGubun eq 'industry'}">
			<th>분류</th>
		</c:if>
		<th>제목</th>
		<c:if test="${searchGubun eq 'company'}">
			<th>투자의견</th>
		</c:if>
		<th>첨부</th>
		<th>발행일</th>
		<c:if test="${searchGubun eq 'company'}">
			<th>당일가</th>
			<th>목표가</th>
			<th>현재가</th>
			<th>증감률</th>
			<th>반등시기</th>
		</c:if>
		<th>수정일자</th>
		<th>등록일자</th>
		<th>수정</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(reserchList) == 0}">
		<tr>
			<c:if test="${searchGubun eq 'company'}">
				<td colspan="14"><spring:message code="common.nodata.msg" /></td>
			</c:if>
			<c:if test="${searchGubun eq 'industry'}">
				<td colspan="9"><spring:message code="common.nodata.msg" /></td>
			</c:if>
		</tr>
	</c:if>
	<c:forEach var="item" items="${reserchList}" varStatus="status">
		<c:set var="pColor" value="#666"/>
		<c:if test="${item.dyaNowPrice lt 0}">
			<c:set var="pColor" value="blue"/>
		</c:if>
		<c:if test="${item.dyaNowPrice gt 0}">
			<c:set var="pColor" value="red"/>
		</c:if>
	<tr >
		<td><input type="checkbox" name="checkField" class="chk" title="선택"/></td>
		<td><c:out value="${item.rn}"/></td>
		<c:if test="${searchGubun eq 'company'}">
			<td>
				<span class="link">
					<a href="#none" onclick="stockOpenPopup('https://finance.naver.com/item/main.naver?code=${item.relStockCode}','${item.stocksName}','1000','600'); return false;" title="네이버 종합정보 이동"><c:out value="${item.stocksName}"/></a>
					<a href="#none" onclick="stockOpenPopup('https://finance.naver.com/item/fchart.naver?code=${item.relStockCode}','${item.stocksName}','1000','600'); return false;" title="네이버 챠트보기 이동"><img src="/images/egovframework/stock/chart.png" style="width: 14px;"></a>
			      	<span class="chart-icons" style="cursor:pointer;">
				        <i class="fa fa-calendar-day" onclick="openChartModal('${item.relStockCode}' , '${item.stocksName}', 'day')" title="일봉차트 이미지팝업">[일]</i>
				        <i class="fa fa-calendar-week" onclick="openChartModal('${item.relStockCode}' , '${item.stocksName}' , 'week')" title="주봉차트 이미지팝업">[주]</i>
				        <i class="fa fa-calendar-alt" onclick="openChartModal('${item.relStockCode}' , '${item.stocksName}' , 'month')" title="월봉차트 이미지팝업">[월]</i>
				    </span>
			    </span>
			</td>
		</c:if>
		<c:if test="${searchGubun eq 'industry'}">
			<td><c:out value="${item.rpCl}"/></td>
		</c:if>
		<td style="text-align: left;"><a href="https://finance.naver.com/research/company_read.naver?nid=${item.originUid}" target="_blank"><c:out value="${item.rpTitle}"/></a></td>
		<c:if test="${searchGubun eq 'company'}">
			<td><c:out value="${item.stockRecommendationNm}"/></td>
		</c:if>
		<td><a href="${item.rpLink}" target="_blank"><img src="/images/egovframework/com/cmm/down_pdf.gif" alt="pdf" ></a></td>
		<td><c:out value="${item.rpDate}"/></td>
		<c:if test="${searchGubun eq 'company'}">
			<td style="text-align: right;"><fmt:formatNumber value="${item.dayPrice}" pattern="#,###" />원</td>
			<td style="text-align: right;"><fmt:formatNumber value="${item.targetPrice}" pattern="#,###" />원</td>
			<td style="text-align: right;"><fmt:formatNumber value="${item.nowPrice}" pattern="#,###" />원</td>
			<td style="color:${pColor};"><c:out value="${item.dyaNowPecent}"/>%</td>
			<td style="text-align: left;"><c:out value="${item.whenRebound}"/></td>
		</c:if>
		<td><c:out value="${item.uptDate}"/></td>
		<td><c:out value="${item.regDate}"/></td>
		<td>
			<div class="button_box1">
				<ul>
					<li>
						<input type="button" class="btn02" onClick="fnDetail('${item.rpId}');" value="수정" title="수정 <spring:message code="input.button" />" />
					</li>
				</ul>
			</div>
		</td>
	</tr>
	</c:forEach>
	</tbody>
	</table>
	<c:if test="${!empty paginationInfo}">
		<div class="pagination">
			<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage"/></ul>
		</div>
	</c:if>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
