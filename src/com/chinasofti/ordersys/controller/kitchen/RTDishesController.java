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
		dishesName = dishesName+"�ѳ��ͣ�";
		session.setAttribute("outInfo",dishesName);
		int count = 0;
		session.setAttribute("count",count);
		return "/pages/kitchen/kitchenmain.jsp";
	}
	@RequestMapping("/dishesdone")
	public void dishesDone(HttpServletRequest request,
			HttpServletResponse response) {
		// ������Ӧ�����
		response.setCharacterEncoding("utf-8");
		// ��ȡ��Ʒ��Ӧ������
		String tableId = request.getParameter("tableId");
		// ��ȡ��Ʒ��
		String dishesName = request.getParameter("dishesName");
		// ����ʹ��ajax�ύ�������Ҫת��
		try {
			dishesName = new String(dishesName.getBytes("iso8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ������Ϣ������
		MessageProducer producer = new MessageProducer();
		// ��ȡ����Ա�ȴ��б�
		ArrayList<String> list = disheses;
		// ��������Ա�ȴ��б�
		for (int i = list.size() - 1; i >= 0; i--) {
			// ��ȡ�ض��ķ���ԱSessionID
			String id = list.get(i);
			// �Ը÷���Ա������Ʒ��ɵȴ����˵���Ϣ
			producer.sendMessage(id, "rtdishes", "����[" + tableId + "]�Ĳ�Ʒ["
					+ dishesName + "]�Ѿ�������ɣ��봫�ˣ�");
			// �ӵȴ��б���ɾ���÷���Ա
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
				// ���������ַ���
				response.setCharacterEncoding("utf-8");
				// TODO Auto-generated method stub
				// ���Դ���ʵʱ��Ϣ
				try {
					// ��ȡ��Կͻ��˵��ı������
					final PrintWriter out = response.getWriter();
					// ������Ϣ������
					MessageHandler handler = new MessageHandler() {
						// ʵʱ��Ϣ����ص�����
						@Override
						public void handle(
								Hashtable<ServerPushKey, Message> messageQueue,
								ServerPushKey key, Message msg) {
							// ����Ϣ���ı�����ֱ�ӷ��͸��ͻ���
							out.print(msg.getMsg());
							// TODO Auto-generated method stub

						}
					};
					// ���ش����õ���Ϣ������
					return handler;
					// ���񴴽���Ϣ������ʱ�������쳣
				} catch (Exception ex) {
					// ����쳣��Ϣ
					ex.printStackTrace();
					// ����쳣��Ϣ
					return null;
				}
			}

			@Override
			public void initService(HttpServletRequest request,
					HttpServletResponse response, HttpSession session) {
				// TODO Auto-generated method stub
				// ����ǰ�Ự���뵽ʵʱ��Ϣ�ȴ��б�
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
			 * ��ȡʵʱ��Ϣ�������Ļص�
			 * 
			 * @param request
			 *            �������
			 * @param response
			 *            ��Ӧ����
			 * @return ��Servletʹ�õ�ʵʱ��Ϣ������
			 * */
			@Override
			public MessageHandler getHandler(HttpServletRequest request,
					HttpServletResponse response) {
				// TODO Auto-generated method stub
				// ���������ַ���
				response.setCharacterEncoding("utf-8");
				// ���Դ���ʵʱ��Ϣ
				try {
					// ��ȡ��Կͻ��˵��ı������
					final PrintWriter out = response.getWriter();
					// ������Ϣ������
					MessageHandler handler = new MessageHandler() {

						@Override
						public void handle(
								Hashtable<ServerPushKey, Message> messageQueue,
								ServerPushKey key, Message msg) {
							// ����Ϣ���ı�����ֱ�ӷ��͸��ͻ���
							out.print(msg.getMsg());
							// TODO Auto-generated method stub

						}
					};
					// ���ش����õ���Ϣ������
					return handler;
					// ���񴴽���Ϣ������ʱ�������쳣
				} catch (Exception ex) {
					// ����쳣��Ϣ
					ex.printStackTrace();
					// ���ؿյ���Ϣ������
					return null;
				}
			}

			/**
			 * ��ʼ��ʵʱ��Ϣ��ȡ����ķ���
			 * 
			 * @param request
			 *            ������Ϣ
			 * @param response
			 *            ��Ӧ����
			 * @param session
			 *            �Ự���ٶ���
			 * */
			@Override
			public void initService(HttpServletRequest request,
					HttpServletResponse response, HttpSession session) {
				// TODO Auto-generated method stub
				// ����ǰ�Ự���뵽ʵʱ��Ϣ�ȴ��б�
				kitchens.add(session.getId());
			}
		};

		template.getMsg(request, response, handler);

	}

	@RequestMapping("/getorderbypage")
	public void getUserByPage(HttpServletRequest request, HttpServletResponse response) {
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// ��ȡϣ����ʾ��ҳ����
		int page = Integer.parseInt(request.getParameter("page"));
		// ��ȡ���ҳ����
		int maxPage = orderService.getMaxPageByState(8,0);
		// �Ե�ǰ��ҳ�������о������С��1����ֱ����ʾ��һҳ������
		page = page < 1 ? 1 : page;
		// �Ե�ǰ��ҳ�������о�������������ҳ�룬��ֱ����ʾ���һҳ������
		page = page > maxPage ? maxPage : page;
		// ���з�ҳ���ݲ�ѯ
		List<OrderInfo> list = orderService.getByPage(page, 8);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("orders");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������е��û���Ϣ
			for (OrderInfo info : list) {
				// ÿһ���û�����һ���û���ǩ
				Element order = doc.createElement("order");
				// �����û�ID��ǩ
				Element orderId = doc.createElement("orderId");
				// �����û�ID��ǩ�ı�����
				orderId.setTextContent(info.getOrderId() + "");
				// ���û�ID��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				order.appendChild(orderId);
				// �����û�����ǩ
				Element tableId = doc.createElement("tableId");
				// �����û�����ǩ�ı�����
				tableId.setTextContent(info.getTableId() + "");
				// ���û�����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				order.appendChild(tableId);
				// ������ɫ����ǩ
				Element dishesName = doc.createElement("dishesName");
				// ���ý�ɫ����ǩ�ı�����
				dishesName.setTextContent(info.getDishesName());
				// ����ɫ����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				order.appendChild(dishesName);
				// ������ɫ������Ϣ��ǩ
				Element num = doc.createElement("num");
				// ���ý�ɫ������Ϣ��ǩ�ı�����
				num.setTextContent(info.getNum() + "");
				// ����ɫ������Ϣ��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				order.appendChild(num);
				// �����û���ǩΪ����ǩ�ӱ�ǩ
				root.appendChild(order);
			}
			// ������ǰҳ�����ı�ǩ
			Element pageNow = doc.createElement("page");
			// ���õ�ǰҳ������ǩ���ı�����
			pageNow.setTextContent(page + "");
			// ����ǰҳ������ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(pageNow);
			// �������ҳ�����ı�ǩ
			Element maxPageElement = doc.createElement("maxPage");
			// �������ҳ������ǩ���ı�����
			maxPageElement.setTextContent(maxPage + "");
			// �����ҳ������ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(maxPageElement);
			// ��������DOM��ת��ΪXML�ĵ��ṹ�ַ���������ͻ���
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));
			// �����ѯ��ת�������е��쳣��Ϣ
		} catch (Exception ex) {
			// ����쳣��Ϣ
			ex.printStackTrace();
		}

	}
}
