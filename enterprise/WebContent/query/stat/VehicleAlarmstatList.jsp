<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/flexHead.jsp"%>


 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
        <title>车辆报警统计</title>
        <script type="text/javascript" src="${basePath}js/query/stat/VehicleAlarmstatList.js"></script>
		<script  type="text/javascript" src="${basePath}js/datepicker/WdatePicker.js"></script>
        <script type="text/javascript" src="${basePath}js/common/dateutil.js"></script>
        <script type="text/javascript" src="${basePath}fusionCharts/JSClass/FusionCharts.js"></script>
        <script type="text/javascript"	src="${basePath}js/sys/tree/tree.js"></script>	
        <%--【自动补全】begin--%>
        <script type="text/javascript" src="${basePath}js/common/autoComplete.js"></script>
        <link rel="stylesheet" type="text/css" href="${basePath}css/autoComplete.css">
        <%--【自动补全】end--%>


</head>

<%--onclickAll 【自动补全】--%>
<body onclick="onclickAll();">
	<%--【自动补全】begin--%>
    <div style="position:absolute; overflow:auto; scroll;height: 143px; width: 120px;" id="popup">
        <table  id="name_table" bgcolor="#FFFAFA" border="0" cellspacing="0" cellpadding="0"/>            
            <tbody id="name_table_body" style="font-size: 13px;"></tbody>
        </table>
    </div>
	<%--【自动补全】end--%> 
<div id="cont_box">
	<div class="main">
        <div class="mon_cont" id="gridBox">
        	<div class="E_Tit">车辆报警统计</div>
        	   <table  border="0" cellspacing="0" cellpadding="0" class="que_tab">
	              <tr>
	                <td width="70" align="right">车牌号码：</td>
	                <td width="150" align="left">
	                <%--【自动补全】车牌号码--%>
	                <input id="registrationNO" name="registrationNO" type="text" class="mon_ser_text" style="width: 130;"
	                                              maxlength="30"
											      style="width:120px"
											      onkeyup="doAutoComplete('registrationNO','name_table','popup','name_table_body')" 
											      onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
							                      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"></td>
	                <td width="70" align="right">开始时间：</td>
	                <td width="150" align="left">
	                <input id="startDate" type="text" class="mon_ser_text"  onFocus="this.blur()" readonly="readonly" />
	                  <img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"
								onClick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('startDate'),dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-{%d-1}'})"/>
	                
	                </td>
	                <td width="80" align="right">结束时间：</td>
	                <td width="150" align="left">
	                <input id="endDate"  type="text" class="mon_ser_text" onFocus="this.blur()" readonly="readonly" />
	                <img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"
							onClick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('endDate'),dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-{%d-1}'})"/>
	                
	                </td>
	                
	                <td width="300"> 
		                <a id="searchBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a><!--
		                <a id="showBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">图表</a>
			            --><a id="exportBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">导出</a>
			            <a id="adSearchBtn" href="javascript:void(0)"  style="margin-left:8px;">高级搜索</a></td>
		             <td></td>
		            </tr>
	                  
               </table>
        	
        	 <div id="adSearch" style="display: none;">
        	 <table  border="0" cellspacing="0" cellpadding="0" style="margin-bottom: 10px">
        	    <tr>
		                <td width="70" align="right">报警类型：</td>
		                <td width="150" align="left">
		                <select id="alarmTypeSel" class="sel_w"></select></td>
		                <td width="70" align="right">所属单位：</td>
	                    <td width="150" align="left"><input 
	                          id="workUnitNameParam" ondblclick="showWorkUnitTree()"
                              name="workUnitName" type="text" class="mon_ser_text" style="width: 130;"
                              maxlength="30"
						      onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
		                      onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"></td>
		                 <td colspan="4"></td>
		        </tr>
        	 </table>
        	 </div>
             
              <div id="VehicleAlarmChart"  style="display: none;width: 1020px; height: 345px; border: 1px outset #EEEEEE; padding-top: 2px; overflow-y: scroll; overflow-x: scroll;"></div> 
          
    </div>
</div>
</div>

 <div id="dialogs" class="hiddiv" style="display: none;padding:5px;top:10px;">
			<iframe src="" id="dialogFrame" name="dialogFrame" width="100%"  height="100%" frameborder="0" scrolling="auto"></iframe>
		</div>
</body>
</html>