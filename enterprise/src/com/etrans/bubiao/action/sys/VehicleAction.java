/**
 * VehicleAction.java
 * Create on 2012-4-25 13:37:52
 * Copyright (c) 2012 by e_trans.
 */
package com.etrans.bubiao.action.sys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.BaseAction;
import com.etrans.bubiao.action.sys.log.LogActionTypes;
import com.etrans.bubiao.action.sys.log.LogUtil;
import com.etrans.bubiao.auth.SessionUser;
import com.etrans.bubiao.entities.Result;
import com.etrans.bubiao.services.sys.VehicleServices;
import com.etrans.bubiao.sys.Constants;
import com.etrans.bubiao.sys.UserContext;
import com.etrans.common.util.FlexiGridUtil;
import com.etrans.common.util.json.JSONUtil;
import com.etrans.common.util.web.Struts2Utils;


@Controller
@Scope("prototype")
@Namespace("/sys")
public class VehicleAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private VehicleServices vehicleServices;

	public VehicleServices getVehicleServices() {
		return vehicleServices;
	}

	public void setVehicleServices(VehicleServices vehicleServices) {
		this.vehicleServices = vehicleServices;
	}

	/**
	 * 车辆分页多条件查询
	 */
	@Action(value = "vehicleList")
	public void vehicleList() {
		
		try {
			Map<String,Object> params = FlexiGridUtil.parseParam(this.getGridParams());
			SessionUser user = UserContext.getLoginUser();
			if(user != null){
				if(UserContext.isBsRootUser()){
					params.put("isSuper", true);
				}else if(user.isWorkUnitSuperAdmin()){
					String fullId = user.getWorkUnitFullId();
					params.put("fullId", fullId);
					params.put("isWorkUnitSuperAdmin", true);
				}else{
					params.put("userId", user.getUserID());
				}
			 }
			
			this.renderJSON(JSONUtil.toJson(vehicleServices.getVehicles(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 导出车辆列表到EXCEL
	 */
	@Action(value = "vehicleExport")
	public void vehicleExport() {
		
		Map<String,Object> params = FlexiGridUtil.parseParam(this.getGridParams());
		params = this.getExportParams(params);
		SessionUser user = UserContext.getLoginUser();
		if(user != null){
			if(UserContext.isBsRootUser()){
				params.put("isSuper", true);
			}else if(user.isWorkUnitSuperAdmin()){
				String fullId = user.getWorkUnitFullId();
				params.put("fullId", fullId);
				params.put("isWorkUnitSuperAdmin", true);
			}else{
				params.put("userId", user.getUserID());
			}
		 }
		try {
			
			String[] titleArray = {};
			titleArray = new String[17];
			titleArray[0]="车牌号码";
			titleArray[1]="车牌颜色";
			titleArray[2]="企业名称";
			titleArray[3]="所属区域";
			titleArray[4]="行业类型";
			titleArray[5]="车辆类型";
			
			titleArray[6]="车牌类型";
			titleArray[7]="车辆颜色";
			titleArray[8]="终端类型";
			titleArray[9]="通信号";
			titleArray[10]="SIM卡号";
			
			titleArray[11]="所属车队";
			titleArray[12]="第一司机";
			titleArray[13]="道路运输证号";
			titleArray[14]="经营许可证号";
			titleArray[15]="使用状态";
			titleArray[16]="盲区处理";
			
			//titleArray[2]="车辆分类";
			//titleArray[5]="平台名称";
			
			String[] columnArray = {};
			columnArray = new String[17];
			columnArray[0]="registrationNo";
			columnArray[1]="noColor";
			columnArray[2]="workUnitName";
			columnArray[3]="areaName";
			columnArray[4]="tradeName";
			columnArray[5]="kindName";
			
			columnArray[6]="registrationNoKindName";
			columnArray[7]="vColor";
			columnArray[8]="terminalTypeName";
			columnArray[9]="commNo";
			columnArray[10]="simNo";
			
			columnArray[11]="vehicleTeamName";
			columnArray[12]="firstDriver";
			columnArray[13]="transportPermits";
			columnArray[14]="licenseNo";
			columnArray[15]="workStatusStr";
			columnArray[16]="isBlindStr";
			
			//columnArray[2]="customerTrade";
			//columnArray[5]="platformName";
			List<Map<String,Object>> rows = vehicleServices.getVehicleList(params);
			exportExl("vehicleList", titleArray, columnArray, rows);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 车牌唯一验证
	 */
	@Action(value = "checkRegistrationNo")
	public void checkRegistrationNo() {
		
		String name = getParameter("name"); 
		String id = getParameter("id"); 
		
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("name", name);
		whereMap.put("id", id);
		
		try {
			this.renderJSON(JSONUtil.toJson(vehicleServices.checkRegistrationNO(whereMap)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 车辆新增
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "createVehicle")
	public void createVehicle() {
		
		
		String jsonParams = getParameter("params"); 
		Map<String, Object> params = JSONUtil.fromJson(jsonParams, Map.class);
		params = putUserParams(params);
		SessionUser user = UserContext.getLoginUser();
		Result result = new Result();
		if(user != null){
			if(UserContext.isBsRootUser()){
				params.put("isSuper", "1");
			}else if(user.isWorkUnitSuperAdmin()){
				String fullId = user.getWorkUnitFullId();
				params.put("fullId", fullId);
				params.put("isSuper", "1");
			}else{
				params.put("userId", user.getUserID());
				params.put("isSuper", "0");
			}
		 }
		try {
			vehicleServices.createVehicle(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
			LogUtil.insertLog(LogActionTypes.INSERT, "成功", "车辆管理", "", "新增车辆信息");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.INSERT, "失败", "车辆管理", "", "新增车辆信息");
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
		
	}
	
	
	/**
	 * 查询车辆详细信息
	 */
	@Action(value = "getVehicleById")
	public void getVehicleById() {
		
		String id = getParameter("id"); 
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", id);
		
		Result result = new Result();
		try {
			Map<String,Object> vehicle = vehicleServices.getVehicleById(params);
			result.setCode(1);
			result.setData(vehicle);
			this.renderJSON(JSONUtil.toJson(result));
			LogUtil.insertLog(LogActionTypes.READ, "成功", "车辆管理", "", "查询车辆详细信息");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.READ, "失败", "车辆管理", "", "查询车辆详细信息");
			e.printStackTrace();
		}
		
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	
	/**
	 * 车辆修改
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "updateVehicle")
	public void updateVehicle() {
		
		String jsonParams = getParameter("params"); 
		Map<String, Object> params = JSONUtil.fromJson(jsonParams, Map.class);
		params = putUserParams(params);
		
		Result result = new Result();
		try {
			vehicleServices.updateVehicle(params);
			/**修改内存中的车辆信息**/
			updateVehicleInfoNC(params);
			
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
			LogUtil.insertLog(LogActionTypes.UPDATE, "成功", "车辆管理", "", "编辑车辆信息");

		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.UPDATE, "失败", "车辆管理", "", "编辑车辆信息");
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	
	/**
	 * 车辆删除
	 */
	@Action(value = "deleteVehicle")
	public void deleteVehicle() {
		
		String id = getParameter("id"); 
		String registrationNo = "["+getParameter("registrationNo")+"]";
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", id);
		params.put("registrationNo", registrationNo);
		SessionUser user = UserContext.getLoginUser();
		params = putUserParams(params);
		if(user != null){
			if(UserContext.isBsRootUser()){
				params.put("isSuper", "1");
			}else if(user.isWorkUnitSuperAdmin()){
				String fullId = user.getWorkUnitFullId();
				params.put("fullId", fullId);
				params.put("isSuper", "1");
			}else{
				params.put("userId", user.getUserID());
				params.put("isSuper", "0");
			}
		 }
		Result result = new Result();
		try {
			vehicleServices.deleteVehicle(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
			LogUtil.insertLog(LogActionTypes.DELETE, "成功", "车辆管理", "", "删除车辆信息");

		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.DELETE, "失败", "车辆管理", "", "删除车辆信息");
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	/**
	 * 暂停车辆
	 */
	@Action(value = "pauseVehicle")
	public void pauseVehicle() {
		
		String id = getParameter("id");
		String workStatus = getParameter("workStatus");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", id);
		params.put("workStatus", workStatus);
		params = putUserParams(params);
		
		Result result = new Result();
		try {
			vehicleServices.updateVehicleWorkStatus(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	/**
	 * 启用车辆
	 */
	@Action(value = "restartVehicle")
	public void restartVehicle() {
		
		String id = getParameter("id");
		String workStatus = getParameter("workStatus");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", id);
		params.put("workStatus", workStatus);
		params = putUserParams(params);
		
		Result result = new Result();
		try {
			vehicleServices.updateVehicleWorkStatus(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	/**
	 * 车辆更新使用状态
	 */
	@Action(value = "updateVehicleWorkStatus")
	public void updateVehicleWorkStatus() {
		
		String id = getParameter("id");
		String workStatus = getParameter("workStatus");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", id);
		params.put("workStatus", workStatus);
		params = putUserParams(params);
		
		Result result = new Result();
		try {
			vehicleServices.updateVehicleWorkStatus(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	
	/**
	 * 更新内存车辆信息
	 * params 更新参数
	 */
	@SuppressWarnings("unchecked")
	public void updateVehicleInfoNC(Map<String, Object> params){
		if(params.get("vehicleId").toString()==null||params.get("vehicleId").toString().equals("")){
			return;
		}
		/**修改内存的车辆**/
		Map<String, String> vehicleMap=(HashMap<String, String>)Struts2Utils.getSessionAttribute(Constants.USER_VEHICLE);
		String vehicleInfoString=vehicleMap.get(params.get("vehicleId").toString());
		if(vehicleInfoString==null){return;}
		/**内存原始数据**/
		String[] vehicleValue=vehicleInfoString.split("\\|");//车牌号|车牌颜色|所属行业|所属业户|sim卡号
		/**修改后的数据**/
		String registrationNo= params.get("registrationNo").toString().equals("") ? vehicleValue[0]:params.get("registrationNo").toString();
		String registrationNoColorName = params.get("registrationNoColorName").toString().equals("") ? vehicleValue[1]:params.get("registrationNoColorName").toString();
		String workUnitIDName = params.get("workUnitIDName").toString().equals("") ? vehicleValue[2]:params.get("workUnitIDName").toString();
		String tradeKindName = params.get("tradeKindName").toString().equals("")? vehicleValue[3]:params.get("tradeKindName").toString();
		String simParam= params.get("simParam").toString().equals("")? vehicleValue[4]:params.get("simParam").toString();
		
		String NoColor = registrationNo+"|"+ registrationNoColorName +"|"+workUnitIDName+"|"+tradeKindName+"|"+simParam;
		vehicleInfoString = NoColor;
		vehicleMap.put(params.get("vehicleId").toString(), vehicleInfoString);
		
		/**修改内存的车辆补全**/
		List<String> vehicleRegistrationNOList=(ArrayList<String>)Struts2Utils.getSessionAttribute(Constants.VEHICLEREGISTRATIONNO);
		for (int i = 0; i < vehicleRegistrationNOList.size(); i++) {
			String strNoName= params.get("registrationNo").toString().equals("")?vehicleValue[0]:params.get("registrationNo").toString();//修改过后的车牌
			String strNo = vehicleRegistrationNOList.get(i);
			if(strNo==null){break;}
			if(strNo.equals(vehicleValue[0].toString())){ //有相同的车牌
				vehicleRegistrationNOList.remove(i);
				vehicleRegistrationNOList.add(strNoName);
			}
		}
		
		Struts2Utils.setSessionArrtibute(Constants.USER_VEHICLE, vehicleMap);
		Struts2Utils.setSessionArrtibute(Constants.VEHICLEREGISTRATIONNO, vehicleRegistrationNOList);//用作车牌自动补全ljy
	}
	
	/**
	 * 车辆更换终端
	 */
	@Action(value = "changeTerminal")
	public void changeTerminal() {
		
		String vehicleId = getParameter("vehicleId");
		String oldTerminalId = getParameter("oldTerminalId");
		String newTerminalId = getParameter("newTerminalId");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("vehicleId", vehicleId);
		params.put("oldTerminalId", oldTerminalId);
		params.put("newTerminalId", newTerminalId);
		params = putUserParams(params);
		
		Result result = new Result();
		try {
			vehicleServices.updateTerminal(params);
			result.setCode(1);
			this.renderJSON(JSONUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
	}
	
	/**
	 * 终端参数分页查询
	 */
	@Action(value = "terminalParamList")
	public void terminalParamList() {
		
		try {
			Map<String,Object> params = FlexiGridUtil.parseParam(this.getGridParams());
			this.renderJSON(JSONUtil.toJson(vehicleServices.getTerminalParams(params)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 获取用户信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> putUserParams(Map<String,Object> params) {
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
		Date date = calendar.getTime();
		
		Long userId = UserContext.isSuperUser() ? 0 : UserContext.getLoginUserID();
		
		params.put("createUserId", userId);
		params.put("modifyUserId", userId);
		params.put("createDateTime", format.format(date));
		params.put("modifyDateTime", format.format(date));
		
		return params;
	}
	

	
	
	
//////////////////////作用于报警设置模块begin/////////////////////////////	
	
	/**
	 * 根据用户id（单位id）得到车辆列表
	 */
	@SuppressWarnings("unchecked")
	@Action(value = "getVehilceListByWorkUnitID")
	public void getVehilceListByWorkUnitID() {
		
		StringBuilder jsonString = new StringBuilder();
		try {
			//车牌号
			String registrationNO = getParameter("registrationNO").trim();
			//取得企业ID（工作单位）
			long workUnitID=UserContext.getLoginUser().getWorkUnitID();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("registrationNO", registrationNO);
			//不是超级管理员!UserContext.isBsRootUser()
			if(!UserContext.isBsRootUser()){
				paramsMap.put("workUnitID", workUnitID);
			}
			
			List<HashMap<String, String>> vehilceList=vehicleServices.getVehilceListByWorkUnitID(paramsMap);
			 
			for (Map vehilceMap : vehilceList) {
				jsonString.append(vehilceMap.get("id") + "=" + vehilceMap.get("registrationNO") + ",");
			}
			Struts2Utils.renderText(jsonString.toString());
//			LogUtil.insertLog(LogActionTypes.READ, "查询", "业务管理", "", "查询车辆信息");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
		
//////////////////////作用于报警设置模块end/////////////////////////////	

}
