/********************************************************
파일명 : util.js
설 명 :  공통 유틸 관련 
수정일				Function명
------------------------------------
2025.02.18		최초생성.
 *********************************************************/
/**
 * AJAX 호출함수.
 * 
 * @param strUrl,
 *            param, callBackFunction(성공 후처리)
 * @returns
 */

function fnDoCallBackAjax(strUrl, param, callBackFunction) {
	jQuery.ajax({
		type : "POST",
		async : false,
		url : strUrl,
		data : param,
		dataType : "JSON", // 옵션이므로 JSON으로 받을게 아니면 안써도 됨
		success : function(data) {
			callBackFunction(data);
		},
		error : function(xhr, status, error) {
			alert("request error ["+ error +"]");
		}
	});
}

function fnDoCallBackAsyncAjax(strUrl, param, callBackFunction) {
	jQuery.ajax({
		type : "POST",
		async : true,
		url : strUrl,
		data : param,
		dataType : "JSON", // 옵션이므로 JSON으로 받을게 아니면 안써도 됨
		success : function(data) {
			callBackFunction(data);
		},
		error : function(xhr, status, error) {
			alert("request error ["+ error +"]");
		}
	});
}

function fnDoCallBackFormAjax(strUrl, frmData, callBackFunction) {
	
	$.ajax({
        url : strUrl,
		enctype: 'multipart/form-data',
		contentType: 'application/x-www-form-urlencoded; charset=utf-8',
        type : 'POST',
        data : frmData,
        contentType: false,
        cache: false,
        async : false,
        processData : false        
    }).done(function(data){
    	callBackFunction(data);
    });

}



/**
 * 이미지 미리보기.
 * 
 * @param 
 * @returns
 */
