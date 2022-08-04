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
		// ��ȡ�Ự����
		HttpSession session = request.getSession();
		// �������ﳵ����
		Cart cart = new Cart();
		// ����Ự�еĴ��ڹ��ﳵ
		if (session.getAttribute("CART") != null) {
			// ֱ�ӻ�ȡ�Ự�еĹ��ﳵ����
			cart = (Cart) session.getAttribute("CART");
		}
		// �������ű���
		Integer tableId = 1;
		// ����Ự�д���������Ϣ
		if (session.getAttribute("TABLE_ID") != null) {
			// ֱ�ӻ�ȡ������Ϣ
			tableId = (Integer) session.getAttribute("TABLE_ID");
		}
		// ���ù��ﳵ��������Ϣ
		cart.setTableId(tableId.intValue());
		// ��ȡ���μ��빺�ﳵ�Ĳ�Ʒ����

		cart.getUnits().add(cart.createUnit(dishes, num));
		// �����ﳵ�������õ��Ự��
		session.setAttribute("CART", cart);

	}

	@RequestMapping("/commitcart")
	public void commitCart(HttpServletRequest request,
			HttpServletResponse response) {
		// ������Ӧ����
		response.setCharacterEncoding("utf-8");
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// ������Ʒ����������

		// ��ȡ�Ự����
		HttpSession session = request.getSession();
		// �������ﳵ����
		Cart cart = new Cart();
		// �������ű���
		Integer tableId = new Integer(1);
		// ���Session�б�����������Ϣ
		if (session.getAttribute("TABLE_ID") != null) {
			// ֱ�ӻ�ȡ������Ϣ
			tableId = (Integer) session.getAttribute("TABLE_ID");

		}
		// ����Ự�д��ڹ��ﳵ��Ϣ
		if (session.getAttribute("CART") != null) {
			// ֱ�ӻ�ȡ�Ự�еĹ��ﳵ����
			cart = (Cart) session.getAttribute("CART");
		}
		// �����ͷ���ԱID����
		int waiterId = 1;
		// ���Session�д��ڵ�¼��Ϣ
		if (session.getAttribute("USER_INFO") != null) {
			// ��ȡ���û����û�ID
			waiterId = ((UserInfo) session.getAttribute("USER_INFO"))
					.getUserId();
		}
		// ������������������

		// ���������������ݿⲢ��ȡ������Ӷ���������
		Object key = oservice.addOrder(waiterId, tableId);
		// System.out.println(key);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("disheses");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĶ�����Ʒ����
			for (Cart.CartUnit unit : cart.getUnits()) {
				// ��������Ʒ����ӳ����Ϣ�������ݿ�
				oservice.addOrderDishesMap(unit, ((Long) key).intValue());
				// ÿһ����Ʒ����һ��dishes��ǩ�ڵ�
				Element dishes = doc.createElement("dishes");
				// �������ű�ǩ
				Element tid = doc.createElement("tableId");
				// �������ű�ǩ�ı�����
				tid.setTextContent(tableId.intValue() + "");
				// �����ű�ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(tid);
				// ������Ʒ����ǩ
				Element dishesName = doc.createElement("dishesName");
				// ��ȡ��Ʒ����
				String dname = service.getDishesById(
						new Integer(unit.getDishesId())).getDishesName();
				// ���ò�Ʒ����ǩ�ı�����
				dishesName.setTextContent(dname);
				// ����Ʒ���Ʊ�ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesName);
				// ������Ʒ������ǩ
				Element num = doc.createElement("num");
				// ����������ǩ�ı�����
				num.setTextContent(unit.getNum() + "");
				// ��������ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(num);
				// ����Ʒ��ǩ����Ϊ����ǩ�ӱ�ǩ
				root.appendChild(dishes);

			}
			// �����ַ������
			StringWriter writer = new StringWriter();
			// ������ʽ�������
			PrintWriter pwriter = new PrintWriter(writer);
			// ��������DOM��ת��ΪXML�ĵ��ṹ�ַ���������ַ��������
			TransformerFactory.newInstance().newTransformer()
					.transform(new DOMSource(doc), new StreamResult(pwriter));
			// ��ȡXML�ַ���
			String msg = writer.toString();
			// �رո�ʽ�������
			pwriter.close();
			// �ر��ַ��������
			writer.close();
			// ��ȡ����ȴ��б�
			ArrayList<String> list = RTDishesController.kitchens;
			// ������Ϣ������
			MessageProducer producer = new MessageProducer();
			// ����ÿһ������ȴ��û�
			for (int i = list.size() - 1; i >= 0; i--) {
				// ��ȡ�����ȴ��û���sessionID
				String id = list.get(i);
				// Ϊ���û����ɵ�˶�����Ϣ
				producer.sendMessage(id, "rtorder", msg);
				// �ڵȴ��б���ɾ�����û�
				list.remove(id);
			}
			// �����յĹ��ﳵ����
			cart = new Cart();
			// ��ջỰ���ﳵ
			session.setAttribute("CART", cart);

			// response.getWriter().write(writer.toString());
			// �����쳣
		} catch (Exception ex) {
			// ����쳣��Ϣ
			ex.printStackTrace();
		}
	}

}
