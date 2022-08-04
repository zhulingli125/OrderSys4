package com.chinasofti.ordersys.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chinasofti.ordersys.service.login.waiters.OrderService;

@Controller
public class ChangeOrderStateController {
	@Autowired
	OrderService service;

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}

	@RequestMapping("/changeorder")
	public void changeOrderState(@RequestParam("orderId") Integer orderId,
			@RequestParam("state") int state) {

		
		// 修改订单的状态值
		service.changeState(orderId, state);

	}

}
