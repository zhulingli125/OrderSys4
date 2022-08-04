package com.chinasofti.ordersys.controller.admin;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.chinasofti.ordersys.vo.OrderSumInfo;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.sun.xml.internal.ws.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.service.login.waiters.OrderService;
import com.chinasofti.ordersys.vo.OrderInfo;

@Controller
public class GetOperateDateController {
	@Autowired
	OrderService service;

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}


	@RequestMapping("/getOrderSum")
	public void getOrderSum(HttpServletRequest request,	HttpServletResponse response){

		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		List<OrderSumInfo> list = service.getOrderSum();

		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("ordersum");


			StringBuffer sb = new StringBuffer();
			int monthSize = list.size();
			if (monthSize < 6){
				monthSize = 6;
			}
			for (int i=0;i<list.size();i++){
				sb.append(list.get(i).getOrdersum()).append(',');
			}
			for (int i=list.size();i<monthSize;i++){
					sb.append('0').append(',');
			}
			root.setTextContent(sb.toString());

			System.out.println(sb.toString());
			doc.appendChild(root);

			// ��������DOM��ת��ΪXML�ĵ��ṹ�ַ���������ͻ���
			TransformerFactory
					.newInstance()
					.newTransformer()
					.transform(new DOMSource(doc),
							new StreamResult(response.getOutputStream()));


		}catch (Exception ex) {
			// ����쳣��Ϣ
			ex.printStackTrace();
		}


	}


	@RequestMapping("/getoperatedate")
	public void getOperateDate(HttpServletRequest request,
			HttpServletResponse response) {
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// ���Դ����������ݽ��XML

		// ��ȡϣ����ʾ��ҳ����
		int page = Integer.parseInt(request.getParameter("page"));

		// ��ȡ���ҳ����
		int maxPage = service.getMaxPage(10,2);
		// �Ե�ǰ��ҳ�������о������С��1����ֱ����ʾ��һҳ������
		page = page < 1 ? 1 : page;
		// �Ե�ǰ��ҳ�������о�������������ҳ�룬��ֱ����ʾ���һҳ������
		page = page > maxPage ? maxPage : page;

		try {

			// �������ڸ�ʽ������
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			System.out.println(request.getParameter("bt"));
			// ��ȡ��ʼʱ��
			Date begin = sdf.parse(request.getParameter("bt"));
			// ��ȡ����ʱ��
			Date end = sdf.parse(request.getParameter("et"));
			System.out.println(request.getParameter("et"));
			// ��ѯ�ᵥʱ���ڿ�ʼʱ�������ʱ��֮������ж�����Ϣ
			List<OrderInfo> list = service.getOrderInfoBetweenDate(begin,end,page,10);
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("orders");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĶ�����Ϣ
			for (OrderInfo info : list) {
				// ��ȡÿ���������ܼ�
				float sumPrice = service.getSumPriceByOrderId(new Integer(info.getOrderId()));
				// // ÿһ����������һ��������ǩ�ڵ�
				Element order = doc.createElement("order");
				// ��������id��ǩ
				Element orderId = doc.createElement("orderId");
				// ���ö���id��ǩ�ı�����
				orderId.setTextContent(info.getOrderId() + "");
				// ������id��ǩ����Ϊ������ǩ���ӱ�ǩ
				order.appendChild(orderId);
				// �������ű�ǩ
				Element tableId = doc.createElement("tableId");
				// �������ű�ǩ�ı�����
				tableId.setTextContent(info.getTableId() + "");
				// �����ű�ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(tableId);
				// �����ܼ۱�ǩ
				Element sumPriceElement = doc.createElement("sumPrice");
				// �����ܼ۱�ǩ�ı�����
				sumPriceElement.setTextContent(sumPrice + "");
				// ���ܼ۱�ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(sumPriceElement);
				// ������ͷ���Ա�û�����ǩ
				Element userAccount = doc.createElement("userAccount");
				// ���õ�ͷ���Ա�û�����ǩ�ı�����
				userAccount.setTextContent(info.getUserAccount());
				// ����ͷ���Ա�û�����ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(userAccount);
				// ���������ᵥʱ���ǩ
				Element orderEndDate = doc.createElement("orderEndDate");
				// ����ʱ�䡢���ڸ�ʽ������
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// ���ýᵥʱ���ǩ����Ϊ��ʽ�����ʱ���ַ���
				orderEndDate.setTextContent(sdf.format(info.getOrderEndDate()));
				// ���ᵥʱ���ǩ����Ϊ������ǩ�ӱ�ǩ
				order.appendChild(orderEndDate);

				Element orderBeginDate = doc.createElement("orderBeginDate");
				// ����ʱ�䡢���ڸ�ʽ������
				sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// ���ýᵥʱ���ǩ����Ϊ��ʽ�����ʱ���ַ���
				orderBeginDate.setTextContent(sdf.format(info.getOrderBeginDate()));
				// ���ᵥʱ���ǩ����Ϊ������ǩ�ӱ�ǩ
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

			TransformerFactory tf = TransformerFactory.newInstance();

			Transformer t = tf.newTransformer();

			t.setOutputProperty("encoding", "UTF-8");// ����������⣬�Թ���GBK����

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			t.transform(new DOMSource(doc), new StreamResult(bos));

			String xmlStr = bos.toString();
			System.out.println(xmlStr);


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