$.fn.setPreview = function(opt){
    "use strict"
    var defaultOpt = {
        inputFile: $(this),
        img: null,
        w: 200,
        h: 200
    };
    $.extend(defaultOpt, opt);
 
    var previewImage = function(){
        if (!defaultOpt.inputFile || !defaultOpt.img) return;
 
        var inputFile = defaultOpt.inputFile.get(0);
        var img       = defaultOpt.img.get(0);
 
        // FileReader
        if (window.FileReader) {
            // image 파일만
            if (!inputFile.files[0].type.match(/image\//)) return;
 
            // preview
            try {
                var reader = new FileReader();
                reader.onload = function(e){
                    img.src = e.target.result;
                    img.style.width  = defaultOpt.w+'px';
                    img.style.height = defaultOpt.h+'px';
                    img.style.display = '';
                }
                reader.readAsDataURL(inputFile.files[0]);
            } catch (e) {
                // exception...
            }
        // img.filters (MSIE)
        } else if (img.filters) {
            inputFile.select();
            inputFile.blur();
            var imgSrc = document.selection.createRange().text;
 
            img.style.width  = defaultOpt.w+'px';
            img.style.height = defaultOpt.h+'px';
            img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(enable='true',sizingMethod='scale',src=\""+imgSrc+"\")";            
            img.style.display = '';
        // no support
        } else {
            // Safari5, ...
        }
    };
 
    // onchange
    $(this).change(function(){
        previewImage();
    });
};

/**
 * 입력값이 이메일을 구성할 수 있는 문자들로 구성되어 있는지 체크 단순한 이메일 입력포맷을 확인한다.
 * 
 * @param obj
 *            Object
 * @return true 이메일 구성이 가능한 문자들로 구성되어 있을 경우
 */


function idValidation(obj){
	var idReg = /^[a-z]+[a-z0-9]{6,11}$/g;
	return isValidFormat(obj, idReg);
}
 
function pwdValidation(obj){
	var pwdReg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^~*+=-])(?=.*[0-9]).{8,15}$/;
	return isValidFormat(obj, pwdReg);
}
function fnIsEmailAddr(obj) {
	var format = /^((\w|[\-\.])+)@((\w|[\-\.])+)\.([A-Za-z]+)$/;
	return isValidFormat(obj, format);
}

function isValidFormat(obj, format) {
	var val = "";
	if ((typeof obj) == "string") {
		val = obj;
	} else {
		val = obj.value;
	}

	if (val.search(format) != -1) {
		return true;
	} else {
		return false;
	}
}
/*******************************************************************************
 * 함수명 : replaceAll 설 명 : 인 자 : reExp(찾을값), replaceText(치환값) 사용법 :
 * str.replaceAll('-','.'); 작성일 : 2013.10.15 작성자 : 공통서비스 개발팀 HDH 수정일 수정자 수정내용
 * ------------------------------------- 2013.10.15 HDH 1.0 최초생성.
 ******************************************************************************/
String.prototype.replaceAll = function(reExp, replaceText) {
	var strOriginal = this;
	while (strOriginal.indexOf(reExp) > -1) {
		strOriginal = strOriginal.replace(reExp, replaceText);
	}
	;
	return strOriginal;
};




var confYn = true;
function fnAlert() {
	var message = arguments[0];
	alert(message);
	// $.alert(message);
}

function fnConfirm() {
	var message = arguments[0];
	return confirm(message);
}

/*
 * = TEXT INPUT VALUE CONTROL
 * ===========================================================
 * 
 * 1. 문자열 관련 기본 기능을 처리. 2. Function List - fnIsNull(Object) : 입력값이 NULL인지 체크 -
 * fnIsEmpty(Object) : 입력값이 공백인지 확인하여 리턴 - fnIsNumber(Object) : 입력된 문자열이 숫자 만을
 * 포함하고 있는지 여부 리턴 - fnIsEmailAddr(Object) : 입력값이 이메일을 구성할 수 있는 문자들로 구성되어 있는지
 * 단순체크
 * ============================================================================
 */
/**
 * 입력값에 스페이스 이외의 의미있는 값이 있는지 체크
 * 
 * @param obj
 *            Object
 * @return true : 공백
 */
function fnIsEmpty(obj) {
	if (obj.value == null || obj.value.replace(/ /gi, "") == "") {
		return true;
	} // end if
	return false;
}

/**
 * 입력값이 NULL인지 체크
 * 
 * @param obj
 *            Object
 * @return true : Null 또는 공백
 */
function fnIsNull(obj) {
	if (obj.value == null || obj.value == "") {
		obj.focus();
		return true;
	} // end if
	return false;
}
/**
 * 입력값이 NULL인지 체크
 * 
 * @param obj
 *            val
 * @return true : 공백 또는 대체값
 */
function fnNvl(val, rval) {
	if ((val == null) || (val == 'undefined') || !val || (val == 'null')) {
		return rval;
	} else {
		return val;
	}
}
/**
 * 입력된 문자열이 숫자 만을 포함하고 있는지 여부 리턴 (음수는 체크 못함)
 * 
 * @param obj
 *            Object
 * @return true - 숫자만을 포함하고 있는 경우
 */
function fnIsNumber(obj) {
	var chars = "0123456789";
	return containsCharsOnly(obj, chars);
}
/**
 * 입력된 문자열이 숫자 만을 포함하고 있는지 여부 리턴 (음수는 체크 못함)
 * 
 * @param p_value
 *            value
 * @return true - 숫자만을 포함하고 있는 경우
 */
function fnIsNumberValue(p_value) {
	var NUM_PATTERN = "/[0-9]{" + p_value.length + "}/";
	var PATTERN = eval(NUM_PATTERN);
	return PATTERN.test(p_value);
}
/**
 * 입력값이 특정 문자(chars)만으로 되어있는지 체크 특정 문자만 허용하려 할 때 사용 ex) if
 * (!containsCharsOnly(form.blood,"ABO")) { alert("혈액형 필드에는 A,B,O 문자만 사용할 수
 * 있습니다."); }
 * 
 * @param obj
 *            Object
 * @return true 특정 문자가 있을 경우
 */
function containsCharsOnly(obj, chars) {
	for ( var inx = 0; inx < obj.value.length; inx++) {
		if (chars.indexOf(obj.value.charAt(inx)) == -1) {
			return false;
		} // end if
	} // end for
	return true;
}




function getJSONPNone(urlV, dataV, callback, errCallBack){
		$.ajax({
			url: urlV,
			data:dataV,
			type: 'GET',
			dataType: 'json',
			xhrFields: { 
				withCredentials: true 
			}, 
		    success: function(data){
		    	if( typeof callback === "function" ) {
		    		callback( data );
	            }
		    },
		    error: function(xhr, ajaxOptions, thrownError) {
		    	if( typeof errCallBack === "function" ) {
		    		errCallBack();
	            }
		    }
		});
}
function getJSONPNoneAnsy(urlV, dataV, callback, errCallBack){
	$.ajax({
		url: urlV,
		data:dataV,
		type: 'GET',
		dataType: 'json',
		async:false,
		xhrFields: { 
			withCredentials: true 
		}, 
	    success: function(data){
	    	if( typeof callback === "function" ) {
	    		callback( data );
            }
	    },
	    error: function(xhr, ajaxOptions, thrownError) {
	    	if( typeof errCallBack === "function" ) {
	    		errCallBack( data );
            }
	    }
	});
}

jQuery.fn.center = function () {
    this.css("position","absolute");
    this.css("top", Math.max(0, (($(window).height() - $(this).outerHeight()) / 2) + $(window).scrollTop()) + "px");
    this.css("left", Math.max(0, (($(window).width() - $(this).outerWidth()) / 2) + $(window).scrollLeft()) + "px");
    return this;
}

/** 
 * 프로그래스 공통
 */
var percent = $(".percent");
var bar = $(".bar");


function progressOn(){
	$("#progressbox").show();
	$("#progressbox").center();
	var total_file = $("input[name^=attfile]").length;	
	getJSONPNoneAnsy('/fupload/progress.json',		// 공지사항 로딩
			"1=1",
			function(data){							// 공지사항 로딩  AJAX 성공
				try {
					if(data.ProgressStatus == false){
						setProgress(data.PercentDone * 1);
						setTimeout(progressOn, 100);
					}else{
						setProgress(100);
						$("#progressbox").hide();
					}
				}catch(exception){
					
				}
			},function(err){	// AJAX 실패
				//alert("장애 내역 저장중 문제가 발생했습니다.");
			}
	);
}

function setProgress(percentComplete)
{
    //Progress bar
	var progressbar = $("#progressbar");
	var statustxt = $("#statustxt");
    progressbar.width(percentComplete + '%') //update progressbar percent complete
    statustxt.html(percentComplete + '%'); //update status text
    if(percentComplete>50)
        {
            statustxt.css('color','#fff'); //change status text to white after 50%
        }
}


function getPaging(fn, totalNum, pageNum, list) { 
	
	var naviNum = 10;
	var listNum = 10;
	if(list){
		listNum = list;
	}
	if(totalNum == 0) return "";
	var totalPage = Math.ceil(parseFloat(totalNum) / parseFloat(listNum));
	var firstPage = (Math.floor(parseFloat(pageNum - 1) / naviNum) * naviNum) + 1;
	var nextPage = firstPage + naviNum;
	var prePage  = firstPage - naviNum;
	if(totalPage < nextPage) {
		nextPage = totalPage;
	}
	if(prePage < 1){
		prePage =1;
	}
	
	var prevImg = '<li onclick="'+fn+'('+(parseInt(pageNum)-1)+');" class="pg_prev"></li>';
	var prevImg2 = '<li onclick="'+fn+'(1);" class="pg_first"></li>';
	if(pageNum == 1){
		prevImg = '<li class="pg_prev"></li>';
		prevImg2 = '<li class="pg_first"></li>';
	}

	var nextImg = '<li onclick="'+fn+'('+(parseInt(pageNum)+1)+');" class="pg_next"></li>';
	var nextImg2 = '<li onclick="'+fn+'('+(totalPage)+');" class="pg_last"></li>';
	if(pageNum == totalPage){
		nextImg = '<li class="pg_next"></li>';
		nextImg2 = '<li class="pg_last"></li>';
	}
	if(parseInt(pageNum)*parseInt(listNum) > totalPage){
		nextImg2 = '<li onclick="'+fn+'('+(totalPage)+');" class="pg_last"></li>';	
	}
	var sb = "";
	sb += prevImg2 + prevImg;
	
	for(var i = firstPage; i <= (firstPage+(naviNum-1)) && i <= totalPage; i++) {
		if(pageNum != i) {
			sb += "<li onclick='"+fn+"("+i+");' class='pg_num'>"+ i +"</li>";
		} else {
			sb += "<li onclick='"+fn+"("+i+");' class='pg_num on'>"+ i +"</li>";
		}
	}
		
	sb += nextImg + nextImg2;
	 
	return sb;
}

function PrintElem(elem)
{
	Popup($(elem).html());
}
function Popup(data)
{
	var mywindow = window.open('', 'my div', 'height=400,width=1200');
	mywindow.document.write('<html><head><title>my div</title>');
	mywindow.document.write('<link rel=\'stylesheet\' type=\'text/css\' href=\'/_admst/css/common.css\' /><style>.sub-title{ margin-top:25px; }	.ui-tooltip { padding: 20px 20px; }</style></head><body ><div class=\"product-regist\" id=\"popupWrap\" style=\"margin: 20px;\">');
	mywindow.document.write(data);
	mywindow.document.write('</div></body></html>');
	mywindow.document.close(); // IE >= 10에 필요
	mywindow.focus(); // necessary for IE >= 10
	mywindow.print();
	mywindow.close();
	return true;
}
function comma(num){
    var len, point, str;
    str = "0";
	if(fnNvl(num, '') != ''){
		
		    num = uncomma(num) + ""; 
		    point = num.length % 3 ;
		    len = num.length; 
		   
		    str = num.substring(0, point); 
		    while (point < len) { 
		        if (str != "") str += ","; 
		        str += num.substring(point, point + 3); 
		        point += 3; 
		    } 
		
    }
    return str;
 
}
function comma2(num){
    var len, point, str;
    str = "";
	if(fnNvl(num, '') != ''){
		if(fnIsNumberValue(uncomma(num))){
		    num = uncomma(num) + ""; 
		    point = num.length % 3 ;
		    len = num.length; 
		   
		    str = num.substring(0, point); 
		    while (point < len) { 
		        if (str != "") str += ","; 
		        str += num.substring(point, point + 3); 
		        point += 3; 
		    } 
		}else{
			str = num;
		}
    }
    return str;
 
}
function uncomma(str) {
    str = String(str);
    
    return str.replace(/[^\d]+/g, '');
}



function check_device(){
	   var mobileKeyWords = new Array('iPhone', 'iPod', 'BlackBerry', 'Android', 'Windows CE', 'LG', 'MOT', 'SAMSUNG', 'SonyEricsson'); 
	   var device_name = ''; 
	   for (var word in mobileKeyWords){ 
	   if (navigator.userAgent.match(mobileKeyWords[word]) != null){ 
	      device_name = mobileKeyWords[word]; break; } 
	   } 
	   if(device_name == 'iPhone'){
	      device_name = 'I';
	   }else if(device_name == 'Android'){
	      device_name = 'A';
	   }else{
	      device_name = 'W';
	   }
	   
	   return device_name; 
	}


function chkword2(obj, maxByte, viewStr) {
	   var viewByte = maxByte;
    var strValue = $(obj).val();
    var strLen = strValue.length;
    var totalByte = 0;
    var len = 0;
    var oneChar = "";
    var str2 = "";

    for (var i = 0; i < strLen; i++) {
        oneChar = strValue.charAt(i);
        if (escape(oneChar).length > 4) {
            totalByte += 2;
        } else {
            totalByte++;
        }

        // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
        if (len <= maxByte) {
            len = i + 1;
        }
    }
    
    // 넘어가는 글자는 자른다.
    if (len > maxByte) {
        alert(maxByte + "Byte 이상 입력 하실 수 없습니다.");
        str2 = strValue.substr(0, (len-1));
        $(obj).val(str2);
        chkword(obj, maxByte, '', viewStr);
    }
    if(len > maxByte)
 	   len = maxByte;
    
    
    if(viewStr != ''){
 	   $("#" + viewStr).html("<span class=\"cur\">"+ comma(len) +"</span>/"+ viewByte +"byte");
    }
    
}
function chkword3(obj, maxByte, viewStr) {
	   var viewByte = maxByte;
 var strValue = $(obj).val();
 var strLen = strValue.length;
 var totalByte = 0;
 var len = 0;
 var oneChar = "";
 var str2 = "";

 for (var i = 0; i < strLen; i++) {
     oneChar = strValue.charAt(i);
     if (escape(oneChar).length > 4) {
         totalByte += 2;
     } else {
         totalByte++;
     }

     // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
     if (len <= maxByte) {
         len = i + 1;
     }
 }
 
 // 넘어가는 글자는 자른다.
 if (len > maxByte) {
     alert(maxByte + "Byte 이상 입력 하실 수 없습니다.");
     str2 = strValue.substr(0, (len-1));
     $(obj).val(str2);
     chkword(obj, maxByte, '', viewStr);
 }
 if(len > maxByte)
	   len = maxByte;
 
 
 if(viewStr != ''){
	   $("#" + viewStr).html("<span class=\"now\">"+ comma(len) +"</span>/"+ viewByte +"byte");
 }
 
}
function chkword(obj, maxByte, viewStr) {
	   var viewByte = maxByte;
       var strValue = $(obj).val();
       var strLen = strValue.length;
       var totalByte = 0;
       var len = 0;
       var oneChar = "";
       var str2 = "";
   
       for (var i = 0; i < strLen; i++) {
           oneChar = strValue.charAt(i);
           if (escape(oneChar).length > 4) {
               totalByte += 2;
           } else {
               totalByte++;
           }
   
           // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
           if (len <= maxByte) {
               len = i + 1;
           }
       }
       
       // 넘어가는 글자는 자른다.
       if (len > maxByte) {
           alert(maxByte + "Byte 이상 입력 하실 수 없습니다.");
           str2 = strValue.substr(0, (len-1));
           $(obj).val(str2);
           chkword(obj, maxByte, '', viewStr);
       }
       if(len > maxByte)
    	   len = maxByte;
       
       
       if(viewStr != ''){
    	   $("#" + viewStr).html(comma(len) + " byte/"+ viewByte +" byte");
       }
       
}
function chkwordSPP(obj, maxByte, viewStr) {
	   var viewByte = maxByte;
       var strValue = $(obj).val();
       var strLen = strValue.length;
       var totalByte = 0;
       var len = 0;
       var oneChar = "";
       var str2 = "";
   
       for (var i = 0; i < strLen; i++) {
           oneChar = strValue.charAt(i);
           if (escape(oneChar).length > 4) {
               totalByte += 2;
           } else {
               totalByte++;
           }
   
           // 입력한 문자 길이보다 넘치면 잘라내기 위해 저장
           if (len <= maxByte) {
               len = i + 1;
           }
       }
       
       // 넘어가는 글자는 자른다.
       if (len > maxByte) {
           alert("You cannot enter more than "+ comma(maxByte)+" chars.");
           str2 = strValue.substr(0, (len-1));
           $(obj).val(str2);
           chkword(obj, maxByte, '', viewStr);
       }
       if(len > maxByte)
    	   len = maxByte;
       
       
       if(viewStr != ''){
    	   $("#" + viewStr).html(comma(len) + " /"+ comma(viewByte) +"");
       }
       
}

function chkValidation(frmName){
	var chkVal = true;
	$("#" + frmName).find("input,select,textarea").each(function(){
		if($(this).attr("type") == 'radio'){
			if($(this).attr("data-require") == 'Y'){
				var name = $(this).attr("name");
				var size = $("input:radio[name="+ name +"]:checked").size();
				if(size == 0){
					alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
					$(this).focus();
					chkVal = false;
					return false;
				}
			}
		}else if($(this).attr("type") == 'file'){
			if($(this).attr("data-require") == 'Y'){
				if($(this).val() == ''){
					var chkIdArr = $(this).attr("id").split("_");
					if($("#"+ chkIdArr[0] +"List_"+  chkIdArr[1])){
						if($("#"+ chkIdArr[0] +"List_"+  chkIdArr[1] + " li").length == 0){
							alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
							$(this).focus();
							chkVal = false;
						}
					}else{
						alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
						$(this).focus();
						chkVal = false;
					}
					return false;
				}
			}
		}else if($(this).attr("type") == 'select'){
			if($(this).attr("data-require") == 'Y'){
				if($(this).attr("data-require") == 'Y'){
					if($(this).val() == ''){
						alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
						$(this).focus();
						chkVal = false;
						return false;
					}
				}
			}
		}else if($(this).attr("type") == 'textarea'){
			if($(this).attr("data-require") == 'Y'){
				if($(this).attr("data-require") == 'Y'){
					if($(this).val() == ''){
						alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
						$(this).focus();
						chkVal = false;
						return false;
					}
				}
			}
		}else{
			if($(this).attr("data-require") == 'Y'){
				if($(this).val() == ''){
					alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
					$(this).focus();
					chkVal = false;
					return false;
				}
			}
		}
	});
	/*if(chkVal){
		$("#" + frmName).find("select").each(function(){
			if($(this).attr("data-require") == 'Y'){
				if($(this).attr("data-require") == 'Y'){
					if($(this).val() == ''){
						alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
						$(this).focus();
						chkVal = false;
						return false;
					}
				}
			}
		});
	}
	if(chkVal){
		$("#" + frmName).find("textarea").each(function(){
			if($(this).attr("data-require") == 'Y'){
				if($(this).val() == ''){
					alert($(this).attr("data-text") + '은[는] 필수 항목 입니다.');
					$(this).focus();
					chkVal = false;
					return false;
				}
			}
		});
	}*/
	return chkVal;
}

function bizNoFormatter(num, type) {

    var formatNum = '';
    if(fnNvl(num, '') != ''){
	    num = num.replace(/\-/g,'');
	    try{
	         if (num.length == 10) {
	              if (type == 0) {
	                   formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-*****');
	              } else {
	                    formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-$3');
	              }
	         }
	    } catch(e) {
	         formatNum = num;
	    }
    }
    return formatNum;
}


function bizCropFormatter(num, type) {

    var formatNum = '';
    if(fnNvl(num, '') != ''){
    num = num.replace(/\-/g,'');
	    try{
	         if (num.length == 13) {
	              if (type == 0) {
	                   formatNum = num.replace(/(\d{6})(\d{7})/, '$1-*****');
	              } else {
	                    formatNum = num.replace(/(\d{6})(\d{7})/, '$1-$2');
	              }
	         }
	    } catch(e) {
	         formatNum = num;
	    }
    }
    return formatNum;
}
function openPopup (sURL, width, height) {
	  var sWidth, sHeight;
	  var sFeatures;
	  var oWindow;
	  var SP2 = false;
	  var POPUP_WIDTH     = 400;
	  var POPUP_HEIGHT    = 300;
	  var B_MAIN_PAGE     = true;
	  var LeftPosition = 0;
	  var TopPosition  = 0;
	  
	  sHeight = POPUP_HEIGHT;
	  sWidth  = POPUP_WIDTH;
	  sTitle = "PopupWindow";

	  try {
	    SP2 = (window.navigator.userAgent.indexOf("SV1") != -1);
	    if (arguments[1] != null && arguments[1] != "") sWidth = arguments[1] ;
	    if (arguments[2] != null && arguments[2] != "") sHeight = arguments[2] ;
	    if (arguments[3] != null && arguments[3] != "") sTitle = arguments[3] ;
	    if (SP2)     {   // XP SP2 브라우저임..
	      sHeight = Number(sHeight)+10;
	    }else{  //그외 브라우저
	    }
	  } catch(e) {}
	  
	  if(sURL.indexOf("printPopup") > 0) {
	    sWidth = 980;
	  }
	  sFeatures =  "width=" + sWidth + ",height=" + sHeight ;    
	  sFeatures += ",left=0,top=0" ;
	  LeftPosition = (screen.width)?(screen.width-sWidth)/2:100;
	TopPosition  = (screen.height)?(screen.height-sHeight)/2:100;
	  if(sURL.indexOf("printPopup") > 0) {
	    sFeatures += ",directories=no,location=no,menubar=no,resizable=no,scrollbars=yes,status=no,titlebar=no,toolbar=no, top="+TopPosition+", left="+LeftPosition;
	  } else {
	    sFeatures += ",directories=no,location=no,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no";
	  }
	  
	  if(sURL!=null && sURL.length > 0) {
	    if(sURL.indexOf("?") > 0) {
	      sURL += "&thref="+location.href;
	    } else {
	      sURL += "?thref="+location.href;
	    }
	  }
	  oWindow = window.open(sURL, sTitle, sFeatures);
	  oWindow.focus();

	  // move to screen center
	 // oWindow.moveTo( (window.screen.availWidth - sWidth) / 2, (window.screen.availHeight - sHeight) / 2);

	  return oWindow;  
	}

function getFileName(val){
	var fileValue = val.split("\\");
	var fileName = fileValue[fileValue.length-1]; // 파일명
	return fileName;
}


function dateCompare(compareDate){
	// 현재 시간 보다 작은 날짜이면 true 이후 날짜 이면 false
	var	startDate = getNowDate();
	
    var startDateArr = startDate.split('-');
    var endDateArr = compareDate.split('-');
    var startDateCompare = new Date(startDateArr[0], parseInt(startDateArr[1])-1, startDateArr[2]);
    var endDateCompare = new Date(endDateArr[0], parseInt(endDateArr[1])-1, endDateArr[2]);
     
    if(startDateCompare.getTime() > endDateCompare.getTime()) {
    	return true;
    }else{
    	return false;
    }
}
function dateDiffCompare(startTime,endTime){
	// 현재 시간 보다 작은 날짜이면 true 이후 날짜 이면 false
	var startDate = new Date(parseInt(startTime.substring(0,4), 10),
             parseInt(startTime.substring(5,7), 10)-1,
             parseInt(startTime.substring(8,10), 10),
             parseInt(startTime.substring(11,13), 10),
             parseInt(startTime.substring(14,16), 10), 0
            );
           
   // 종료일시
   var endDate   = new Date(parseInt(endTime.substring(0,4), 10),
             parseInt(endTime.substring(5,7), 10)-1,
             parseInt(endTime.substring(8,10), 10),
             parseInt(endTime.substring(11,13), 10),
             parseInt(endTime.substring(14,16), 10), 0
             
            );

   // 두 일자(startTime, endTime) 사이의 차이를 구한다.
   var dateGap = endDate.getTime() - startDate.getTime();
   var timeGap = new Date(0, 0, 0, 0, 0, 0, endDate - startDate); 
   
   
   // 두 일자(startTime, endTime) 사이의 간격을 "일-시간-분"으로 표시한다.
   var diffDay  = Math.floor(dateGap / (1000 * 60 * 60 * 24)); // 일수       
   var diffHour = timeGap.getHours();       // 시간
   var diffMin  = timeGap.getMinutes();      // 분
   var diffSec  = timeGap.getSeconds();      // 초
   if(dateGap > 0){
	   return true;
   }else{
	   return false;
   }

}
function getNowDate(){
	var date = new Date(); 
	var year = date.getFullYear(); 
	var month = new String(date.getMonth()+1); 
	var day = new String(date.getDate()); 

	// 한자리수일 경우 0을 채워준다. 
	if(month.length == 1){ 
	  month = "0" + month; 
	} 
	if(day.length == 1){ 
	  day = "0" + day; 
	} 
	
	return year + "-" + month + "-" + day;

}

function dateAdd(sDate, v, t) {
		var yy = parseInt(sDate.substr(0, 4), 10);
		var mm = parseInt(sDate.substr(5, 2), 10);
		var dd = parseInt(sDate.substr(8), 10);

		if(t == "d"){
			d = new Date(yy, mm - 1, dd + v);
		}else if(t == "m"){
			d = new Date(yy, mm - 1 + v, dd);
		}else if(t == "y"){
			d = new Date(yy + v, mm - 1, dd);
		}else{
			d = new Date(yy, mm - 1, dd + v);
		}
		yy = d.getFullYear();
		mm = d.getMonth() + 1; mm = (mm < 10) ? '0' + mm : mm;
		dd = d.getDate(); dd = (dd < 10) ? '0' + dd : dd;

		return '' + yy + '-' +  mm  + '-' + dd;

}
function getInputDayLabel(inputDate) {
    var week = new Array('Sun.', 'Mon.', 'Tue.', 'Wed.', 'Thu.', 'Fri.', 'Sat.');
    var today = new Date(inputDate).getDay();
    var todayLabel = week[today];
    return todayLabel;
}
function getFormatArrVal(arrVal, strGubun, newStrGubun){
  var result = "";
  if(arrVal != ''){
	var gubun = "";
	var arr =   arrVal.split(strGubun);
	for(var i = 1; i < arr.length; i++){
	   result = result + gubun + arr[i];
	   gubun = newStrGubun;
	}
	result = result.substring(0, result.length - newStrGubun.length);
  }
  return result;
}

function genRowspan(className){
    $("." + className).each(function() {
        var rows = $("." + className + ":contains('" + $(this).text() + "')");
        if (rows.length > 1) {
            rows.eq(0).attr("rowspan", rows.length);
            rows.not(":eq(0)").remove();
        }
    });
}

Date.prototype.format = function (f) {

    if (!this.valueOf()) return " ";



    var weekKorName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

    var weekKorShortName = ["일", "월", "화", "수", "목", "금", "토"];

    var weekEngName = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

    var weekEngShortName = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

    var d = this;



    return f.replace(/(yyyy|yy|MM|dd|KS|KL|ES|EL|HH|hh|mm|ss|a\/p)/gi, function ($1) {

        switch ($1) {

            case "yyyy": return d.getFullYear(); // 년 (4자리)

            case "yy": return (d.getFullYear() % 1000).zf(2); // 년 (2자리)

            case "MM": return (d.getMonth() + 1).zf(2); // 월 (2자리)

            case "dd": return d.getDate().zf(2); // 일 (2자리)

            case "KS": return weekKorShortName[d.getDay()]; // 요일 (짧은 한글)

            case "KL": return weekKorName[d.getDay()]; // 요일 (긴 한글)

            case "ES": return weekEngShortName[d.getDay()]; // 요일 (짧은 영어)

            case "EL": return weekEngName[d.getDay()]; // 요일 (긴 영어)

            case "HH": return d.getHours().zf(2); // 시간 (24시간 기준, 2자리)

            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2); // 시간 (12시간 기준, 2자리)

            case "mm": return d.getMinutes().zf(2); // 분 (2자리)

            case "ss": return d.getSeconds().zf(2); // 초 (2자리)

            case "a/p": return d.getHours() < 12 ? "오전" : "오후"; // 오전/오후 구분

            default: return $1;

        }

    });

};
function moveUrl(Url){
	location.href=Url;
}


String.prototype.string = function (len) { var s = '', i = 0; while (i++ < len) { s += this; } return s; };

String.prototype.zf = function (len) { return "0".string(len - this.length) + this; };

Number.prototype.zf = function (len) { return this.toString().zf(len); };