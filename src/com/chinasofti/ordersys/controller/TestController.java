package com.chinasofti.ordersys.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinasofti.ordersys.service.admin.UserService;
import com.chinasofti.ordersys.vo.UserInfo;

@Controller
public class TestController {
	@Autowired
	UserService service;

	public UserService getService() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}

	@RequestMapping("/test")
	public String test(HttpServletResponse response,
			HttpServletRequest request, @RequestParam("arg1") int i, int j)
			throws IOException {
		System.out.println("TestAction");
		System.out.println(i + "        " + j);
		// response.getWriter().println(
		// "HelloSpringMVC" + request.getSession().getServletContext());

		return "pages/test.jsp";
	}

	@RequestMapping("/test1")
	public String test1() {
		List<UserInfo> list = service.getByPage(1, 5);
		for (UserInfo info : list) {
			System.out.println(info.getUserAccount() + "\t"
					+ info.getUserPass() + "\t" + info.getFaceimg());
		}

		UserInfo info = service.getUserById(new Integer(1));
		System.out.println("---" + info.getUserAccount());

		return "redirect:test.order?arg1=100&j=20";
	}

}
