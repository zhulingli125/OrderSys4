package com.chinasofti.ordersys.controller.kitchen;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.chinasofti.ordersys.controller.login.LoginController;
import com.chinasofti.ordersys.service.login.LoginService;
import com.chinasofti.ordersys.service.login.waiters.OrderService;
import com.chinasofti.ordersys.vo.OrderInfo;
import com.chinasofti.ordersys.vo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.util.web.serverpush.Message;
import com.chinasofti.util.web.serverpush.MessageConsumer;
import com.chinasofti.util.web.serverpush.MessageHandler;
import com.chinasofti.util.web.serverpush.MessageProducer;
import com.chinasofti.util.web.serverpush.ServerPushKey;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Controller
public class RTDishesController {

	@Autowired
	GetPushMsgTemplate template;
	@Autowired
	LoginService loginService;
	@Autowired
	OrderService orderService;

	public GetPushMsgTemplate getTemplate() {
		return template;
	}

	public void setTemplate(GetPushMsgTemplate template) {
		this.template = template;
	}

	public static ArrayList<String> disheses = new ArrayList<String>();
	public static ArrayList<String> kitchens = new ArrayList<String>();

	@RequestMapping("/outorder")
	public String outorder(HttpSession session, int orderId, String dishesName){
		System.out.println(dishesName);
		orderService.updateOrder(orderId);
		List<OrderInfo> orderByCompletedState = loginService.orderInfoList();
		System.out.println(orderByCompletedState);

		session.setAttribute("orderInfoKitchen",orderByCompletedState);
		dishesName = dishesName+"已出餐！";
		session.setAttribute("outInfo",dishesName);
		int count = 0;
		session.setAttribute("count",count);
		return "/pages/kitchen/kitchenmain.jsp";
	}
	@RequestMapping("/dishesdone")
	public void dishesDone(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置响应结果集
		response.setCharacterEncoding("utf-8");
		// 获取菜品对应的桌号
		String tableId = request.getParameter("tableId");
		// 获取菜品名
		String dishesName = request.getParameter("dishesName");
		// 由于使用ajax提交，因此需要转码
		try {
			dishesName = new String(dishesName.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 创建消息生产者
		MessageProducer producer = new MessageProducer();
		// 获取服务员等待列表
		ArrayList<String> list = disheses;
		// 遍历服务员等待列表
		for (int i = list.size() - 1; i >= 0; i--) {
			// 获取特定的服务员SessionID
			String id = list.get(i);
			// 对该服务员生产菜品完成等待传菜的消息
			producer.sendMessage(id, "rtdishes", "桌号[" + tableId + "]的菜品["
					+ dishesName + "]已经烹制完成，请传菜！");
			// 从等待列表中删除该服务员
			list.remove(id);
		}

	}

	@RequestMapping("/getrtdishes")
	public void getRTDishes(HttpServletRequest request,
			HttpServletResponse response) {

		GetPushMsgHandler handler = new GetPushMsgHandler() {

			@Override
			public MessageHandler getHandler(HttpServletRequest request,
					HttpServletResponse response) {
				// TODO Auto-generated method stub
				// 设置请求字符集
				response.setCharacterEncoding("utf-8");
				// TODO Auto-generated method stub
				// 尝试处理实时消息
				try {
					// 获取针对客户端的文本输出流
					final PrintWriter out = response.getWriter();
					// 创建消息处理器
					MessageHandler handler = new MessageHandler() {
						// 实时消息处理回调方法
						@Override
						public void handle(
								Hashtable<ServerPushKey, Message> messageQueue,
								ServerPushKey key, Message msg) {
							// 将消息的文本内容直接发送给客户端
							out.print(msg.getMsg());
							// TODO Auto-generated method stub

						}
					};
					// 返回创建好的消息处理器
					return handler;
					// 捕获创建消息处理器时产生的异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 输出异常信息
					return null;
				}
			}

			@Override
			public void initService(HttpServletRequest request,
					HttpServletResponse response, HttpSession session) {
				// TODO Auto-generated method stub
				// 将当前会话加入到实时消息等待列表
				disheses.add(session.getId());
			}

		};

		template.getMsg(request, response, handler);
	}

	@RequestMapping("/getrtorder")
	public void getRTOrder(HttpServletRequest request,
			HttpServletResponse response) {

		GetPushMsgHandler handler = new GetPushMsgHandler() {

			/**
			 * 获取实时消息处理器的回调
			 * 
			 * @param request
			 *            请求对象
			 * @param response
			 *            响应对象
			 * @return 本Servlet使用的实时消息处理器
			 * */
			@Override
			public MessageHandler getHandler(HttpServletRequest request,
					HttpServletResponse response) {
				// TODO Auto-generated method stub
				// 设置请求字符集
				response.setCharacterEncoding("utf-8");
				// 尝试处理实时消息
				try {
					// 获取针对客户端的文本输出流
					final PrintWriter out = response.getWriter();
					// 创建消息处理器
					MessageHandler handler = new MessageHandler() {

						@Override
						public void handle(
								Hashtable<ServerPushKey, Message> messageQueue,
								ServerPushKey key, Message msg) {
							// 将消息的文本内容直接发送给客户端
							out.print(msg.getMsg());
							// TODO Auto-generated method stub

						}
					};
					// 返回创建好的消息处理器
					return handler;
					// 捕获创建消息处理器时产生的异常
				} catch (Exception ex) {
					// 输出异常信息
					ex.printStackTrace();
					// 返回空的消息处理器
					return null;
				}
			}

			/**
			 * 初始化实时消息获取服务的方法
			 * 
			 * @param request
			 *            请求信息
			 * @param response
			 *            响应对象
			 * @param session
			 *            会话跟踪对象
			 * */
			@Override
			public void initService(HttpServletRequest request,
					HttpServletResponse response, HttpSession session) {
				// TODO Auto-generated method stub
				// 将当前会话加入到实时消息等待列表
				kitchens.add(session.getId());
			}
		};

		template.getMsg(request, response, handler);

	}

	@RequestMapping("/getorderbypage")
	public void getUserByPage(HttpServletRequest request, HttpServletResponse response) {
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 获取希望显示的页码数
		int page = Integer.parseInt(request.getParameter("page"));
		// 获取最大页码数
		int maxPage = orderService.getMaxPageByState(8,0);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 进行分页数据查询
		List<OrderInfo> list = orderService.getByPage(page, 8);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("orders");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的用户信息
			for (OrderInfo info : list) {
				// 每一个用户创建一个用户标签
				Element order = doc.createElement("order");
				// 创建用户ID标签
				Element orderId = doc.createElement("orderId");
				// 设置用户ID标签文本内容
				orderId.setTextContent(info.getOrderId() + "");
				// 将用户ID标签设置为用户标签子标签
				order.appendChild(orderId);
				// 创建用户名标签
				Element tableId = doc.createElement("tableId");
				// 设置用户名标签文本内容
				tableId.setTextContent(info.getTableId() + "");
				// 将用户名标签设置为用户标签子标签
				order.appendChild(tableId);
				// 创建角色名标签
				Element dishesName = doc.createElement("dishesName");
				// 设置角色名标签文本内容
				dishesName.setTextContent(info.getDishesName());
				// 将角色名标签设置为用户标签子标签
				order.appendChild(dishesName);
				// 创建角色锁定信息标签
				Element num = doc.createElement("num");
				// 设置角色锁定信息标签文本内容
				num.setTextContent(info.getNum() + "");
				// 将角色锁定信息标签设置为用户标签子标签
				order.appendChild(num);
				// 设置用户标签为根标签子标签
				root.appendChild(order);
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
}
