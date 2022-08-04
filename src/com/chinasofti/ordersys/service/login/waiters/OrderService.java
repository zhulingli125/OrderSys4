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
 * Description: ��������������
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
	 * ���Ӷ����ķŷ�
	 * 
	 * @param waiterId
	 *            ������͵ķ���Աid
	 * @param tableId
	 *            ������Ӧ������
	 * @return ��ӳɹ��Ķ�����Ӧ������ֵ(Long��)
	 * */
	public Object addOrder(int waiterId, int tableId) {

		OrderInfo info = new OrderInfo();
		info.setWaiterId(waiterId);
		info.setTableId(tableId);
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// ��ȡ��Ӷ���ʱ��ʱ��
//		try {
//			info.setOrderBeginDate(sdf.parse(new Date().toString()));
//		} catch (Exception e){
//			log.error(e);
//		}
		info.setOrderBeginDate(new Date());

		mapper.addOrder(info);
		// ���ڶ�����ֻ�е�����������˽���һ�����ɵ�����ֵ����
		return new Long(info.getOrderId());
	}

	/**
	 * ��Ӷ�����Ʒ��ϸ��Ϣ�ķ���
	 * 
	 * @param unit
	 *            ������Ʒ����
	 * @param key
	 *            ��Ӧ�Ķ���Id
	 * */
	public void addOrderDishesMap(Cart.CartUnit unit, int key) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		mapper.addOrderDishesMap(unit, key);

	}

	/**
	 * �Է�ҳ��ʽ��ȡ��֧ͬ��״̬������Ϣ�ķ���
	 * 
	 * @param page
	 *            ��Ҫ��ȡ��ҳ����
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @param state
	 *            ��Ҫ��ѯ��֧��״̬��Ϣ
	 * @return ��ѯ����б�
	 * */
	public List<OrderInfo> getNeedPayOrdersByPage(int page, int pageSize,
			int state) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���

		// ArrayList<OrderInfo> list = helper
		// .preparedForPageList(
		// "select orderId,orderBeginDate,orderEndDate,waiterId,orderState,dishes,num from orderinfo,orderdishes where orderinfo.orderId=orderdishes.orderReference and orderinfo.orderState=0",
		// new Object[] {}, page, pageSize, OrderInfo.class);

		int first = (page - 1) * pageSize;
		// ���в�ѯ����
		List<OrderInfo> list = mapper.getNeedPayOrdersByPage(first, pageSize,
				state);
		// ���ز�ѯ�Ľ��
		return list;

	}

	/**
	 * ��ȡ�ض�֧��״̬��������ҳ��
	 * 
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @param state
	 *            ����֧��״̬
	 * @return ��ҳ��
	 * */
	public int getMaxPage(int pageSize, int state) {

		// ��ѯ��������������Ŀ��
		Long rows = mapper.getMaxPage(state);
		// ������ҳ��������
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * ��ȡ������Ϣ�����ҳ��
	 *
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰ���ݿ������ݵ����ҳ��
	 * */
	public int getMaxPageByState(int pageSize,int completedState) {

		// ��ȡ���ҳ����Ϣ
		Long rows = mapper.getMaxPageByState(completedState);
		// �������ҳ��
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * ��ҳ��ȡ�������ݵķ���
	 *
	 * @param page
	 *            Ҫ��ȡ���ݵ�ҳ��
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰҳ���û������б�
	 * */
	public List<OrderInfo> getByPage(int page, int pageSize) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		int first = (page - 1) * pageSize;
		// ���ؽ��
		return mapper.getOrderByPage(first, pageSize);
	}

	/**
	 * ��ȡ��֧ͬ��״̬������Ϣ�ķ���
	 * 
	 * @param state
	 *            ��Ҫ��ѯ��֧��״̬��Ϣ
	 * @return ������Ϣ����
	 */
	public List<OrderInfo> getNeedPayOrders(int state) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���

		// ���ز�ѯ���
		return mapper.getNeedPayOrders(state);

	}

	/**
	 * ����֧�������ķ���
	 * 
	 * @param orderId
	 *            ����֧�������Ķ�����
	 */
	public void requestPay(Integer orderId) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		java.sql.Date now = new java.sql.Date(System.currentTimeMillis());
		mapper.requestPay(orderId, now);

	}

	/**
	 * ���ݶ����Ż�ȡ��������ķ���
	 * 
	 * @param orderId
	 *            ��Ҫ��ȡ����Ķ�����
	 * @return ��ѯ���Ķ�����ϸ��Ϣ
	 * */
	public OrderInfo getOrderById(Integer orderId) {

		// ִ�в�ѯ�����ؽ��
		return mapper.getOrderById(orderId);

	}

	/**
	 * ��ȡ��һ�������ܼ�
	 * 
	 * @param
	 * @return ��ѯ�����ܼ�
	 * */
	public float getSumPriceByOrderId(Integer orderId) {

		// ��ѯ�ܼ�
		Double sum = mapper.getSumPriceByOrderId(orderId);
		System.out.println(orderId + "-------------------------" + sum);
		// �����ܼ�
		return sum.floatValue();
	}

	/**
	 * ���ݶ����Ż�ȡ��������
	 * Ҫ��ȡ����Ķ�����
	 * @param
	 * @return ���������б�
	 * */
	public List<OrderInfo> getOrderDetailById(Integer orderId) {

		// ��ѯ�����ض��������б�
		return mapper.getOrderDetailById(orderId);

	}

	/**
	 * �޸Ķ���֧��״̬�ķ���
	 * 
	 * @param orderId
	 *            Ҫ�޸�״̬�Ķ�����
	 * @param state
	 *            Ŀ��״ֵ̬
	 * */
	public void changeState(Integer orderId, int state) {
		mapper.changeState(orderId, state);

	}

	/**
	 * ���ݽᵥʱ��β�ѯ������Ϣ�ķ���
	 * 
	 * @param beginDate
	 *            ��ѯ�Ŀ�ʼʱ��
	 * @param endDate
	 *            ��ѯ�Ľ���ʱ��
	 * @return �ᵥʱ���ڿ�ʼʱ��ͽ���ʱ��֮������ж����б�
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
