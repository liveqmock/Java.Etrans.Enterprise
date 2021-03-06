$(function() {
	
	initGrid();
	
	//初始化验证插件
	$("#editWindow").validation();
	initSelects();

	
	//按钮绑定点击事件
	$('#searchBtn').bind('click', toSearch);
    $('#createBtn').bind('click', toCreate);
    $('#editBtn').bind('click', toEdit);
    $('#deleteBtn').bind('click', toDelete);
    $('#cancelBtn').bind('click', hide);
    $('#reSetBtn').bind('click', reSetAddForm);
//    $('#workBtn').bind('click', showWorkUnitTree);
    $('#workaddBtn').bind('click', showWorkUnitAddTree);
	    
});


//设置文本框可编辑
//function doEdit(){
//	
//	
//	
//    
//}

function initGrid(){
		$("#simCardList").flexigrid( {
			url : 'sys/simCardList.action',
			dataType : 'json',
			colModel : [ /*电话号码、卡编号、消费套餐、开通日期、购买日期、运营商、开卡所属地、是否使用、开通语音类别、
			             是否开通短信、预交费用金额(元)、短信包月条数、超出流量收费情况、预交费用到期时间(年月日)、IP、APN、
			             是否粤港两地卡、工作单位、操作（编辑  删除）*/
             {
 				display : '企业名称',
 				name : 'WorkUnitID',
 				width : 200,
 				sortable : true,
 				align : 'center'
 			},{
				display : '电话号码',//表头
				name : 'PhoneNO',//JSON数据中属性名
				width : 100,// 得加上 要不IE报错
				sortable : true,//此字段是否能排序
				align : 'center'//对齐方式
			}, {
				display : '卡编号',
				name : 'Code',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '是否使用',
				name : 'IsInUse',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : '开通语音类别',
				name : 'SoundKind',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '是否开通短信',
				name : 'IsNote',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '是否粤港两地卡',
				name : 'IsTwoCities',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '开通日期',
				name : 'EffectiveDate',
				width : 100,
				sortable : false,
				align : 'center'
			}, {
				display : '购买日期',
				name : 'BuyDate',
				width : 100,
				sortable : true,
				align : 'center'
			},/*{
				display : '运营商',
				name : 'OperationID',
				width : 100,
				sortable : false,
				align : 'left'
			},*/{
				display : '开卡所属地',
				name : 'Address',
				width : 200,
				sortable : true,
				align : 'center'
			},/*{
				display : '消费套餐',
				name : 'SIMChargeID',
				width : 80,
				sortable : false,
				align : 'left'
			},*/{
				display : '预交费用金额',
				name : 'PrepaySum',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '短信包月条数',
				name : 'NoteCount',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : '超出流量收费情况',
				name : 'OverSpend',
				width : 110,
				sortable : true,
				align : 'center'
			},{
				display : '预交费用到期时间',
				name : 'EndDate',
				width : 110,
				sortable : true,
				align : 'center'
			},{
				display : 'IP',
				name : 'IP',
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : 'APN',
				name : 'APN',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : '操作',
				name : 'Handler',
				handlefunction : 'getHandleColumn',
				paramcolnames : ['ID'],
				width : 80,
				sortable : false,//操作列不能排序
				align : 'center'
			}],
			
			sortname : "ID",//第一次加载数据时排序列
			sortorder : "desc",//第一次加载数据时排序类型
			usepager : true,//是否分页，默认为true。
			showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
			useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
			rp : 8,//每页记录数，默认为10
			//checkbox : true,//是否要多选框,默认为false。
			rowId : 'ID',// 多选框绑定行的id,只有checkbox : true时才有效。
			singleSelect:false,
			width : 'auto',//表格宽度
			height :getNormalHeight()-20//表格高度
		});
		
		
	    
};

/**
 * 组装操作列显示内容
 * @param id
 * @returns {String}
 */
function getHandleColumn(id){
	
	var editStr = "";
	var deleteStr = "";
	//变量resources为用户的所有资源权限 格式：|findPlatFormList||createPlatForm||updatePlatForm||deletePlatForm|
	if(resources!=null){
		//判断ACTION的访问权限
		 if(resources.indexOf("|updateSimCard|")!=-1){
			 editStr ='<a href="javascript:void(0)" onclick="doEdit(' + id + ')">编辑</a>'
		 }
		 if(resources.indexOf("|deleteSimCard|")!=-1){
			 deleteStr ='<a href="javascript:void(0)" onclick="doDelete(' + id + ')">删除</a>'
		 }
	}
	return editStr + '&nbsp;&nbsp;' + deleteStr;
	
}

