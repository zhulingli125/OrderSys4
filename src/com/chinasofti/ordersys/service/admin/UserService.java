/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.ordersys.mapper.UserInfoMapper;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.sec.Passport;

/**
 * <p>
 * Title: UserService
 * </p>
 * <p>
 * Description: 用户管理服务对象
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
@Service
public class UserService {
	@Autowired
	UserInfoMapper mapper;
	@Autowired
	Passport passport;

	public Passport getPassport() {
		return passport;
	}

	public void setPassport(Passport passport) {
		this.passport = passport;
	}

	public UserInfoMapper getMapper() {
		return mapper;
	}

	public void setMapper(UserInfoMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 分页获取用户数据的方法
	 * 
	 * @param page
	 *            要获取数据的页号
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前页的用户数据列表
	 * */
	public List<UserInfo> getByPage(int page, int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		int first = (page - 1) * pageSize;
		// 返回结果
		return mapper.getUserByPage(first, pageSize);
	}

	/**
	 * 获取用户信息的最大页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前数据库中数据的最大页数
	 * */
	public int getMaxPage(int pageSize) {

		// 获取最大页数信息
		Long rows = mapper.getMaxPage();
		// 返回最大页数
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 添加用户的方法
	 * 
	 * @param info
	 *            需要添加的用户信息
	 * */
	public void addUser(UserInfo info) {

		// 创建加密工具

		info.setUserPass(passport.md5(info.getUserPass()));
		// 执行用户信息插入操作
		mapper.addUser(info);
	}

	/**
	 * 删除用户的方法
	 * 
	 * @param userId
	 *            待删除用户的Id
	 * */
	public void deleteUser(Integer userId) {
		// 获取带有连接池的数据库模版操作工具对象
		mapper.deleteUser(userId);
	}

	/**
	 * 修改用户自身信息的方法
	 * 
	 * @param info
	 *            需要修改的用户信息，其中userId属性指明需要修改的用户ID，其他信息为目标值，本人修改信息只能修改密码和头像
	 * */
	public void modify(UserInfo info) {
		// 获取带有连接池的数据库模版操作工具对象
		info.setUserPass(passport.md5(info.getUserPass()));
		// 修改本人信息
		mapper.modify(info);

	}

	/**
	 * 管理员修改用户信息的方法
	 * 
	 * @param info
	 *            需要修改的用户信息，其中userId属性指明需要修改的用户ID，其他信息为目标值
	 * */
	public void adminModify(UserInfo info) {
		info.setUserPass(passport.md5(info.getUserPass()));
		// 修改本人信息
		mapper.adminModify(info);

	}

	/**
	 * 根据ID获取用户详细信息的方法
	 * 
	 * @param userId
	 *            需要获取详细信息的用户ID
	 * @return 返回查询到的用户详细信息
	 * */
	public UserInfo getUserById(Integer userId) {

		return mapper.getUserById(userId);
	}

	public List<UserInfo> findUserByName(String userAccount) {
		return mapper.findUsersByName(userAccount);
	}
	


}
