package com.chinasofti.ordersys.mapper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.chinasofti.ordersys.vo.UserInfo;

public class Test {
	public static void main(String[] args) {

		ApplicationContext context = new ClassPathXmlApplicationContext(
				"applicationContext-common.xml");
		UserInfoMapper mapper = (UserInfoMapper) context.getBean(UserInfoMapper.class);
		for(UserInfo info:mapper.getAllUser()){
		 System.out.println(info.getUserAccount()+"\t"+info.getRoleName());
		 }
		
		
//		UserInfo info=new UserInfo();
//		info.setUserAccount("TestMBObject");
//		info.setUserPass("1234");
//		info.setRoleId(1);
//		info.setFaceimg("1.png");
//
//		System.out.println(mapper.addUser(info));
//		System.out.println(info.getUserId());

	}

}
