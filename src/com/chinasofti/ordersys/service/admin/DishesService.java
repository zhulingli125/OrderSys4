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
 * Description: ��Ʒ����������
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
	 * ��ҳ��ȡ��Ʒ���ݵķ���
	 * 
	 * @param page
	 *            Ҫ��ȡ���ݵ�ҳ��
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰҳ�Ĳ�Ʒ�����б�
	 * */
	public List<DishesInfo> getDishesInfoByPage(int page, int pageSize) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		int first = (page - 1) * pageSize;
		// ���ؽ��
		return mapper.getDishesInfoByPage(first, pageSize);

	}

	/**
	 * ��ȡ��Ʒ��Ϣ�����ҳ��
	 * 
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰ���ݿ������ݵ����ҳ��
	 * */
	public int getMaxPage(int pageSize) {

		Long rows = mapper.getMaxPage();
		// �������ҳ��
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * ���ݲ�ƷIDֵɾ����Ʒ��Ϣ�ķ���
	 * 
	 * @param dishesId
	 *            Ҫɾ���Ĳ�ƷId
	 * */
	public void deleteDishesById(Integer dishesId) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		mapper.deleteDishesById(dishesId);
	}

	/**
	 * ��Ӳ�Ʒ�ķ���
	 * 
	 * @param info
	 *            ��Ҫ��ӵĲ�Ʒ��Ϣ
	 * */
	public void addDishes(DishesInfo info) {
		mapper.addDishes(info);

	}

	/**
	 * ����dishesId��ȡ��Ʒ��ϸ��Ϣ�ķ���
	 * 
	 * @param dishesId
	 *            Ҫ��ȡ��Ϣ���ض���ƷId
	 * @return ���ظ�id�Ĳ�Ʒ��ϸ��Ϣ
	 * */
	public DishesInfo getDishesById(Integer dishesId) {

		return mapper.getDishesById(dishesId);
	}

	/**
	 * �޸Ĳ�Ʒ��Ϣ�ķ���
	 * 
	 * @param Info
	 *            Ҫ�޸ĵĲ�Ʒ��Ϣ������dishesIdΪ�޸����ݣ�������ϢΪ�޸ĵ�Ŀ��ֵ
	 * */
	public void modifyDishes(DishesInfo info) {
		mapper.modifyDishes(info);

	}

	/**
	 * ��ȡͷ4���Ƽ���Ʒ����Ϣ
	 * 
	 * @return ͷ4���Ƽ���Ʒ�б�
	 * */
	public List<DishesInfo> getTop4RecommendDishes() {

		return mapper.getTop4RecommendDishes();

	}

}
