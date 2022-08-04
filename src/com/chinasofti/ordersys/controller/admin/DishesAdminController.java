package com.chinasofti.ordersys.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.chinasofti.ordersys.service.admin.DishesService;
import com.chinasofti.ordersys.service.admin.UserService;
import com.chinasofti.ordersys.vo.DishesInfo;
import com.chinasofti.ordersys.vo.UserInfo;
import com.chinasofti.util.web.upload.MultipartRequestParser;
import com.chinasofti.web.common.taglib.TokenTag;


@Controller
public class DishesAdminController {
	@Autowired
	DishesService service;

	public DishesService getService() {
		return service;
	}

	public void setService(DishesService service) {
		this.service = service;
	}

	private static Log log = LogFactory.getLog(DishesAdminController.class);

	@RequestMapping("/adddishes")
	public String addDishes(HttpServletRequest request,
			HttpServletResponse response) {

		// �ж��Ƿ���ڱ��ύ����
		
			// �������������������
			MultipartRequestParser parser = new MultipartRequestParser();
			// ������ȡDishesInfo��Ʒ��Ϣ����
			DishesInfo info = (DishesInfo) parser.parse(request,
					DishesInfo.class);

			// ִ����Ӳ�Ʒ����
			service.addDishes(info);
			// �ͷű��ύ����
		//	TokenTag.releaseToken();
		
		// ��ת����Ʒ�������
		// response.sendRedirect("/OrderSys/todishesadmin.order");
		return "redirect:todishesadmin.order";

	}

	@RequestMapping("/deletedishes")
	public void deleteDishes(Integer dishesId) {
		service.deleteDishesById(dishesId);
	}

