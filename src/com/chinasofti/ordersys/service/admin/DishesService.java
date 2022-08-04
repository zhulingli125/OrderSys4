/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.ordersys.mapper.DishesInfoMapper;
import com.chinasofti.ordersys.vo.DishesInfo;

/**
 * <p>
 * Title: DishesService
 * </p>
 * <p>
 * Description: 菜品管理服务对象
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
public class DishesService {
	@Autowired
	DishesInfoMapper mapper;

	public DishesInfoMapper getMapper() {
		return mapper;
	}

	public void setMapper(DishesInfoMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 分页获取菜品数据的方法
	 * 
	 * @param page
	 *            要获取数据的页号
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前页的菜品数据列表
	 * */
	public List<DishesInfo> getDishesInfoByPage(int page, int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		int first = (page - 1) * pageSize;
		// 返回结果
		return mapper.getDishesInfoByPage(first, pageSize);

	}

	/**
	 * 获取菜品信息的最大页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前数据库中数据的最大页数
	 * */
	public int getMaxPage(int pageSize) {

		Long rows = mapper.getMaxPage();
		// 返回最大页数
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 根据菜品ID值删除菜品信息的方法
	 * 
	 * @param dishesId
	 *            要删除的菜品Id
	 * */
	public void deleteDishesById(Integer dishesId) {
		// 获取带有连接池的数据库模版操作工具对象
		mapper.deleteDishesById(dishesId);
	}

	/**
	 * 添加菜品的方法
	 * 
	 * @param info
	 *            需要添加的菜品信息
	 * */
	public void addDishes(DishesInfo info) {
		mapper.addDishes(info);

	}

	/**
	 * 根据dishesId获取菜品详细信息的方法
	 * 
	 * @param dishesId
	 *            要获取信息的特定菜品Id
	 * @return 返回该id的菜品详细信息
	 * */
	public DishesInfo getDishesById(Integer dishesId) {

		return mapper.getDishesById(dishesId);
	}

	/**
	 * 修改菜品信息的方法
	 * 
	 * @param Info
	 *            要修改的菜品信息，其中dishesId为修改依据，其余信息为修改的目标值
	 * */
	public void modifyDishes(DishesInfo info) {
		mapper.modifyDishes(info);

	}

	/**
	 * 获取头4条推荐菜品的信息
	 * 
	 * @return 头4条推荐菜品列表
	 * */
	public List<DishesInfo> getTop4RecommendDishes() {

		return mapper.getTop4RecommendDishes();

	}

}
