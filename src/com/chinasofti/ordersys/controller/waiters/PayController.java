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
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");

		// �����ѯ����ҳ�����
		int page = 1;
		// ��������а���ҳ����Ϣ
		if (request.getParameter("page") != null) {
			// ��ȡ�����е�ҳ����Ϣ
			page = Integer.parseInt(request.getParameter("page"));
		}
		// ��ȡ���ҳ����
		int maxPage = service.getMaxPage(5, 0);
		// �Ե�ǰ��ҳ�������о������С��1����ֱ����ʾ��һҳ������
		page = page < 1 ? 1 : page;
		// �Ե�ǰ��ҳ�������о�������������ҳ�룬��ֱ����ʾ���һҳ������
		page = page > maxPage ? maxPage : page;
		// ����ҳ����Ϣ��ѯ��Ҫ�򵥵Ķ�����Ϣ
		List<OrderInfo> list = service.getNeedPayOrdersByPage(page, 5, 0);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("orderes");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ������ÿһ��������Ϣ
			for (OrderInfo info : list) {
				// ÿһ����������һ��������ǩ
				Element order = doc.createElement("order");
				// ��������Id��ǩ
				Element orderId = doc.createElement("orderId");
				// ���ö���ID��ǩ���ı�����
				orderId.setTextContent(info.getOrderId() + "");
				// ������ID��ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(orderId);
				// �������ű�ǩ
				Element tableId = doc.createElement("tableId");
				// �������ű�ǩ�ı�����
				tableId.setTextContent(info.getTableId() + "");
				// �����ű�ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(tableId);
				// �������Ա�ʺű�ǩ
				Element userAccount = doc.createElement("userAccount");
				// ���õ��Ա�ʺű�ǩ�ı�����
				userAccount.setTextContent(info.getUserAccount());
				// �����Ա�ʺű�ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(userAccount);
				// ����������ʼʱ���ǩ
				Element orderBeginDate = doc.createElement("orderBeginDate");
				// ��������ʱ���ʽ������
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				// ���ö�����ʼʱ���ǩ�ı�����
				orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
				// ��������ʼʱ���ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(orderBeginDate);
				// ��������ǩ����Ϊ����ǩ�ӱ�ǩ
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

	@RequestMapping("/requestpay")
	public void requestToPay(HttpServletRequest request,
			HttpServletResponse response, Integer orderId) {
		// ��ȡ��Ҫ�򵥵Ķ���ID

		// ������������������

		// �޸����ݿ��еĶ���״̬��Ϣ
		service.requestPay(orderId);
		// ��ȡ��������
		OrderInfo info = service.getOrderById(orderId);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("order");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ��������ID�ڵ�
			Element oid = doc.createElement("orderId");
			// ���ö���ID�ڵ��ı�����
			oid.setTextContent(info.getOrderId() + "");
			// ������ID�ڵ�����Ϊ���ڵ��ӽڵ�
			root.appendChild(oid);
			// �������Ա�û�����ǩ
			Element userAccount = doc.createElement("userAccount");
			// ���õ��Ա�û�����ǩ�ı�����
			userAccount.setTextContent(info.getUserAccount());
			// �����Ա�û�����ǩ����Ϊ���ڵ��ӽڵ�
			root.appendChild(userAccount);
			// �������ű�ǩ
			Element tid = doc.createElement("tableId");
			// �������ű�ǩ�ı�����
			tid.setTextContent(info.getTableId() + "");
			// �����ű�ǩ����Ϊ����ǩ�ӱ�ǩ
			root.appendChild(tid);
			// ����������ʼʱ���ǩ
			Element orderBeginDate = doc.createElement("orderBeginDate");
			// ��������ʱ���ʽ������
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// ���ö�����ʼʱ���ǩ�ı�����
			orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
			// ��������ʼʱ���ǩ����Ϊ����ǩ�ӱ�ǩ
			root.appendChild(orderBeginDate);
			// �����ᵥʱ���ǩ
			Element orderEndDate = doc.createElement("orderEndDate");
			// ���ýᵥʱ���ǩ�ı�����
			orderEndDate.setTextContent(sdf.format(info.getOrderEndDate()));
			// ���ڵ�ʱ���ǩ����Ϊ����ǩ�ӱ�ǩ
			root.appendChild(orderEndDate);
			// ��ȡ�����ܽ��
			double sum = service.getSumPriceByOrderId(orderId);
			// �����ܽ���ǩ
			Element sumPrice = doc.createElement("sumPrice");
			// �����ܽ���ǩ�ı�����
			sumPrice.setTextContent(sum + "");
			// ���ܽ���ǩ����Ϊ����ǩ�ӱ�ǩ
			root.appendChild(sumPrice);
			// �����ַ��������
			StringWriter writer = new StringWriter();
			// ������ʽ�������
			PrintWriter pwriter = new PrintWriter(writer);
			// ��������DOM��ת��ΪXML�ĵ��ṹ�ַ���������ַ���
			TransformerFactory.newInstance().newTransformer()
					.transform(new DOMSource(doc), new StreamResult(pwriter));
			// ��ȡXML�ַ���
			String msg = writer.toString();
			// ��ʽ��������ر�
			pwriter.close();
			// �ַ���������ر�
			writer.close();
			// ��ȡ��������Ա�ȴ��б�
			ArrayList<String> list = RTPayController.pays;
			// ������Ϣ������
			MessageProducer producer = new MessageProducer();
			// �������еĵȴ�����Ա
			for (int i = list.size() - 1; i >= 0; i--) {
				// ��ȡ����ԱsessionID
				String id = list.get(i);
				// Ϊ����Ա���Ͷ�������Ϣ
				producer.sendMessage(id, "rtpay", msg);
				// ��������Ա�ӵȴ��û��б���ɾ��
				list.remove(id);
			}

			// response.getWriter().write(msg);
			// �����쳣��Ϣ
		} catch (Exception ex) {
			// ����쳣��Ϣ
			ex.printStackTrace();
		}

	}
	
	

}
