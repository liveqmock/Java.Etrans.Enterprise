package com.etrans.bubiao.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.etrans.bubiao.entities.PageBean;
import com.etrans.bubiao.entities.Result;
import com.etrans.bubiao.services.IbatisService;
import com.etrans.bubiao.sys.Constants;
import com.etrans.common.util.json.JSONUtil;

@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/httpService")
public class HttpServiceAction extends BaseAction implements HttpServiceSupport
{
   
	private String jsonParam;

	@Autowired(required = true)
	private IbatisService ibatisService;
	 
	 /**
		 * 描述：调用存储过程
		 * 其中tableName表示的是ibatis查询id
		 * @author lihaiyan
		 * @since Create on 2012-2-14
		 * @version Copyright (c) 2012 by e_trans.
		 */
		@Override
		@SuppressWarnings("unchecked")
		@Action("EHCallProcedure")
	 public void EHCallProcedure()  throws Exception{
		Map<String, Object> mapJsonParams = this.getJsonParam();
		Result result=new Result();
		if(mapJsonParams==null){
			result.setCode(3);
			result.setMsg("jsonParam为空");
			this.renderJSON(result);
		}
		String tableName =(String)mapJsonParams.get(Constants.TABLE_NAME);
		Map<String,Object> mapSetParam=(Map<String,Object>)mapJsonParams.get(Constants.SET_PARAM);
		 if(StringUtils.isEmpty(tableName)){
			 result.setCode(3);
				result.setMsg("tableName为空");
				this.renderJSON(result);
		 }else {
			try
			{   
				 List<HashMap<String,String>> rows=this.ibatisService.callProcedure(tableName, mapSetParam);
				 result.setData(rows);
				 result.setCode(0);
				 String jsonResult=JSONUtil.toJson(result);
				 this.renderJSON(jsonResult);
			} catch (Exception e)
			{
				e.printStackTrace();
				result.setCode(1);
				result.setMsg(e.getMessage());
				this.renderJSON(result);
			}
		}
	 }
		/**
		 * 调用sql语句
		 * @author hgq
		 */
		@Override
		@SuppressWarnings("unchecked")
		@Action("EHSelectData")
	public void EHSelectData() throws Exception {
		Map<String, Object> mapJsonParams = this.getJsonParam();
		Result result=new Result();
		if(mapJsonParams==null){
			result.setCode(3);
			result.setMsg("jsonParam为空");
			this.renderJSON(result);
		}
		String tableName =(String)mapJsonParams.get(Constants.TABLE_NAME);
		Map<String,Object> mapWhereParam=(Map<String,Object>)mapJsonParams.get(Constants.WHERE_PARAM);
		 if(StringUtils.isEmpty(tableName)){
			 result.setCode(3);
				result.setMsg("tableName为空");
				this.renderJSON(result);
		 }else {
			try
			{   
				 List<HashMap<String,String>> rows=this.ibatisService.findIbatisList(tableName, mapWhereParam);
				 result.setData(rows);
				 result.setCode(0);
				 String jsonResult=JSONUtil.toJson(result);
				 this.renderJSON(jsonResult);
			} catch (Exception e)
			{
				e.printStackTrace();
				result.setCode(1);
				result.setMsg(e.getMessage());
				this.renderJSON(result);
			}
		}
	}	
		

