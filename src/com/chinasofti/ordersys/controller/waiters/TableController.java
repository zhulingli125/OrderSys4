package com.chinasofti.ordersys.controller.waiters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TableController {

	@RequestMapping("/settableid")
	public void setTableId(HttpServletRequest request,Integer tableId) {

		// 获取会话对象
		HttpSession session = request.getSession();
		// 将桌号存放到会话中
		session.setAttribute("TABLE_ID", tableId);
	}
}
