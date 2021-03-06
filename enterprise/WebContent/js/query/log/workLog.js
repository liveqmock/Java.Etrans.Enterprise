$(function() {
	//new一个当前时间
	//var nowDate = dateutil.formatDate(new Date(dateutil.setDateBefore(new Date(),0)),dateutil.FORMAT_DATE_LONG);
	//把时间显示到文本框
//	$("#startDate").val(nowDate);
//	$("#endDate").val(nowDate);
	
//	var startDate = nowDate + " 00:00:00";
//	var endDate = nowDate + " 23:59:59";
	
	var startDate = $("#startDate").val(); //开始时间
	var endDate = $("#endDate").val();     //结束时间
	
	//后台查询条件
	var params = [{
		name : 'startDate',
		value : startDate
	} ,{
		name : 'endDate',
		value : endDate
	}];
	
	//报表组件调用
	$("#workDataLog").flexigrid( {
		url : 'query/worklog/getWorkLog.action',
		dataType : 'json',
		colModel : [ //用户名、时间、操作类型、模块、动作、描述
		     		 {
		     			display : '用户名',
		     			name : 'u_name',
		     			width : 100,
		     			sortable : true,
		     			align : 'center'
		     		}, {
		     			display : '时间',
		     			name : 'LogTime',
		     			width : 150,
		     			sortable : true,
		     			align : 'center'
		     		}, {
		     			display : '操作类型',
		     			name : 't_name',
		     			width : 150,
		     			sortable : true,
		     			align : 'center'
		     		},
		     		{
		     			display : '模块名称',
		     			name : 'ModuleName',
		     			width : 150,
		     			sortable : true,
		     			align : 'center'
		     		},
		     		{
		     			display : '动作',
		     			name : 'ActionName',
		     			width : 150,
		     			sortable : false,
		     			align : 'center'
		     		},
		     		{
		     			display : '描述',
		     			name : 'Description',
		     			width : 250,
		     			sortable : false,
		     			align : 'center'
		     		}
		     		],
		     		sortname : "id",//第一次加载数据时排序列
		    		sortorder : "desc",//第一次加载数据时排序类型
		    		params : params,
		    		usepager : true,//是否分页，默认为true。
		    		showTableToggleBtn : true,//是否显示收起/打开按钮,默认不显示。
		    		useRp : true,//是否可以动态设置每页显示的结果数，默认为false。
		    		rp : 8,//每页记录数，默认为10
		    		singleSelect:true,
		    		width : 'auto',//表格宽度
		    		height : getNormalHeight()-20//表格高度
	});
	
	//按钮绑定点击事件
	$('#searchBtn').bind('click').bind('click', doSearch);//查询
    $('#exportBtn').bind('click', toExportExl);//导出
    $('#mimeographBtn').bind('click', mimeographBtn_Go);//打印
    
})

/**
 * 得到查询条件
 * @return
 */
function getParams(){
//	var startDate = $("#startDate").val() + " 00:00:00"; //开始时间
//	var endDate = $("#endDate").val() + " 23:59:59";     //结束时间
	
	var startDate = $("#startDate").val(); //开始时间
	var endDate = $("#endDate").val();     //结束时间
	var moduleName = $("#moduleName").val(); //登录名称
	
	//查询参数
	var params = [{
		name : 'startDate',
		value : startDate
	} ,{
		name : 'endDate',
		value : endDate
	},{
		name : 'moduleName',
		value : moduleName
	} ];
	return params;
}

/**
 * 多条件查询方法
 */
function doSearch()
{
	var params= getParams();
	
	// 重置表格的某些参数
	$("#workDataLog").flexOptions({
				newp : 1,// 设置起始页
				params : params// 设置查询参数
			}).flexReload();// 重新加载
}

/**
 * 导出方法入口
*/
function toExportExl(){
	exportExl('workDataLog','query/worklog/upWorkDataLogExportExl.action');
}

/**
 * 打印入口
 */
function mimeographBtn_Go(){
	
//	var startDate = $("#startDate").val() + " 00:00:00"; //开始时间
//	var endDate = $("#endDate").val() + " 23:59:59";     //结束时间
	
	var startDate = $("#startDate").val(); //开始时间
	var endDate = $("#endDate").val();     //结束时间
	var moduleName = $("#moduleName").val(); //登录名称
	
	//后台查询数据
	$.ajax({
	    type : "POST",
	    url : "query/worklog/getMimeograph.action",
	    data : {startDate:startDate,endDate:endDate,moduleName:moduleName},
	    dataType : "JSON",
	    success : function(data) {
	    	if(data.code=='0'){
	    		alert("没有数据可以打印！");
	    		return false;
	    	}
	    	else if(data.code == '1'){
	    		var dataList = data.data;
	    		var ListStr = "";
	    		ListStr+= "<table style=\"width: 100%\">";
	    		ListStr+="<tr><td>用户名</td><td>时间</td><td>操作类型</td><td>模块名称</td><td>动作</td><td>描述</td></tr>";
	    		for ( var f = 0; f < (dataList.length); f++) {
	    			ListStr+="<tr><td>"+dataList[f].u_name+"</td><td>"+dataList[f].LogTime+"</td><td>"+dataList[f].t_name+"</td><td>"
	    			+dataList[f].ModuleName+"</td><td>"+dataList[f].ActionName+"</td><td>"+dataList[f].Description+"</td></tr>";
	    		}
	    		ListStr+="</table>";
	    		$("#contents").html(ListStr);
	    		/**打印**/
	    		var object = document.getElementById('content');
	    		startPrint(object);
	    	}else{
	    		showError();
	    	}
	    },
	    error : function(data) {
	    	showError();
	    }
    });
}

///***********************  

///打印指定区域页面  

///说明：obj–通过getElementById或其它方式获取标签标识，打印此obj内的文字  

///开发：陆君用  

///日期：2012/6/1

function startPrint(obj)  

{  
  var oWin=window.open("","_blank");  

  var strPrint="<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />\n";
  
  strPrint=strPrint+"<h4 style=’font-size:18px; text-align:center;’>打印预览区</h4>\n";     
  
  
  strPrint=strPrint + "<script type=\"text/javascript\">\n";
  
  strPrint=strPrint + "function printWin()\n";  

  strPrint=strPrint + "{";  

  strPrint=strPrint +    "var oWin=window.open(\"\",\"_blank\");\n";  
	
  strPrint=strPrint + "oWin.document.write(document.getElementById(\"content\").innerHTML);\n";  
  
  strPrint=strPrint + "oWin.focus();\n";  

  strPrint=strPrint + "oWin.document.close();\n";  

  strPrint=strPrint + "oWin.print()\n";  

  strPrint=strPrint + "oWin.close()\n";  

  strPrint=strPrint + "}\n";  

  strPrint=strPrint + "<\/script>\n";  

  strPrint=strPrint + "<hr size='1' />\n";  

  strPrint=strPrint + "<div id=\"content\">\n";  

  strPrint=strPrint + obj.innerHTML + "\n";  

  strPrint=strPrint + "</div>\n";  

  strPrint=strPrint + "<hr size='1' />\n";  

  strPrint=strPrint + "<div style='text- align:center'><button onclick='printWin()' style='padding- left:4px;padding-right:4px;'>打  印</button><button onclick='window.opener=null;window.close();'  style='padding-left:4px;padding-right:4px;'>关  闭</button></div>\n";  

  oWin.document.write(strPrint);  

  oWin.focus();  

  oWin.document.close();  

}  


