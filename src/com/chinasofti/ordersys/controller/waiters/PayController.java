package com.chinasofti.ordersys.controller.waiters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.controller.admin.RTPayController;
import com.chinasofti.ordersys.service.login.waiters.OrderService;
import com.chinasofti.ordersys.vo.OrderInfo;
import com.chinasofti.util.web.serverpush.MessageProducer;

@Controller
public class PayController {

	@Autowired
	OrderService service;

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}

	@RequestMapping("/getpaylist")
	public void getPayList(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置返回的MIME类型为xml
		response.setContentType("text/xml");

		// 定义查询数据页码变量
		int page = 1;
		// 如果请求中包含页码信息
		if (request.getParameter("page") != null) {
			// 获取请求中的页面信息
			page = Integer.parseInt(request.getParameter("page"));
		}
		// 获取最大页码数
		int maxPage = service.getMaxPage(5, 0);
		// 对当前的页码数进行纠错，如果小于1，则直接显示第一页的内容
		page = page < 1 ? 1 : page;
		// 对当前的页码数进行纠错，如果大于最大页码，则直接显示最后一页的内容
		page = page > maxPage ? maxPage : page;
		// 根据页码信息查询需要买单的订单信息
		List<OrderInfo> list = service.getNeedPayOrdersByPage(page, 5, 0);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("orderes");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 循环遍历每一个订单信息
			for (OrderInfo info : list) {
				// 每一个订单创建一个订单标签
				Element order = doc.createElement("order");
				// 创建订单Id标签
				Element orderId = doc.createElement("orderId");
				// 设置订单ID标签的文本内容
				orderId.setTextContent(info.getOrderId() + "");
				// 将订单ID标签设置为订单标签子标签
				order.appendChild(orderId);
				// 创建桌号标签
				Element tableId = doc.createElement("tableId");
				// 设置桌号标签文本内容
				tableId.setTextContent(info.getTableId() + "");
				// 将桌号标签设置为订单标签子标签
				order.appendChild(tableId);
				// 创建点餐员帐号标签
				Element userAccount = doc.createElement("userAccount");
				// 设置点餐员帐号标签文本内容
				userAccount.setTextContent(info.getUserAccount());
				// 将点餐员帐号标签设置为订单标签子标签
				order.appendChild(userAccount);
				// 创建订单开始时间标签
				Element orderBeginDate = doc.createElement("orderBeginDate");
				// 创建日期时间格式化工具
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				// 设置订单开始时间标签文本内容
				orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
				// 将订单开始时间标签设置为订单标签子标签
				order.appendChild(orderBeginDate);
				// 将订单标签设置为根标签子标签
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

	@RequestMapping("/requestpay")
	public void requestToPay(HttpServletRequest request,
			HttpServletResponse response, Integer orderId) {
		// 获取需要买单的订单ID

		// 创建订单管理服务对象

		// 修改数据库中的订单状态信息
		service.requestPay(orderId);
		// 获取订单详情
		OrderInfo info = service.getOrderById(orderId);
		// 尝试将结果结构化为xml文档
		try {
			// 创建XML DOM树
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// 创建XML根节点
			Element root = doc.createElement("order");
			// 将根节点加入DOM树
			doc.appendChild(root);
			// 创建订单ID节点
			Element oid = doc.createElement("orderId");
			// 设置订单ID节点文本内容
			oid.setTextContent(info.getOrderId() + "");
			// 将订单ID节点设置为根节点子节点
			root.appendChild(oid);
			// 创建点餐员用户名标签
			Element userAccount = doc.createElement("userAccount");
			// 设置点餐员用户名标签文本内容
			userAccount.setTextContent(info.getUserAccount());
			// 将点餐员用户名标签设置为根节点子节点
			root.appendChild(userAccount);
			// 创建桌号标签
			Element tid = doc.createElement("tableId");
			// 设置桌号标签文本内容
			tid.setTextContent(info.getTableId() + "");
			// 将桌号标签设置为根标签子标签
			root.appendChild(tid);
			// 创建订单开始时间标签
			Element orderBeginDate = doc.createElement("orderBeginDate");
			// 创建日期时间格式化工具
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 设置订单开始时间标签文本内容
			orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
			// 将订单开始时间标签设置为根标签子标签
			root.appendChild(orderBeginDate);
			// 创建结单时间标签
			Element orderEndDate = doc.createElement("orderEndDate");
			// 设置结单时间标签文本内容
			orderEndDate.setTextContent(sdf.format(info.getOrderEndDate()));
			// 将节点时间标签设置为根标签子标签
			root.appendChild(orderEndDate);
			// 获取订单总金额
			double sum = service.getSumPriceByOrderId(orderId);
			// 创建总金额标签
			Element sumPrice = doc.createElement("sumPrice");
			// 设置总金额标签文本内容
			sumPrice.setTextContent(sum + "");
			// 将总金额标签设置为根标签子标签
			root.appendChild(sumPrice);
			// 创建字符串输出流
			StringWriter writer = new StringWriter();
			// 创建格式化输出流
			PrintWriter pwriter = new PrintWriter(writer);
			// 将完整的DOM树转换为XML文档结构字符串输出到字符串
			TransformerFactory.newInstance().newTransformer()
					.transform(new DOMSource(doc), new StreamResult(pwriter));
			// 获取XML字符串
			String msg = writer.toString();
			// 格式化输出流关闭
			pwriter.close();
			// 字符串输出流关闭
			writer.close();
			// 获取餐厅管理员等待列表
			ArrayList<String> list = RTPayController.pays;
			// 创建消息生产者
			MessageProducer producer = new MessageProducer();
			// 遍历所有的等待管理员
			for (int i = list.size() - 1; i >= 0; i--) {
				// 获取管理员sessionID
				String id = list.get(i);
				// 为管理员推送订单买单信息
				producer.sendMessage(id, "rtpay", msg);
				// 将本管理员从等待用户列表中删除
				list.remove(id);
			}

			// response.getWriter().write(msg);
			// 捕获异常信息
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace();
		}

	}
	
	

}
