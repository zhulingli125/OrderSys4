/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.login.waiters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.chinasofti.ordersys.vo.OrderSumInfo;
import com.chinasofti.ordersys.vo.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.ordersys.mapper.OrderMapper;
import com.chinasofti.ordersys.vo.Cart;
import com.chinasofti.ordersys.vo.OrderInfo;

/**
 * <p>
 * Title: OrderService
 * </p>
 * <p>
 * Description: 订单管理服务对象
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
public class OrderService {
	@Autowired
	OrderMapper mapper;
	private static Log log = LogFactory.getLog(OrderService.class);

	public OrderMapper getMapper() {
		return mapper;
	}

	public int updateOrder(int orderId){
		return mapper.updateOrderState(orderId);
	}

	public void setMapper(OrderMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * 增加订单的放发
	 * 
	 * @param waiterId
	 *            订单点餐的服务员id
	 * @param tableId
	 *            订单对应的桌号
	 * @return 添加成功的订单对应的主键值(Long型)
	 * */
	public Object addOrder(int waiterId, int tableId) {

		OrderInfo info = new OrderInfo();
		info.setWaiterId(waiterId);
		info.setTableId(tableId);
		// 获取带有连接池的数据库模版操作工具对象
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取添加订单时的时间
//		try {
//			info.setOrderBeginDate(sdf.parse(new Date().toString()));
//		} catch (Exception e){
//			log.error(e);
//		}
		info.setOrderBeginDate(new Date());

		mapper.addOrder(info);
		// 由于订单表只有单列主键，因此将第一个生成的主键值返回
		return new Long(info.getOrderId());
	}

	/**
	 * 添加订单菜品详细信息的方法
	 * 
	 * @param unit
	 *            订单菜品详情
	 * @param key
	 *            对应的订单Id
	 * */
	public void addOrderDishesMap(Cart.CartUnit unit, int key) {
		// 获取带有连接池的数据库模版操作工具对象
		mapper.addOrderDishesMap(unit, key);

	}

	/**
	 * 以分页方式获取不同支付状态订单信息的方法
	 * 
	 * @param page
	 *            需要获取的页码数
	 * @param pageSize
	 *            每页显示的条目数
	 * @param state
	 *            需要查询的支付状态信息
	 * @return 查询结果列表
	 * */
	public List<OrderInfo> getNeedPayOrdersByPage(int page, int pageSize,
			int state) {
		// 获取带有连接池的数据库模版操作工具对象

		// ArrayList<OrderInfo> list = helper
		// .preparedForPageList(
		// "select orderId,orderBeginDate,orderEndDate,waiterId,orderState,dishes,num from orderinfo,orderdishes where orderinfo.orderId=orderdishes.orderReference and orderinfo.orderState=0",
		// new Object[] {}, page, pageSize, OrderInfo.class);

		int first = (page - 1) * pageSize;
		// 进行查询操作
		List<OrderInfo> list = mapper.getNeedPayOrdersByPage(first, pageSize,
				state);
		// 返回查询的结果
		return list;

	}

	/**
	 * 获取特定支付状态订单的总页数
	 * 
	 * @param pageSize
	 *            每页显示的条目数
	 * @param state
	 *            订单支付状态
	 * @return 总页数
	 * */
	public int getMaxPage(int pageSize, int state) {

		// 查询符合条件的总条目数
		Long rows = mapper.getMaxPage(state);
		// 计算总页数并返回
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 获取订单信息的最大页数
	 *
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前数据库中数据的最大页数
	 * */
	public int getMaxPageByState(int pageSize,int completedState) {

		// 获取最大页数信息
		Long rows = mapper.getMaxPageByState(completedState);
		// 返回最大页数
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * 分页获取订单数据的方法
	 *
	 * @param page
	 *            要获取数据的页号
	 * @param pageSize
	 *            每页显示的条目数
	 * @return 当前页的用户数据列表
	 * */
	public List<OrderInfo> getByPage(int page, int pageSize) {
		// 获取带有连接池的数据库模版操作工具对象
		int first = (page - 1) * pageSize;
		// 返回结果
		return mapper.getOrderByPage(first, pageSize);
	}

	/**
	 * 获取不同支付状态订单信息的方法
	 * 
	 * @param state
	 *            需要查询的支付状态信息
	 * @return 订单信息集合
	 */
	public List<OrderInfo> getNeedPayOrders(int state) {
		// 获取带有连接池的数据库模版操作工具对象

		// 返回查询结果
		return mapper.getNeedPayOrders(state);

	}

	/**
	 * 请求支付订单的方法
	 * 
	 * @param orderId
	 *            请求支付订单的订单号
	 */
	public void requestPay(Integer orderId) {
		// 获取带有连接池的数据库模版操作工具对象
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		mapper.requestPay(orderId, now);

	}

	/**
	 * 根据订单号获取订单详情的方法
	 * 
	 * @param orderId
	 *            需要获取详情的订单号
	 * @return 查询到的订单详细信息
	 * */
	public OrderInfo getOrderById(Integer orderId) {

		// 执行查询并返回结果
		return mapper.getOrderById(orderId);

	}

	/**
	 * 获取单一订单的总价
	 * 
	 * @param
	 * @return 查询到的总价
	 * */
	public float getSumPriceByOrderId(Integer orderId) {

		// 查询总价
		Double sum = mapper.getSumPriceByOrderId(orderId);
		System.out.println(orderId + "-------------------------" + sum);
		// 返回总价
		return sum.floatValue();
	}

	/**
	 * 根据订单号获取订单详情
	 * 要获取详情的订单号
	 * @param
	 * @return 订单详情列表
	 * */
	public List<OrderInfo> getOrderDetailById(Integer orderId) {

		// 查询并返回订单详情列表
		return mapper.getOrderDetailById(orderId);

	}

	/**
	 * 修改订单支付状态的方法
	 * 
	 * @param orderId
	 *            要修改状态的订单号
	 * @param state
	 *            目标状态值
	 * */
	public void changeState(Integer orderId, int state) {
		mapper.changeState(orderId, state);

	}

	/**
	 * 根据结单时间段查询订单信息的方法
	 * 
	 * @param beginDate
	 *            查询的开始时间
	 * @param endDate
	 *            查询的结束时间
	 * @return 结单时间在开始时间和结束时间之间的所有订单列表
	 * */
	public List<OrderInfo> getOrderInfoBetweenDate(Date beginDate, Date endDate,int curPage,int pageSize) {

		return mapper.getOrderInfoBetweenDate(
				new java.sql.Date(beginDate.getTime()), new java.sql.Date(
						endDate.getTime()),curPage,pageSize);
	}

	public List<OrderSumInfo> getOrderSum(){
		return mapper.getOrderSum();
	}



}
