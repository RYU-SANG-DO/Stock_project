<!DOCTYPE html>

<%--
 /**
  * @Class Name : EgovMenuList.jsp
  * @Description : 메뉴목록 화면
  * @Modification Information
  * @
  * @ 수정일               수정자             수정내용
  * @ ----------   --------   ---------------------------
  * @ 2009.03.10   이용               최초 생성
  *   2013.10.04   이기하            메뉴트리 위치 변경
  *   2018.09.10   신용호            표준프레임워크 v3.8 개선
  *
  *  @author 공통서비스 개발팀 이용
  *  @since 2009.03.10
  *  @version 1.0
  *  @see
  *
  */

  /* Image Path 설정 */
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%
//String imagePath_icon   = "/images/egovframework/com/sym/mnu/mpm/icon/";
//String imagePath_button = "/images/egovframework/com/sym/mnu/mpm/button/";
%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="comSymMnuMpm.menuList.title" /></title>
<link href="<c:url value="/css/egovframework/com/com.css"/>" rel="stylesheet" type="text/css">
<link href="<c:url value="/css/egovframework/com/button.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value="/validator.do" />"></script>
<script type="text/javascript">
var imgpath = "<c:url value='/images/egovframework/com/cmm/utl/'/>";
</script>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/egovframework/com/cmm/jqueryui.css' />">
<script src="<c:url value='/js/egovframework/com/cmm/jquery.js' />"></script>
<script src="<c:url value='/js/egovframework/com/cmm/jqueryui.js' />"></script>

<script type="text/javaScript" src="<c:url value='/js/egovframework/com/sym/mnu/mpm/EgovMenuList.js' />"></script>
<script type="text/javaScript">
$(function(){
	$('#popupUpperMenuId').click(function (e) {
    	e.preventDefault();
        //var page = $(this).attr("href");
        var pagetitle = $(this).attr("title");
        var page = "<c:url value='/stock/com/theme/selectComThemeCodeListPop.do'/>";
        var $dialog = $('<div style="overflow:hidden;padding: 0px 0px 0px 0px;"></div>')
        .html('<iframe style="border: 0px; " src="' + page + '" width="100%" height="100%"></iframe>')
        .dialog({
        	autoOpen: false,
            modal: true,
            width: 400,
            height: 550,
            title: pagetitle
    	});
    	$dialog.dialog('open');
	});
	
});

/* 노드 클릭 시 상세 정보 바인딩 */
function choiceNodes(nodeNum) {
	console.log(nodeNum);
	var nodeValues = treeNodes[nodeNum].split("|");
	console.log(nodeValues);
	var nodeId = nodeValues[0];
    var f = document.forms.menuManageVO; // form 이름
    // 트리 데이터에서 해당 ID의 정보를 찾아 폼에 채움 (구현 필요)
    // EgovMenuList.js의 로직을 따르거나 아래와 같이 직접 구현
    
    // 여기서는 예시로 hidden input을 순회하며 찾음
    var nodes = document.getElementsByName("tmp_themeId");
    for(var i=0; i<nodes.length; i++){
        if(nodes[i].value == nodeId){
            f.themeId.value = document.getElementsByName("tmp_themeId")[i].value;
            f.themeName.value = document.getElementsByName("tmp_themeName")[i].value;
            f.upThemeId.value = document.getElementsByName("tmp_upThemeId")[i].value;
            f.themeLevel.value = document.getElementsByName("tmp_themeLevel")[i].value;
            f.themeDesc.value = document.getElementsByName("tmp_themeDesc")[i].value;
            f.themeCode.value = document.getElementsByName("tmp_themeCode")[i].value;
            f.themeOrdr.value = document.getElementsByName("tmp_themeOrdr")[i].value;
            f.cmd.value = "update"; // 수정 모드
            return;
        }
    }
}

/* 입력 초기화 (신규 등록 모드) */
function fn_init_regist() {
    var f = document.forms.menuManageVO;
    f.themeId.value = "";
    f.themeName.value = "";
    f.upThemeId.value = "";
    f.themeLevel.value = "";
    f.themeDesc.value = "";
    f.themeCode.value = "";
    f.themeOrdr.value = "";
    f.cmd.value = "insert";
}

/* 등록/수정/삭제 처리 */
function fn_process(cmd) {
    var f = document.forms.menuManageVO;
    if(cmd == 'delete'){
        if(!confirm("정말 삭제하시겠습니까?")) return;
        f.cmd.value = 'delete';
    } else {
    	if(!fn_validatorThemeList()){return;}
        if(f.cmd.value == 'insert') {
             if(!confirm("등록하시겠습니까?")) return;
        } else {
             if(!confirm("수정하시겠습니까?")) return;
        }
    }
    f.action = "<c:url value='/stock/com/theme/stockComThemeCodeRegist.do'/>";
    f.submit();
}

/* ********************************************************
 * 입력값 validator 함수
 ******************************************************** */
