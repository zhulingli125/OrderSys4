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
 * Description: �û�����������
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
	 * ��ҳ��ȡ�û����ݵķ���
	 * 
	 * @param page
	 *            Ҫ��ȡ���ݵ�ҳ��
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰҳ���û������б�
	 * */
	public List<UserInfo> getByPage(int page, int pageSize) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		int first = (page - 1) * pageSize;
		// ���ؽ��
		return mapper.getUserByPage(first, pageSize);
	}

	/**
	 * ��ȡ�û���Ϣ�����ҳ��
	 * 
	 * @param pageSize
	 *            ÿҳ��ʾ����Ŀ��
	 * @return ��ǰ���ݿ������ݵ����ҳ��
	 * */
	public int getMaxPage(int pageSize) {

		// ��ȡ���ҳ����Ϣ
		Long rows = mapper.getMaxPage();
		// �������ҳ��
		return (int) ((rows.longValue() - 1) / pageSize + 1);
	}

	/**
	 * ����û��ķ���
	 * 
	 * @param info
	 *            ��Ҫ��ӵ��û���Ϣ
	 * */
	public void addUser(UserInfo info) {

		// �������ܹ���

		info.setUserPass(passport.md5(info.getUserPass()));
		// ִ���û���Ϣ�������
		mapper.addUser(info);
	}

	/**
	 * ɾ���û��ķ���
	 * 
	 * @param userId
	 *            ��ɾ���û���Id
	 * */
	public void deleteUser(Integer userId) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		mapper.deleteUser(userId);
	}

	/**
	 * �޸��û�������Ϣ�ķ���
	 * 
	 * @param info
	 *            ��Ҫ�޸ĵ��û���Ϣ������userId����ָ����Ҫ�޸ĵ��û�ID��������ϢΪĿ��ֵ�������޸���Ϣֻ���޸������ͷ��
	 * */
	public void modify(UserInfo info) {
		// ��ȡ�������ӳص����ݿ�ģ��������߶���
		info.setUserPass(passport.md5(info.getUserPass()));
		// �޸ı�����Ϣ
		mapper.modify(info);

	}

	/**
	 * ����Ա�޸��û���Ϣ�ķ���
	 * 
	 * @param info
	 *            ��Ҫ�޸ĵ��û���Ϣ������userId����ָ����Ҫ�޸ĵ��û�ID��������ϢΪĿ��ֵ
	 * */
	public void adminModify(UserInfo info) {
		info.setUserPass(passport.md5(info.getUserPass()));
		// �޸ı�����Ϣ
		mapper.adminModify(info);

	}

	/**
	 * ����ID��ȡ�û���ϸ��Ϣ�ķ���
	 * 
	 * @param userId
	 *            ��Ҫ��ȡ��ϸ��Ϣ���û�ID
	 * @return ���ز�ѯ�����û���ϸ��Ϣ
	 * */
	public UserInfo getUserById(Integer userId) {

		return mapper.getUserById(userId);
	}

	public List<UserInfo> findUserByName(String userAccount) {
		return mapper.findUsersByName(userAccount);
	}
	


}