/**
 * 查询方法
 */
function toSearch(){
	
		var phonePram = $("#phonePram").val();
		var codePram = $("#codePram").val();
		var workUnitIdPram = $("#workUnitId").val();
		//查询参数
		var params = [{
			name : 'phonePram',
			value : phonePram
		},{
			name : 'codePram',
			value : codePram
		},{
			name : 'workUnitIdPram',
			value : workUnitIdPram
		}];
		// 重置表格的某些参数
		$("#simCardList").flexOptions({
				newp : 1,// 设置起始页
				params : params// 设置查询参数
			}).flexReload();// 重新加载
	
	
}

/**
 * 新增加方法入口
 */
function toCreate(){
	$("#titleInfo").html("新增SIM卡信息");
	$("#submitBtn").unbind("click");
	initSelects();
	resetForm("editWindow");
	showEditForm();
	$('#submitBtn').bind('click', doCreate);
}
/**
 *初始化SIM卡信息管理界面下拉框
 */
function initSelects(){
//	initAjaxSelect("workUnitIdPram","sys/initWorkUnits.action","1");
//	initAjaxSelect("workUnitID","sys/initWorkUnits.action","2");
	initIsInUse("isInUse");
	initIsNote("isNote");
	initSoundKind("soundKind");
	initIsTwoCities("isTwoCities");
}

/**
 * 封装界面表单属性参数
 */
function getAddForm(){
	
	var id = $("#id").val();
	var Code = $("#code").val();
	var PhoneNO = $("#phoneNO").val();
	var SIMChargeID = $("#simChargeID").val();
	var EffectiveDate = $("#effectiveDate").val();
	var BuyDate = $("#buyDate").val();
	//var OperationID = "0";
	var Address = $("#address").val();
	var IsInUse = $("#isInUse").val();
	var SoundKind = $("#soundKind").val();
	var IsNote = $("#isNote").val();
	var PrepaySum = $("#prepaySum").val();
	var NoteCount = $("#noteCount").val();
	var OverSpend = $("#overSpend").val();
	var EndDate = $("#endDate").val();
	var IP = $("#IP").val();
	var APN = $("#APN").val();
	var IsTwoCities = $("#isTwoCities").val();
	var WorkUnitID = $("#workUnitID1").val();
	

	//表单参数
	var params = {
			id : id,
			Code : Code,
			PhoneNO : PhoneNO,
			SIMChargeID : SIMChargeID,
			EffectiveDate : EffectiveDate,
			BuyDate : BuyDate,
			Address : Address,
			IsInUse : IsInUse,
			SoundKind : SoundKind,
			IsNote : IsNote,
			PrepaySum : PrepaySum,
			NoteCount : NoteCount,
			OverSpend : OverSpend,
			EndDate : EndDate,
			IP : IP,
			APN : APN,
			IsTwoCities : IsTwoCities,
			WorkUnitID : WorkUnitID
	};
	
	return params;
}

/**
 * 执行后台方法新增数据
 */
function doCreate(){
	
	var canSubmit = $("#editWindow").beforeSubmit();
	if(canSubmit){
		var params = getAddForm();
		$.ajax({
		    type : "POST",
		    url : "sys/createSimCard.action",
		    data : {params : $.toJSON(params)},
		    dataType : "JSON",
		    success : function(data) {
		    	if(data != null){
		    		hide();
		    		$("#simCardList").flexReload();
		    	}else{
		    		showError();
		    	}
		    },
		    error : function(data) {
		    	showError();
		    }
		});
	}
}

/**
 * 编辑方法入口
 */
function toEdit(){
	var checkedIds = $("#simCardList").getCheckedRows();
	if(checkedIds.length<1){
		showWarning("请选择一行后进行编辑操作！");
		return;
	}
	if(checkedIds.length>1){
		showWarning("只能选择一行进行编辑操作！");
		return;
	}
	if(checkedIds.length == 1){
		doEdit(checkedIds[0]);
	}
}

/**
 * 查询机构信息显示在编辑窗口
 * @param id
 */
