<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ include file="/common/flexHead.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>操作日志</title>
		<script type="text/javascript" src="${basePath}js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${basePath}js/query/log/workLog.js"></script>
		<script type="text/javascript" src="${basePath}js/common/dateutil.js"></script>
<%--		<link rel="stylesheet" href="${basePath}cs/mimeograph.css" type="text/css">--%>
	</head>
	
<!--	<script type="text/javascript">-->
<!--		function onssss(){-->
<!--			alert(1);-->
<!--		}-->
<!--	</script>-->
	<body>
	
		<div style="width: 100%;" id="cont_box">
		<div class="main">
		<div class="mon_cont">
			<div class="E_Tit">操作日志</div>
			<table border="0" cellspacing="0" cellpadding="0" class="que_tab" style="margin-bottom: 10px">
				<tr>
<!--					<td width="80" align="right">开始时间：</td>-->
<!--					<td width="150" align="left">-->
<!--						<input type="text" id="startDate" name="startDate" class="mon_ser_text" onFocus="this.blur()" readonly="readonly"/>-->
<!--						<img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"-->
<!--							onClick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('startDate'),dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endDate\')}'})"/>-->
<!--					</td>-->
<!--					<td width="80" align="right">结束时间：</td>-->
<!--					<td width="150" align="left">-->
<!--						<input type="text" id="endDate" name="endDate" class="mon_ser_text" onFocus="this.blur()" readonly="readonly"/>-->
<!--						<img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"-->
<!--							onClick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('endDate'),dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'startDate\')}'})"/>-->
<!--					</td>-->
					
					<td width="70" align="right">开始时间:</td>
	                     <td width="170" align="left">
	                         <input type="text" id="startDate" class="mon_ser_text" name="startDate" onFocus="this.blur()" readonly="readonly" style="width: 130px" value="<%=DateFormatUtils.format(new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()), "yyyy-MM-dd HH:mm:ss") %>"/>
							<img src="Images/time.jpg" width="18" height="23" style="margin-left:2px;"
								onclick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('startDate'),dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
	                     </td>
						 <td width="60" align="right">结束时间:</td>
	                     <td width="170" align="left">
						<input type="text" id="endDate" class="mon_ser_text" name="endDate" onFocus="this.blur()" readonly="readonly" value="<%=DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") %>" style="width: 130px"/>
						    <img src="Images/time.jpg" width="18" height="23" style="margin-left:2px;"
								onclick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('endDate'),dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startDate\')}'})"/>
					   </td>
					   
					<td width="80" align="right">模块名称：</td>
					<td width="150" align="left">
						<input type="text" id="moduleName" name="moduleName" class="mon_ser_text" style="width: 130;"
							maxlength="30"
							onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
			                onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"/>
					</td>
					<td width="200">
						<a id="searchBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a>
						<a id="mimeographBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">打印</a>
<%--						<a id="exportBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">导出</a>--%>
					</td>
				</tr>
			</table>
			<!-- 列表 -->
			<table id="workDataLog" style="display: none"></table>
			<!-- 打印 class="tabp"-->
			<div id="content" style="display: none;">
				<div id="contents" style="border-color:#FFFFFF; border-style: solid;   border-top-width: 5px;   border-right-width: 50px;   border-bottom-width: 5px;   border-left-width: 50px;" ></div>
			</div>
			
		</div>
		</div>
		</div>
	</body>
	
	
</html>