		/**
		 * 描述：分页查询表记录
		 * 其中tableName表示的是ibatis查询id
		 * orderString的值为：排序:字段名=排序方式|字段名 =排序方式等以 | 隔开，注意，这里的字段名不要跟whereString里的有重复，例如按id降序 idOrder=desc
		 * 参数示例：{whereParam={page=1, pageSize=10}, tableName=getOrganzationType, orderParam={idOrder=asc}, totalName=getOrganzationTypeCount}
		 * @author lihaiyan
		 * @since Create on 2012-2-13
		 * @version Copyright (c) 2012 by e_trans.
		 */
		@Override
		@SuppressWarnings("unchecked")
		@Action("EHSelectData1")
		public void EHSelectData1() throws Exception{
			Map<String,Object> mapJsonParams=this.getJsonParam();
			Result result=new Result();
			if(mapJsonParams==null){
				result.setCode(3);
				result.setMsg("jsonParam为空");
				this.renderJSON(result);
			}
			String tableName=(String)mapJsonParams.get(Constants.TABLE_NAME);
			Map<String,Object> mapWhereParam=(Map<String,Object>)mapJsonParams.get(Constants.WHERE_PARAM);
			Map<String,Object> mapOrderParam=(Map<String,Object>)mapJsonParams.get(Constants.ORDER_PARAM);
			
			if(StringUtils.isEmpty(tableName))
			{
				 result.setCode(3);
				 result.setMsg("tableName为空");
				this.renderJSON(result);
			}else{
				/**
				 * 组装参数
				 */
				HashMap<String,Object> mapParams=new HashMap<String, Object>();
				try
				{	
					
				    //查询条件 
					if(mapWhereParam!=null&&mapWhereParam.size()>0){
						Iterator<Map.Entry<String, Object>> iterator=mapWhereParam.entrySet().iterator();
						while (iterator.hasNext())
						{
							Map.Entry<String,Object> entry =iterator.next();
							mapParams.put(entry.getKey(), entry.getValue());
						}
					}
					//排序
					if(mapOrderParam!=null&&mapOrderParam.size()>0){
						Iterator<Map.Entry<String, Object>> iterator=mapOrderParam.entrySet().iterator();
						while (iterator.hasNext())
						{
							Map.Entry<String,Object> entry =iterator.next();
							mapParams.put(entry.getKey(), entry.getValue());
						}
					}
					
				} catch (Exception e)
				{
					e.printStackTrace();
					result.setCode(2);
					 result.setMsg(e.getMessage());
					this.renderJSON(result);
				}
				try{
					 List<HashMap<String, String>> rows=this.ibatisService.findIbatisList(tableName, mapParams);
					 String totalName=(String)mapJsonParams.get(Constants.TOTAL_NAME);
					if(StringUtils.isNotEmpty(totalName)){//有分页
						Integer page=Integer.valueOf((String)mapWhereParam.get(Constants.PAGE));
						 PageBean pageBean=new PageBean();
						 pageBean.setPage(page);
						 pageBean.setRows(rows);
						 long total=0;
						
						 if(StringUtils.isNotEmpty(totalName)){
							 total=this.ibatisService.findIbatisListCount(totalName, mapParams);
						 }
						 pageBean.setTotal(total);
						 result.setData(pageBean);
					}else{
						result.setData(rows);
					}
					result.setCode(0);
					 this.renderJSON(result);
				}catch (Exception e) {
					e.printStackTrace();
					//查询出错
					result.setCode(1);
					result.setMsg(e.getMessage());
					this.renderJSON(result);
				}
			}
		}
		 /**
			 * 描述：调用存储过程
			 * 其中tableName表示的是ibatis查询id
			 * @author lihaiyan
			 * @since Create on 2012-2-14
			 * @version Copyright (c) 2012 by e_trans.
			 */
			@SuppressWarnings("unchecked")
			@Action("test")
		 public void getTest()  throws Exception{
				 List<HashMap<String,String>> rows=this.ibatisService.callProcedure("test", null);
				 this.renderJSON(rows);
		 }
		
