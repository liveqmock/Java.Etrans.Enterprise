$(function() {
	var beginTime = $("#beginTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	var params = [ {
		name : 'beginTime',
		value : beginTime
	},{
		name : 'endTime',
		value : endTime
	}];
	
	initGrid(params);
	/**
	 * 查询条件更改
	 */
	$("#slSearchType").change(function(){
		$(".divSearch").hide();
		$("#div" +this.value).show();
	});
	
	$('#searchBtn').bind('click').bind('click', doSearch);
	$('#exportBtn').bind('click', toExportExl);
 
});

/**
 * 按照条件查询数据
 */

function doSearch() {

	var params = [];
	var RegistrationNopram = $("#RegistrationNopram").val();//车牌号码

	var beginTime = $("#beginTime").val();//开始时间
	var endTime = $("#endTime").val();//结束时间
	var vehicleIds=$("#vehicleIds").val();
	var params = [ {
		name : 'RegistrationNopram',
		value : RegistrationNopram
	},{
		name : 'beginTime',
		value : beginTime
	},{
		name : 'endTime',
		value : endTime
	},{
		name : 'vehicleIds',
		value : vehicleIds
	}];

	$("#historyImageSelectList").flexOptions({// 重置表格的某些参数
		newp : 1,// 设置起始页
		params : params
	}).flexReload();
	document.getElementsByName("vehicleIds")[0].value="";
	document.getElementsByName("RegistrationNopram")[0].value="";

}
function initGrid(params) {
	$("#historyImageSelectList").flexigrid({
		url : 'sys/historyImageSelectList.action',
		dataType : 'json',
		params : params,
		colModel : [ /* 车牌号码、 GPS时间、上传时间、经度、纬度、操作 */
         {
 			display : '车牌号码',
 			name : 'registrationNo',
 			width : 100,
 			sortable : false,
 			align : 'center'
 		},
 		{
 			display : '车牌颜色',
 			name : 'RegistrationNOColor',
 			width : 100,
 			sortable : false,
 			align : 'center'
 		},{
			display : 'GPS时间',// 表头
			name : 'gpsTime',// JSON数据中属性名
			width : 150,// 得加上 要不IE报错
			sortable : true,// 此字段是否能排序
			align : 'center'// 对齐方式
		}, {
			display : '上传时间',
			name : 'uploadTime',
			width : 150,
			sortable : true,
			align : 'center'
		},{
			display : '经度',
			name : 'longitude',
			width : 150,
			sortable : false,
			align : 'center'
		}, {
			display : '纬度',
			name : 'latitude',
			width : 150,
			sortable : true,
			align : 'center'
		}, {
			display : '操作',
			name : 'Handler',
			handlefunction : 'getHandleColumn',
			paramcolnames : ['imgId'],
			width : 150,
			sortable : false,//操作列不能排序
			align : 'center'
		}],
		sortname : "registrationNo",
		sortorder : "asc",
		usepager : true,//是否分页，默认为true。
		showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
		useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
		rp : 8,//每页记录数，默认为10
		singleSelect:true,
		width : 'auto',//表格宽度
		height : getNormalHeight()-20//表格高度
	});
	
};


function getHandleColumn(imgId){
	var lookStr = '<a href="javascript:void(0)" onclick="showImage(' + imgId + ')">查看图片</a>';
	return lookStr;
}

function showImage(id) {
	var url = "sys/showImage.action?imgId=" + id;
	openDialog(url, 800, 400, "查看图片");
}

function openDialog(src, width, height, title) {

	$("#dialogs").css("display", "block");
	$("#dialogFrame").attr("src", src);
	$("#dialogs").dialog({
		width : width,
		height : height,
		title : title,
		maximizable : false,
		onClose:function(){
		$("#dialogFrame").attr("src", "");
		}
	});
}

/**
 * 导出方法入口
 */
function toExportExl() {
	exportExlWithPic('historyImageSelectList', 'sys/exportHistoryImageSelect.action');
}
