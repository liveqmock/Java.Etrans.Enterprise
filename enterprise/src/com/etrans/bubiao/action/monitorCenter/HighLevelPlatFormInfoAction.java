package com.etrans.bubiao.action.monitorCenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.action.BaseAction;
import com.etrans.bubiao.repository.parent.ParentCommandRepository;
import com.etrans.bubiao.sys.Constants;
import com.etrans.common.util.CommandCode;
import com.etrans.common.util.CommandTools;
import com.etrans.common.util.Tools;
import com.etrans.common.util.web.Struts2Utils;

/**
 * @author lihaiyan
 * @version 1.0
 * @brief
 */

@Controller
@Scope("prototype")
@Namespace("/monitorCenter")
public class HighLevelPlatFormInfoAction extends BaseAction
{

	@Autowired
	private ParentCommandRepository parentCommandRepository;

	/**
	 * 描述：判断是否有上级信息
	 * 
	 * @author lihaiyan
	 * @since Create on 2012-3-7
	 * @version Copyright (c) 2012 by e_trans.
	 */
	@Action(value = "isPlatFormInfo")
	public void isPlatFormInfo()
	{
		Queue<String> queue = parentCommandRepository.getFlatQueue();
		if (queue == null || queue.size() <= 0)
		{
			this.renderText("false");
		} else
		{
			this.renderText("true");
		}
	}

	
	/**
	 * 描述：获取上级信息
	 * 
	 * @author lihaiyan
	 * @since Create on 2012-3-7
	 * @version Copyright (c) 2012 by e_trans.
	 * @throws Exception 
	 */
	@Action(value = "getPlatFormInfo")
	public void getPlatFormInfo() throws Exception
	{
		
		List<String> lst = null;
		try{
			Queue<String> queue = parentCommandRepository.getFlatQueue();
			if (queue == null)
			{
				this.renderJSON("");
			} else
			{
				String[] strings = queue.toArray(new String[]{});
				if (null != strings)
				{
					lst = new ArrayList<String>();
	
					for (String str : strings)
					{
						String[] msg = str.split("\\|");
						int commandCode = Integer.parseInt(msg[0]);
						
						switch(commandCode){
						case CommandCode.COMMAND_7106://报警督办请求
							lst.add(msg[0]+"|"+CommandTools.convertOverseeing(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						case CommandCode.COMMAND_7107://报警预警
							lst.add(msg[0]+"|"+CommandTools.convertAlarmAdvance(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						case CommandCode.COMMAND_7108://实时交换报警
							lst.add(msg[0]+"|"+CommandTools.convertRealSwapAlarm(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						case CommandCode.COMMAND_7109://交换车辆静态信息
							lst.add(msg[0]+"|"+CommandTools.convertSwapVehicleInfo(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						case CommandCode.COMMAND_7113://车辆定位信息交换补发
							lst.add(msg[0]+"|"+CommandTools.convertVehicleGpsSwapSend(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						case CommandCode.COMMAND_7112://交换车辆实时定位信息
							lst.add(msg[0]+"|"+CommandTools.convertSwapVehicleGps(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							break;
						default:
							lst.add(str);
						
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		this.renderJSON(lst);
	}

	/**
	 * 描述：获取上级信息总条数
	 * 
	 * @author lihaiyan
	 * @since Create on 2012-3-7
	 * @version Copyright (c) 2012 by e_trans.
	 */
	@Action(value = "getPlatFormInfoCount")
	public void getPlatFormInfoCount()
	{
		int count=0;
		Queue<String> queue = parentCommandRepository.getFlatQueue();
	    if(queue!=null){
	    	count=queue.size();
	    }
	    this.renderJSON("{\"count\":"+count+"}");
	}

	public ParentCommandRepository getParentCommandRepository()
	{
		return parentCommandRepository;
	}

	public void setParentCommandRepository(ParentCommandRepository parentCommandRepository)
	{
		this.parentCommandRepository = parentCommandRepository;
	}
	
	
	/********************************************有查岗信息、报警督办信息begin**********************************************/
	
	/**
	 * 判断是否有查岗，或者督办信息
	 * return 有则返回“true”没有则返回“false”
	 */
	@Action(value = "isCgOrDb")
	public void isCgOrDb(){
		System.out.println("laile........");
		Queue<String> queue = parentCommandRepository.getFlatQueue();
		if (queue == null || queue.size() <= 0) //没有数据
		{
			this.renderText("false");
		} else//有数据
		{
			String[] strings = queue.toArray(new String[]{});
			if (null != strings)
			{
				for (String str : strings)
				{
					String[] msg = str.split("\\|");
					int commandCode = Integer.parseInt(msg[0]);
					
					if(commandCode==CommandCode.COMMAND_7106){//有报警督办信息
						/**过滤已经回复的报警督办信息begin**/
						/**取得信息（车辆ID-报警督办id-报警督办接收时间） 协议解释请看CommandTools.convertOverseeing方法**/
						String[] msgArr = msg[1].split(",");
						String infoId = msgArr[1]+"-"+msgArr[5]+"-"+msg[2];//车辆ID-报警督办id-报警督办接收时间
						
						if(validateCgOrDb(infoId)&&validateTime(Tools.getArrayStr(msg, 2))){ //未处理并且时间再一分钟之内
							this.renderText("true");
							break;
						}
						/**过滤已经回复的报警督办信息end**/
					}else if(commandCode==CommandCode.COMMAND_7104){//平台查岗信息
						/**过滤已经回复的查岗信息begin**/
						/**取得（信息ID-查岗接收时间）**/
						//msg的格式为：[指令代码|描述@@@回复指令参数1#回复指令参数2#回复指令参数3...|接受时间]
						// msg[1]的格式为【描述@@@回复指令参数1#回复指令参数2#回复指令参数3...】
						//msg2的格式为：【查岗对象类型#查岗对象ID#信息ID】这个格式请参照CommandTools类的convertChaGuan方法
						String[] msg2 = msg[1].split("#");
						String infoId = msg2[2]+"-"+msg[2];//信息ID-查岗接收时间
						
						if(validateCgOrDb(infoId)&&validateTime(Tools.getArrayStr(msg, 2))){ //未处理
							this.renderText("true");
							break;
						}
						/**过滤已经回复的查岗信息end**/
					}
				}
			}else{//没有数据
				this.renderText("false");
			}
		}
	}
	

	/**
	 * 验证时间范围
	 * @param timeStr 接收信息的时间
	 * @return 是否在需要弹出的范围之内
	 */
	public Boolean validateTime(String timeStr){
		Boolean result = false;
		if(timeStr.equals("")||null==timeStr)return result;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd HH:mm:ss"); //格式化和解析日期的类
		Calendar calendar=Calendar.getInstance(); //日历之间转换对象
		TimeZone timeZoneChina = TimeZone.getTimeZone("Asia/Shanghai");// 获取中国的时区
		sdf.setTimeZone(timeZoneChina);// 设置系统时区
		calendar.setTimeZone(timeZoneChina);//使用给定的时区值来设置时区。
		Long nowTime=calendar.getTimeInMillis();//返回此 Calendar 的时间值，以毫秒为单位
		
		try {
			Long gpsTime=sdf.parse(timeStr).getTime();// 接收信息的时间 转化成 毫秒数
			
			if((nowTime-gpsTime)<=(1*60*1000)){ //1分钟内
				result =true;
			}else{
				result = false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * 获取查岗，或者督办信息列表
	 * @throws Exception
	 */
	@Action(value = "getCgOrDbInfo")
	public void getCgOrDbInfo() throws Exception{
		List<String> lst = null;
		try{
			Queue<String> queue = parentCommandRepository.getFlatQueue();
			if (queue == null)
			{
				this.renderJSON("");
			} else
			{
				String[] strings = queue.toArray(new String[]{});
				if (null != strings)
				{
					lst = new ArrayList<String>();
					
					for (String str : strings)
					{
						String[] msg = str.split("\\|");
						int commandCode = Integer.parseInt(msg[0]);
						
						if(commandCode==CommandCode.COMMAND_7106){//报警督办请求
							/**过滤已经回复的报警督办信息begin**/
							/**取得信息（车辆ID-报警督办id-报警督办接收时间） 协议解释请看CommandTools.convertOverseeing方法**/
							String[] msgArr = msg[1].split(",");
							String infoId = msgArr[1]+"-"+msgArr[5]+"-"+msg[2];//车辆ID-报警督办id-报警督办接收时间
							
							if(validateCgOrDb(infoId)){ //未处理
								lst.add(msg[0]+"|"+CommandTools.convertOverseeing(msg[1],this.parentCommandRepository)+"|"+msg[2]);
							}
							/**过滤已经回复的报警督办信息end**/
						}else if(commandCode==CommandCode.COMMAND_7104){//平台查岗信息
							/**过滤已经回复的查岗信息begin**/
							/**取得（信息ID-查岗接收时间）**/
							//msg的格式为：[指令代码|描述@@@回复指令参数1#回复指令参数2#回复指令参数3...|接受时间]
							// msg[1]的格式为【描述@@@回复指令参数1#回复指令参数2#回复指令参数3...】
							//msg2的格式为：【查岗对象类型#查岗对象ID#信息ID】这个格式请参照CommandTools类的convertChaGuan方法
							String[] msg2 = msg[1].split("#");
							String infoId = msg2[2]+"-"+msg[2];//信息ID-查岗接收时间
							
							if(validateCgOrDb(infoId)){ //未处理
								lst.add(str);
							}
							/**过滤已经回复的查岗信息end**/
						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		this.renderJSON(lst);
		
	}
	
	/**
	 * 验证是否已经处理了此报查岗
	 * @param alarmMapKey key值，由【信息id】
	 * @return true 已经处理，false未处理
	 */
	@SuppressWarnings({ "unchecked", "null" })
	public boolean validateCgOrDb(String CgOrDbMapKey){
		//内存已经处理的数据
//		Map<String, String> cgOrDbMap = (Map<String, String>)Struts2Utils.getSessionAttribute(Constants.CGORDB);
		Map<String, String> cgOrDbMap = Constants.cGOrDbMap;
		if(cgOrDbMap==null&&cgOrDbMap.size()<=0){ //未处理
			return true;
		}else{
			String CgOrDbStr2 = cgOrDbMap.get(CgOrDbMapKey);
			if(null==CgOrDbStr2||CgOrDbStr2.equals("")){  //未处理
				return true;
			}else{//已经处理
				return false;
			}
		}
	}
	
	
	/********************************************有查岗信息、报警督办信息	END**********************************************/

}
