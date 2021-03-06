var daystr=getNowFormatDate();

$(function() {
		//查询参数
		var nowDate = dateutil.formatDate(new Date(dateutil.setDateBefore(new Date(),1)),dateutil.FORMAT_DATE_LONG);
		$("#startDate").val(nowDate);
		$("#endDate").val(nowDate);
		
		
		var startDate = nowDate + " 00:00:00";
		var endDate = nowDate + " 23:59:59";
		
		
		var params = [{
			name : 'startDate',
			value : startDate
		} ,{
			name : 'endDate',
			value : endDate
		},{
			name : 'timeDay',
			value : 1
		}];
		$("#vehicleOnlinePercertList_mysql").flexigrid( {
			url : 'query/stat/statDataTA/vehicleOnlineList_TA.action',
			dataType : 'json',
			params : params,
			colModel : [ 
			  {
				display : '车牌',
				name : 'registrationNo',
				width :150,
				sortable : true,
				align : 'center'
			},{
				display : '所属单位',
				name : 'workUnitName',
				width :250,
				sortable : true,
				align : 'center'
			}, {
				display : '车辆在线总次数',
				name : 'vehiclecount',
				width :125,
				sortable : true,
				align : 'center'
			}, {
				display : '车辆在线率',
				name : 'onLineRate',
				handlefunction : 'getOnLineAate',
				paramcolnames : ['onLineRate'],
				width :125,
				sortable : true,
				align : 'center'
			}, {
				display : '车辆离线率',
				name : 'offLineRate',
				handlefunction : 'getOffLineAate',
				paramcolnames : ['offLineRate'],
				width :125,
				sortable : false,
				align : 'center'
			}],	
			sortname : "VehicleID",//第一次加载数据时排序列
			sortorder : "asc",//第一次加载数据时排序类型
			usepager : true,//是否分页，默认为true。
			showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
			useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
			rp : 8,//每页记录数，默认为10
			singleSelect:false,
			width : 'auto',//表格宽度
			height : getNormalHeight()-20//表格高度
		});
		
		//按钮绑定点击事件
		$('#searchBtn').bind('click', toSearch);
		$('#exportBtn').bind('click', toExportExl);
});

function getOnLineAate(onLineRate){
	return (onLineRate)+"%";
}
function getOffLineAate(offLineRate){
	return (offLineRate)+"%";
}

//计算天数差的函数，通用
function DateDiff(sDate1, sDate2){  //sDate1和sDate2是2004-10-18格式
  var aDate, oDate1, oDate2, iDays
   aDate = sDate1.split("-")
   oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0])  //转换为10-18-2004格式
   aDate = sDate2.split("-")
   oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0])
   iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 /24)  //把相差的毫秒数转换为天数

   return iDays+1
 } 

/**
 * 打开高级搜索框
 */
//function toOpenAds(){
//	$("#adSearch").animate({height: 'toggle', opacity: 'toggle'}, 400);
//}


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
	var registrationNo=$("#registrationNo").val();
	var workUnitName = $("#workUnitNameParam").val();
	var startDate = $("#startDate").val()+" 00:00:00";
	var endDate = $("#endDate").val()+" 23:59:59";
	//计算当前的时期差
	var start=$("#startDate").val();
	var end=$("#endDate").val();
	var timeDay=DateDiff(start,end);
	//查询参数
	var params = [{
		name : 'registrationNo',
		value : registrationNo
	},{
		name : 'workUnitNameWhere',
		value : workUnitName
	},{
		name : 'startDate',
		value : startDate
	},{
		name : 'endDate',
		value : endDate
	},{
		name : 'timeDay',
		value : timeDay
	}];
	return params;
}

/**
* 查询方法
*/
function toSearch(){
	//查询参数
	var params=getparams();
	// 重置表格的某些参数
	$("#vehicleOnlinePercertList_mysql").flexOptions({
			newp : 1,// 设置起始页
			params : params// 设置查询参数
		}).flexReload();// 重新加载
	$("#vehicleOnlinePercertList_mysql").show();
}


/**
 * 导出方法入口
 */
function toExportExl() {
	exportExl('vehicleOnlinePercertList_mysql', 'query/stat/statDataTA/vehicleOnlineTAListExportExl.action');
}
