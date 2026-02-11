<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!-- <div class="preloader"></div> -->
<div id="map" class="loadingImg"></div>
<div id="chartModal" class="modal" tabindex="-1" role="dialog" style="display:none; position:fixed; z-index:9999; left:0; top:0; width:100%; height:100%; background-color:rgba(0,0,0,0.7);">
    <div class="modal-content" style="background-color:#fff; margin:5% auto; padding:20px; width:80%; max-width:800px; border-radius:10px; position:relative;">
        <span onclick="closeModal()" style="position:absolute; right:20px; top:10px; font-size:28px; font-weight:bold; cursor:pointer;">&times;</span>
        <h3 id="modalTitle" style="margin-bottom:15px;">종목 차트</h3>
        <div id="modalBody" style="text-align:center;">
            <img id="bigChartImg" src="" style="width:100%; height:auto; border:1px solid #eee;">
        </div>
    </div>
</div>
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
var quill = null;
//에디터의 HTML 구조를 직접 설정
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
	if ($('#editor').length > 0) {
		quill = new Quill('#editor', {
		    modules: {
		        toolbar: {
		        			container: [
		        			   				[{ 'font': [] }],                                 	// 폰트 설정
					        			    [{ 'header': [1, 2, 3, 4, 5, 6, false] }],	// 텍스트 크기 설정
					        			    ['bold', 'italic', 'underline', 'strike'],        	// toggled buttons
					        			    [{ 'color': [] }, { 'background': [] }],       // dropdown with defaults from theme
					        			    [{ 'align': [] }],                                	// 텍스트 정렬
					        			    [{ 'list': 'ordered'}, { 'list': 'bullet' }],     	// 리스트 항목
					        			    [{ 'indent': '-1'}, { 'indent': '+1' }],         // outdent/indent
					        			    ['blockquote', 'code-block'],                   // 코드블럭, 인용 문구
					        			    ['link', 'image'],                                		// 링크, 이미지 추가
					        			    ['clean']                                         		// remove formatting button
					        			],
							handlers: {
					                        image: imageHandler 
					                    }
					        }
		    },
		    theme: 'snow'                                     // 테마는 snow로 설정
		});
	 //Loading();
	 //setTimeout("closeLoading()", 3000);
		//quill.root.innerHTML = document.getElementById('userSummary').value;
	     document.getElementById('byte-count').innerText = getByteLength(quill.getText().trim())+" Byte";
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

function imageHandler() {
    // 1. 숨겨진 input 생성하여 파일 선택창 띄우기
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.setAttribute('accept', 'image/*');
    input.click();

    input.onchange = async () => {
        const file = input.files[0];
        const formData = new FormData();
        formData.append('imageFile', file); // Java에서 받을 파라미터명

        // 2. 서버로 AJAX 업로드 (jQuery 사용)
        $.ajax({
            type: 'POST',
            url: "<c:url value='/stock/com/uploadEditorImageAjax.do'/>", // 서버 업로드 URL
            data: formData,
            processData: false,
            contentType: false,
            dataType : 'json',
            success: function(data) {
            	if(data.uploaded == 1) {
                    const range = quill.getSelection();
                    // 서버에서 보낸 'url' 키값을 사용하여 에디터에 삽입
                    quill.insertEmbed(range.index, 'image', data.url);
                } else {
                    alert("업로드 실패: " + data.error);
                }
            },
            error: function() {
                alert("이미지 업로드 중 오류가 발생했습니다.");
            }
        });
    };
}
    
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
	
function openChartModal(sCode, type) {
    let typeName = (type === 'day') ? '일봉' : (type === 'week') ? '주봉' : '월봉';
    
    // 1. 제목 설정
    $("#modalTitle").text(sCode + " - " + typeName + " 차트");
    
    // 2. 네이버 캔들차트 대형 이미지 URL 설정
    // 기존 이미지보다 더 상세한 정보를 담은 캔들 URL을 사용합니다.
    let chartUrl = "https://ssl.pstatic.net/imgfinance/chart/item/candle/" + type + "/" + sCode + ".png";
    
    // 3. 이미지 로딩 (캐시 방지 타임스탬프 포함)
    $("#bigChartImg").attr("src", chartUrl + "?t=" + new Date().getTime());
    
    // 4. 모달 표시
    $("#chartModal").fadeIn(200);
}

function closeModal() {
    $("#chartModal").fadeOut(200);
}
</script>
</body>
</html>
