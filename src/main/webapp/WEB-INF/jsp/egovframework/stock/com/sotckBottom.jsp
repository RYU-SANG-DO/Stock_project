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
$(function(){
	 //Loading();
	 //setTimeout("closeLoading()", 3000);
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
