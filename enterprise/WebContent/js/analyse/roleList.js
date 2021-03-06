$(function(){

	/**
	 * 查询
	 */
	$("#btnSearch").click(searchRoles);
	
	/**
	 * 打开关闭添加面板
	 */
	$("#btnAddRolePanel").click(function(){showOrClosePanel("divAddRolePanel");});
	
	$("#btnUpdateRole").click(doEdit);
	$("#btnUpdateRoleCancle").click(function(){jsUtil.showOrClosePanel("divUpdateRolePanel");});
	
	//初始化验证插件
	$("#addForm").validation();
	$("#editFrom").validation();
	
	$('#createBtn').bind('click', toCreate);
	$('#cancelBtn').bind('click', hide);
	$('#editBtn').bind('click', toEdit);
	$('#cancelBtnEdit').bind('click', hideEdit);
	
	/**
	 * 加载角色列表
	 */
	findRoles();
});



/**
 * 新增加方法入口
 */
function toCreate(){
	$("#submitBtn").unbind("click");
	clearForm("divAddRolePanel");
	showEditForm();
	$("#submitBtn").bind("click",create); 
}

function create(){
	var flag = $("#addForm").beforeSubmit();
    if(flag){
    var roleName = $("#txtAddRoleName").val();
    var shortRoleName = $("#shortRoleName").val();
    var workUnitId = null;
    try{
    	workUnitId =  $("#WorkUnitID").val();
    }catch(e){}
    var state =  $("#status").val();
	$.ajax({
	    type : "POST",
	    url : "sys/role/createRole.action",
	    data : {roleName:roleName,shortRoleName:shortRoleName,workUnitId:workUnitId,state:state},
	    dataType : "JSON",
	    success : function(data) {
	    	if(data.code == '0'){
	    		 hide();
	    		$("#tbRoles").flexReload();
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
	var checkedIds = $("#tbRoles").getCheckedRows();
	
	if(checkedIds.length<1){
		$.messager.alert('提示信息','请选择一行后进行编辑操作！','info');
		return;
	}
	if(checkedIds.length>1){
		$.messager.alert('提示信息','只能选择一行进行编辑操作！','info');
		return;
	}
	if(checkedIds.length == 1){
		doEdit(checkedIds[0]);
	}
}



function doEdit(id){
	hide();
	showEdit();
	loadRoleById(id);
}

/**
 * 加载单个角色信息
 * @param id
 */
function loadRoleById(id)
{
	$("#submitbtnEidt").unbind("click");
	jsUtil.defaultAjax(
			"sys/role/findRole.action", 
			{id:id},
			function(data)
			{	
				$("#upddateShortRoleName").val(data.abbre);
				$("#txtUpdateRoleName").val(data.name);
				$("input[name='Status'][value="+data.Status+"]").attr("checked",true);
				$("#txtRoleId").val(id);
				$('#submitbtnEidt').bind('click', doUpdate);
				checkedSelectByValue("UpdateWorkUnitID",data.workUnitId);
				checkedSelectByValue("Status",data.status);
			},
			function(data) {
		    	showError();
		    }
		);

}
function checkedSelectByValue(ID,Value){
	$("#"+ID+" option").each(function() {  
		if ($(this).val() == Value) {  
		$(this).attr("selected", "selected");  
			return false;
		}  
	});     
}

function doUpdate(){
	var flag = $("#editFrom").beforeSubmit();
	if(flag){
		var id = $("#txtRoleId").val();
		var name = $("#txtUpdateRoleName").val();
		var shortName = $("#upddateShortRoleName").val();
		var status = $("#Status").val();
		var workUnitId =  $("#UpdateWorkUnitID").val();
		$.ajax({
		    type : "POST",
		    url : "sys/role/editRole.action",
		    data : {"role.id":id,"role.name":name,"role.status":status,"role.abbre":shortName,"role.workUnitId":workUnitId},
		    dataType : "JSON",
		    success : function(data) {
		    	if(data.code == '0'){
		    		hideEdit();
		    		$("#tbRoles").flexReload();
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



//新增
function openAddDialog(id) {
	$("#dialogFrame").attr("src", "analyse/configRole.jsp?roleId="+id);
	openDialog("dialogs",320, 460, '权限管理');
}


//打开一个对话框
function openDialog(id, widthValue, heightValue, titleValue) {
    $("#" + id).css("display", "block");
	$("#" + id).dialog( {
		width : widthValue,
		height : heightValue,
		title : titleValue,
		maximizable : false
	});
}


/**
 * 查询用户角色
 */
function findRoles(){
	$("#tbRoles").flexigrid( {
		url : 'sys/role/findRoles.action',
		dataType : 'json',
		colModel : 
			[
		    {
			display : '角色名称',//表头
			name : 'Name',//序号列为固定值fid
			width : 180,// 得加上，要不IE报错
			sortable:false,// 序号列不能排序
			align : 'left'//对齐方式
		}, 
		
		  {
			display : '角色状态',//表头
			name : 'Status',//序号列为固定值fid
			handlefunction : 'showStatus',
			paramcolnames : ['Status'],
			width : 120,// 得加上，要不IE报错
			sortable:false,// 序号列不能排序
			align : 'center'//对齐方式
		}, 
		{
			display :'创建时间',//表头
			name : 'CreateDate',//JSON数据中属性名
			width : 200,// 得加上，要不IE报错
			sortable : true,//此列是否能排序
			align : 'center'//对齐方式
		},  
		{
			display :'创建者',//表头
			name : 'userName',//JSON数据中属性名
			width : 120,// 得加上，要不IE报错
			sortable : false,//此列是否能排序
			align : 'left'//对齐方式
		}, 
		/*{
			display :'权限设置',//表头
			name : 'authority',//JSON数据中属性名
			handlefunction : 'showAuthorityLink',
			paramcolnames : ['ID'],
			width : 120,// 得加上，要不IE报错
			sortable : false,//此列是否能排序
			align : 'center'//对齐方式
		},*/
		{
			display : '操作',
			name : 'Handler',
			handlefunction : 'getHandleColumn',
			paramcolnames : ['ID'],
			width : 350,
			sortable : false,//操作列不能排序
			align : 'center'
		} ],
		sortname : "ID",//第一次加载数据时排序列
		sortorder : "desc",//第一次加载数据时排序类型
		usepager : true,//是否分页，默认为true。
		showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
		useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
		rp : 8,//每页记录数，默认为10
		checkbox : false,//是否要多选框,默认为false。
		rowId : 'ID',// 多选框绑定行的id,只有checkbox : true时才有效。
		singleSelect:false,
		width : 'auto',//表格宽度
		height : getBaseHeight()//表格高度
	});
	
}

/**
 * 组装操作列显示内容
 * @param id
 * @returns {String}
 */
function getHandleColumn(ID){
	var authorityLinkStr = "";
	var editStr = "";
	var deleteStr = "";
	if(resources!=null){
		if(resources.indexOf("|createRole|")!=-1){
			authorityLinkStr = '<a href="javascript:void(0)" title="权限管理" onclick="openAddDialog(' + ID + ')">权限管理</a>';
		}
		 if(resources.indexOf("|editRole|")!=-1){
			    editStr = '<a href="javascript:void(0)" title="编辑"  onclick="doEdit(' + ID + ')">编辑</a>';
			 }
		if(resources.indexOf("|deleteRole|")!=-1){
			deleteStr = '<a href="javascript:void(0)" title="删除" onclick="deletecase(' + ID + ')">删除</a>';

		}
	}
	return authorityLinkStr + '&nbsp;&nbsp;' + editStr+ '&nbsp;&nbsp;' + deleteStr;
}


/**
 * 打开或者关闭添加角色面板
 */
function showOrClosePanel(pannelId)
{
	$("#"+pannelId).slideToggle(500);
}



/**
 * 角色查询
 */
function searchRoles()
{
	var name = $("#txtRoleName").val();
	
	//查询参数
	var params = 
	[{
		name : 'name',
		value : name
	}];
	
	reloadDate(params);
}



/**
 * 重新加载列表
 * @param params
 */
function reloadDate(params)
{
	if(params)
	{
		// 重置表格的某些参数
		$("#tbRoles").flexOptions({
					newp : 1,// 设置起始页
					params : params// 设置查询参数
				}).flexReload();// 重新加载
	}
	else
	{
		// 重置表格的某些参数
		$("#tbRoles").flexOptions({
					newp : 1// 设置起始页
				}).flexReload();// 重新加载
	}
	
}



/**
 * 状态装换
 * @param stauts
 * @returns
 */
function showStatus(stauts)
{
	
	return stauts=="false"?"失效":"正常";

}


/**
 * 显示分配功能的链接
 * @param roleId
 * @returns {String}
 */
function showAuthorityLink(roleId)
{
	return  "<a href='javascript:void(0)' onclick='openAddDialog("+roleId+")'>分配功能</a>&nbsp;&nbsp;";
}


function  roleAuthority(roleId)
{
	
	$("#functionFrame").attr("src", "functionMenu.jsp?id=" + roleId);
	$("#authDialog").dialog( {
		width : 800,
		height : 400,
		title : '分配功能'
	});
	
	$("#authDialog").css("display", "block");
	$(".dialog-content").css("display", "block");
	
	
	
}



/**
 * 删除
 */
function deletecase(id)
{
	
	if(confirm("确定删除该角色"))
	{
		jsUtil.useAjax(
				"sys/role/deleteRole.action", 
				{"role.id":id},
				function(msg){
					if(msg=="SUCCESS")
					{
						 reloadDate();
					}
				},
				"text",
				null,
				function(){
					$.messager.alert('提示信息','删除失败！','info');
				}
			);
	}
	
}




/**
 * 打开编辑窗口
 */
function showEditForm(){
	hideEdit();
	show();
	$("#addForm .errorMsg").closeMessage();
}


/**
 * 显示错误信息
 */
function showError(){
	showWarning('服务器忙，请重试！');
}

