package com.chinasofti.ordersys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DomainController {

	@RequestMapping("/todomainerror")
	public String toDomainError() {
		return "/pages/domainerror.jsp";
	}

	@RequestMapping("/toknowledge")
	public String toKnowlege() {
		return "/pages/knowledge.jsp";
	}

}