			/**
			 * 描述：分页查询表记录
			 * 其中tableName表示的是ibatis查询id
			 * orderString的值为：排序:字段名=排序方式|字段名 =排序方式等以 | 隔开，注意，这里的字段名不要跟whereString里的有重复，例如按id降序 idOrder=desc
			 * 参数示例：{whereParam={page=1, pageSize=10}, tableName=getOrganzationType, orderParam={idOrder=asc}, totalName=getOrganzationTypeCount}
			 * @author lihaiyan
			 * @since Create on 2012-2-13
			 * @version Copyright (c) 2012 by e_trans.
			 */
			@Override
			@SuppressWarnings("unchecked")
			@Action("EHSelectDataPage")
			public void EHSelectDataPage() throws Exception{
				Map<String,Object> mapJsonParams=this.getJsonParam();
				Result result=new Result();
				if(mapJsonParams==null){
					result.setCode(3);
					result.setMsg("jsonParam为空");
					this.renderJSON(result);
				}
				String tableName=(String)mapJsonParams.get(Constants.TABLE_NAME);
				Map<String,Object> mapWhereParam=(Map<String,Object>)mapJsonParams.get(Constants.WHERE_PARAM);
				Map<String,Object> mapOrderParam=(Map<String,Object>)mapJsonParams.get(Constants.ORDER_PARAM);
				
				if(StringUtils.isEmpty(tableName))
				{
					 result.setCode(3);
					 result.setMsg("tableName为空");
					this.renderJSON(result);
				}else{
					/**
					 * 组装参数
					 */
					HashMap<String,Object> mapParams=new HashMap<String, Object>();
					try
					{	
						
					    //查询条件 
						if(mapWhereParam!=null&&mapWhereParam.size()>0){
							Iterator<Map.Entry<String, Object>> iterator=mapWhereParam.entrySet().iterator();
							while (iterator.hasNext())
							{
								Map.Entry<String,Object> entry =iterator.next();
								mapParams.put(entry.getKey(), entry.getValue());
							}
						}
						//排序
						if(mapOrderParam!=null&&mapOrderParam.size()>0){
							Iterator<Map.Entry<String, Object>> iterator=mapOrderParam.entrySet().iterator();
							while (iterator.hasNext())
							{
								Map.Entry<String,Object> entry =iterator.next();
								mapParams.put(entry.getKey(), entry.getValue());
							}
						}
						
					} catch (Exception e)
					{
						e.printStackTrace();
						result.setCode(2);
						 result.setMsg(e.getMessage());
						this.renderJSON(result);
					}
					try{
						 List<HashMap<String, String>> rows=this.ibatisService.findIbatisList(tableName, mapParams);
						 String totalName=(String)mapJsonParams.get(Constants.TOTAL_NAME);
						if(StringUtils.isNotEmpty(totalName)){//有分页
							Integer page=Integer.valueOf((String)mapWhereParam.get(Constants.PAGE));
							 PageBean pageBean=new PageBean();
							 pageBean.setPage(page);
							 pageBean.setRows(rows);
							 long total=0;
							
							 if(StringUtils.isNotEmpty(totalName)){
								 total=this.ibatisService.findIbatisListCount(totalName, mapParams);
							 }
							 pageBean.setTotal(total);
							 result.setData(pageBean);
						}else{
							result.setData(rows);
						}
						result.setCode(0);
						 this.renderJSON(result);
					}catch (Exception e) {
						e.printStackTrace();
						//查询出错
						result.setCode(1);
						result.setMsg(e.getMessage());
						this.renderJSON(result);
					}
				}
			}		

		
	@SuppressWarnings("unchecked")
	private Map<String,Object> getJsonParam()
	{
		return JSONUtil.fromJson(this.jsonParam, Map.class);
	}

	public void setJsonParam(String jsonParam)
	{
		this.jsonParam = jsonParam;
	}


	
	public static void main(String[] args){
		String jsonString="{\"tableName\":\"MSC_MSCConfigure\",\"setParam\":[{\"mscname\":\"test11\",\"mschost\":\"127.0.0.1\",\"mscport\":8},{\"mscname\":\"test12\",\"mschost\":\"127.0.0.1\",\"mscport\":88},{\"mscname\":\"test13\",\"mschost\":\"127.0.0.1\",\"mscport\":88}]}";
	     Map<String, Object> test=JSONUtil.fromJson(jsonString, Map.class);
	}
	
	
	
	
   
	
}
