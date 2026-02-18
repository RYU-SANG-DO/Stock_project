<!DOCTYPE html>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
 /**
  * @Class Name : stockComThemeListPop.jsp
  * @Description : 상튀 테마 ID 조회 팝업
  * @Modification Information
  * @
  * @ 수정일					수정내용
  * @ ---------------  ---------------------------
  * @ 2026.01.26			최초 생성
  *
  *
  */
  /* Image Path 설정 */
  String imagePath_icon   = "/images/egovframework/com/sym/mnu/mpm/icon/";
  String imagePath_button = "/images/egovframework/com/sym/mnu/mpm/button/";

%>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<title><spring:message code="comSymMnuMpm.menuMvmn.title"/></title><!-- 메뉴이동 -->
<link href="<c:url value="/css/egovframework/com/com.css"/>" rel="stylesheet" type="text/css">
<link href="<c:url value="/css/egovframework/com/button.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
var imgpath = "<c:url value='/images/egovframework/com/cmm/utl/'/>";
</script>
<script type="text/javaScript" src="<c:url value='/js/egovframework/com/sym/mnu/mpm/EgovMenuList.js' />" /></script>
<script type="text/javaScript">
<!--

/* ********************************************************
 * 상세내역조회 함수
 ******************************************************** */
function choiceNodes(nodeNum) {
	var nodeValues = treeNodes[nodeNum].split("|");
	parent.document.menuManageVO.upThemeId.value = nodeValues[0];
	parent.document.menuManageVO.upThemeNm.value = nodeValues[2];
	parent.$('.ui-dialog-content').dialog('close');
}
/* ********************************************************
 * 조회 함수
 ******************************************************** */
function selectMenuListTmp() {
	document.menuListForm.req_RetrunPath.value = "<c:url value='/sym/mnu/mpm/EgovMenuMvmn'/>";
    document.menuListForm.action = "<c:url value='/sym/mnu/mpm/EgovMenuListSelectTmp.do'/>";
    document.menuListForm.submit();
}
-->
</script>
</head>
<body>
<form name="searchUpperMenuIdForm" action ="<c:url value='/sym/mnu/mpm/EgovMenuListSelectTmp.do'/>" method="post">
<div style="visibility:hidden;display:none;"><input name="iptSubmit" type="submit" value="전송" title="전송"></div>
<input type="hidden" name="req_RetrunPath" value="/sym/mnu/mpm/EgovMenuMvmn">
<c:forEach var="result" items="${resultList}" varStatus="status" >
<input type="hidden" name="tmp_themeNmVal" value="${result.themeId}|${result.upThemeId}|${result.themeName}|${result.themeOrdr}|${result.themeDesc}|">
</c:forEach>

<div class="wTableFrm" style="width:580px">
	<!-- 타이틀 -->
	<h2>테마 내역</h2><!-- 메뉴이동 -->

	<!-- 등록폼 -->
	<div style="clear:both;"></div>
</div>

<DIV id="main" style="display:">

<table width="570" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="10">&nbsp;</td>
  </tr>
</table>

<table width="570" cellpadding="8" class="table-line">
  <tr>
    <td>
 		<div class="tree" style="width:400px;">
			<script language="javascript" type="text/javaScript">

			var Tree = new Array;
			
			if ( typeof document.searchUpperMenuIdForm.req_RetrunPath == "object"
					&& typeof document.searchUpperMenuIdForm.tmp_themeNmVal == "object"
					&& document.searchUpperMenuIdForm.tmp_themeNmVal.length > 0 ) {
				for (var j = 0; j < document.searchUpperMenuIdForm.tmp_themeNmVal.length; j++) {
					Tree[j] = document.searchUpperMenuIdForm.tmp_themeNmVal[j].value;
			    }
				createTree(Tree, true);
            }else{
            	alert("<spring:message code="comSymMnuMpm.menuMvmn.validate.alert.menu"/>");
            	window.close();
            }
           </script>
		</div>
    </td>
  </tr>
</table>
</DIV>

</form>
</body>
</html>

