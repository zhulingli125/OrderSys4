/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.ordersys.service.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinasofti.ordersys.mapper.UserInfoMapper;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.sec.Passport;

/**
 * <p>
 * Title: CheckUserPassService
 * </p>
 * <p>
 * Description: �ж��û������Ƿ���ȷ�ķ������
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
public class CheckUserPassService {
	@Autowired
	UserInfoMapper mapper;
	
	

	public UserInfoMapper getMapper() {
		return mapper;
	}



	public void setMapper(UserInfoMapper mapper) {
		this.mapper = mapper;
	}



	/**
	 * ��֤�û��û��������Ƿ���ȷ�ķ���
	 * 
	 * @param info
	 *            �����ж��û�����������û�����
	 * @return �û����������Ƿ���֤ͨ����true��ʾ�û���������ȷ��false��ʾ�û������������
	 * */
	public boolean checkPass(UserInfo info) {
		
		// ���ݸ������û�����ѯ�û���Ϣ
		List<UserInfo> userList = mapper.checkPass(info);
		// �ж���ѯ�������
		switch (userList.size()) {
		// ���û�в�ѯ���κ�����
		case 0:
			// ������֤ʧ��
			return false;
			// �����ѯ��һ����¼���ж������Ƿ�һ��
		case 1:
			// �������ܶ���
			Passport passport = new Passport();
			// �ж��û���������������ݿ��е������Ƿ�һ��
			if (userList.get(0).getUserPass()
					.equals(passport.md5(info.getUserPass()))) {
				// ���һ�£��򷵻�true
				return true;
				// �����һ��
			} else {
				// �����û��������벻ƥ��
				return false;
			}

		}
		// ��������·�����֤ʧ��
		return false;
	}
}
