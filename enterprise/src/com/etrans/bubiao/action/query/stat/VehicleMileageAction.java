package com.etrans.bubiao.action.query.stat;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.BaseAction;
import com.etrans.bubiao.action.sys.log.LogActionTypes;
import com.etrans.bubiao.action.sys.log.LogUtil;
import com.etrans.bubiao.auth.SessionUser;
import com.etrans.bubiao.http.HttpException;
import com.etrans.bubiao.http.ParamKey;
import com.etrans.bubiao.services.query.stat.VehicleMileageService;
import com.etrans.bubiao.services.sys.InitSelectServices;
import com.etrans.bubiao.sys.UserContext;
import com.etrans.common.util.FlexiGridUtil;
import com.etrans.common.util.json.JSONUtil;
import com.etrans.common.util.web.RowNumUtil;

@Controller
@Scope("prototype")
@Namespace("/query/stat")
public class VehicleMileageAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String checkUserAuth() throws Exception{
		//-1:超级管理员   1：普通用户  0：企业管理员
		String isSuper = "1";
		if(UserContext.isBsRootUser()){
			isSuper = "-1";
		}else if(UserContext.getLoginUser().isWorkUnitSuperAdmin()){
			isSuper = "0";
		}
		return isSuper;
	}
	
	/**
	 *行驶里程统计	
	 */
	@Action(value = "findVehicleMileage")
	public void findVehicleMileage() {
		
		try {
			//-1:超级管理员   1：普通用户  0：企业管理员
			String isSuper = checkUserAuth();
			
			Map<String,Object> params = FlexiGridUtil.parseParam(this.getGridParams());
			
			SessionUser user = UserContext.getLoginUser();
			if(user != null){
				if(UserContext.isBsRootUser()){
					params.put("isSuper", isSuper);
				}else if(user.isWorkUnitSuperAdmin()){
					String fullId = user.getWorkUnitFullId();
					params.put("fullId", fullId);
					params.put("isSuper", isSuper);
				}else{
					params.put("userId", user.getUserID());
					params.put("isSuper", isSuper);
				}
			 }
			
//			params.put("isSuper", isSuper);
//			params.put("userId",UserContext.getLoginUserID());
//			params.put("workUnitId", UserContext.getLoginUser().getWorkUnitID());
			
			String vehicleStr = this.VehicleMileageService.getVehilces(params);
			
			params.put("vehicleStr", vehicleStr==null?"":vehicleStr);
			
			this.renderJSON(JSONUtil.toJson(VehicleMileageService.getVehicleMileages(params)));
			LogUtil.insertLog(LogActionTypes.READ, "成功", "行驶里程统计查询", "", "行驶里程统计查询");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.READ, "失败", "行驶里程统计查询", "", "行驶里程统计查询");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 统计导出EXECL表格
	 */
	@Action(value = "vehicleMileageExportExl")
	public void vehicleMileageExportExl() {
		
		try {
			//-1:超级管理员   1：普通用户  0：企业管理员
			String isSuper = checkUserAuth();
			
			// 导出数据时的开始页数
			String fromPage = getParameter("frompage");
			// 导出数据时的结束页数
			String toPage = getParameter("topage");
			
			Map<String,Object> params = FlexiGridUtil.parseParam(this.getGridParams());

			String pageSize = String.valueOf(params.get(ParamKey.PAGE_SIZE));
			Integer fromRow = RowNumUtil.getFromRowNum(fromPage, pageSize);
			Integer toRow = RowNumUtil.getToRowNum(toPage, pageSize);
			params.put("@FromRow",String.valueOf(fromRow));
			params.put("@ToRow",String.valueOf(toRow));
			
			params.put("isSuper", isSuper);
			params.put("userId", UserContext.getLoginUserID());
			params.put("workUnitId", UserContext.getLoginUser().getWorkUnitID());
			
			String[] titleArray = {};
			titleArray = new String[5];
			titleArray[0]="车牌号码";
			titleArray[1]="车牌颜色";
			titleArray[2]="所属单位";
			titleArray[3]="统计时间段";
			titleArray[4]="里程";
			
			String[] columnArray = {};
			columnArray = new String[5];
			columnArray[0]="registrationNO";
			columnArray[1]="color";
			columnArray[2]="workUnitName";
			columnArray[3]="gpsTime";
			columnArray[4]="newMileage";
			
			String vehicleStr = this.VehicleMileageService.getVehilces(params);
			
			params.put("vehicleStr", vehicleStr==null?"":vehicleStr);
			
			List<Map<String,Object>>  rows = VehicleMileageService.vehicleMileagesExportExl(params);
			exportExl("VehicleMileage", titleArray, columnArray, rows);
			LogUtil.insertLog(LogActionTypes.READ, "成功", "行驶里程统计导出", "", "行驶里程统计导出");
		} catch (HttpException e) {
			LogUtil.insertLog(LogActionTypes.READ, "失败", "行驶里程统计导出", "", "行驶里程统计导出");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Autowired
	private VehicleMileageService VehicleMileageService;

	public VehicleMileageService getVehicleMileageService() {
		return VehicleMileageService;
	}

	public void setVehicleMileageService(VehicleMileageService VehicleMileageService) {
		this.VehicleMileageService = VehicleMileageService;
	}

	@Autowired
	private InitSelectServices initSelectServices;

	public InitSelectServices getInitSelectServices() {
		return initSelectServices;
	}

	public void setVehicleMileageService(InitSelectServices initSelectServices) {
		this.initSelectServices = initSelectServices;
	}
	
	
	

}