function fn_validatorThemeList() {

	//if(document.menuManageVO.themeId.value == ""){alert("메뉴번호는 Not Null 항목입니다."); return false;}
	if(!checkNumber(document.menuManageVO.themeId.value)){alert("메뉴번호는 숫자만 입력 가능합니다."); return false;} 

	if(document.menuManageVO.themeOrdr.value == ""){alert("메뉴순서는 Not Null 항목입니다."); return false;}
	if(!checkNumber(document.menuManageVO.themeOrdr.value)){alert("메뉴순서는 숫자만 입력 가능합니다."); return false;}

	if(document.menuManageVO.upThemeId.value == ""){alert("상위메뉴번호는 Not Null 항목입니다."); return false;}
	if(!checkNumber(document.menuManageVO.upThemeId.value)){alert("상위메뉴번호는 숫자만 입력 가능합니다."); return false;}

    return true;
}

/* ********************************************************
 * 필드값 Number 체크 함수
 ******************************************************** */
function checkNumber(str) {
    var flag=true;
    if (str.length > 0) {
        for (i = 0; i < str.length; i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9') {
                flag=false;
            }
        }
    }
    return flag;
}
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
</script>
</head>
<body>
<noscript class="noScriptTitle"><spring:message code="common.noScriptTitle.msg" /></noscript>
<div style="display:none">
    <c:forEach var="result" items="${resultList}">
        <input type="hidden" name="tmp_themeId" value="${result.themeId}" />
        <input type="hidden" name="tmp_themeOrdr" value="${result.themeOrdr}" />
        <input type="hidden" name="tmp_upThemeId" value="${result.upThemeId}" />
        <input type="hidden" name="tmp_themeName" value="${result.themeName}" />
        <input type="hidden" name="tmp_themeLevel" value="${result.themeLevel}" />
        <input type="hidden" name="tmp_themeDesc" value="${result.themeDesc}" />
        <input type="hidden" name="tmp_themeCode" value="${result.themeCode}" />
    </c:forEach>
</div>
<div id="content" style="width:1000px">
    <div class="board">
        <h1>주식 테마/업종 관리</h1>
    </div>

    <div style="float:left; width:30%; height:500px; overflow:auto; border:1px solid #ccc; padding:10px;">
        <div id="treeView">
        	<script>
        	 	var Tree = new Array;
	        	<c:forEach var="result" items="${resultList}" varStatus="status">
		            Tree[${status.index}] = "${result.themeId}|${result.upThemeId}|${result.themeName}|${result.themeDesc}|${result.themeLevel}|${result.themeCode}|";
		        </c:forEach>
        
        		createTree(Tree, true);
        	</script>
        </div>
    </div>

    <div style="float:right; width:67%;">
        <form name="menuManageVO" action ="<c:url value='/stock/com/theme/stockComThemeCodeRegist.do'/>" method="post">
            <input type="hidden" name="cmd" value="insert" />

            <table class="board_list" summary="테마 정보 입력">
                <caption>테마 정보</caption>
                <colgroup>
                    <col style="width:30%" />
                    <col style="width:70%" />
                </colgroup>
                <tr>
                    <th>테마 ID</th>
                    <td class="left"><input type="text" name="themeId" id="themeId" size="20" class="input_txt" readonly="readonly"/></td>
                </tr>
                <tr>
                    <th><span class="pilsu">*</span>상위 테마 ID</th>
                    <td class="left">
                    	<input type="text" name="upThemeId" size="20" class="input_txt" />
                    	<a id="popupUpperMenuId" href="#none" target="_blank" title="상위 테마 ID" style="selector-dummy:expression(this.hideFocus=false);">
                    		<img src="<c:url value='/images/egovframework/com/cmm/icon/search2.gif' />" alt='' width="15" height="15" />(테마선택 검색)
	         			</a><!-- 메뉴선택 검색 -->
                    </td>
                </tr>
                <tr>
                    <th><span class="pilsu">*</span>테마 명</th>
                    <td class="left"><input type="text" name="themeName" id="themeName" size="30" class="input_txt" /></td>
                </tr>
                <tr>
                    <th><span class="pilsu">*</span>순번</th>
                    <td class="left"><input type="text" name="themeOrdr" id="themeOrdr" size="5" class="input_txt" /></td>
                </tr>
                <tr>
                    <th>테마 레벨</th>
                    <td class="left"><input type="text" name="themeLevel" id="themeLevel" size="5" class="input_txt" /></td>
                </tr>
                <tr>
                    <th>테마 코드</th>
                    <td class="left"><input type="text" name="themeCode" id="themeCode" size="5" class="input_txt" /></td>
                </tr>
                <tr>
                    <th>설명</th>
                    <td><textarea name="themeDesc" rows="5" cols="50" class="textarea"></textarea></td>
                </tr>
            </table>
            
            <div style="margin-top:10px; text-align:right;">
                <button type="button" onclick="fn_init_regist();">초기화(신규)</button>
                <button type="button" onclick="fn_process('insert');">등록</button>
                <button type="button" onclick="fn_process('update');">수정</button>
                <button type="button" onclick="fn_process('delete');">삭제</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>

