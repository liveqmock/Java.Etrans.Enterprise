<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ include file="/common/flexHead.jsp"%>
<%
			 Calendar calendar=Calendar.getInstance();
				TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");// 获取中国的时区
				calendar.setTimeZone(timeZoneChina);
				calendar.setTimeZone(timeZoneChina);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>"></base>

		<title>历史图片查询</title>
		<link rel="stylesheet" type="text/css" href="<%=basePath%>css/body.css">
		<script type="text/javascript" src="${basePath}js/datepicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${basePath}js/sys/historyImageSelect.js"></script>
		 <script type="text/javascript"	src="${basePath}js/sys/tree/vehicleMaintree.js"></script>
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
	
		<div style="width: 100%;" id="cont_box">
		<div class="main">
		<div class="mon_cont">
			<div class="E_Tit">历史图片查询</div>
			<table  border="0" cellspacing="0" cellpadding="0"
				class="que_tab" >
<%--				<tr>--%>
<%--					<td width="50" align="left">--%>
<%--						<select id="slSearchType" >--%>
<%--							<option value="registrationNo">车牌号</option>--%>
<%--							<option value="gpsTime">定位时间</option>--%>
<%--						</select>--%>
<%--						--%>
<%--					</td>--%>
<%--					<td width="530" align="left">--%>
<%--						<div id="divregistrationNo" class="divSearch"  >--%>
<%--				          	【自动补全】车牌号码--%>
<%--				          	<input id="vehicleIds" type="hidden" name="vehicleIds"/>--%>
<%--				          	<input  id="txtRegistrationNo" name="registrationNo" type="text" class="mon_ser_text" style="width: 120px" --%>
<%--				          		maxlength="30"--%>
<%--				          		ondblclick="showVehicleTree('txtRegistrationNo')"--%>
<%--								onkeyup="doAutoComplete('txtRegistrationNo','name_table','popup','name_table_body')" --%>
<%--								onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" --%>
<%--			                	onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"/>--%>
<%--				        </div>--%>
<%--				        --%>
<%--				        <div id="divgpsTime" style="display: none" class="divSearch" >--%>
<%--				     	   开始时间：--%>
<%--	                         <input type="text" id="beginTime" class="mon_ser_text" style="width:130px" name="beginTime" onFocus="this.blur()" readonly="readonly" value="<%=DateFormatUtils.format(new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()), "yyyy-MM-dd HH:mm:ss") %>"/>--%>
<%--								<img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"--%>
<%--									onclick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('beginTime'),dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'endTime\')}'})"/>--%>
<%--	                   	 结束时间：--%>
<%--						<input type="text" id="endTime" class="mon_ser_text" style="width:130px" name="endTime" onFocus="this.blur()" readonly="readonly" value="<%=DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") %>"/>--%>
<%--							   <img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"--%>
<%--									onclick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('endTime'),dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"/>--%>
<%--					   </td>--%>
<%--				    </div>--%>
<%--					<td   align="left">--%>
<%--				         <a id="searchBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a>--%>
<%--				         <a href="javascript:void(0)" id="exportBtn" class="ser_btn" style="color: white;">导出</a>--%>
<%--					</td>--%>
<%--					--%>
<%--				</tr>--%>

               <tr>
				 <td  width="80" align="right">车牌号码：</td>
			 		<td width="150" align="left">
							<%--【自动补全】车牌号码--%>
							<input id="vehicleIds" type="hidden" name="vehicleIds"/>
							<input id="RegistrationNopram" name="RegistrationNopram" type="text"
								class="mon_ser_text"  style="width:120px"  
								ondblclick="showVehicleTree('RegistrationNopram')"
								onkeyup="doAutoComplete('RegistrationNopram','name_table','popup','name_table_body')" 
								onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
			                    onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))" />
						</td>
			        <td width="80" align="right">开始时间：</td>
	                 <td width="170" align="left">
	                 <input id="beginTime" type="text" class="mon_ser_text" style="width:130px" onFocus="this.blur()" readonly="readonly" value="<%=DateFormatUtils.format(new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()), "yyyy-MM-dd HH:mm:ss") %>"/>
	                 <img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"
								onClick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('beginTime'),dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})"/>
	                 
	                 </td>
	                <td width="80" align="right">结束时间：</td>
	                <td width="170" align="left">
	                <input type="text" id="endTime"  name="endTime" onFocus="this.blur()" style="width:130px" class="mon_ser_text"  readonly="readonly" value="<%=DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss") %>"/>
							   <img src="Images/time.jpg" width="20" height="23" style="margin-left:2px;"
									onclick="WdatePicker({firstDayOfWeek:1,isShowWeek:true,el:document.getElementById('endTime'),dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'beginTime\')}'})"/>
	                </td>
					<td width="150" align="right">
				        <a id="searchBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a>
				    	<a href="javascript:void(0)" id="exportBtn" class="ser_btn" style="color: white;">导出</a>
				    </td>
				
				</tr>
			</table>
			
			<table id="historyImageSelectList" style="display: none"></table>
		</div>
		</div>
		</div>
		<div id="dialogs" icon="icon-save" class="hiddiv" style="display: none;padding:5px;top:10px;">
			<iframe src="" id="dialogFrame" name="dialogFrame" width="100%"  height="100%" frameborder="0" scrolling="auto"></iframe>
		</div>
	</body>
</html>
