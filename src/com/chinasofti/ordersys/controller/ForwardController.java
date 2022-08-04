package com.chinasofti.ordersys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

	@RequestMapping("/toadddishes")
	public String toAddDishes() {
		return "/pages/admin/adddishes.jsp";
	}

	@RequestMapping("/toadd")
	public String toAddUserServlet() {
		return "/pages/admin/adduser.jsp";
	}

	@RequestMapping("/toadminmain")
	public String toAdminMain() {
		return "/pages/admin/main.jsp";
	}

	@RequestMapping("/todishesadmin")
	public String toDishesAdmin() {
		return "/pages/admin/dishesadmin.jsp";
	}

	@RequestMapping("/tokitchenmain")
	public String toKitchenMain() {
		return "/pages/kitchen/kitchenmain.jsp";
	}

	@RequestMapping("/toonlinekitchen")
	public String toOnlineKitchen() {
		return "/pages/admin/onlinekitchens.jsp";
	}

	@RequestMapping("/toonlinewaiters")
	public String toOnlineWaiters() {
		return "/pages/admin/onlinewaiters.jsp";
	}

	@RequestMapping("/tooperatedata")
	public String toOperateData() {
		return "/pages/admin/operatedata.jsp";

	}

	@RequestMapping("/topaylist")
	public String toPayList() {
		return "/pages/waiters/paylist.jsp";
	}

	@RequestMapping("/towaitermain")
	public String toWaiterMain() {
		return "/pages/waiters/takeorder.jsp";
	}

	@RequestMapping("/modifymyinfo")
	public String toModifyMyInfo() {
		return "/pages/users/modifyuser.jsp";
	}

	@RequestMapping("/touseradmin")
	public String toUserAdmin() {
		return "/pages/admin/useradmin.jsp";
	}

	@RequestMapping("/")
	public String toRoot() {
		return "/pages/login.jsp";
	}
}
