package com.chinasofti.ordersys.controller.admin;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.listeners.OrderSysListener;
import com.chinasofti.ordersys.service.admin.UserService;
import com.chinasofti.ordersys.service.login.CheckUserPassService;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.web.upload.MultipartRequestParser;
import com.chinasofti.web.common.taglib.TokenTag;

@Controller
public class UserAdminController {
	@Autowired
	UserService service;
	@Autowired
	CheckUserPassService cservice;

	public CheckUserPassService getCservice() {
		return cservice;
	}

	public void setCservice(CheckUserPassService cservice) {
		this.cservice = cservice;
	}

	public UserService getService() {
		return service;
	}

	public void setService(UserService service) {
		this.service = service;
	}

	@RequestMapping("/adminmodifyuser")
	public String adminModify(HttpServletRequest request,
			HttpServletResponse response) {

		// 创建表单请求解析器工具
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析获取UserInfo角色信息对象
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// 执行修改操作
		service.adminModify(info);
		return "redirect:touseradmin.order";
	}

	@RequestMapping("/deleteuser")
	public void deleteUser(Integer userId) {
		service.deleteUser(userId);
	}

	@RequestMapping("/getonlinekitchen")
	public void getOnlineKitchen(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 从监听器中获取在线的后厨人员列表
		ArrayList<UserInfo> kitchen = OrderSysListener.getOnlineKitchens();
		// 获取所有的会话数目
		int sessions = OrderSysListener.onlineSessions;
		// 尝试将在线后厨人员列表结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("users");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的后厨人员信息
			for (UserInfo info : kitchen) {
				// 每一个员工构建一个用户标签节点
				Element user = doc.createElement("user");
				// 创建用户id节点标签
				Element userId = doc.createElement("userId");
				// 设置用户id标签文本内容
				userId.setTextContent(info.getUserId() + "");
				// 将用户id标签设置为用户标签子标签
				user.appendChild(userId);
				// 创建用户名标签
				Element userAccount = doc.createElement("userAccount");
				// 设置用户名标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将用户名标签设置为用户标签子标签
				user.appendChild(userAccount);
				// 创建角色id标签
				Element roleId = doc.createElement("roleId");
				// 设置角色id标签文本内容
				roleId.setTextContent(info.getRoleId() + "");
				// 将角色id标签设置为用户标签的子标签
				user.appendChild(roleId);
				// 创建角色名标签
				Element roleName = doc.createElement("roleName");
				// 设置角色名标签文本内容
				roleName.setTextContent(info.getRoleName());
				// 将角色名标签设置为用户标签的子标签
				user.appendChild(roleName);
				// 创建用户锁定状态标签
				Element locked = doc.createElement("locked");
				// 设置用户锁定状态标签文本内容
				locked.setTextContent(info.getLocked() + "");
				// 将用户锁定状态标签设置为用户标签子标签
				user.appendChild(locked);
				// 创建角色头像标签
				Element faceimg = doc.createElement("faceimg");
				// 设置角色头像标签文本内容
				faceimg.setTextContent(info.getFaceimg() + "");
				// 将角色头像标签设置为用户标签子标签
				user.appendChild(faceimg);
				// 将角色标签设置为根标签子节点
				root.appendChild(user);

			}
			// 创建会话数标签
			Element sessionNum = doc.createElement("sessionNum");
			// 设置会话数标签文本内容
			sessionNum.setTextContent(sessions + "");
			// 将会话数标签设置为根标签的子标签
			root.appendChild(sessionNum);
			// 创建后厨人员数标签
			Element waitersNum = doc.createElement("kitchenNum");
			// 设置后厨人员数标签文本内容
			waitersNum.setTextContent(kitchen.size() + "");
			// 将后厨人员数标签设置为根标签的子标签
			root.appendChild(waitersNum);
			// 将完整的DOM树转换为XML文档结构字符串输出到客户端
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));
			// 捕获查询、转换过程中的异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}
	}

	@RequestMapping("/getonlinewaiters")
	public void getOnlineWaiters(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 从监听器中获取在线的服务员列表
		ArrayList<UserInfo> waiters = OrderSysListener.getOnlineWaiters();
		// 获取所有的会话数目
		int sessions = OrderSysListener.onlineSessions;
		// 尝试将在线服务员列表结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("users");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的管理员信息
			for (UserInfo info : waiters) {
				// 每一个员工构建一个用户标签节点
				Element user = doc.createElement("user");
				// 创建用户id节点标签
				Element userId = doc.createElement("userId");
				// 设置用户id标签文本内容
				userId.setTextContent(info.getUserId() + "");
				// 将用户id标签设置为用户标签子标签
				user.appendChild(userId);
				// 创建用户名标签
				Element userAccount = doc.createElement("userAccount");
				// 设置用户名标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将用户名标签设置为用户标签子标签
				user.appendChild(userAccount);
				// 创建角色id标签
				Element roleId = doc.createElement("roleId");
				// 设置角色id标签文本内容
				roleId.setTextContent(info.getRoleId() + "");
				// 将角色id标签设置为用户标签的子标签
				user.appendChild(roleId);
				// 创建角色名标签
				Element roleName = doc.createElement("roleName");
				// 设置角色名标签文本内容
				roleName.setTextContent(info.getRoleName());
				// 将角色名标签设置为用户标签的子标签
				user.appendChild(roleName);
				// 创建用户锁定状态标签
				Element locked = doc.createElement("locked");
				// 设置用户锁定状态标签文本内容
				locked.setTextContent(info.getLocked() + "");
				// 将用户锁定状态标签设置为用户标签子标签
				user.appendChild(locked);
				// 创建角色头像标签
				Element faceimg = doc.createElement("faceimg");
				// 设置角色头像标签文本内容
				faceimg.setTextContent(info.getFaceimg() + "");
				// 将角色头像标签设置为用户标签子标签
				user.appendChild(faceimg);
				// 将角色标签设置为根标签子节点
				root.appendChild(user);

			}
			// 创建会话数标签
			Element sessionNum = doc.createElement("sessionNum");
			// 设置会话数标签文本内容
			sessionNum.setTextContent(sessions + "");
			// 将会话数标签设置为根标签的子标签
			root.appendChild(sessionNum);
			// 创建服务员数标签
			Element waitersNum = doc.createElement("waitersNum");
			// 设置服务员数标签文本内容
			waitersNum.setTextContent(waiters.size() + "");
			// 将服务员数标签设置为根标签的子标签
			root.appendChild(waitersNum);
			// 将完整的DOM树转换为XML文档结构字符串输出到客户端
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));
			// 捕获查询、转换过程中的异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}

	}

	@RequestMapping("/getuserbypage")
	public void getUserByPage(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 获取希望显示的页码数
		int page = Integer.parseInt(request.getParameter("page"));

		// 获取最大页码数
		int maxPage = service.getMaxPage(10);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 进行分页数据查询
		List<UserInfo> list = service.getByPage(page, 10);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("users");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的用户信息
			for (UserInfo info : list) {
				// 每一个用户创建一个用户标签
				Element user = doc.createElement("user");
				// 创建用户ID标签
				Element userId = doc.createElement("userId");
				// 设置用户ID标签文本内容
				userId.setTextContent(info.getUserId() + "");
				// 将用户ID标签设置为用户标签子标签
				user.appendChild(userId);
				// 创建用户名标签
				Element userAccount = doc.createElement("userAccount");
				// 设置用户名标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将用户名标签设置为用户标签子标签
				user.appendChild(userAccount);
				// 创建角色id标签
				Element roleId = doc.createElement("roleId");
				// 设置角色id标签文本内容
				roleId.setTextContent(info.getRoleId() + "");
				// 将角色id标签设置为用户标签子标签
				user.appendChild(roleId);
				// 创建角色名标签
				Element roleName = doc.createElement("roleName");
				// 设置角色名标签文本内容
				roleName.setTextContent(info.getRoleName());
				// 将角色名标签设置为用户标签子标签
				user.appendChild(roleName);
				// 创建角色锁定信息标签
				Element locked = doc.createElement("locked");
				// 设置角色锁定信息标签文本内容
				locked.setTextContent(info.getLocked() + "");
				// 将角色锁定信息标签设置为用户标签子标签
				user.appendChild(locked);
				// 创建角色头像标签
				Element faceimg = doc.createElement("faceimg");
				// 设置角色头像标签文本内容
				faceimg.setTextContent(info.getFaceimg() + "");
				// 设置头像标签为用户标签子标签
				user.appendChild(faceimg);
				// 设置用户标签为根标签子标签
				root.appendChild(user);
			}
			// 创建当前页码数的标签
			Element pageNow = doc.createElement("page");
			// 设置当前页码数标签的文本内容
			pageNow.setTextContent(page + "");
			// 将当前页码数标签设置为根标签的子标签
			root.appendChild(pageNow);
			// 创建最大页码数的标签
			Element maxPageElement = doc.createElement("maxPage");
			// 设置最大页码数标签的文本内容
			maxPageElement.setTextContent(maxPage + "");
			// 将最大页码数标签设置为根标签的子标签
			root.appendChild(maxPageElement);
			// 将完整的DOM树转换为XML文档结构字符串输出到客户端

			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer t = tf.newTransformer();

			t.setOutputProperty("encoding", "UTF-8");// 解决中文问题，试过用GBK不行

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			t.transform(new DOMSource(doc), new StreamResult(bos));

			String xmlStr = bos.toString();
			System.out.println(xmlStr);


			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));
			// 捕获查询、转换过程中的异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}

	}

	@RequestMapping("/modifyuser")
	public String modifyMyInfo(HttpServletRequest request,
			HttpServletResponse response) {

		// 创建表单请求解析器对象
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析请求获取UserInfo用户信息对象
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// 执行用户信息修改操作
		service.modify(info);
		// 修改信息后自动注销
		return "redirect:logut.order";

	}

	@RequestMapping("/adduser")
	public String addUser(HttpServletRequest request,
			HttpServletResponse response) {

		// 创建表单请求解析器工具
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析获取UserInfo用户信息对象
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);

		// 添加用户
		service.addUser(info);
		
		

		// 跳转到用户管理界面
		return "redirect:touseradmin.order";

	}

	@RequestMapping("/checkuser")
	public void checkAddUser(HttpServletRequest request,
			HttpServletResponse response) {

		// 获取请求参数中的用户名信息
		String userAccount = request.getParameter("name");
		try {
			// 由于是ajax请求，因此需要转码
			userAccount = new String(userAccount.getBytes("iso8859-1"), "utf-8");
			// 获取带有连接池的数据库模版操作工具对象

			// 查询对应用户名的用户信息
			List<UserInfo> list = service.findUserByName(userAccount);

			PrintWriter pw = response.getWriter();
			// 如果数据库中无数据
			if (list.size() == 0) {
				// 输出可以添加标识
				pw.print("OK");
				// 如果数据库中有数据
			} else {
				// 输出不能添加标识
				pw.print("FAIL");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping("/checkpass")
	public void checkUserPass(HttpServletRequest request,
			HttpServletResponse response) {

		try {
			// 创建表单请求数据解析工具对象
			MultipartRequestParser parser = new MultipartRequestParser();
			// 解析并获取UserInfo对象
			UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);

			// 获取针对客户端的文本输出流
			PrintWriter pw = response.getWriter();
			// 如果密码正确
			if (cservice.checkPass(info)) {
				// 输出密码正确的标识
				pw.print("OK");
				// 密码错误
			} else {
				// 输出密码错误的标识
				pw.print("FAIL");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@RequestMapping("/tomodifyuser")
	public String toModifyUser(HttpServletRequest request,
			HttpServletResponse response, Integer userId) {
		UserInfo info = service.getUserById(userId);
		// 将用户信息加入request作用域
		request.setAttribute("MODIFY_INFO", info);
		// 跳转到用户信息修改界面
		return "/pages/admin/modifyuser.jsp";
	}

}
