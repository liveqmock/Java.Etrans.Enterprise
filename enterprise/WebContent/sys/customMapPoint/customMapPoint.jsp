<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/validateHead.jsp"%>
<%@taglib prefix="auth"  uri="/auth-tags"  %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>兴趣点设置</title><script type="text/javascript" src="${basePath}js/common/HashMap.js"></script>
		<script type="text/javascript" src="${basePath}js/sys/customMapPoint/customMapPoint.js"></script>	
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.4"></script>
	<style type="text/css">
	<!--
	body{overflow:hidden;}
	.anchorBL{
	display:none;
	}
	-->
	</style>
	</head>
	<body>
	
		<div style="width: 100%" id="cont_box">
		<div class="main">
		<div class="mon_cont">
			<div class="E_Tit">兴趣点设置</div>
			<div id="adSearch">
		<table border="0" cellspacing="0" cellpadding="0" class="que_tab" >
              <tr>
                <td width="80" align="right">名称：</td>
                <td width="120" align="left">
                <input id="txName"  type="text" class="mon_ser_text" 
	                style="width: 130px"
	                maxlength="50"
					onchange="value=value.replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,'')" 
	                onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^0-9a-zA-Z\u4e00-\u9fa5]/g,''))"/>
                </td>
                <td width="80" align="right">类型：</td>
                <td width="120" align="left">
                    <select id=nameType  style="width:120px;"></select>
                    <input type="hidden" id="imageURL"  >
                </td>
                <td width="350">
                    <!--<a id="CMPBtn" href="javascript:void(0)" onclick="getCMP()" class="ser_btn" style="color: white;" >兴趣点</a>&nbsp;&nbsp;
                	-->
                	<a id="btnSearch" href="javascript:void(0)" class="ser_btn" style="color: white;">查询</a>&nbsp;&nbsp;
                   <auth:authorize operation="deleteEntCustomMapPoint">
                    <a id="deleteBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">删除</a> &nbsp;&nbsp;
                    </auth:authorize>
                    <auth:authorize operation="getEntCustomMapPointByIdList">
                    <a id="showBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">显示</a>  &nbsp;&nbsp;
                    </auth:authorize>
                     <auth:authorize operation="getEntCustomMapPointByIdList">
                     <a id="delCMPBtn" href="javascript:void(0)" class="ser_btn" style="color: white;">移除</a> 
                     </auth:authorize>
                </td>
                </tr>
            </table>
			</div>
			
			<div>
			   <table>
			     <tr>
			       <td width="50%">
			          <div id="cpMap"></div>
			       </td>
			       <td width="50%">
			           <table id="customMapPointList" style="display: none"></table>
			       </td>
			     </tr>
			   </table>
			</div>
			
		
			 <div id="dialogs" class="hiddiv" style="display: none;padding:5px;top:100px;">
				<iframe src="" id="dialogFrame" name="dialogFrame" width="100%"  height="100%" frameborder="0" scrolling="auto"></iframe>
		     </div>
		
			
		</div>
	</div>
 </div>
</body>
</html>
