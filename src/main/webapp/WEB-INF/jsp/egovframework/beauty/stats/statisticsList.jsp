<%
 /**
  * @Description : 미용실 통계 화면
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
<%-- <script src="<c:url value='/webjars/chartjs/4.4.3/chart.umd.js'/>"></script> --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script type="text/javaScript">
$(function(){	
	
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
	 $("#searchDate").datepicker(  
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
function fncSelectList(){
	 loadAllStats();
}



function press() {
	//Loading();
    if (event.keyCode==13) {
    	fncSelectList();
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

//전역 변수로 차트 객체를 선언하여 상태를 유지합니다.
let monthlyChart = null;
let styleChart = null;
function loadAllStats() {
	var searchYear = document.listForm.searchYear.value;
	var searchMonth = document.listForm.searchMonth.value;
	var searchStyle = document.listForm.searchStyle.value;
	var searchPmType = document.listForm.searchPmType.value;
    $.ajax({
        url: "<c:url value='/beauty/stats/getChartData.do'/>",
        type: "GET",
        data: { 
        		year: searchYear,
        		month: searchMonth,
        		style: searchStyle,
        		pmType: searchPmType
        	},
        success: function(response) {
        	
        	// 1. 전체 금액 표시 로직 추가
            const formattedPrice = response.totalYearPrice.toLocaleString(); // 1,000,000 형식
            $('#selectedYear').text(searchYear);
            $('#totalPriceDisplay').text(formattedPrice);
        	
            // response.monthly -> 월별 데이터
            // response.style   -> 스타일 데이터

            // --- 1. 월별 매출 차트 (Line) ---
            const ctx1 = document.getElementById('monthlySalesChart').getContext('2d');
         // 기존에 생성된 차트가 있다면 파괴(destroy)합니다.
            if (monthlyChart !== null) {
                monthlyChart.destroy();
            }
            monthlyChart = new Chart(ctx1, {
                type: 'line',
                data: {
                    labels: response.monthly.map(d => d.dateLabel+"월"),
                    datasets: [{
                        label: searchYear + '년 매출액',
                        data: response.monthly.map(d => d.statValue),
                        borderColor: '#36a2eb',
                        fill: false
                    }]
                }
            });

            // --- 2. 스타일별 비중 차트 (Pie) ---
            const ctx2 = document.getElementById('styleTypeChart').getContext('2d');
         // 기존 차트 파괴
            if (styleChart !== null) {
                styleChart.destroy();
            }
            styleChart = new Chart(ctx2, {
                type: 'pie',
                data: {
                    labels: response.style.map(d => d.styleTypeNm),
                    datasets: [{
                        data: response.style.map(d => d.totalPrice),
                        backgroundColor: ['#ff6384', '#36a2eb', '#ffce56', '#4bc0c0']
                    }]
                }
            });
        },
        error: function(err) {
            alert("데이터를 불러오는 중 오류가 발생했습니다.");
        }
    });
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

.chart-container {
        display: flex;
        justify-content: space-around;
        flex-wrap: wrap;
        gap: 20px;
        padding: 20px;
    }
    .chart-box {
        width: 45%;  /* 한 줄에 두 개 배치 */
        min-width: 400px;
        border: 1px solid #ddd;
        padding: 15px;
        border-radius: 8px;
        background-color: #fff;
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
				<li><div style="line-height:4px;">&nbsp;</div><div>년도 : </div></li>
				<li>
					<select name="searchYear" id="searchYear" class="select" title="검색구분">						
						<%-- <option value="" <c:if test="${empty searchYear}">selected="selected"</c:if>>선택</option> --%>
						<c:forEach items="${yearList}" var="info" varStatus="status">
							<option value="${info.pmYear}" <c:if test="${searchYear eq info.pmYear}">selected="selected"</c:if>>${info.pmYear}</option>
						</c:forEach>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>월 : </div></li>
				<li>
					<select name="searchMonth" id="searchMonth" class="select" title="검색구분">						
						<option value="" <c:if test="${empty searchMonth}">selected="selected"</c:if>>선택</option>
						<c:forEach var="m" begin="01" end="12" varStatus="status">
							<option value="${m}" <c:if test="${searchMonth eq m}">selected="selected"</c:if>>${m}</option>
						</c:forEach>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>결재타입 : </div></li>
				<li>
					<select name="searchPmType" id="searchPmType" class="select" title="결재타입">
						<option value="" <c:if test="${empty searchPmType}">selected="selected"</c:if>>선택</option>
							<c:forEach items="${pmtypeList}" var="info" varStatus="status">
								<option value="${info.code}" <c:if test="${searchPmType eq info.code}">selected="selected"</c:if>>${info.codeNm}</option>
							</c:forEach>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>스타일 : </div></li>
				<li>
					<select name="searchStyle" id="searchStyle" class="select" title="결재타입">
						<option value="" <c:if test="${empty searchStyle}">selected="selected"</c:if>>선택</option>
							<c:forEach items="${styleList}" var="info" varStatus="status">
								<option value="${info.code}" <c:if test="${searchStyle eq info.code}">selected="selected"</c:if>>${info.codeNm}</option>
							</c:forEach>
					</select>
				</li>
				<li><div style="line-height:4px;">&nbsp;</div><div>검색일자 : </div></li>
				<li>
					<input class="s_input" name="searchDate" id="searchDate" type="text"  size="10" style="width: auto;" title="검색일자 <spring:message code="input.input" />" value='<c:out value="${searchDate}"/>'>
				</li>
				<%-- <li class="stype keyword" style="border: 0px solid #d2d2d2;">
					<input class="s_input" name="keyword" id="keyword" type="text"  size="10" style="width: 150px;" title="<spring:message code="title.search" /> <spring:message code="input.input" />" value='<c:out value="${keyword}"/>'>
				</li> --%>
			
				<li  style="border: 0px solid #d2d2d2;">
					<input type="button" class="s_btn"  onClick="fncSelectList('1');" value="<spring:message code="button.inquire" />" title="<spring:message code="title.inquire" /> <spring:message code="input.button" />" />
				</li>
			</ul>
		</div>
		
	</form>
	
	<div class="chart-container">
    <div class="chart-box">
        <h3>월별 매출 현황</h3>
	    <p style="font-size: 1.2em; font-weight: bold; color: #2c3e50;">
	        <span id="selectedYear">2026</span>년 총 매출액: 
	        <span id="totalPriceDisplay" style="color: #e74c3c;">0</span>원
	    </p>
        <canvas id="monthlySalesChart"></canvas>
    </div>

    <div class="chart-box">
        <h3>스타일별 인기 순위</h3>
        <canvas id="styleTypeChart"></canvas>
    </div>
</div>
</div>

<jsp:include page="/WEB-INF/jsp/egovframework/stock/com/sotckBottom.jsp" flush="true" />
