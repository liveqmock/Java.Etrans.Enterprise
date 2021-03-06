<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="auth"  uri="/auth-tags"  %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		    <title>普通用户管理</title>
	</head>
	<base href="<%=basePath%>"></base>
	<script type="text/javascript" src="${basePath}js/jq/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${basePath}js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${basePath}js/sys/userManage/userManage.js"></script>
   
	<style type="text/css">
		#wizard {border:1px solid #789;font-size:12px;height:100%;width:100%;overflow:hidden;position:relative;-moz-border-radius:0px;-webkit-border-radius:0px;}
		#wizard .items{width:100%;  height:100%;clear:both; position:absolute;}
		#wizard .right{float:right;}
		#wizard #status{margin:1px; height:30px; line-height:10px; color:#fff; font-weight:600; background:#508AC5;padding-left:10px;}
		
		#status li{float:left;color:#fff;padding:10px 30px;}
		#status li.active{background-color:#369;font-weight:normal;}
		
		
		</style>	
<body >

        	
<div id="wizard">
   <ul id="status">         
    <li id=""><strong>1.</strong>新建用户</li>         
    <li><strong>2.</strong>车辆分组</li> 
    <li><strong>3.</strong>分配权限</li> 
    <li><a  id="serA" href="javascript:void(0);"><strong>查看用户信息</strong></a></li>           
   </ul> 
	<div  id="userDiv">
             <iframe src="<%=basePath%>sys/userManage/userList.jsp"  style="height:380px;" id="userFrame" name="userFrame" width="100%" style="height:100px;" frameborder="0" scrolling="no"></iframe>
             <div class="btn_nav">
                <input type="button" id="userbnt" value="下一步&raquo;" />
                
             </div>
          </div>
          <!--
          <div id="roleDiv" style="display: none;">
             <iframe src="sys/userManage/roleList.jsp"  style="height:380px;" id="roleFrame" name="roleFrame" width="100%" style="height:100px;" frameborder="0" scrolling="yes"  scrolling="auto"></iframe>
             <div class="btn_nav">
                 <input type="button"  id="rolebntBack" value="&laquo;上一步" />
                 <input type="button"  id="rolebntNext" value="下一步&raquo;" />
             </div>
          </div>
	-->
	<div id="vehilceGroupDiv" style="display: none;">
             <iframe src="<%=basePath%>sys/userManage/vehilceGroupList.jsp"  style="height:380px;" id="vehilceGroupFrame" name="vehilceGroupFrame" width="100%" style="height:100px;" frameborder="0" scrolling="no" ></iframe>
             <div class="btn_nav">
                <input type="button" id="vehilceGroupBntBack" value="&laquo;上一步" />
                <input type="button" id="vehilceGroupBntNext" value="下一步&raquo;" />
             </div>
          </div>
	
	
	<div id="authorityDiv" style="display: none;">
             <iframe src="<%=basePath%>sys/userManage/authorityList.jsp"  style="height:380px;" id="authorityFrame" name="authorityFrame" width="100%" style="height:100px;" frameborder="0" scrolling="auto" ></iframe>
             <div class="btn_nav">
                <input type="button" id="authorityBntBack" value="&laquo;上一步" />
                <input type="button" id="authorityBnt" value="确定" />
             </div>
          </div>
	<div id="serchUserInfos" style="display: none;width:100%;height:100%;">
             <iframe id="showUserInfo" src="sys/userManage/userListInfo.jsp"  style="height:395px; width:100%;"   frameborder="0" scrolling="no"  ></iframe>
    </div>
 </div>
   <input type="hidden" id="userId">
   <input type="hidden" id="workUnitIds">
   <input type="hidden" id="roleId">
   <input type="hidden" id="workUnitName">
   <input type="hidden" id="groupId">
   <input type="hidden" id="vehicleGroupId">
</body>
</html>