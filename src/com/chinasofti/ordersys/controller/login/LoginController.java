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

		// ��ȡ�û��Ự��Ϣ
		HttpSession session = request.getSession();
		// ����Ѿ���¼
		if (session.getAttribute("USER_INFO") != null) {
			// ɾ����¼��Ϣ
			session.removeAttribute("USER_INFO");
		}
		return "redirect:/";

	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
		// �����������Զ�����������
		MultipartRequestParser parser = new MultipartRequestParser();
		// �������������ȡUserInfo�û���Ϣ����
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// ��ȡ���ݼ��ܹ��߶���
		Passport passport = new Passport();
		// ���û������������md5�뷽ʽ����
		info.setUserPass(passport.md5(info.getUserPass()));
		// ������վ�Ƿ������ж��������
		DomainProtectedService domainService = new DomainProtectedService();
		// ����Ǳ�վ�Ϸ�����
		if (domainService.isFromSameDomain()) {
			// �����û���¼�������
			//LoginService loginService = new LoginService();
			// ִ�е�¼�ж�
			switch (loginService.login(info)) {
			// ����û�������
			case LoginService.WRONG_USERNAME:
				// ���������б����û��������ڵĴ�����ʾ
				request.setAttribute("ERROR_MSG", "�û��������ڣ�");
				// ���������б����û���д����Ϣ
				request.setAttribute("USER_INFO", info);
				// ��ת�ص�¼����
				return "/pages/login.jsp";

				// ����������
			case LoginService.WRONG_PASSWORD:
				// ���������б����������Ĵ�����ʾ
				request.setAttribute("ERROR_MSG", "�û����벻ƥ�䣡");
				// ���������б����û���д����Ϣ
				request.setAttribute("USER_INFO", info);
				// ��ת�ص�¼����
				return "/pages/login.jsp";

				// �����½�ɹ�
			case LoginService.LOGIN_OK:
				// �ڻỰ��Ϣ�б����û�����ϸ��Ϣ
				request.getSession().setAttribute("USER_INFO",
						loginService.getLoginUser());
				// �ж��û����
				switch (loginService.getLoginUser().getRoleId()) {
				// ����ǲ�������Ա
				case 1:
					// ��ת������Ա������
					return "redirect:toadminmain.order";

					// ����Ǻ����Ա
				case 2:
					// ��ת�������Ա������
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

					// ����ǲ�������Ա
				case 3:
					// ��ת������Ա������
					return "redirect:towaitermain.order";

				}

				break;
			// ����û��Ѿ�������
			case LoginService.WRONG_LOCKED:
				// ���������б����û��������Ĵ�����ʾ
				request.setAttribute("ERROR_MSG", "���û��Ѿ���������");
				// ���������б����û���д����Ϣ
				request.setAttribute("USER_INFO", loginService.getLoginUser());
				// ��ת�ص�¼����
				return "/pages/login.jsp";

				// ����û��Ѿ�����
			case LoginService.USER_ALREADY_ONLINE:
				// ���������б����û��Ѿ����ߵĴ�����ʾ
				request.setAttribute("ERROR_MSG", "���û��Ѿ����ߣ������ظ���¼��");
				// ���������б����û���д����Ϣ
				request.setAttribute("USER_INFO", info);
				// ��ת�ص�¼����
				return "/pages/login.jsp";

			}
			// �������վ�Ƿ�����
		} else {
			// �ڻỰ�б����û���д����Ϣ
			request.getSession().setAttribute("USER_INFO", info);
			// ��ת���Ƿ�������ʾ����
			return "redirect:todomainerror.order";

		}
		return "";

	}
}
