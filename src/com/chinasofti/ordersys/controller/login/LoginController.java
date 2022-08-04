package com.chinasofti.ordersys.controller.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.chinasofti.ordersys.mapper.OrderMapper;
import com.chinasofti.ordersys.vo.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.ordersys.service.DomainProtectedService;
import com.chinasofti.ordersys.service.login.LoginService;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.sec.Passport;
import com.chinasofti.util.web.upload.MultipartRequestParser;

import java.util.List;

@Controller
public class LoginController {

	@Autowired
	LoginService loginService;


	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {

		// 获取用户会话信息
		HttpSession session = request.getSession();
		// 如果已经登录
		if (session.getAttribute("USER_INFO") != null) {
			// 删除登录信息
			session.removeAttribute("USER_INFO");
		}
		return "redirect:/";

	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
		// 创建表单请求自动解析器对象
		MultipartRequestParser parser = new MultipartRequestParser();
		// 根据请求解析获取UserInfo用户信息对象
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// 获取数据加密工具对象
		Passport passport = new Passport();
		// 将用户输入的密码用md5码方式加密
		info.setUserPass(passport.md5(info.getUserPass()));
		// 创建外站非法请求判定服务对象
		DomainProtectedService domainService = new DomainProtectedService();
		// 如果是本站合法请求
		if (domainService.isFromSameDomain()) {
			// 创建用户登录服务对象
			//LoginService loginService = new LoginService();
			// 执行登录判定
			switch (loginService.login(info)) {
			// 如果用户名错误
			case LoginService.WRONG_USERNAME:
				// 在作用域中保存用户名不存在的错误提示
				request.setAttribute("ERROR_MSG", "用户名不存在！");
				// 在作用域中保存用户填写的信息
				request.setAttribute("USER_INFO", info);
				// 跳转回登录界面
				return "/pages/login.jsp";

				// 如果密码错误
			case LoginService.WRONG_PASSWORD:
				// 在作用域中保存密码错误的错误提示
				request.setAttribute("ERROR_MSG", "用户密码不匹配！");
				// 在作用域中保存用户填写的信息
				request.setAttribute("USER_INFO", info);
				// 跳转回登录界面
				return "/pages/login.jsp";

				// 如果登陆成功
			case LoginService.LOGIN_OK:
				// 在会话信息中保存用户的详细信息
				request.getSession().setAttribute("USER_INFO",
						loginService.getLoginUser());
				// 判定用户身份
				switch (loginService.getLoginUser().getRoleId()) {
				// 如果是餐厅管理员
				case 1:
					// 跳转带管理员主界面
					return "redirect:toadminmain.order";

					// 如果是后厨人员
				case 2:
					// 跳转到后厨人员主界面
					List<OrderInfo> orderByCompletedState = loginService.orderInfoList();
					for (OrderInfo orderInfo : orderByCompletedState) {
						System.out.println(orderInfo.toString());
					}
//					model.addAttribute("order",orderByCompletedState);
					session.setAttribute("orderInfoKitchen",orderByCompletedState);
					int count = 0;
					session.setAttribute("count",count);
					return "redirect:tokitchenmain.order";
					//return "/pages/kitchen/kitchenmain.jsp";

					// 如果是餐厅服务员
				case 3:
					// 跳转到服务员主界面
					return "redirect:towaitermain.order";

				}

				break;
			// 如果用户已经被锁定
			case LoginService.WRONG_LOCKED:
				// 在作用域中保存用户被锁定的错误提示
				request.setAttribute("ERROR_MSG", "该用户已经被锁定！");
				// 在作用域中保存用户填写的信息
				request.setAttribute("USER_INFO", loginService.getLoginUser());
				// 跳转回登录界面
				return "/pages/login.jsp";

				// 如果用户已经在线
			case LoginService.USER_ALREADY_ONLINE:
				// 在作用域中保存用户已经在线的错误提示
				request.setAttribute("ERROR_MSG", "该用户已经在线，不能重复登录！");
				// 在作用域中保存用户填写的信息
				request.setAttribute("USER_INFO", info);
				// 跳转回登录界面
				return "/pages/login.jsp";

			}
			// 如果是外站非法请求
		} else {
			// 在会话中保存用户填写的信息
			request.getSession().setAttribute("USER_INFO", info);
			// 跳转到非法请求提示界面
			return "redirect:todomainerror.order";

		}
		return "";

	}
}