function doEdit(id){
	$("#titleInfo").html("编辑SIM卡信息");
	if(id != null && id != ''){
		
		$("#submitBtn").unbind("click");
		clearForm("editWindow");
		initSelects();
		showEditForm();
		
		$.ajax({
		    type : "POST",
		    url : "sys/getSimCardById.action",
		    data : {id:id},
		    dataType : "JSON",
		    success : function(data) {
		    	if(data!= null){
		    		var otInfo =  eval("("+data+")");
		    		if(otInfo.length > 0){
		    			$("#id").val(otInfo[0].ID);
		    			$("#code").val(otInfo[0].Code);
		    			$("#phoneNO").val(otInfo[0].PhoneNO);
		    			$("#simChargeID").val(otInfo[0].SIMChargeID);
		    			$("#effectiveDate").val(otInfo[0].EffectiveDate);
		    			$("#buyDate").val(otInfo[0].BuyDate);
		    			$("#isInUse option[value='" + otInfo[0].IsInUse + "']").attr("selected", true);
		    			//$("#operationID").val(otInfo[0].OperationID);
		    			$("#address").val(otInfo[0].Address);
		    			$("#soundKind").val(otInfo[0].SoundKind);
		    			$("#isNote option[value='" + otInfo[0].IsNote + "']").attr("selected", true);
		    			$("#noteCount").val(otInfo[0].NoteCount);
		    			$("#prepaySum").val(otInfo[0].PrepaySum);
		    			$("#endDate").val(otInfo[0].EndDate);
		    			$("#overSpend").val(otInfo[0].OverSpend);
		    			$("#APN").val(otInfo[0].APN);
		    			$("#IP").val(otInfo[0].IP);
		    			$("#isTwoCities option[value='" + otInfo[0].IsTwoCities + "']").attr("selected", true);
//		    			$("#workUnitID option[value='" + otInfo[0].WorkUnitID + "']").attr("selected", true);
		    			$("#workUnitID1").val(otInfo[0].WorkUnitID);
		    			$("#workUnitIDName").val(otInfo[0].workUnitIDName);
		    			$('#submitBtn').bind('click', doUpdate);
		    		}
		    	}else{
		    		showError();
		    	}
		    },
		    error : function(data) {
		    	showError();
		    }
	    });
	}
}

/**
 * 执行后台方法更新数据
 */
function doUpdate(){
	
	var canSubmit = $("#editWindow").beforeSubmit();
	
	if(canSubmit){
		var params = getAddForm();
		$.ajax({
		    type : "POST",
		    url : "sys/updateSimCard.action",
		    data : {params : $.toJSON(params)},
		    dataType : "JSON",
		    success : function(data) {
		    	if(data!= null){
		    		hide();
		    		$("#simCardList").flexReload();
		    	}else{
		    		
		    		showError();
		    	}
		    },
		    error : function(data) {
		    	showError();
		    }
	    });
	}
}

/**
 * 打开编辑窗口
 */
function showEditForm(){
	show();
	$("#editWindow .errorMsg").closeMessage();
}

/**
 * 显示错误信息
 */
function showError(){
	showWarning('服务器忙，请重试！');
}

/**
 * 显示提示信息
 */
function showWarning(str){
	$.messager.alert('提示信息',str,'info');
}

/**
 * 删除方法入口
 */
function toDelete(){
	var checkedIds = $("#simCardList").getCheckedRows();
	if(checkedIds.length<1){
		showWarning("请选择一行后进行删除操作！");
		return;
	}
	doDelete(checkedIds);
}

/**
 * 表单重置方法入口
 */
function reSetAddForm(){
	resetForm("editWindow");

}
/**
 * 执行后台方法删除数据
 * @param ids
 * @returns {Boolean}
 */
function doDelete(ids){
	
	if (ids != null || ids.length > 0) {
		if (!confirm("确定删除选中的SIM卡信息?")) {
			return false;
		} else {
			$.ajax({
			    type : "POST",
			    url : "sys/deleteSimCard.action",
			    data : {ids:ids.toString()},
			    dataType : "JSON",
			    success : function(data) {
			    	if(data!=null){
			    		$("#simCardList").flexReload();
			    	}else{
			    		showError();
			    	}
			    },
			    error : function(data) {
			    	showError();
			    }
		    });
		return true;
		}
	}
}




