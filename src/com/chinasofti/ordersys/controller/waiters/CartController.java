package com.chinasofti.ordersys.controller.waiters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.controller.kitchen.RTDishesController;
import com.chinasofti.ordersys.service.admin.DishesService;
import com.chinasofti.ordersys.service.login.waiters.OrderService;
import com.chinasofti.ordersys.vo.Cart;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.web.serverpush.MessageProducer;

@Controller
public class CartController {

	@Autowired
	OrderService oservice;
	@Autowired
	DishesService service;

	public OrderService getOservice() {
		return oservice;
	}

	public void setOservice(OrderService oservice) {
		this.oservice = oservice;
	}

	public DishesService getService() {
		return service;
	}

	public void setService(DishesService service) {
		this.service = service;
	}

	@RequestMapping("/addcart")
	public void addCart(HttpServletRequest request,
			HttpServletResponse response, int num, int dishes) {
		// 获取会话对象
		HttpSession session = request.getSession();
		// 创建购物车对象
		Cart cart = new Cart();
		// 如果会话中的存在购物车
		if (session.getAttribute("CART") != null) {
			// 直接获取会话中的购物车对象
			cart = (Cart) session.getAttribute("CART");
		}
		// 定义桌号变量
		Integer tableId = 1;
		// 如果会话中存在桌号信息
		if (session.getAttribute("TABLE_ID") != null) {
			// 直接获取桌号信息
			tableId = (Integer) session.getAttribute("TABLE_ID");
		}
		// 设置购物车的桌号信息
		cart.setTableId(tableId.intValue());
		// 获取本次加入购物车的菜品数量

		cart.getUnits().add(cart.createUnit(dishes, num));
		// 将购物车对象设置到会话中
		session.setAttribute("CART", cart);

	}

	@RequestMapping("/commitcart")
	public void commitCart(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置响应编码
		response.setCharacterEncoding("utf-8");
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");
		// 创建菜品管理服务对象

		// 获取会话对象
		HttpSession session = request.getSession();
		// 创建购物车对象
		Cart cart = new Cart();
		// 定义桌号变量
		Integer tableId = new Integer(1);
		// 如果Session中保存了桌号信息
		if (session.getAttribute("TABLE_ID") != null) {
			// 直接获取桌号信息
			tableId = (Integer) session.getAttribute("TABLE_ID");

		}
		// 如果会话中存在购物车信息
		if (session.getAttribute("CART") != null) {
			// 直接获取会话中的购物车对象
			cart = (Cart) session.getAttribute("CART");
		}
		// 定义点餐服务员ID变量
		int waiterId = 1;
		// 如果Session中存在登录信息
		if (session.getAttribute("USER_INFO") != null) {
			// 获取本用户的用户ID
			waiterId = ((UserInfo) session.getAttribute("USER_INFO"))
					.getUserId();
		}
		// 创建订单管理服务对象

		// 将本订单存入数据库并获取本次添加订单的主键
		Object key = oservice.addOrder(waiterId, tableId);
		// System.out.println(key);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("disheses");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历结果集合中的订单菜品详情
			for (Cart.CartUnit unit : cart.getUnits()) {
				// 将订单菜品详情映射信息存入数据库
				oservice.addOrderDishesMap(unit, ((Long) key).intValue());
				// 每一个菜品构建一个dishes标签节点
				Element dishes = doc.createElement("dishes");
				// 创建桌号标签
				Element tid = doc.createElement("tableId");
				// 设置桌号标签文本内容
				tid.setTextContent(tableId.intValue() + "");
				// 将桌号标签设置为菜品标签子标签
				dishes.appendChild(tid);
				// 创建菜品名标签
				Element dishesName = doc.createElement("dishesName");
				// 获取菜品名称
				String dname = service.getDishesById(
						new Integer(unit.getDishesId())).getDishesName();
				// 设置菜品名标签文本内容
				dishesName.setTextContent(dname);
				// 将菜品名称标签设置为菜品标签子标签
				dishes.appendChild(dishesName);
				// 创建菜品数量标签
				Element num = doc.createElement("num");
				// 设置数量标签文本内容
				num.setTextContent(unit.getNum() + "");
				// 将数量标签设置为菜品标签子标签
				dishes.appendChild(num);
				// 将菜品标签设置为根标签子标签
				root.appendChild(dishes);

			}
			// 创建字符输出流
			StringWriter writer = new StringWriter();
			// 创建格式化输出流
			PrintWriter pwriter = new PrintWriter(writer);
			// 将完整的DOM树转换为XML文档结构字符串输出到字符输出流中
			TransformerFactory.newInstance().newTransformer()
					.transform(new DOMSource(doc), new StreamResult(pwriter));
			// 获取XML字符串
			String msg = writer.toString();
			// 关闭格式话输出流
			pwriter.close();
			// 关闭字符串输出流
			writer.close();
			// 获取后厨等待列表
			ArrayList<String> list = RTDishesController.kitchens;
			// 创建消息生产者
			MessageProducer producer = new MessageProducer();
			// 遍历每一个后厨等待用户
			for (int i = list.size() - 1; i >= 0; i--) {
				// 获取单个等待用户的sessionID
				String id = list.get(i);
				// 为该用户生成点菜订单消息
				producer.sendMessage(id, "rtorder", msg);
				// 在等待列表中删除该用户
				list.remove(id);
			}
			// 创建空的购物车对象
			cart = new Cart();
			// 清空会话购物车
			session.setAttribute("CART", cart);

			// response.getWriter().write(writer.toString());
			// 捕获异常
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}
	}

}
