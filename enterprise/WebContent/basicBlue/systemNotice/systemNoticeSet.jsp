<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="com.etrans.bubiao.sys.UserContext"%>
<%@ include file="/common/validateHead.jsp"%>
<%@taglib prefix="auth"  uri="/auth-tags"  %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>系统公告设置</title>
		<script type="text/javascript" src="${basePath}js/sys/systemNotice/systemNoticeSet.js"></script>
		<%
			 String userRoot=String.valueOf(((SessionUser) request.getSession().getAttribute(Constants.LOGIN_USER)).getIsSuperUser());
		      boolean isRoot = Boolean.parseBoolean(userRoot);
		      boolean workUnitSuperAdmin =((SessionUser) request.getSession().getAttribute(Constants.LOGIN_USER)).isWorkUnitSuperAdmin();
		%>
	</head>
            <script type="text/javascript">
		    	var isBsRoot = <%=isRoot%>;
		    	var isWorkUnitSuperAdmin=<%=workUnitSuperAdmin%>;
		    </script>
	<body>
	
		<div style="width: 100%" id="cont_box">
		<div class="main">
		<div class="mon_cont">
			<div class="E_Tit">系统公告设置</div>
			<div id="adSearch">
				<table border="0" cellspacing="0" cellpadding="0" class="que_tab">
					<tr>
						<td width="100" align="right">标题:</td>
						<td width="150" align="left">
							<input id="sname" name="sname" type="text" class="mon_ser_text" style="width: 150;"
								maxlength="30"
								onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
				                onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"/>
						</td>
						<td width="60">
							<a id="searchBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a>
						</td>
						 <%if(isRoot){%> 
							<auth:authorize operation="createSimCard">
							<td width="60">
								<a id="createBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">新增</a>
							</td>
							</auth:authorize>
						<%}%>
					</tr>
				</table>
			</div>
			<div id="editWindow" class="wDiv" style="width:100%;display: none;border: 1px solid #d0d0d0;">
				<div class="td_title" id="titleInfo">编辑系统公告设置</div>
				<table width="100%">
					<tr>
					   
					    <td width="120" align="right"><span class="xin_red">*</span>标题：</td>
						<td width="350" align="left">
						    <input type="hidden" name="id" id="id" class="td_input" size="50"/>
						     <input type="hidden" name="isDefault" id="isDefault" class="td_input" size="50"/>
							<input type="text" name="systemNoticeName" id="systemNoticeName" size="150"  class="td_input" style="width: 430px;"
								formCheck="true" 
								required="true" requiredError="请输入系统公告！" 
								ajaxAction="systemNotice/getSystemNoticeByName.action" 
								ajaxDataId="id" 
								ajaxActionError="已存在此名称的外设类别，请重新输入！">
							<span id="systemNoticeNamespan" class="errorMsg" style="display: none"></span>
						</td>
						
						
						
						<td rowspan="2" align="center" width="200">
							<a id="submitBtn" href="javascript:void(0)" class="ser_btn" style="margin-bottom: 3px;">提交</a><br/>
							<a id="cancelBtn" href="javascript:void(0)" class="ser_btn" style="margin-bottom: 3px;">取消</a>
						</td>
					</tr>
					
					  <tr>
				                
					  <td align="right" width="120">内容：</td>
						<td align="left" width="240" >
							<textarea id="systemNoticeContent" name="systemNoticeContent" cols="52" rows="2" class="pre" style="text-align: left; height:150px; width:430px" 
							formCheck="true" textLength="0-4000" valLengthError="请输入少于4000个字符!"></textarea>
							<span id="systemNoticeContentspan" class="errorMsg" style="display: none"></span>
						</td>
			           </tr>
					
				</table>
			</div>
			 <table id="systemNoticeList" style="display: none"></table>
			</div>
		</div>
		</div>
	</body>
</html>
