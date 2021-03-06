package com.etrans.bubiao.action.http.mobile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.http.Config;
import com.etrans.bubiao.action.http.HttpServiceAction;
import com.etrans.bubiao.action.http.TicketManager;
import com.etrans.bubiao.entities.GpsInfo;
import com.etrans.bubiao.entities.HttpResult;
import com.etrans.bubiao.entities.ParamBean;
import com.etrans.bubiao.repository.CommandRepository;
import com.etrans.bubiao.services.HistoryGpsInfoServices;
import com.etrans.bubiao.services.IbatisServices;
import com.etrans.common.util.Tools;
import com.etrans.common.util.json.JSONUtil;

/**
 * GPS相关接口程序
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
@Controller("eTGetVehicleGpsInfoAction")
@Scope("prototype")
@Namespace("/httpMobile")
public class ETGetGpsInfoAction extends HttpServiceAction {

	@Autowired(required = true)
	private IbatisServices ibatisServices;

	@Autowired
	private CommandRepository commandRepository;

	@Autowired
	private HistoryGpsInfoServices historyGpsInfoServices;

	// @SuppressWarnings("unchecked")
	// /**
	// *http://localhost:8080/enterprise/httpService/ETGetGpsInfoList.action?jsonParam={"name":"23","password":"F65E","vehicleNoList":["粤AD6473","粤AW5423阳光家园至罗岗"]}
	// *根据车牌取用户下所有车辆的最新定位信息
	// */
	// @Action(value = "ETGetGpsInfoList")
	// public void ETGetGpsInfoList()
	// {
	// //返回对象
	// Result result = new Result();
	// ParamBean paramBean = JSONUtil.fromJson(super.jsonParam,ParamBean.class);
	// Map param=new HashMap<String, Object>();
	// List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	// param.put("userName", paramBean.getName().toString().trim());
	// param.put("passWord", paramBean.getPassword().toString().trim());
	//
	// //验证用户
	// lists=ibatisServices.queryForList(Map.class,"getUserNameSQL",param);
	// Map<String, Object> map = new HashMap<String, Object> ();
	// map =lists.get(0);
	//
	// param.put("userId",map.get("userId"));
	//
	// System.out.println("userId=====:"+map.get("userId"));
	//
	// Map<String, Object> paramMap = new HashMap<String, Object> ();
	//
	// if(lists.size()==0){
	// // 返回json数据
	// result.setCode(1);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	//
	// String[] str = paramBean.getVehicleNoList().toString().split(",");
	// System.out.println("str=====:"+str.length);
	//
	// if(str.length>20){
	// result.setCode(9);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	// String registrationNo="";
	// List<String> gpsInfoStringList=new ArrayList<String>();
	// for (int i = 0; i < paramBean.getVehicleNoList().size(); i++) {
	//
	// try {
	//
	// //取客户端传来的车牌号码
	// registrationNo = new
	// String(paramBean.getVehicleNoList().get(i).getBytes("ISO-8859-1"),"gbk");
	// param.put("RegistrationNo", new
	// String(paramBean.getVehicleNoList().get(i).getBytes("ISO-8859-1"),"gbk"));
	//
	// //验证车牌号码 是否属于这个用户
	// lists=ibatisServices.queryForList(Map.class,"getRegistrationNoSQL",param);
	//
	// System.out.println("lists.size===========:"+lists.size());
	// if(lists.size()==0){
	// // 返回json数据
	// result.setCode(1);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	// //根据车牌号码 查询 车牌ID
	// lists=ibatisServices.queryForList(Map.class,"getVehicleIdSQL",param);
	// map =lists.get(0);
	// System.out.println("id=====:"+map.get("id")+"=====color:"+map.get("RegistrationNOColor"));
	//
	// String
	// gpsInfo=this.commandRepository.getGpsInfo(String.valueOf(map.get("id")));
	// System.out.println("gpsInfo======:"+gpsInfo);
	//
	// WebServiceGpsInfo webServiceGps=this.toGpsInfo(gpsInfo);
	// webServiceGps.setRegistrationNo(registrationNo);
	// gpsInfoStringList.add(webServiceGps.toString());
	// String[] arr=webServiceGps.toString().split(",");
	//
	// paramMap.put("RegistrationNo", arr[0]);//车牌号码
	// paramMap.put("longitude", arr[1]);//经度
	// paramMap.put("latitude", arr[2]);//纬度
	// paramMap.put("speed", arr[3]);//GPS速度
	// paramMap.put("head", arr[4]);//方向
	// paramMap.put("gpsValid", arr[5]);//是否有效定位 盲区或者准确
	// paramMap.put("gpsState", arr[6]);//状态信息
	// paramMap.put("gpsMileage", arr[7]);//里程
	// paramMap.put("gpsOil", arr[8]);//油位
	// paramMap.put("gpsTime", arr[9]);//定位时间
	// result.setCode(0);
	// result.setData(paramMap);
	// System.out.println("==========："+webServiceGps.toString());
	// }
	//
	//
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	//
	// }
	// this.renderJSON(result);
	// }

	// /**
	// *
	// http://localhost:8080/enterprise/httpService/ETGetGpsInfo.action?jsonParam={"name":"23","password":"F65E","vehicleNo":"粤AD6473"}
	// *取某辆车的最新定位信息
	// */
	// @SuppressWarnings("unchecked")
	// @Action(value = "ETGetGpsInfo")
	// public void ETGetGpsInfo()
	// {
	// //返回对象
	// Result result = new Result();
	// ParamBean paramBean = JSONUtil.fromJson(super.jsonParam,ParamBean.class);
	// Map param=new HashMap<String, Object>();
	// List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	// param.put("userName", paramBean.getName().toString().trim());
	// param.put("passWord", paramBean.getPassword().toString().trim());
	//
	// //验证用户
	// lists=ibatisServices.queryForList(Map.class,"getUserNameSQL",param);
	// Map<String, Object> map = new HashMap<String, Object> ();
	// map =lists.get(0);
	//
	// param.put("userId",map.get("userId"));
	//
	// System.out.println("userId=====:"+map.get("userId"));
	//
	// Map<String, Object> paramMap = new HashMap<String, Object> ();
	//
	// if(lists.size()==0){
	// // 返回json数据
	// result.setCode(1);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	//
	// String registrationNo="";
	// List<String> gpsInfoStringList=new ArrayList<String>();
	//
	//
	// try {
	//
	// //取客户端传来的车牌号码
	// registrationNo = new
	// String(paramBean.getVehicleNo().getBytes("ISO-8859-1"),"gbk");
	// param.put("RegistrationNo", new
	// String(paramBean.getVehicleNo().getBytes("ISO-8859-1"),"gbk").replace("?",
	// ""));
	//
	// //验证车牌号码 是否属于这个用户
	// lists=ibatisServices.queryForList(Map.class,"getRegistrationNoSQL",param);
	//
	// if(lists.size()==0){
	// // 返回json数据
	// result.setCode(1);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	// //根据车牌号码 查询 车牌ID
	// lists=ibatisServices.queryForList(Map.class,"getVehicleIdSQL",param);
	// map =lists.get(0);
	// System.out.println("id=====:"+map.get("id")+"=====color:"+map.get("RegistrationNOColor"));
	//
	// String
	// gpsInfo=this.commandRepository.getGpsInfo(String.valueOf(map.get("id")));
	// System.out.println("gpsInfo======:"+gpsInfo);
	//
	// WebServiceGpsInfo webServiceGps=this.toGpsInfo(gpsInfo);
	// webServiceGps.setRegistrationNo(registrationNo);
	// gpsInfoStringList.add(webServiceGps.toString());
	// String[] arr=webServiceGps.toString().split(",");
	//
	// paramMap.put("RegistrationNo", arr[0]);//车牌号码
	// paramMap.put("longitude", arr[1]);//经度
	// paramMap.put("latitude", arr[2]);//纬度
	// paramMap.put("speed", arr[3]);//GPS速度
	// paramMap.put("head", arr[4]);//方向
	// paramMap.put("gpsValid", arr[5]);//是否有效定位 盲区或者准确
	// paramMap.put("gpsState", arr[6]);//状态信息
	// paramMap.put("gpsMileage", arr[7]);//里程
	// paramMap.put("gpsOil", arr[8]);//油位
	// paramMap.put("gpsTime", arr[9]);//定位时间
	// result.setCode(0);
	// result.setData(paramMap);
	// System.out.println("==========："+webServiceGps.toString());
	// }
	//
	//
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// this.renderJSON(result);
	// }
	//
	// /**
	// * 把gps信息组装成gps对象
	// * @author lihaiyan
	// * @param gpsInfo
	// * @return WebServiceGpsInfo
	// * @createTime 2011-12-21
	// */
	// private WebServiceGpsInfo toGpsInfo(String gpsInfo){
	// WebServiceGpsInfo gps=new WebServiceGpsInfo();
	// if(gpsInfo!=null){
	// String[] gpsInfoMessageArray = gpsInfo.split(",");
	// int st=Integer.parseInt(gpsInfoMessageArray[12]);//是否有效定位
	// gps.setGpsValid(st==1?true:false);
	// gps.setLatitude(gpsInfoMessageArray[3]);//纬度
	// gps.setLongitude(gpsInfoMessageArray[4]);//经度
	// gps.setSpeed(gpsInfoMessageArray[7]);//速度
	// gps.setHead(this.getHead(Integer.parseInt((gpsInfoMessageArray[8]))));//方向
	// gps.setGpsState(gpsInfoMessageArray[17]);//状态
	// try{
	// String[] stateArray=gpsInfoMessageArray[17].split("\\、");
	// for(int i=0;i<stateArray.length;i++){
	// String message=stateArray[i];
	// if(message.indexOf("里程数1=")!=-1){//仪表里程
	// String[] messageArray=message.split("=");//
	// if(messageArray!=null&&messageArray.length>=2){
	// gps.setGpsMileage(messageArray[14]);//里程
	// }
	// }
	// if(StringUtils.isEmpty(gps.getGpsMileage())||" ".equals(gps.getGpsMileage())){
	// if(message.indexOf("里程数2=")!=-1){//GPS里程
	// String[] messageArray=message.split("=");//
	// if(messageArray!=null&&messageArray.length>=2){
	// gps.setGpsMileage(messageArray[15]);//里程
	// }
	// }
	// }
	// if(message.indexOf("当前油量：")!=-1){
	// String[] messageArray=message.split("：");
	// if(messageArray!=null&&messageArray.length>=2){
	// gps.setGpsOil(messageArray[16]);//油位
	// }
	// }
	// }
	//
	// }catch(Exception e2){
	// e2.printStackTrace();
	// }
	// gps.setGpsTime(gpsInfoMessageArray[1]);
	// }
	// return gps;
	// }
	//

	// /**
	// *http://localhost:8080/enterprise/httpService/ETGetHistoryGpsInfo.action?jsonParam={"name":"23","password":"F65E","vehicleNo":"粤AD6473","startDate":"","endDate":""]}
	// *取某辆车某段时间内的轨迹数据
	// */
	// @SuppressWarnings("unchecked")
	// @Action(value = "ETGetHistoryGpsInfo")
	// public void ETGetHistoryGpsInfo()
	// {
	// //返回对象
	// Result result = new Result();
	// ParamBean paramBean = JSONUtil.fromJson(super.jsonParam,ParamBean.class);
	// Map param=new HashMap<String, Object>();
	// List<Map<String, Object>> lists = new ArrayList<Map<String,Object>>();
	// param.put("userName", paramBean.getName().toString().trim());
	// param.put("passWord", paramBean.getPassword().toString().trim());
	//
	// //验证用户
	// lists=ibatisServices.queryForList(Map.class,"getUserNameSQL",param);
	// Map<String, Object> map = new HashMap<String, Object> ();
	// map =lists.get(0);
	//
	// param.put("userId",map.get("userId"));
	//
	// System.out.println("userId=====:"+map.get("userId"));
	//
	// if(lists.size()==0){
	// // 返回json数据
	// result.setCode(1);//1:帐户有误
	// this.renderJSON(result);
	// }else{
	//
	// }
	// }

	public HistoryGpsInfoServices getHistoryGpsInfoServices() {
		return historyGpsInfoServices;
	}

	public void setHistoryGpsInfoServices(
			HistoryGpsInfoServices historyGpsInfoServices) {
		this.historyGpsInfoServices = historyGpsInfoServices;
	}

	/**
	 * http://localhost:80/enterprise/httpService/ETGetVehicleGpsInfos.action?
	 * jsonParam={"userID":"51","ticket":"1358835197986-5021"
	 * ,vehicleIdList:["301","302"]} 根据车牌取用户下所有车辆的最新定位信息
	 */
	@Action(value = "ETGetVehicleGpsInfos")
	public void ETGetVehicleGpsInfos() throws Exception {

		// 返回结果
		HttpResult result = new HttpResult();

		try {
			boolean flag = true;
			result.setCode(Config.SUCCESS);

			/**************** 数据验证*********START ***************/
			// 步骤一：解释参数
			ParamBean paramBean = JSONUtil.fromJson(super.jsonParam,
					ParamBean.class);

			// 步骤二：获取参数
			String ticket = paramBean.getTicket().trim();

			// 步骤三：判断安全票据是否为空
			if (StringUtils.isEmpty(ticket)) {
				result.setCode(Config.TICKET_ENPTY);
				flag = false;
			}

			// 步骤四：验证用户是否失效
			if (TicketManager.getInstance().checkTicketAble(ticket)) {
				result.setCode(Config.TICKET_UNABLE);
				flag = false;
			}
			/**************** 数据验证********END ****************/
			List<String> paramLs = null;

			if (paramBean.getVehicleIdList().size() > 10) {
				paramLs = paramBean.getVehicleIdList().subList(0, 9);
			} else {
				paramLs = paramBean.getVehicleIdList();
			}

			if (flag && paramLs.size() > 0) {
				/**************** 返回结果********START ****************/
				// 步骤五：组装返回对象
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("vehicleIdList", paramLs);

				List<HashMap<String, String>> ls = this.ibatisServices
						.findIbatisList("querySimpleVehicleInfoByIdSQL", map);

				for (HashMap<String, String> paramMap : ls) {

					GpsInfo info = this.commandRepository.getVehicleGpsInfo(
							String.valueOf(paramMap.get("vehicleID")), "0");
					paramMap.put("gpsValid", info.getGv());// 是否有效定位 盲区或者准确
					paramMap.put("latitude", info.getLat());// 纬度
					paramMap.put("longitude", info.getLon());// 经度
					paramMap.put("speed1", info.getSd());// GPS速度
					paramMap.put("speed2", info.getSd2());// 行驶记录速度
					paramMap.put("head",info.getHd() == null ? "-1" : info.getHd());// 方向
					//paramMap.put("head", Tools.getHead(Integer.parseInt(info.getHd() == null ? "-1" : info.getHd())));// 方向
					paramMap.put("status", info.getGs());// 状态信息
					paramMap.put("gpsMileage1", info.getSh());// 里程
					paramMap.put("gpsMileage2", info.getSm());// 里程
					paramMap.put("gpsOil", info.getOi());// 油位
					paramMap.put("gpsTime", info.getGt());// 定位时间
				}

				String str = JSONUtil.toJson(ls);
				result.setData(str);
				/**************** 返回结果********END ****************/

				/**************** 更新票据有效时间********START ****************/
				// 步骤六：更新票据有效时间
				TicketManager.getInstance().putTicket(ticket);

				/**************** 更新票据有效时间********START ****************/
			}
		} catch (Exception e) {
			result.setCode(Config.OTHER_ERROR);
			log.error("[" + Tools.formatDate(new Date()) + "]---->", e);
		}

		this.renderJSON(result);
	}

	/**
	 * http://localhost:8080/enterprise/httpMobile/ETGetTrackBack.action?
	 * jsonParam={"userID":"51","ticket":"1359099191954-1548"
	 * ,"vehicleId":"51920", "startDate":"2012-10-18 10:00:00",
	 * "endDate":"2012-10-18 12:00:00"} 1.6.11 获取某辆车某段时间内的历史轨迹数据
	 */
	@Action(value = "ETGetTrackBack")
	public void ETGetTrackBack() throws Exception {

		// 返回结果
		HttpResult result = new HttpResult();

		try {
			boolean flag = true;
			result.setCode(Config.SUCCESS);

			/**************** 数据验证*********START ***************/
			// 步骤一：解释参数
			ParamBean paramBean = JSONUtil.fromJson(super.jsonParam,
					ParamBean.class);

			// 步骤二：获取参数
			String ticket = paramBean.getTicket().trim();

			// 步骤三：判断安全票据是否为空
			if (StringUtils.isEmpty(ticket)) {
				result.setCode(Config.TICKET_ENPTY);
				flag = false;
			}

			// 步骤四：验证用户是否失效
			if (TicketManager.getInstance().checkTicketAble(ticket)) {
				result.setCode(Config.TICKET_UNABLE);
				flag = false;
			}
			/**************** 数据验证********END ****************/

			if (flag) {
				/**************** 返回结果********START ****************/
				// 步骤五：组装返回对象
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("startTime", paramBean.getStartDate());
				paramMap.put("endTime", paramBean.getEndDate());
				paramMap.put("vehicleID", paramBean.getVehicleId());

				List<HashMap<String, Object>> historyGpsInfoList = this.historyGpsInfoServices
						.getHistoryGpsInfo(paramMap);
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("vehicleID", paramBean.getVehicleId());
				List<HashMap<String, String>> registrationNOMap = this.historyGpsInfoServices
						.getUserVehicleIDByRegistrationNO(paramsMap);

				List<HashMap<String, Object>> newList = new ArrayList<HashMap<String, Object>>();
				double tempLon = 0;
				double tempLat = 0;
				for (HashMap<String, Object> infoMap : historyGpsInfoList) {
					double lonDouble = (Double) infoMap.get("longitude");
					double latDouble = (Double) infoMap.get("latitude");

					if (tempLon == lonDouble && tempLat == latDouble) {
						continue;
					}
					tempLon = lonDouble;
					tempLat = latDouble;

					infoMap.put("longitude",
							String.valueOf(lonDouble / 1000000));
					infoMap.put("latitude", String.valueOf(latDouble / 1000000));

					if (registrationNOMap != null
							&& registrationNOMap.size() > 0) {
						Map<String, String> map = registrationNOMap.get(0);
						infoMap.put("registrationNO", map.get("registrationNO"));
						infoMap.put("registrationNOColor",
								map.get("registrationNOColor"));
					}
					newList.add(infoMap);
				}
				String str = JSONUtil.toJson(newList);
				result.setData(str);
				/**************** 返回结果********END ****************/

				/**************** 更新票据有效时间********START ****************/
				// 步骤六：更新票据有效时间
				TicketManager.getInstance().putTicket(ticket);

				/**************** 更新票据有效时间********START ****************/
			}
		} catch (Exception e) {
			result.setCode(Config.OTHER_ERROR);
			log.error("[" + Tools.formatDate(new Date()) + "]---->", e);
		}

		this.renderJSON(result);
	}
}
