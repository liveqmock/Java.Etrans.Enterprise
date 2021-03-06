$(function(){
	if(!isShowHandle){
		document.getElementById("guide-step").style.display = "block";
	    showSearchTip();
		setSearchTip();
		SetCookie("tStatus",1);
	}
})

function nextStep(next){
	
	$(".tipbox").css({"visibility":"hidden","display":"none"});
	$(".tipbar").hide();
	$("#step" + next).css({"visibility":"visible","display":"block"});
	$("#tipbar" + (next -1)).show();
	if(next == 2) {
		$("#searchTip").css("top","60px");
	}
	else if(next == 3) {
		$("#searchTip").css("top","30px");
	}else{
		$("#searchTip").css("top","30px");
	}
}

//关闭提示框
function hideTip(){
	
//显示系统公告	
	if(!isShowNotice){
		getSystemNoticeSet();
	}

//更新当前已经有过提示
	$.ajax({
	    type : "POST",
	    url : "sys/user/setIsShowHandle.action",
	    data : {isShowHandle:"1"},
	    dataType : "TEXT",
	    success : function(data) {
	    	if(data=="SUCCESS")
			{
	    		document.getElementById("guide-step").style.display = "none";
	    		$("#searchTipBg").hide();
	    		$("#searchTip").hide();
	    		$(".tipbar").hide();
	    		$(".tipbox").css({"visibility":"hidden","display":"none"});
	    		$("#step1").css({"visibility":"visible","display":"block"});
	    		SetCookie("tipVisible","no");
			}
			else
			{
				$.messager.alert('提示信息','关闭失败！','info');
			}
	    },
	    error : function(data) {
	    	showError();
	    }
    });
	
}

function setSearchTip(){
	var windowW = $(window).width(),
		windowH = $(window).height(),
		width = $("#searchTip").width(),
		ml = width/2;
	if($("#searchTip").length > 0 && $("#searchTipBg").length > 0){
		if($.browser.msie && $.browser.version == '6.0' && !$.support.style){
		  	var scrollT = $(window).scrollTop(),
			  	scrollL = $(window).scrollLeft();
			$("#searchTipBg").css({"width":windowW + scrollL,"height":windowH + scrollT});
		}else {
			$("#searchTipBg").css({"width":windowW,"height":windowH});
		}
		$("#searchTip").css({"margin-left":-ml});
	}
}

function showSearchTip(){
	var position = $.browser.msie && $.browser.version == '6.0' && !$.support.style ? "absolute" : "fixed";
	   var searchTipInner = "<div class='tipbox' id='step1'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'>" +
							"</span><span class='tipboxNextbtn' onclick='nextStep(2)'></span><ol class='progress'><li class='on'></li><li></li><li>" +
							"</li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step2'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'>" +
							"</span><span class='tipboxNextbtn1' onclick='nextStep(3)'></span><ol class='progress'><li></li>" +
							"<li class='on'></li><li></li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step3'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'>" +
							"</span><span class='tipboxNextbtn' onclick='nextStep(4)'></span><ol class='progress'><li></li><li>" +
							"</li><li class='on'></li><li></li></ol></div>";
		searchTipInner += "<div class='tipbox' id='step4'><div class='tipword'></div><span class='tipboxBtn' onclick='hideTip()'>" +
							"</span><span class='tipboxNextbtn' onclick='hideTip()'></span><div class='notip'></div></div>";
	var switchBtn = "<div class='tipSwitchAnimate' style='display:none; left:410px; top:353px;'></div>";	
	if($("#searchTip").length == 0){
		$("#guide-step").before("<div id='searchTipBg' style='width:100%; height:100%; left:0px; top:0px; z-index:999; background-color:#000; opacity:0.55; filter:alpha(opacity=55);position:absolute'></div>");
		$("#guide-step").before("<div id='searchTip' style='left:50%; top:60px; z-index:1005; background-color:transparent; position:absolute;width:1365px;height:600px;'>"+ searchTipInner +"</div>");
		$(switchBtn).appendTo($(".tipbardiv"));
		$("#step1").css({"visibility":"visible","display":"block"});
		if(GetCookie("tipVisible") == "no" || GetCookie("neverShow") == "no"){
			$("#step4 .notip").hide();
		}
	}
	if($("#searchTip").css("display") == "none"){
		$("#searchTip").css("top","60px").show();
		$("#searchTipBg").show();
		$(".tipbox").css({"visibility":"hidden","display":"none"});
		$("#step1").css({"visibility":"visible","display":"block"});
	}
	if($(".tipbarwrap").css("display") == "none"){
		$(".tipbarwrap").show();
	}
}



