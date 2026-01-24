<%
 /**
  * @Class Name : StockNaverController.java
  * @Description : NAVER 리서치 List 화면
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
	
		$("#searchType").on("change",function(){
			let type = $(this).val();
			$(".stype").hide();
			$("."+type).show();
		});
});
//검색
function fncSelectDataList(pageNo){
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
	    document.listForm.action = "<c:url value='/stock/naver/selectNaverResearchList.do'/>";
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
    document.listForm.action = "<c:url value='/stock/naver/selectNaverResearchMain.do'/>";
    document.listForm.submit();
}

function menuMove(param , name){
	document.listForm2.searchGubun.value = param;
	document.listForm2.searchGubunNm.value = name;
    document.listForm2.action = "<c:url value='/stock/naver/selectNaverResearchList.do'/>";
    document.listForm2.submit();
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
	<h1>NAVER 리서치 리포트 (${searchGubunNm})</h1>
	<form name="listForm2" method="post">
		<input type="hidden" name="stockSite" value="${stockSite}"/>
		<input type="hidden" name="searchGubun"	value="${searchGubun}"/>
		<input type="hidden" name="searchGubunNm" value="${searchGubunNm}"/>
	</form>
	<div style="padding-top: 10px; border: 1px solid #b1bbcb; margin-bottom: 7px;">
		<table summary="원하시는 항목을 선택하여 결과를 보실 수 있습니다." class="item_list" style="margin-bottom: -6px;">
				<colgroup>
					<col>
					<col>
					<col>
					<col>
					<col>
					<col>
				</colgroup>
				<tbody>
				<tr>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'market_info'}">nosel</c:if>" onClick="menuMove('market_info','시황정보')" value="시황정보"/></li></ul></div></td>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'invest'}">nosel</c:if>" onClick="menuMove('invest','투자정보')" value="투자정보"/></li></ul></div></td>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'company'}">nosel</c:if>" onClick="menuMove('company','종목분석')" value="종목분석"/></li></ul></div></td>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'industry'}">nosel</c:if>" onClick="menuMove('industry','산업분석')" value="산업분석"/></li></ul></div></td>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'economy'}">nosel</c:if>" onClick="menuMove('economy','경제분석')" value="경제분석"/></li></ul></div></td>
					<td><div class="button_box mainbtn"><ul><li><input type="button" class="s_btn <c:if test="${searchGubun ne 'debenture'}">nosel</c:if>" onClick="menuMove('debenture','채권분석')" value="채권분석"/></li></ul></div></td>
				</tr>	
			</tbody>
			</table>
	</div>
	<form name="listForm" method="post">
		<input type="hidden" name="pageIndex"	id="pageIndex"/>
		<input type="hidden" name="stockSite"	value="${stockSite}"/>
		<input type="hidden" name="searchGubun"	value="${searchGubun}"/>
		<input type="hidden" name="searchGubunNm" value="${searchGubunNm}"/>
		
		<!-- 검색영역 -->
		<div class="search_box" title="<spring:message code="common.searchCondition.msg" />" style="padding: 10px;">
			<ul style="margin-bottom: 0px;">
				<li><div style="line-height:4px;">&nbsp;</div><div>검색구분 : </div></li>
				<li>
					<select name="searchType" id="searchType" class="select" title="검색구분">
						<option value="" <c:if test="${empty searchType}">selected="selected"</c:if>>선택</option>
						<option value="keyword" <c:if test="${'keyword' eq searchType}">selected="selected"</c:if>>제목+내용</option>
						<option value="brokerCode" <c:if test="${'brokerCode' eq searchType}">selected="selected"</c:if>>증권사</option>
						<option value="writeDate" <c:if test="${'writeDate' eq searchType}">selected="selected"</c:if>>기간</option>
						<c:if test="${'company' eq searchGubun}">
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
				<li class="stype writeDate"><div style="line-height:4px;">&nbsp;</div><div>기간 : </div></li>
				<li class="stype writeDate">
					<input type="text" name="writeFromDate" id="writeFromDate" size="15" maxlength="10" value="${writeFromDate == null ? today : writeFromDate}" style="width: 80px; text-align: center;" title="시작 기간 입력" />  ~
					<input type="text" name="writeToDate" id="writeToDate" size="15" maxlength="10" value="${writeToDate == null ? today : writeToDate}" style="width: 80px; text-align: center;" title="끝나는 기간 입력" >	
				</li>
				<li class="stype brokerCode"><div style="line-height:4px;">&nbsp;</div><div>증권사 : </div></li>
				<li class="stype brokerCode">
					<select name="brokerCode" class="select" title="증권사 선택">
						<option value="" <c:if test="${empty brokerCode}">selected="selected"</c:if>>선택</option>
						<option value="60" <c:if test="${'60' eq brokerCode}">selected="selected"</c:if>>BNK투자증권</option>
						<option value="55" <c:if test="${'55' eq brokerCode}">selected="selected"</c:if>>DB금융투자</option>
						<option value="66" <c:if test="${'66' eq brokerCode}">selected="selected"</c:if>>DS투자증권</option>
						<option value="40" <c:if test="${'40' eq brokerCode}">selected="selected"</c:if>>IBK투자증권</option>
						<option value="61" <c:if test="${'61' eq brokerCode}">selected="selected"</c:if>>iM증권</option>
						<option value="58" <c:if test="${'58' eq brokerCode}">selected="selected"</c:if>>KB증권</option>
						<option value="49" <c:if test="${'49' eq brokerCode}">selected="selected"</c:if>>KTB투자증권</option>
						<option value="9" <c:if test="${'9' eq brokerCode}">selected="selected"</c:if>>NH투자증권</option>
						<option value="69" <c:if test="${'69' eq brokerCode}">selected="selected"</c:if>>NICE평가정보</option>
						<option value="64" <c:if test="${'64' eq brokerCode}">selected="selected"</c:if>>SK증권</option>
						<option value="68" <c:if test="${'68' eq brokerCode}">selected="selected"</c:if>>골든브릿지투자증권</option>
						<option value="62" <c:if test="${'62' eq brokerCode}">selected="selected"</c:if>>교보증권</option>
						<option value="72" <c:if test="${'72' eq brokerCode}">selected="selected"</c:if>>나이스디앤비</option>
						<option value="76" <c:if test="${'76' eq brokerCode}">selected="selected"</c:if>>다올투자증권</option>
						<option value="15" <c:if test="${'15' eq brokerCode}">selected="selected"</c:if>>대신증권</option>
						<option value="17" <c:if test="${'17' eq brokerCode}">selected="selected"</c:if>>메리츠증권</option>
						<option value="56" <c:if test="${'56' eq brokerCode}">selected="selected"</c:if>>미래에셋증권</option>
						<option value="78" <c:if test="${'78' eq brokerCode}">selected="selected"</c:if>>삼성증권</option>
						<option value="21" <c:if test="${'21' eq brokerCode}">selected="selected"</c:if>>신한투자증권</option>
						<option value="41" <c:if test="${'41' eq brokerCode}">selected="selected"</c:if>>에프앤가이드</option>
						<option value="18" <c:if test="${'18' eq brokerCode}">selected="selected"</c:if>>유안타증권</option>
						<option value="63" <c:if test="${'63' eq brokerCode}">selected="selected"</c:if>>유진투자증권</option>
						<option value="26" <c:if test="${'26' eq brokerCode}">selected="selected"</c:if>>이베스트증권</option>
						<option value="59" <c:if test="${'59' eq brokerCode}">selected="selected"</c:if>>케이프투자증권</option>
						<option value="39" <c:if test="${'39' eq brokerCode}">selected="selected"</c:if>>키움증권</option>
						<option value="57" <c:if test="${'57' eq brokerCode}">selected="selected"</c:if>>하나증권</option>
						<option value="74" <c:if test="${'74' eq brokerCode}">selected="selected"</c:if>>한국IR협의회</option>
						<option value="79" <c:if test="${'79' eq brokerCode}">selected="selected"</c:if>>한국기술신용평가(주)</option>
						<option value="71" <c:if test="${'71' eq brokerCode}">selected="selected"</c:if>>한국기업데이터</option>
						<option value="43" <c:if test="${'43' eq brokerCode}">selected="selected"</c:if>>한국기업평가</option>
						<option value="67" <c:if test="${'67' eq brokerCode}">selected="selected"</c:if>>한국투자증권</option>
						<option value="65" <c:if test="${'65' eq brokerCode}">selected="selected"</c:if>>한양증권</option>
						<option value="16" <c:if test="${'16' eq brokerCode}">selected="selected"</c:if>>한화투자증권</option>
						<option value="38" <c:if test="${'38' eq brokerCode}">selected="selected"</c:if>>현대차증권</option>
					</select>
				</li>
				<c:if test="${'industry' eq searchGubun}"><!-- 산업분석 리포트 -->
				<li class="stype upjong"><div style="line-height:4px;">&nbsp;</div><div>업종 : </div></li>
				<li class="stype upjong">
					<select name="upjong" class="select" title="업종 선택">
						<option value="" <c:if test="${empty upjong}">selected="selected"</c:if>>선택</option>
						<option value="건설" <c:if test="${'건설' eq upjong}">selected="selected"</c:if>>건설</option>
						<option value="건자재" <c:if test="${'건자재' eq upjong}">selected="selected"</c:if>>건자재</option>
						<option value="광고" <c:if test="${'광고' eq upjong}">selected="selected"</c:if>>광고</option>
						<option value="금융" <c:if test="${'금융' eq upjong}">selected="selected"</c:if>>금융</option>
						<option value="기계" <c:if test="${'기계' eq upjong}">selected="selected"</c:if>>기계</option>
						<option value="휴대폰" <c:if test="${'휴대폰' eq upjong}">selected="selected"</c:if>>휴대폰</option>
						<option value="담배" <c:if test="${'담배' eq upjong}">selected="selected"</c:if>>담배</option>
						<option value="유통" <c:if test="${'유통' eq upjong}">selected="selected"</c:if>>유통</option>
						<option value="미디어" <c:if test="${'미디어' eq upjong}">selected="selected"</c:if>>미디어</option>
						<option value="바이오" <c:if test="${'바이오' eq upjong}">selected="selected"</c:if>>바이오</option>
						<option value="반도체" <c:if test="${'반도체' eq upjong}">selected="selected"</c:if>>반도체</option>
						<option value="보험" <c:if test="${'보험' eq upjong}">selected="selected"</c:if>>보험</option>
						<option value="석유화학" <c:if test="${'석유화학' eq upjong}">selected="selected"</c:if>>석유화학</option>
						<option value="섬유의류" <c:if test="${'섬유의류' eq upjong}">selected="selected"</c:if>>섬유의류</option>
						<option value="소프트웨어" <c:if test="${'소프트웨어' eq upjong}">selected="selected"</c:if>>소프트웨어</option>
						<option value="운수창고" <c:if test="${'운수창고' eq upjong}">selected="selected"</c:if>>운수창고</option>
						<option value="유틸리티" <c:if test="${'유틸리티' eq upjong}">selected="selected"</c:if>>유틸리티</option>
						<option value="은행" <c:if test="${'은행' eq upjong}">selected="selected"</c:if>>은행</option>
						<option value="인터넷포탈" <c:if test="${'인터넷포탈' eq upjong}">selected="selected"</c:if>>인터넷포탈</option>
						<option value="자동차" <c:if test="${'자동차' eq upjong}">selected="selected"</c:if>>자동차</option>
						<option value="전기전자" <c:if test="${'전기전자' eq upjong}">selected="selected"</c:if>>전기전자</option>
						<option value="제약" <c:if test="${'제약' eq upjong}">selected="selected"</c:if>>제약</option>
						<option value="조선" <c:if test="${'조선' eq upjong}">selected="selected"</c:if>>조선</option>
						<option value="종이" <c:if test="${'종이' eq upjong}">selected="selected"</c:if>>종이</option>
						<option value="증권" <c:if test="${'증권' eq upjong}">selected="selected"</c:if>>증권</option>
						<option value="철강금속" <c:if test="${'철강금속' eq upjong}">selected="selected"</c:if>>철강금속</option>
						<option value="타이어" <c:if test="${'타이어' eq upjong}">selected="selected"</c:if>>타이어</option>
						<option value="통신" <c:if test="${'통신' eq upjong}">selected="selected"</c:if>>통신</option>
						<option value="항공운송" <c:if test="${'항공운송' eq upjong}">selected="selected"</c:if>>항공운송</option>
						<option value="홈쇼핑" <c:if test="${'홈쇼핑' eq upjong}">selected="selected"</c:if>>홈쇼핑</option>
						<option value="음식료" <c:if test="${'음식료' eq upjong}">selected="selected"</c:if>>음식료</option>
						<option value="여행" <c:if test="${'여행' eq upjong}">selected="selected"</c:if>>여행</option>
						<option value="게임" <c:if test="${'게임' eq upjong}">selected="selected"</c:if>>게임</option>
						<option value="IT" <c:if test="${'IT' eq upjong}">selected="selected"</c:if>>IT</option>
						<option value="에너지" <c:if test="${'에너지' eq upjong}">selected="selected"</c:if>>에너지</option>
						<option value="해운" <c:if test="${'해운' eq upjong}">selected="selected"</c:if>>해운</option>
						<option value="지주회사" <c:if test="${'지주회사' eq upjong}">selected="selected"</c:if>>지주회사</option>
						<option value="디스플레이" <c:if test="${'디스플레이' eq upjong}">selected="selected"</c:if>>디스플레이</option>
						<option value="화장품" <c:if test="${'화장품' eq upjong}">selected="selected"</c:if>>화장품</option>
						<option value="자동차부품" <c:if test="${'자동차부품' eq upjong}">selected="selected"</c:if>>자동차부품</option>
						<option value="교육" <c:if test="${'교육' eq upjong}">selected="selected"</c:if>>교육</option>
						<option value="기타" <c:if test="${'기타' eq upjong}">selected="selected"</c:if>>기타</option>
					</select>
				</li>	
				</c:if>
			
				<c:if test="${'company' eq searchGubun}">
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
					<input type="button" class="s_btn"  onClick="fncSelectDataList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
				</li>
			</ul>
		</div>
		
		<div class="button_box">
			<ul style="margin-bottom: 0px;">
				<!-- 검색키워드 및 조회버튼 -->
				<li style="border: 0px solid #d2d2d2;">
					<%-- <input type="button" class="s_btn" onClick="fncSelectList()" value="목록" title="목록 <spring:message code="input.button" />" /> --%>
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
		<c:forEach var="subject" items="${titleList}" varStatus="substatus">
			<c:set var="col_style" value=""/>
			<c:if test="${subject.title eq '분류'}">		<c:set var="col_style" value="10"/></c:if>
			<c:if test="${subject.title eq '증권사'}">	<c:set var="col_style" value="10"/></c:if>
			<c:if test="${subject.title eq '첨부'}">		<c:set var="col_style" value="10"/></c:if>
			<c:if test="${subject.title eq '작성일'}">	<c:set var="col_style" value="10"/></c:if>
			<c:if test="${subject.title eq '조회수'}">	<c:set var="col_style" value="10"/></c:if>
			<col <c:if test="${not empty col_style}">style="width: ${col_style}%;"</c:if>>
		</c:forEach>
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th></th>
		<th>순번</th>
		<c:forEach var="subject" items="${titleList}" varStatus="substatus">
			<th>${subject.title}</th>
		</c:forEach>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(researchList) == 0}">
		<tr>
			<td colspan="${fn:length(titleList)+2}"><spring:message code="common.nodata.msg" /></td>
		</tr>
	</c:if>
	<c:forEach var="item" items="${researchList}" varStatus="status">
	<tr>
		<td><input type="checkbox" name="chk_report" value=""/></td>
		<td><c:out value="${item.nuidx}"/></td>
		<c:forEach var="subject" items="${item.detailList}" varStatus="substatus">
			<td>
				<c:choose>
					<c:when test="${not empty subject.dhref}">
							<c:choose>
								<c:when test="${subject.idx eq '0'}">
									<a href="https://finance.naver.com${subject.dhref}" target="_blank">
										<c:out value="${subject.dcn}"/>
									</a>
								</c:when>
								<c:when test="${subject.dclass eq 'file'}">
									<a href="${subject.dhref}" target="_blank">
										<img src="/images/egovframework/com/cmm/down_pdf.gif" alt="pdf" >
									</a>
								</c:when>
								<c:otherwise>
									<a href="https://finance.naver.com/research/${subject.dhref}" target="_blank">
										<c:out value="${subject.dcn}"/>
									</a>
								</c:otherwise>
							</c:choose>
					</c:when>
					<c:otherwise>
						<c:out value="${subject.dcn}"/>
					</c:otherwise>
				</c:choose>
			</td>
		</c:forEach>
	
	</tr>
	</c:forEach>
	</tbody>
	</table>
	<c:if test="${!empty paginationInfo}">
		<!-- paging navigation -->
		<div class="pagination">
			<ul><ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="fncSelectDataList"/></ul>
		</div>
	</c:if>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
