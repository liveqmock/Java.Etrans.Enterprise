$(function() {
	var nowDate = dateutil.formatDate(new Date(dateutil.setDateBefore(new Date(),1)),dateutil.FORMAT_DATE_LONG);
	$("#startDate").val(nowDate);
	$("#endDate").val(nowDate);
	
		var registrationNo="";
		var endDate  = $("#endDate").val()+" 23:59:59";
		var startDate  = $("#startDate").val()+" 00:00:00";
		
		var params = [{
			name : 'registrationNO',
			value : ''
		} ,{
			name : 'startTime',
			value : startDate
		},{
			name : 'endTime',
			value : endDate
		}];
		$("#vehicleMileageList").flexigrid( {
			url : 'query/stat/findVehicleMileage.action',
			dataType : 'json',
			params : params,
			colModel : [ 
			{
				display : '车牌号码',//表头
				name : 'registrationNO',//JSON数据中属性名
				width : 200,// 得加上 要不IE报错
				sortable : false,//此字段是否能排序
				align : 'center'//对齐方式
			}, {
				display : '车牌颜色',
				name : 'color',
				width :100,
				sortable : false,
				align : 'center'
			}, {
				display : '所属单位',
				name : 'workUnitName',
				width :250,
				sortable : false,
				align : 'center'
			}, {
				display : '统计时间段',
				name : 'gpsTime',
				width :300,
				sortable : false,
				align : 'center'
			}, {
				display : '里程',
				name : 'newMileage',
				width :100,
				sortable : false,
				align : 'center'
			}],	
			sortname : "id",//第一次加载数据时排序列
			sortorder : "asc",//第一次加载数据时排序类型
			usepager : true,//是否分页，默认为true。
			showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
			useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
			rp : 50,//每页记录数，默认为10
			singleSelect:false,
			width : 'auto',//表格宽度
			height : getNormalHeight()-20//表格高度
		});
		
		//按钮绑定点击事件
		$('#submitBtn').bind('click', toSearch);
		$('#exportBtn').bind('click', toExportExl);
		$('#adSearchBtn').bind('click', toOpenAds);
		
		$("[name=rp]").empty();
		
		var unit = $("[name=rp]").get(0);
		unit.options.add(new Option("50","50"));
		
});


/**
 * 打开高级搜索框
 */
function toOpenAds(){
	$("#adSearch").animate({height: 'toggle', opacity: 'toggle'}, 400);
}


/**
 * 查询参数
 * @return
 */
function getparams(){
	var registrationNO = $("#registrationNO").val();
	var startDate = $("#startDate").val()+" 00:00:00";
	var endDate = $("#endDate").val()+" 23:59:59";
	var workUnitName=$.trim($("#workUnitNameParam").val());
	
	var startDate1 = $("#startDate").val()+" 00:00:00";
	var endDate1 = $("#endDate").val()+" 23:59:59";
	
	//查询参数
	var params = [{
		name : 'registrationNO',
		value : registrationNO
	} ,{
		name : 'startTime',
		value : startDate
	},{
		name : 'endTime',
		value : endDate
	},{
		name:'workUnitName',
		value: workUnitName
	}
	];
	return params;
}

/**
* 查询方法
*/
function toSearch(){
	//查询参数
	var params=getparams();
	// 重置表格的某些参数
	$("#vehicleMileageList").flexOptions({
			newp : 1,// 设置起始页
			params : params// 设置查询参数
		}).flexReload();// 重新加载
}


/**
 * 导出方法入口
 */
function toExportExl(){
	exportExl('vehicleMileageList','query/stat/vehicleMileageExportExl.action');
}