/**************************************************************车辆监控操作***************************************************/
function showVehicleSearchTip(){
	var position = $.browser.msie && $.browser.version == '6.0' && !$.support.style ? "absolute" : "fixed";
	   var searchTipInner = "<div class='tipboxVehicle' id='stepVehicle1'><div class='tipwordVehicle'></div><span class='tipboxBtnVehicle' onclick='hideVehicleTip()'>" +
							"</span><span class='tipboxVehicleNextbtn' onclick='nextVehicleStep(2)'></span><ol class='progress'><li class='on'></li><li></li><li>" +
							"</li><li></li></ol></div>";
		searchTipInner += "<div class='tipboxVehicle' id='stepVehicle2'><div class='tipwordVehicle'></div><span class='tipboxBtnVehicle' onclick='hideVehicleTip()'>" +
							"</span><span class='tipboxVehicleNextbtn1' onclick='nextVehicleStep(3)'></span><ol class='progress'><li></li>" +
							"<li class='on'></li><li></li><li></li></ol></div>";
//		searchTipInner += "<div class='tipboxVehicle' id='stepVehicle3'><div class='tipwordVehicle'></div><span class='tipboxBtnVehicle' onclick='hideVehicleTip()'>" +
//							"</span><span class='tipboxVehicleNextbtn' onclick='nextVehicleStep(4)'></span><ol class='progress'><li></li><li>" +
//							"</li><li class='on'></li><li></li></ol></div>";
		searchTipInner += "<div class='tipboxVehicle' id='stepVehicle3'><div class='tipwordVehicle'></div><span class='tipboxBtnVehicle' onclick='hideVehicleTip()'>" +
							"</span><span class='tipboxVehicleNextbtn' onclick='hideVehicleTip()'></span><div class='notip'></div></div>";
	var switchBtn = "<div class='tipSwitchAnimateVehicle' style='display:none; left:410px; top:353px;'></div>";	
	if($("#searchTipVehicle").length == 0){
		$("#vehicleMonitoring-step").before("<div id='searchTipBgVehicle' style='width:100%; height:100%; left:0px; top:0px; z-index:998; background-color:#000; opacity:0.55; filter:alpha(opacity=55);position:absolute'></div>");
		$("#vehicleMonitoring-step").before("<div id='searchTipVehicle' style='left:50%; top:10px; z-index:1005; background-color:transparent; position:absolute;width:1365px;height:600px;'>"+ searchTipInner +"</div>");
		$(switchBtn).appendTo($(".tipbardivVehicle"));
		$("#stepVehicle1").css({"visibility":"visible","display":"block"});
		if(GetCookie("tipVisibleVehicle") == "no" || GetCookie("neverShow") == "no"){
			$("#stepVehicle3 .notip").hide();
		}
	}
	if($("#searchTipVehicle").css("display") == "none"){
		$("#searchTipVehicle").css("top","10px").show();
		$("#searchTipBgVehicle").show();
		$(".tipboxVehicle").css({"visibility":"hidden","display":"none"});
		$("#stepVehicle1").css({"visibility":"visible","display":"block"});
	}
	if($(".tipbarwrapVehicle").css("display") == "none"){
		$(".tipbarwrapVehicle").show();
	}
}

/**车辆监控操作时关闭**/
function hideVehicleTip(){
	document.getElementById("vehicleMonitoring-step").style.display = "none";
	$("#searchTipBgVehicle").hide();
	$("#searchTipVehicle").hide();
	$(".tipbarVehicle").hide();
	$(".tipboxVehicle").css({"visibility":"hidden","display":"none"});
	$("#stepVehicle1").css({"visibility":"visible","display":"block"});
//	SetCookie("tipVisible","no");
	
}

/**车辆监控下一步***/
function nextVehicleStep(next){
	$(".tipboxVehicle").css({"visibility":"hidden","display":"none"});
	$(".tipbarVehicle").hide();
	$("#stepVehicle" + next).css({"visibility":"visible","display":"block"});
	$("#tipbarVehicle" + (next -1)).show();
	if(next == 2) {
		$("#searchTipVehicle").css("top","20px");
	}
	else if(next == 3) {
		$("#searchTipVehicle").css("top","20px");
	}else{
		$("#searchTipVehicle").css("top","20px");
	}
}


function setSearchTipVehicle(){
	var windowW = $(window).width(),
		windowH = $(window).height(),
		width = $("#searchTipVehicle").width(),
		ml = width/2;
	if($("#searchTipVehicle").length > 0 && $("#searchTipBgVehicle").length > 0){
		if($.browser.msie && $.browser.version == '6.0' && !$.support.style){
		  	var scrollT = $(window).scrollTop(),
			  	scrollL = $(window).scrollLeft();
			$("#searchTipBgVehicle").css({"width":windowW + scrollL,"height":windowH + scrollT});
		}else {
			$("#searchTipBgVehicle").css({"width":windowW,"height":windowH});
		}
		$("#searchTipVehicle").css({"margin-left":-ml});
	}
}
/**************************************************************车辆监控操作**/
function GetCookie(name){
    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
    if(arr != null) return decodeURIComponent(arr[2]); return null;
}

function SetCookie(name,value,options){
    var expires = '', path = '', domain = '', secure = ''; 
    if(options)
    {
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var exp;
            if (typeof options.expires == 'number') {
                exp = new Date();
                exp.setTime(exp.getTime() + options.expires*24*60*60*1000);
            }
            else{
                exp = options.expires;
            }
            expires = ';expires=' + exp.toUTCString();
        }
        path = options.path ? '; path=' + options.path : ''; 
        domain = options.domain ? ';domain=' + options.domain : ''; 
        secure = options.secure ? ';secure' : ''; 
    }
    document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
}