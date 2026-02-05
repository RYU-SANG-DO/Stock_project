<%
 /**
  * @Class Name : StockDartController.java
  * @Description : 공시정보 > 기업개황 List 화면
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
     	let corpCls = document.listForm.corp_cls.value;
     	let gubun ="";
     	if(corpCls != ""){
     		if(corpCls == "Y"){
     			gubun ="KOSPI";
     		}else if(corpCls == "K"){
     			gubun ="KOSDAQ";
     		}
     	}
         //var page = $(this).attr("href");
         var pagetitle = $(this).attr("title");
         var page = "<c:url value='/stock/data/selectStocksPopList.do'/>?gubun="+gubun;
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
//검색
function fncSelectDataList(pageNo){
	if($("#bgn_de").val() == ""){
		alert("검색기간에 시작일을 선택해 주세요.");
		return;
	}else if($("#end_de").val() == ""){	
		alert("검색기간에 종료일을 선택해 주세요.");
		return;
	}else if($("#stock_code").val() == ""){	
		alert("종목을 선택해 주세요.");
		return;
	}
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
			<li><div style="line-height:4px;">&nbsp;</div><div>법인구분 : </div></li>
			<li>
				<select name="corp_cls" class="select" title="법인구분">
					<option value="" <c:if test="${empty corp_cls}">selected="selected"</c:if>>전체</option>
					<option value="Y" <c:if test="${'Y' eq corp_cls}">selected="selected"</c:if>>유가</option>
					<option value="K" <c:if test="${'K' eq corp_cls}">selected="selected"</c:if>>코스닥</option>
					<option value="N" <c:if test="${'N' eq corp_cls}">selected="selected"</c:if>>코넥스</option>
					<option value="E" <c:if test="${'E' eq corp_cls}">selected="selected"</c:if>>기타</option>
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
		<col style="width: 10%;">
		<col>
	</colgroup>
	<thead>
	<tr class="algin-center">
		<th>순번</th>
		<th>명칭</th>
		<th>내용</th>
	</tr>
	</thead>
	<tbody class="ov">
	<c:if test="${fn:length(list) == 0}">
		<tr>
			<td colspan="3"><spring:message code="common.nodata.msg" /></td>
		</tr>
	</c:if>
	<c:forEach var="item" items="${list}" varStatus="status">
		<tr>
			<td>1 </td>	
			<td>에러 및 정보 코드</td>	
			<td style="text-align: left;"> : <c:out value="${item.status}"/></td>
		</tr>
		<tr>
			<td>2 </td>	
			<td>에러 및 정보 메시지</td>	
			<td style="text-align: left;"> : <c:out value="${item.message}"/></td>
		</tr>
		<tr>
			<td>3 </td>	
			<td>정식명칭</td>				
			<td style="text-align: left;"> : <c:out value="${item.corp_name}"/></td>
		</tr>
		<tr>
			<td>4 </td>	
			<td>영문명칭</td>				
			<td style="text-align: left;"> : <c:out value="${item.corp_name_eng}"/></td>
		</tr>
		<tr>
			<td>5 </td>	
			<td>종목명</td>				
			<td style="text-align: left;"> : <c:out value="${item.stock_name}"/></td>
		</tr>
		<tr>
			<td>6 </td>	
			<td>종목코드</td>				
			<td style="text-align: left;"> : <c:out value="${item.stock_code}"/></td>
		</tr>
		<tr>
			<td>7 </td>	
			<td>대표자명</td>				
			<td style="text-align: left;"> : <c:out value="${item.ceo_nm}"/></td>
		</tr>
		<tr>
			<td>8 </td>	
			<td>법인구분</td>				
			<td style="text-align: left;"> : 
					<c:if test="${'Y' eq item.corp_cls}">유가(코스피)</c:if>
					<c:if test="${'K' eq item.corp_cls}">코스닥</c:if>
					<c:if test="${'M' eq item.corp_cls}">코넥스</c:if>
					<c:if test="${'E' eq item.corp_cls}">기타</c:if>
			</td>
		</tr>
		<tr>
			<td>9 </td>	
			<td>법인등록번호</td>	
			<td style="text-align: left;"> : <c:out value="${item.jurir_no}"/></td></tr>
		<tr>
			<td>10</td>	
			<td>사업자등록번호</td>		
			<td style="text-align: left;"> : <c:out value="${item.bizr_no}"/></td>
		</tr>
		<tr>
			<td>11</td>	
			<td>주소</td>					
			<td style="text-align: left;"> : <c:out value="${item.adres}"/></td>
		</tr>
		<tr>
			<td>12</td>	
			<td>홈페이지</td>				
			<td style="text-align: left;"> : <c:out value="${item.hm_url}"/></td>
		</tr>
		<tr>
			<td>13</td>	
			<td>IR홈페이지</td>			
			<td style="text-align: left;"> : <c:out value="${item.ir_url}"/></td>
		</tr>
		<tr>
			<td>14</td>	
			<td>전화번호</td>				
			<td style="text-align: left;"> : <c:out value="${item.phn_no}"/></td>
		</tr>
		<tr>
			<td>15</td>	
			<td>팩스번호</td>				
			<td style="text-align: left;"> : <c:out value="${item.fax_no}"/></td>
		</tr>
		<tr>
			<td>16</td>	
			<td>업종코드</td>				
			<td style="text-align: left;"> : <c:out value="${item.induty_code}"/></td>
		</tr>
		<tr>
			<td>17</td>	
			<td>설립일</td>				
			<td style="text-align: left;"> : <c:out value="${item.est_dt}"/></td>
		</tr>
		<tr>
			<td>18</td>	
			<td>결산월</td>				
			<td style="text-align: left;"> : <c:out value="${item.acc_mt}"/></td>
		</tr>
	</c:forEach>
	</tbody>
	</table>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
