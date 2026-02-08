<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- <div class="preloader"></div> -->
<div id="map" class="loadingImg"></div>
<style type="text/css">
div.preloader {
   position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   z-index: 9999;
   background-image: url('/images/loading.gif');
   background-repeat: no-repeat; 
   background-color: #FFF;
   background-position: center;
   opacity: 0.5;
}
</style>
<script type="text/javaScript" defer="defer">
function getByteLength(str) {
    let byteCount = 0;
    for (let i = 0; i < str.length; i++) {
        const charCode = str.charCodeAt(i);
        if (charCode <= 0x7F) byteCount += 1;
        else if (charCode <= 0x7FF) byteCount += 2;
        else byteCount += 3;
    }
    return byteCount;
}

$(function(){
	 //Loading();
	 //setTimeout("closeLoading()", 3000);
	if(quill != null){	
		//3. text-change 이벤트 리스너 등록
		 quill.on('text-change', function() {
		     const maxByte = 3900;
		     // 태그 제외 순수 텍스트만 가져오기 (마지막 줄바꿈 제외)
		     const text = quill.getText().trim(); 
		     console.log(text)
		     const currentByte = getByteLength(text);

		     document.getElementById('byte-count').innerText = currentByte+" Byte";

		     // 바이트 초과 시 처리
		     if (currentByte > maxByte) {
		         alert("최대 3,900바이트를 초과할 수 없습니다.");
		         
		         // 초과 시 마지막 입력을 취소하거나 텍스트를 자르는 로직
		         // 예: 4000바이트까지만 남기고 자르기 (복잡한 서식 유지 시 주의 필요)
		         const delta = quill.getContents();
		         // 실제로는 입력을 막거나 경고를 주는 방식을 권장합니다.
		     }
		 });
	}
});    
    
function Loading() {
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    var mask       = "<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
     
    loadingImg +=" <div id='loadingImg'>";
    loadingImg +=" <img src='${pageContext.request.contextPath}/images/loading.gif' style='position:absolute; text-align:center; display:block; top:50%; left:50%;'/>";
    loadingImg += "</div>";  
 
    $('body').append(mask)
 
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
    
    $('#mask').show();
  
    $('.loadingImg').append(loadingImg);
    $('#loadingImg').show();
}    

function closeLoading() {
	  $('#mask, #loadingImg').hide();
	  $('#mask, #loadingImg').remove(); 
}
	
</script>
</body>
</html>