	@RequestMapping("/getdishesbypage")
	public void getDishesInfoByPage(HttpServletRequest request,
			HttpServletResponse response) {

		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// ��ȡϣ����ʾ��ҳ����
		int page = Integer.parseInt(request.getParameter("page"));

		// ��ȡ���ҳ����
		int maxPage = service.getMaxPage(8);
		// �Ե�ǰ��ҳ�������о������С��1����ֱ����ʾ��һҳ������
		page = page < 1 ? 1 : page;
		// �Ե�ǰ��ҳ�������о�������������ҳ�룬��ֱ����ʾ���һҳ������
		page = page > maxPage ? maxPage : page;
		// ���з�ҳ���ݲ�ѯ
		List<DishesInfo> list = service.getDishesInfoByPage(page, 8);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("disheses");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĲ�Ʒ��Ϣ
			for (DishesInfo info : list) {
				// ÿһ����Ʒ����һ��dishes��ǩ�ڵ�
				Element dishes = doc.createElement("dishes");
				// ������ƷID��ǩ
				Element dishesId = doc.createElement("dishesId");
				// ���ò�ƷID��ǩ���ı�����
				dishesId.setTextContent(info.getDishesId() + "");
				// ����ƷID��ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesId);
				// ������Ʒ����ǩ
				Element dishesName = doc.createElement("dishesName");
				// ���ò�Ʒ����ǩ���ı�����
				dishesName.setTextContent(info.getDishesName());
				// ����Ʒ����ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(dishesName);
				// ������Ʒ������ǩ
				Element dishesDiscript = doc.createElement("dishesDiscript");
				// ���ò�Ʒ������ǩ�ı�����
				dishesDiscript.setTextContent(info.getDishesDiscript());
				// ����Ʒ������ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesDiscript);
				// ������ƷͼƬ��ǩ
				Element dishesImg = doc.createElement("dishesImg");
				// ���ò�ƷͼƬ��ǩ���ı�����
				dishesImg.setTextContent(info.getDishesImg());
				// ����ƷͼƬ��ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(dishesImg);
				// ������Ʒ��ϸ�ı���ǩ
				Element dishesTxt = doc.createElement("dishesTxt");
				// ��ȡ��Ʒ��ϸ��������
				String txt = info.getDishesTxt();
				// ���ո��滻Ϊ����ָ���
				txt = txt.replaceAll(" ", "ordersysspace");
				// ��\r�滻Ϊ���ַ���
				txt = txt.replaceAll("\r", "");
				// �������滻Ϊ����ָ���
				txt = txt.replaceAll("\n", "ordersysbreak");
				// ��˫�����滻Ϊת���ַ�
				txt = txt.replaceAll("\"", "\\\\\"");
				// ���������滻Ϊת���ַ�
				txt = txt.replaceAll("\'", "\\\\\'");
				// ���ò�Ʒ��ϸ�ı���ǩ���ı�����
				dishesTxt.setTextContent(txt);
				// ����Ʒ��ϸ�ı���ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(dishesTxt);
				// �����Ƿ��Ƽ��ӱ�ǩ
				Element recommend = doc.createElement("recommend");
				// �����Ƿ��Ƽ���Ʒ��ǩ�ı�����
				recommend.setTextContent(info.getRecommend() + "");
				// ���Ƿ��Ƽ���Ʒ��ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(recommend);
				// ������Ʒ�۸��ǩ
				Element dishesPrice = doc.createElement("dishesPrice");
				// ���ò�Ʒ�۸��ǩ�ı�����
				dishesPrice.setTextContent(info.getDishesPrice() + "");
				// ����Ʒ�۸��ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesPrice);
				// ����Ʒ��ǩ����Ϊ����ǩ���ӱ�ǩ
				root.appendChild(dishes);

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

	@RequestMapping("/toprecommend")
	public void getTop4RecommendDishes(HttpServletRequest reuqest,
			HttpServletResponse response) {

		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");

		// ��ȡͷ4���Ƽ���Ʒ��Ϣ�б�
		List<DishesInfo> list = service.getTop4RecommendDishes();
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("disheses");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĲ�Ʒ��Ϣ
			for (DishesInfo info : list) {
				// ÿһ����Ʒ����һ��dishes��ǩ�ڵ�
				Element dishes = doc.createElement("dishes");
				// ������ƷID��ǩ
				Element dishesId = doc.createElement("dishesId");
				// ���ò�ƷID��ǩ���ı�����
				dishesId.setTextContent(info.getDishesId() + "");
				// ����ƷID��ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesId);
				// ������Ʒ����ǩ
				Element dishesName = doc.createElement("dishesName");
				// ���ò�Ʒ����ǩ���ı�����
				dishesName.setTextContent(info.getDishesName());
				// ����Ʒ����ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(dishesName);
				// ������Ʒ������ǩ
				Element dishesDiscript = doc.createElement("dishesDiscript");
				// ���ò�Ʒ������ǩ�ı�����
				dishesDiscript.setTextContent(info.getDishesDiscript());
				// ����Ʒ������ǩ����Ϊ��Ʒ��ǩ�ӱ�ǩ
				dishes.appendChild(dishesDiscript);
				// ������ƷͼƬ��ǩ
				Element dishesImg = doc.createElement("dishesImg");
				// ���ò�ƷͼƬ��ǩ���ı�����
				dishesImg.setTextContent(info.getDishesImg());
				// ����ƷͼƬ��ǩ����Ϊ��Ʒ��ǩ���ӱ�ǩ
				dishes.appendChild(dishesImg);
				// ����Ʒ��ǩ����Ϊ����ǩ���ӱ�ǩ
				root.appendChild(dishes);

			}
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

	@RequestMapping("/modifydishes")
	public String modifyDishes(HttpServletRequest request,
			HttpServletResponse response) {

		// �ж��Ƿ���ڱ��ύ����
	

			// �������������������
			MultipartRequestParser parser = new MultipartRequestParser();
			// ������ȡDishesInfo��Ʒ��Ϣ����
			DishesInfo info = (DishesInfo) parser.parse(request,
					DishesInfo.class);
			// ִ�в�Ʒ��Ϣ�޸Ĺ���
			service.modifyDishes(info);
			// �ͷű��ύ����
			//TokenTag.releaseToken();
		
		// ��ת����Ʒ�������
		return "redirect:todishesadmin.order";

	}

	@RequestMapping("/tomodifydishes")
	public String toModifyDishes(HttpServletRequest request,
			HttpServletResponse response, Integer dishesId) {

		// ��ȡҪ�޸ĵĲ�ƷID����ѯ��Ӧ�Ĳ�Ʒ��Ϣ
		DishesInfo info = service.getDishesById(dishesId);
		// ����Ʒ��Ϣ����request������
		request.setAttribute("DISHES_INFO", info);

		return "/pages/admin/modifydishes.jsp";
	}
}
