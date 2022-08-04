/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.login;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.chinasofti.ordersys.mapper.OrderMapper;
import com.chinasofti.ordersys.vo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.ordersys.listeners.OrderSysListener;
import com.chinasofti.ordersys.mapper.UserInfoMapper;
import com.chinasofti.ordersys.vo.UserInfo;

/**
 * <p>
 * Title: LoginService
 * </p>
 * <p>
 * Description: �ж��û��Ƿ��¼�ɹ��ķ������
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
public class LoginService {

	@Autowired
	UserInfoMapper mapper;
	@Autowired
	OrderMapper orderMapper;
	public UserInfoMapper getMapper() {
		return mapper;
	}

	public void setMapper(UserInfoMapper mapper) {
		this.mapper = mapper;
	}


	public List<OrderInfo> orderInfoList(){
		return orderMapper.getOrderByCompletedState();
	}

	/**
	 * �û�������ı�ʶ
	 * */
	public static final int WRONG_USERNAME = 0;
	/**
	 * �������ı�ʶ
	 * */
	public static final int WRONG_PASSWORD = 1;
	/**
	 * �û��Ѿ����߲����ظ���¼�ı�ʶ
	 * */
	public static final int USER_ALREADY_ONLINE = 2;
	/**
	 * �û���������ʶ
	 * */
	public static final int WRONG_LOCKED = 3;
	/**
	 * ��������ı�ʶ
	 * */
	public static final int WRONG_OTHER = 4;
	/**
	 * ��¼�ɹ���ʶ
	 * */
	public static final int LOGIN_OK = 5;

	/**
	 * ��¼�ɹ����û���Ϣ
	 * */
	private UserInfo loginUser = null;

	/**
	 * ��ȡ��¼�ɹ����û���Ϣ�ķ���
	 * 
	 * @return ���ص�¼�ɹ����û���Ϣ
	 * */
	public UserInfo getLoginUser() {
		// �����û���Ϣ
		return loginUser;
	}

	/**
	 * ��¼���ж�����
	 * 
	 * @param Info
	 *            �û���������ж���¼�û���Ϣ
	 * @return ��¼�ж������ʶֵ
	 * */
	public int login(UserInfo info) {
		// ��ȡ��ǰ���ߵ��û���Ϣ�������ж��û��Ƿ��Ѿ�����
		Hashtable<String, UserInfo> loginUserMap = OrderSysListener.sessions;
		// ��ȡ���������Ѿ���¼�û���Ӧ��sessionId
		Set<String> loginIds = loginUserMap.keySet();
		// ��ȡsessionId������
		Iterator<String> it = loginIds.iterator();
		// ����sessionId
		while (it.hasNext()) {
			// ��ȡ�ض��������û���Ϣ
			UserInfo user = loginUserMap.get(it.next());
			// ���ĳ�������û����û������û���¼���û�����ͬ��˵����ͼ��¼���û��Ѿ�����
			if (user.getUserAccount().equals(info.getUserAccount())) {
				// ���ش����ʶ
				return USER_ALREADY_ONLINE;
			}
		}
		/////////////////////////////////////////////

		//////////////////////////////////////////////
		// ��ȡ�������ӳص����ݿ�ģ��������߶���

		List<UserInfo> userList = mapper.findUsersByName(info.getUserAccount());
		// �ж��Ƿ��ѯ������
		switch (userList.size()) {
		// ���û�в�ѯ�����ݣ�˵���û���������
		case 0:
			// �����û�������ı�ʶ
			return WRONG_USERNAME;
			// �����ѯ������
		case 1:
			// ��ȡ���ݿ��е��û���Ϣ
			UserInfo dbUser = userList.get(0);

			// System.out.println(info.getLocked());
			// ����û��Ѿ�������
			if (dbUser.getLocked() == 1) {
				// �����¼�û���Ϣ
				loginUser = dbUser;
				// �����û��Ѿ��������ı�ʶ
				return WRONG_LOCKED;
			}
			// ����û�����ƥ��
			if (info.getUserPass().equals(dbUser.getUserPass())) {
				// �����¼�û���Ϣ
				loginUser = dbUser;
				// ���ص�¼�ɹ���ʶ
				return LOGIN_OK;
				// ������벻ƥ��
			} else {
				// ������������ʶ
				return WRONG_PASSWORD;
			}
			// �������
		default:
			// �������������ʶ
			return WRONG_OTHER;

		}

	}
}
