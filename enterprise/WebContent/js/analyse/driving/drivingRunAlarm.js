var daystr=getNowFormatDate();


$(function() {
	
		load(); 
	
		//查询参数
		var nowDate = dateutil.formatDate(new Date(dateutil.setDateBefore(new Date(),0)),dateutil.FORMAT_DATE_LONG);
		$("#startDate").val(nowDate);
		$("#endDate").val(nowDate);
		
	    var startDate = nowDate + " 00:00:00";
		var endDate = nowDate + " 23:59:59";
		var analyseGroupID = $("#analyseGroupID").val();//轨迹分析组id
		
		var params = [{
			name : 'analyseGroupID',
			value : analyseGroupID
		},{
			name : 'startDate',
			value : startDate
		} ,{
			name : 'endDate',
			value : endDate
		}];
		$("#drivingRunAlarmList").flexigrid( {
			url : 'analyse/driving/findDrivingRunAlarmList.action',
			dataType : 'json',
			params : params,
			colModel : [ 
			{
				display : '车牌号',//表头
				name : 'RegistrationNO',//JSON数据中属性名
				width : 150,// 得加上 要不IE报错
				sortable : true,//此字段是否能排序
				align : 'left'//对齐方式
			}, {
				display : '轨迹分析组',
				name : 'AnalyseGroupName',
				width :150,
				sortable : true,
				align : 'left'
			}, {
				display : '记录生成时间',
				name : 'GenerateTime',
				width :150,
				sortable : true,
				align : 'left'
			}, {
				display : '门限值(米)',
				name : 'BounceRunDistance',
				width :150,
				sortable : true,
				align : 'left'
			}, {
				display : '报警时距离(米)',
				name : 'RecordDistance',
				width :150,
				sortable : true,
				align : 'left'
			}, {
				display : '报警时时间',
				name : 'RecordTime',
				width :150,
				sortable : true,
				align : 'left'
			}],	
			sortname : "id",//第一次加载数据时排序列
			sortorder : "asc",//第一次加载数据时排序类型
			usepager : true,//是否分页，默认为true。
			showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
			useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
			rp : 8,//每页记录数，默认为10
			singleSelect:false,
			width : 'auto',//表格宽度
			height : getNormalHeight()//表格高度
		});
		
		//按钮绑定点击事件
		$('#searchBtn').bind('click', toSearch);
		$('#exportBtn').bind('click', toExportExl);
		
		//初始化
		load();
});






/**
 * js格式化当前时间为yyyy-mm-dd形式 
 * @return CurrentDate
 */
function getNowFormatDate() 
{ 
	var day = new Date(); 
	var Year = 0; 
	var Month = 0; 
	var Day = 0; 
	var CurrentDate = ""; 
	//初始化时间 
	Year= day.getFullYear();//ie火狐下都可以 
	Month= day.getMonth()+1; 
	Day = day.getDate(); 
	CurrentDate += Year + "-"; 
	if (Month >= 10 ) 
	{ 
	CurrentDate += Month + "-"; 
	} 
	else 
	{ 
	CurrentDate += "0" + Month + "-"; 
	} 
	if (Day >= 10 ) 
	{ 
	CurrentDate += Day ; 
	} 
	else 
	{ 
	CurrentDate += "0" + Day ; 
	} 
	return CurrentDate; 
} 

/**
 * 查询参数
 * @return
 */
function getparams(){
	var registrationNO = $("#registrationNO").val();
	var startDate = $("#startDate").val()+" 00:00:00";
	var endDate = $("#endDate").val()+" 23:59:59";
	var analyseGroupID = $("#analyseGroupID").val();//轨迹分析组id
	
	//查询参数
	var params = [{
		name : 'registrationNO',
		value : registrationNO
	},{
		name : 'analyseGroupID',
		value : analyseGroupID
	},{
		name : 'startDate',
		value : startDate
	},{
		name : 'endDate',
		value : endDate
	}];
	return params;
}

/**
* 查询方法
*/
function toSearch(){
	$("#drivingRunAlarmList").hide();
	//查询参数
	var params=getparams();
	// 重置表格的某些参数
	$("#drivingRunAlarmList").flexOptions({
			newp : 1,// 设置起始页
			params : params// 设置查询参数
		}).flexReload();// 重新加载
	$("#drivingRunAlarmList").show();
}


/**
 * 导出方法入口
 */
function toExportExl() {
	exportExl('drivingRunAlarmList', 'analyse/driving/drivingRunAlarmExport.action');
}


/**
 * 初始化
 * @return
 */
function load(){
	//轨迹分析组下拉框
	initAjaxSelect_Ansynce("analyseGroupID","sys/getAnalyseGroup.action",2,false);
}
