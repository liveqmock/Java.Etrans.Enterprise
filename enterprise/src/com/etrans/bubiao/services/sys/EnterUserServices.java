/**
 * UserServices.java
 * Create on 2012-2-10下午02:43:49
 * Copyright (c) 2012 by e_trans.
 */
package com.etrans.bubiao.services.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.etrans.bubiao.entities.PageBean;
import com.etrans.bubiao.entities.User;
import com.etrans.bubiao.services.IbatisServices;
import com.etrans.common.util.ParamKey;

/**
 * 企业用户信息管理Services
 * 
 * @author Ivan
 * @version 1.0
 */
@Service
public class EnterUserServices {

	@Autowired
	private IbatisServices ibatisServices;
	
	private String adminState;
	
	private long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAdminState() {
		return adminState;
	}

	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	/**
	 * setIbatisServices
	 * 
	 * @param ibatisServices
	 */
	public void setIbatisServices(IbatisServices ibatisServices) {
		this.ibatisServices = ibatisServices;
	}

	/**
	 * 编辑企业用户信息
	 * 
	 * @throws Exception
	 */
	public void editUser(Map<String, Object> param) throws Exception {
		this.ibatisServices.updateIbatisObject("Pub_User", param);
	}

	/**
	 * 验证名称是否存在
	 * 
	 * @param whereMap
	 * @return
	 * @throws Exception
	 */
	public HashMap getPubUserByName(Map<String, Object> paramMap) throws Exception {
		return this.ibatisServices.queryForObject(HashMap.class, "getPubUserByNameSQL", paramMap );
	}
	
	
	
	/**
	 * 根据userId判断是否超级管理员
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findIsSuperUser(Map params){
		return this.ibatisServices.queryForList(Map.class, "findIsSuperUserSQL",params);
	}
	
	/**
	 * 根据userId判断是否企业管理员
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findIsAdminUser(Map params){
		return this.ibatisServices.queryForList(Map.class, "findIsAdminUserSQL",params);
	}
	
	/**
	 * 分页查询企业用户信息,返回表格数据
	 * @param queryJSON
	 * @return
	 * @throws Exception
	 */
	public PageBean findEnterUsers(Map<String,Object> params) throws Exception {
		PageBean pageBean = new PageBean();
		List<Map<String,Object>> list = (List<Map<String, Object>>) this.findUsers(params);
		Long total = findUsersCount(params);
		pageBean.setPage((Integer)params.get(ParamKey.PAGE));
		pageBean.setRows(list);
		pageBean.setTotal(total);
		return pageBean;
		
	}
	
	
	/**
	 * 分页查询企业用户信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({"unchecked" })
	public List<Map<String,Object>> findUsers(Map params) throws Exception {
		
		List<Map<String,Object>> List = this.ibatisServices.queryForList(Map.class, "findEnterUsers",params);
		return List;
		
	}
	
	/**
	 * 查询企业用户数量
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Long findUsersCount(Map<String,Object> params) throws Exception {
		return this.ibatisServices.findIbatisListCount("findEnterUsersCount", params);
		
	}
	
	

	/**
	 * 根据用户ID查找企业用户
	 * 
	 * @param id
	 * @return User
	 * @throws Exception
	 */
	public User findEnterUserById(String id)throws Exception{
		return ibatisServices.queryForObject(User.class, "findEnterUserByIdSQL", id);
	}
	
	/**
	 * 修改用户密码
	 * 
	 * @param param
	 * @throws Exception
	 */
	public void updatePassword(Map<String,Object> param)throws Exception{
		this.ibatisServices.updateIbatisObject("enterPasswordUpdateSql", param);
	}
	
	/**
	 * 更新企业管理员
	 * 1、获取原企业管理员
	 * 2、更新企业管理员ID，同步更新用户超级管理员字段
	 * @param userId
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Integer updateWorkUnitAdminId(Long userId,Long workUnitId) throws Exception{
		HashMap<String,Object> map = new HashMap<String, Object>();
		Integer oldUserAdminId = 0;
	
		
		map.put("id", workUnitId);
		
		
		HashMap<String,Object> valueMap = ibatisServices.queryForObject(HashMap.class, "getWorkUnitByIdSQL", map);
		if(valueMap!=null){
			if("0".equals(getAdminState())){
				map.put("userId", getUserId());
				List<Map> List = this.ibatisServices.queryForList(Map.class, "getCreateUserIdSQL",map);//取到当前要设置的用户的Id 所对应的createUserId
				if(List.size()>0){
					if("1".equals(List.get(0).get("createUserId").toString())){
						map.put("createUserId", getUserId());
					}else{
						map.put("createUserId", List.get(0).get("createUserId").toString());
					}
					ibatisServices.updateIbatisObject("updateCreateUserId_1", map);
					ibatisServices.updateIbatisObject("updateUserAdminId", map);
					ibatisServices.updateIbatisObject("updateCreateUserId_x", map);
				}
				map.put("userId", null);
				if(valueMap.get("AdminUserID")!=null){
					map.put("flag", "0");
					ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
				}else{
					map.put("flag", "1");
					ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
					oldUserAdminId = Integer.valueOf(String.valueOf(getUserId()));
				}
			}
			if("1".equals(getAdminState())){
				map.put("userId", getUserId());
				List<Map> List = this.ibatisServices.queryForList(Map.class, "getCreateUserIdSQL",map);//取到当前要设置的用户的Id 所对应的createUserId
				if(List.size()>0){
					map.put("createUserId", List.get(0).get("createUserId").toString());
					ibatisServices.updateIbatisObject("updateCreateUserId_1", map);
					ibatisServices.updateIbatisObject("updateUserAdminId", map);
					ibatisServices.updateIbatisObject("updateCreateUserId_x", map);
				}
				if(valueMap.get("AdminUserID")!=null){
					map.put("flag", "0");// 如果是‘0’ 设置 adminUserId =null
					ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
				}else{
					map.put("flag", "1");//如果是‘1’ 设置 adminUserId =userId
					ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
					oldUserAdminId = Integer.valueOf(String.valueOf(getUserId()));
				}
			}
			
			map.put("userId", getUserId());
			if(valueMap.get("AdminUserID")!=null){
				map.put("flag", "0");
				ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
			}else{
				map.put("flag", "1");
				ibatisServices.updateIbatisObject("updateWorkUnitAdminId", map);
				oldUserAdminId = Integer.valueOf(String.valueOf(getUserId()));
			}
		}else{
			throw new Exception("没有查询到所属企业!请核对企业编号是否存在!企业编号:"+workUnitId);
		}
		return oldUserAdminId;
	}
	
	/**
	 * 更新用户超级管理员标志
	 * 
	 * @param userId
	 * @param isSuper
	 * @return effectRow
	 */
	public int updateUserIsSuper(long userId,int isSuper){
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("IsSuperUser", isSuper);
		return ibatisServices.updateIbatisObject("updateUserIsSuperUser", map);
	}
	
	/**
	 * 更新用是否显示操作标志
	 * 
	 * @param userId
	 * @param IsShowHandle
	 * @return effectRow
	 */
	public int updateShowHandle(long userId,int isShowHandle){
		HashMap<String,Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("IsShowHandle", isShowHandle);
		return ibatisServices.updateIbatisObject("updateUserIsShowHandle", map);
	}

}
