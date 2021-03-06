package com.etrans.bubiao.action.sys;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.util.StringUtil;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.BaseAction;
import com.etrans.bubiao.action.sys.log.LogActionTypes;
import com.etrans.bubiao.action.sys.log.LogUtil;
import com.etrans.bubiao.auth.SessionUser;
import com.etrans.bubiao.http.HttpClient;
import com.etrans.bubiao.http.Response;
import com.etrans.bubiao.services.sys.CustomMapServices;
import com.etrans.bubiao.sys.Constants;
import com.etrans.bubiao.sys.UserContext;
import com.etrans.common.util.Tools;
import com.etrans.common.util.json.JSONUtil;
import com.etrans.common.util.web.Struts2Utils;

@Controller
@Scope("prototype")
@Namespace("/sys")
public class CustomMapAction extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 取区域面
	 */
	@Action(value = "findCustomMapPlane")
	public void findCustomMapPlane() {
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			String shapeId=getParameter("shapeId");
			Map<String, Object> paramsMap = new HashMap<String, Object>(); // 区域面查询参数
			paramsMap.put("userId", users.getUserID());
			paramsMap.put("shapeId", shapeId);
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			
			// 取区域面
			List<HashMap<String, String>> planeList=customMapServices.getCustomMapPlane(paramsMap);
			Map<String, Object> paramsPoint= new HashMap<String, Object>(); // 坐标点查询参数
			paramsPoint.put("shapeId", shapeId);
			for (Map<String, String> map : planeList) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("id", String.valueOf(map.get("id")));
				temp.put("name", String.valueOf(map.get("name")));
				temp.put("radius", String.valueOf(map.get("radius")));
				paramsPoint.put("mapPlaneId", map.get("id"));
				
				// 取区域面坐标
				List<HashMap<String, String>> pointList=customMapServices.getPlanePoint(paramsPoint);
				if(pointList!=null && pointList.size()>0){
					String location = "";
					for (Map<String, String> point : pointList) {
						location += String.valueOf(point.get("longitude")) + "," + String.valueOf(point.get("latitude")) + "*";
					}
					temp.put("location", location.substring(0, location.length() - 1));
				}
				
				list.add(temp);
			}
			if (list != null && list.size() > 0) {
			    jsonString=JSONUtil.toJson(list);
			}
		} catch (Exception e) {
			jsonString = "false";
			e.printStackTrace();
		}
		this.renderJSON(jsonString);
	}
	
	
	/**
	 * 取线面
	 */
	@Action(value = "findCustomMapLine")
	public void findCustomMapLine() {
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			Map<String, Object> paramsMap = new HashMap<String, Object>(); // 线面查询参数
			paramsMap.put("userId", users.getUserID());
			List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
			
			// 取线面
			List<HashMap<String, String>> planeList=customMapServices.getCustomMapLine(paramsMap);
			Map<String, Object> paramsPoint= new HashMap<String, Object>(); // 线坐标点查询参数
			for (Map<String, String> map : planeList) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("id", String.valueOf(map.get("id")));
				temp.put("name", String.valueOf(map.get("name")));
				paramsPoint.put("mapLineId", map.get("id"));
				
				// 取线面坐标
				List<HashMap<String, String>> pointList=customMapServices.getLinePoint(paramsPoint);
				if(pointList!=null && pointList.size()>0){
					String location = "";
					for (Map<String, String> point : pointList) {
						location += String.valueOf(point.get("longitude")) + "," + String.valueOf(point.get("latitude")) + "*";
					}
					temp.put("location", location.substring(0, location.length() - 1));
				}
				
				list.add(temp);
			}
			if (list != null && list.size() > 0) {
			    jsonString=JSONUtil.toJson(list);
			}
		} catch (Exception e) {
			jsonString = "false";
			e.printStackTrace();
		}
		this.renderJSON(jsonString);
	}
	
	
	/**
	 * 取地物点
	 */
	@Action(value = "findCustomMapPoint")
	public void findCustomMapPoint() {
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			Map<String, Object> paramsMap = new HashMap<String, Object>(); // 线面查询参数
			paramsMap.put("userId", users.getUserID());
			// 取地物点
			List<HashMap<String, String>> pointList=customMapServices.getCustomMapPoint(paramsMap);
			
			if (pointList != null && pointList.size() > 0) {
			    jsonString=JSONUtil.toJson(pointList);
			}
		}catch(Exception e) {
			jsonString = "false";
			e.printStackTrace();
		}
		this.renderJSON(jsonString);
	}
	
	
	/**
	 * 新增区域面
	 */
	@Action(value = "createCustomMapPlane")
	public void createCustomMapPlane(){
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			String name=getParameter("name");
			String radius=getParameter("radius");
			String shapeId=getParameter("shapeId");
			String lonlat=getParameter("lonlat");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", users.getUserID());
			//根据用户查询图层
			List<HashMap<String, String>> customMapLayerMap=customMapServices.getCustomMapLayer(paramMap);
			String mapLayerId=null;
			if(customMapLayerMap != null && customMapLayerMap.size()>0){
				Map<String,String> mapId=customMapLayerMap.get(0);
				mapLayerId = String.valueOf(mapId.get("id"));
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("code", 0);
				params.put("mapLayerId", mapLayerId);
				params.put("name", name);
				params.put("radius", radius);
				params.put("shapeId", shapeId);
				//查询该区域是否存在
				List<HashMap<String, String>> planeList=customMapServices.getPlane(params);
				if (planeList != null && planeList.size() > 0) {
					jsonString = "false";
				}else{
					//新增区域面
					customMapServices.createCustomMapPlane(params);
					
					//查询刚新增的区域面
					List<HashMap<String, String>> planeMap=customMapServices.getPlane(params);
					Map<?, ?> map = planeMap.get(0);
					int a = 0;
					for (String lonlatStr : lonlat.split("\\*")) {
						a++;
						Map<String, Object> paramsMapTemp = new HashMap<String, Object>(); // 查询参数
						paramsMapTemp.put("mapPlaneId", map.get("id"));
						paramsMapTemp.put("sequence", a);
						paramsMapTemp.put("longitude", lonlatStr.split(",")[0]);
						paramsMapTemp.put("latitude", lonlatStr.split(",")[1]);
						//新增区域面坐标
						customMapServices.createCustomMapPlanePoint(paramsMapTemp);
					}
					jsonString = "true";
					LogUtil.insertLog(LogActionTypes.INSERT, "成功", "点线区域管理", "", "新增区域面");
					
				}
			}
		}catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.INSERT, "失败", "点线区域管理", "", "新增区域面");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "新增区域json字符串写到输出流", ServletActionContext.getResponse());	
   }
	
	
	/**
	 * 新增线面
	 */
	@Action(value = "createCustomMapLine")
	public void createCustomMapLine(){
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			String name=getParameter("name");
			String lonlat=getParameter("lonlat");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", users.getUserID());
			//根据用户查询图层
			List<HashMap<String, String>> customMapLayerMap=customMapServices.getCustomMapLayer(paramMap);
			String mapLayerId=null;
			if(customMapLayerMap != null && customMapLayerMap.size()>0){
				Map<String,String> mapId=customMapLayerMap.get(0);
				mapLayerId = String.valueOf(mapId.get("id"));
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("name", name);
				params.put("mapLayerId", mapLayerId);
				
				//查询该线面是否存在
				List<HashMap<String, String>> LineList=customMapServices.getLine(params);
				if (LineList != null && LineList.size() > 0) {
					jsonString = "false";
				}else{
					//新增线面
					customMapServices.createCustomMapLine(params);
					
					//查询刚新增的线面
					List<HashMap<String, String>> LineMap=customMapServices.getLine(params);
					Map<?, ?> map = LineMap.get(0);
					int a=0;
					for (String lonlatStr : lonlat.split("\\*")) {
						a++;
						Map<String, Object> paramsMapTemp = new HashMap<String, Object>(); // 查询参数
						paramsMapTemp.put("mapLineId", map.get("id"));
						paramsMapTemp.put("sequence", a);
						paramsMapTemp.put("longitude", lonlatStr.split(",")[0]);
						paramsMapTemp.put("latitude", lonlatStr.split(",")[1]);
						//新增线面坐标
						customMapServices.createCustomMapLinePoint(paramsMapTemp);
					}
					jsonString = "true";
					LogUtil.insertLog(LogActionTypes.INSERT, "成功", "点线区域管理", "", "新增线面");
				}
			}
		} catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.INSERT, "失败", "点线区域管理", "", "新增线面");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "新增线面json字符串写到输出流", ServletActionContext.getResponse());	
   }
	
	
	/**
	 * 新增地物点（标点）
	 */
	@Action(value = "createCustomMapPoint")
	public void createCustomMapPoint(){
		String jsonString = "false";
		try {
			// 取用户信息;
			SessionUser users = UserContext.getLoginUser();
			String name=getParameter("name");
			String lonlat=getParameter("lonlat");
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("userId", users.getUserID());
			//根据用户查询图层
			List<HashMap<String, String>> customMapLayerMap=customMapServices.getCustomMapLayer(paramMap);
			String mapLayerId=null;
			if(customMapLayerMap != null && customMapLayerMap.size()>0){
				Map<String,String> mapId=customMapLayerMap.get(0);
				mapLayerId = String.valueOf(mapId.get("id"));
				
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("name", name);
				params.put("mapLayerId", mapLayerId);
				params.put("longitude", lonlat.split(",")[0]);
				params.put("latitude", lonlat.split(",")[1]);
				params.put("MapIconID", 1);
				
				//查询该地物点是否存在
				List<HashMap<String, String>> PointList=customMapServices.getPoint(params);
				if (PointList != null && PointList.size() > 0) {
					jsonString = "false";
				}else{
					//新增线面
					customMapServices.createCustomMapPoint(params);
					jsonString = "true";
					LogUtil.insertLog(LogActionTypes.INSERT, "成功", "点线区域管理", "", "新增地物点");
				}
			}
		}catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.INSERT, "失败", "点线区域管理", "", "新增地物点");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "新增地物点json字符串写到输出流", ServletActionContext.getResponse());	
   }
	

	
	
	/**
	 * 删除区域面
	 */
	@Action(value = "deleteCustomMapPlane")
	public void deleteCustomMapPlane(){
		String jsonString = "false";
		String ids = getParameter("ids");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("@_ids", ids);
		try {
			customMapServices.delCustomMapPlane(paramMap);
			jsonString = "true";
			LogUtil.insertLog(LogActionTypes.DELETE, "成功", "点线区域管理", "", "删除区域面");
		}catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.DELETE, "失败", "点线区域管理", "", "删除区域面");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "删除区域面json字符串写到输出流", ServletActionContext.getResponse());
	}
	
	
	/**
	 * 删除线面
	 */
	@Action(value = "deleteCustomMapLine")
	public void deleteCustomMapLine(){
		String jsonString = "false";
		String ids = getParameter("ids");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("@_ids", ids);
		try {
			customMapServices.delCustomMapLine(paramMap);
			jsonString = "true";
			LogUtil.insertLog(LogActionTypes.DELETE, "成功", "点线区域管理", "", "删除线面");
		}catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.DELETE, "失败", "点线区域管理", "", "删除线面");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "删除线面json字符串写到输出流", ServletActionContext.getResponse());
	}
	
	
	/**
	 * 删除地物点
	 */
	@Action(value = "deleteCustomMapPoint")
	public void deleteCustomMapPoint(){
		String jsonString = "false";
		String ids = getParameter("ids");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("@_ids", ids);
		try {
			customMapServices.delCustomMapPoint(paramMap);
			jsonString = "true";
			LogUtil.insertLog(LogActionTypes.DELETE, "成功", "点线区域管理", "", "删除地物点");
		}catch (Exception e) {
			LogUtil.insertLog(LogActionTypes.DELETE, "失败", "点线区域管理", "", "删除地物点");
			jsonString = "false";
			e.printStackTrace();
		}
		Tools.writeToOutputStream(jsonString, "删除地物点json字符串写到输出流", ServletActionContext.getResponse());
	}
	
	

	/**
	 * 定时定区域查车
	 * */
	@SuppressWarnings("unchecked")
	@Action(value = "findTimeAreaVehicle")
	public void findTimeAreaVehicle(){
		
		Map<String, String> vehicleMap=(HashMap<String, String>)Struts2Utils.getSessionAttribute(Constants.USER_VEHICLE);


		String jsonString = "false";
		String leftLatLon=getParameter("leftLatLon");
		String rightLatLon=getParameter("rightLatLon");
		String beginTime=getParameter("beginTime");
		String endTime=getParameter("endTime");
		double leftLat = Double.parseDouble(leftLatLon.split("\\|")[0]); // 左上角经度
		double leftLon = Double.parseDouble(leftLatLon.split("\\|")[1]); // 左上角纬度
		double rightLat = Double.parseDouble(rightLatLon.split("\\|")[0]); // 右下方经度
		double rightLon = Double.parseDouble(rightLatLon.split("\\|")[1]);// 右下方纬度
		String[] leftRSH=Tools.getRSHRealLngLat(leftLat+","+leftLon);//反偏移
		leftLat=Double.parseDouble(leftRSH[0]);
		leftLon=Double.parseDouble(leftRSH[1]);
		String[] rightRSH=Tools.getRSHRealLngLat(rightLat+","+rightLon);//反偏移
		rightLat=Double.parseDouble(rightRSH[0]);
		rightLon=Double.parseDouble(rightRSH[1]);
		int leftLat2 = (int) (leftLat * 1000000);
		int leftLon2 = (int) (leftLon * 1000000);
		int rightLat2 = (int) (rightLat * 1000000);
		int rightLon2 = (int) (rightLon * 1000000);
		
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			param.put("startTime", sdf.format(new Date(Long.valueOf(beginTime)))); // 开始时间
			param.put("endTime", sdf.format(new Date(Long.valueOf(endTime)))); // 结束时间
			param.put("minLongitude",leftLat2); // 最小经度
			param.put("maxLongitude", rightLat2); // 最大经度
			param.put("minLatitude", leftLon2); // 最小纬度
			param.put("maxLatitude",rightLon2); // 最大纬度
			
//			param.put("minLongitude",106442220); // 最小经度
//			param.put("maxLongitude", 116722020); // 最大经度
//			param.put("minLatitude", 29512170); // 最小纬度, 
//			param.put("maxLatitude",39586540); // 最大纬度
			
//			param.put("minLongitude",101375993); // 最小经度
//			param.put("maxLongitude", 118375993); // 最大经度
//			param.put("minLatitude", 20803240); // 最小纬度
//			param.put("maxLatitude",40803240); // 最大纬度
			
			
			
			
			List<HashMap<String, String>> backList = this.customMapServices.getTimeAreaVehicle(param);
			List<HashMap<String, String>> VehicleList  =new ArrayList<HashMap<String, String>> ();
			
			//匹配方法一
			/*if(vehicleMap!=null && vehicleMap.size()>0){
				HashMap<String, String> areaVehicleList = new HashMap<String, String>();
				 if (backList != null && backList.size() > 0) {
					  for(Map<String, String> map : backList){
						    String vehicleID=String.valueOf(map.get("vehicleID"));
							double lonDouble= Double.parseDouble(String.valueOf(map.get("longitude")));
							double latDouble= Double.parseDouble(String.valueOf(map.get("latitude")));
							double  longitude=lonDouble/1000000;
							double  latitude=latDouble/1000000;
							String gpsTime=String.valueOf(map.get("gpsTime"));
							Double speedDouble=Double.parseDouble(String.valueOf(map.get("speed")));
							int speed=speedDouble.intValue();
							String speedstr=null;
							if(speed>0){
								speedstr="行驶";
							}else{
								speedstr="停止";
							}
							areaVehicleList.put("vehicleID", vehicleID);
							areaVehicleList.put("longitude", String.valueOf(longitude));
							areaVehicleList.put("latitude", String.valueOf(latitude));
							areaVehicleList.put("gpsTime", gpsTime);
							areaVehicleList.put("speedstr", speedstr);
					  }
				  }
				 Set<String> set=vehicleMap.keySet();
				 Iterator<String> it = set.iterator();
				 while(it.hasNext())
				  {
					  Object key=it.next();
					  if(areaVehicleList.containsKey(key)){
						  String[] vehicleValue=vehicleMap.get(key).split("\\|");//车牌号|车牌颜色|所属行业|所属业户
						  String registrationNO=vehicleValue[0];
						  areaVehicleList.put("registrationNO", registrationNO);
						  VehicleList.add(areaVehicleList);
					  }
					  
				  }
			}*/
			
			
			//匹配方法二
			if (backList != null && backList.size() > 0) {
				for(Map<String, String> mapVehicle : backList){
					
					Double vIdDouble= Double.parseDouble(String.valueOf(mapVehicle.get("vehicleID")));
					double lonDouble= Double.parseDouble(String.valueOf(mapVehicle.get("longitude")));
					double latDouble= Double.parseDouble(String.valueOf(mapVehicle.get("latitude")));
					double  longitude=lonDouble/1000000;
					double  latitude=latDouble/1000000;
					String gpsTime=String.valueOf(mapVehicle.get("gpsTime"));
					Double speedDouble=Double.parseDouble(String.valueOf(mapVehicle.get("speed")));
					int vehicleID=vIdDouble.intValue();
					int speed=speedDouble.intValue();
					String speedstr=null;
					if(speed>0){
						speedstr="行驶";
					}else{
						speedstr="停止";
					}
					if(vehicleMap!=null && vehicleMap.size()>0){
						String vehicleValueString=vehicleMap.get(String.valueOf(vehicleID));
						if(vehicleValueString==null){
							continue;
						}
						String[] vehicleValue=vehicleValueString.split("\\|");//车牌号|车牌颜色|所属行业|所属业户
						String registrationNO=vehicleValue[0];
						if(vehicleValue.length>0 && registrationNO!=null){
							HashMap<String, String> areaVehicleMap = new HashMap<String, String>();
							areaVehicleMap.put("vehicleID", String.valueOf(vehicleID));
							areaVehicleMap.put("registrationNO", registrationNO);
							areaVehicleMap.put("gpsTime", gpsTime);
							areaVehicleMap.put("longitude", String.valueOf(longitude));
							areaVehicleMap.put("latitude", String.valueOf(latitude));
							areaVehicleMap.put("speedstr", speedstr);
							VehicleList.add(areaVehicleMap);
						}
					}
				}
				jsonString = JSONUtil.toJson(VehicleList);
			} else {
				jsonString = "false";
			}
		} catch (Exception e) {
			e.printStackTrace();
			jsonString = "false";
		}

		this.renderJSON(jsonString);
//		Tools.writeToOutputStream(jsonString, "定时定区域查车json字符串写到输出流", ServletActionContext.getResponse());
   }
	
	
	
	@Autowired
	private CustomMapServices customMapServices;

	public CustomMapServices getCustomMapServices() {
		return customMapServices;
	}

	public void setCustomMapServices(CustomMapServices customMapServices) {
		this.customMapServices = customMapServices;
	}

	
	
	
	

}
