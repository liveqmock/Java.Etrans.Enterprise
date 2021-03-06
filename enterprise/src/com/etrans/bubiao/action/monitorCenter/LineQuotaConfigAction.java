package com.etrans.bubiao.action.monitorCenter;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.BaseAction;
import com.etrans.bubiao.action.sys.log.LogActionTypes;
import com.etrans.bubiao.action.sys.log.LogUtil;
import com.etrans.bubiao.entities.Result;
import com.etrans.bubiao.services.monitorCenter.LineQuotaConfigServices;
import com.etrans.bubiao.sys.UserContext;
import com.etrans.common.util.json.JSONUtil;

/**
 * 路段限速报警设置Action
 * @author Administrator
 *
 */
@Controller
@Scope("prototype")
@Namespace("/monitorCenter")
public class LineQuotaConfigAction extends BaseAction{
	
	private static final long serialVersionUID = 1L;
	
	protected Logger log = LogManager.getLogger(this.getClass().getName());
	
	@Autowired
	private LineQuotaConfigServices lineQuotaConfigServices;

	public void setLineQuotaConfigServices(LineQuotaConfigServices lineQuotaConfigServices) {
		this.lineQuotaConfigServices = lineQuotaConfigServices;
	}
	
	
	/**
	 * 查询路段信息list列表
	 */
	@Action(value="findLineQuotaConfigList")
	public void findLineQuotaConfigList(){
		try {
			long workUnitID=UserContext.getLoginUser().getWorkUnitID();
			this.renderJSON(lineQuotaConfigServices.findList(this.getGridParams(),workUnitID,new Random().nextLong()));
			LogUtil.insertLog(LogActionTypes.READ, "成功", "路段限速报警", "", "路段限速报警");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.READ, "失败", "路段限速报警", "", "路段限速报警");
			e.printStackTrace();
			log.error("查询路段信息list列表异常！"+e.getMessage());
		}
		
	}
	
	/**
	 * 判断线路名称重复
	 */
	@Action(value = "checkLineName")
	public void checkLineName() {
		String name = getParameter("name");
		String id = getParameter("id");
		Map<String,Object> whereMap = new HashMap<String,Object>();
		whereMap.put("name", name);
		whereMap.put("id", id);
		try {
			this.renderJSON(lineQuotaConfigServices.checkLineByName(whereMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 新增路段限速报警设置
	 */
	@Action(value = "createQuotaConfig")
	public void createQuotaConfig() {
		
		String lineQuotaConfig = getParameter("lineQuotaConfig");
		String vehicleIds=getParameter("idStr");
		Map obj = JSONUtil.fromJson(lineQuotaConfig, Map.class);
		//得到企业id
		long workUnitID=UserContext.getLoginUser().getWorkUnitID();
		//用户名
		String userName =UserContext.getLoginUser().getUserName();
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("vehicleIds",vehicleIds);//车辆id字符串
		paramMap.put("lineName",obj.get("lineNameTxt") );//线路名称
		paramMap.put("maxSpeed",obj.get("maxSpeed") );//速度上线
		paramMap.put("lineId",obj.get("lineIdSel") );//线路id
		paramMap.put("isAlarm",obj.get("isAlarmSel") );//是否报警
		paramMap.put("dateTypeId",obj.get("dateTypeIDSel") );//检测类型id
		paramMap.put("workingDays",obj.get("workingDaysSel") );//检测日期
		paramMap.put("beginDate",obj.get("beginDate") );//开始日期
		paramMap.put("endDate",obj.get("endDate") );//结束日期
		paramMap.put("beginTime",obj.get("beginTime"));//开始时间
		paramMap.put("endTime",obj.get("endTime") );//结束时间
		paramMap.put("workUnitId", workUnitID);//企业id
		paramMap.put("userName", userName);//用户名
		
		try {
			this.renderJSON(lineQuotaConfigServices.createQuotaConfig(paramMap));
			LogUtil.insertLog(LogActionTypes.INSERT, "成功", "路段限速设置", "", "新增路段限速报警设置");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.INSERT, "失败", "路段限速设置", "", "新增路段限速报警设置");
			e.printStackTrace();
			log.error("新增路段限速报警设置异常！"+e.getMessage());
		}
		
	}
	
	
	/**
	 * 删除线路限速报警设置
	 */
	@Action(value = "updateQuotaConfig")
	public void updateQuotaConfig() {
		String id = getParameter("id");
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		
		try {
			this.renderJSON(lineQuotaConfigServices.deleteQuotaConfig(params));
			LogUtil.insertLog(LogActionTypes.DELETE, "成功", "路段限速设置", "", "删除线路限速报警设置");
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.DELETE, "失败", "路段限速设置", "", "删除线路限速报警设置");
			e.printStackTrace();
			log.error("删除线路限速报警设置异常！"+e.getMessage());
		}		
	}
	
	
	
	/**
	 * 查询详细信息
	 */
	@Action(value = "getQuotaConfigById")
	public void getQuotaConfigById() {
		String id = getParameter("id"); 
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("id", id);
		
		Result result = new Result();
		
		try {
			Map<String,Object> areaConfig = lineQuotaConfigServices.getQuotaConfigById(params);
			result.setCode(1);
			result.setData(areaConfig);
			this.renderJSON(JSONUtil.toJson(result));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("查询线路限速报警设置详细信息异常！"+e.getMessage());
		}
		result.setCode(0);
		this.renderJSON(JSONUtil.toJson(result));
		
	}
	
}
