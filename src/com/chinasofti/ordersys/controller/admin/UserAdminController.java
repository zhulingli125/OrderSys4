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

		// �������������������
		MultipartRequestParser parser = new MultipartRequestParser();
		// ������ȡUserInfo��ɫ��Ϣ����
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// ִ���޸Ĳ���
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
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// �Ӽ������л�ȡ���ߵĺ����Ա�б�
		ArrayList<UserInfo> kitchen = OrderSysListener.getOnlineKitchens();
		// ��ȡ���еĻỰ��Ŀ
		int sessions = OrderSysListener.onlineSessions;
		// ���Խ����ߺ����Ա�б�ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("users");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĺ����Ա��Ϣ
			for (UserInfo info : kitchen) {
				// ÿһ��Ա������һ���û���ǩ�ڵ�
				Element user = doc.createElement("user");
				// �����û�id�ڵ��ǩ
				Element userId = doc.createElement("userId");
				// �����û�id��ǩ�ı�����
				userId.setTextContent(info.getUserId() + "");
				// ���û�id��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userId);
				// �����û�����ǩ
				Element userAccount = doc.createElement("userAccount");
				// �����û�����ǩ�ı�����
				userAccount.setTextContent(info.getUserAccount());
				// ���û�����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userAccount);
				// ������ɫid��ǩ
				Element roleId = doc.createElement("roleId");
				// ���ý�ɫid��ǩ�ı�����
				roleId.setTextContent(info.getRoleId() + "");
				// ����ɫid��ǩ����Ϊ�û���ǩ���ӱ�ǩ
				user.appendChild(roleId);
				// ������ɫ����ǩ
				Element roleName = doc.createElement("roleName");
				// ���ý�ɫ����ǩ�ı�����
				roleName.setTextContent(info.getRoleName());
				// ����ɫ����ǩ����Ϊ�û���ǩ���ӱ�ǩ
				user.appendChild(roleName);
				// �����û�����״̬��ǩ
				Element locked = doc.createElement("locked");
				// �����û�����״̬��ǩ�ı�����
				locked.setTextContent(info.getLocked() + "");
				// ���û�����״̬��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(locked);
				// ������ɫͷ���ǩ
				Element faceimg = doc.createElement("faceimg");
				// ���ý�ɫͷ���ǩ�ı�����
				faceimg.setTextContent(info.getFaceimg() + "");
				// ����ɫͷ���ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(faceimg);
				// ����ɫ��ǩ����Ϊ����ǩ�ӽڵ�
				root.appendChild(user);

			}
			// �����Ự����ǩ
			Element sessionNum = doc.createElement("sessionNum");
			// ���ûỰ����ǩ�ı�����
			sessionNum.setTextContent(sessions + "");
			// ���Ự����ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(sessionNum);
			// ���������Ա����ǩ
			Element waitersNum = doc.createElement("kitchenNum");
			// ���ú����Ա����ǩ�ı�����
			waitersNum.setTextContent(kitchen.size() + "");
			// �������Ա����ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(waitersNum);
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

	@RequestMapping("/getonlinewaiters")
	public void getOnlineWaiters(HttpServletRequest request,
			HttpServletResponse response) {
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// �Ӽ������л�ȡ���ߵķ���Ա�б�
		ArrayList<UserInfo> waiters = OrderSysListener.getOnlineWaiters();
		// ��ȡ���еĻỰ��Ŀ
		int sessions = OrderSysListener.onlineSessions;
		// ���Խ����߷���Ա�б�ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("users");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������еĹ���Ա��Ϣ
			for (UserInfo info : waiters) {
				// ÿһ��Ա������һ���û���ǩ�ڵ�
				Element user = doc.createElement("user");
				// �����û�id�ڵ��ǩ
				Element userId = doc.createElement("userId");
				// �����û�id��ǩ�ı�����
				userId.setTextContent(info.getUserId() + "");
				// ���û�id��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userId);
				// �����û�����ǩ
				Element userAccount = doc.createElement("userAccount");
				// �����û�����ǩ�ı�����
				userAccount.setTextContent(info.getUserAccount());
				// ���û�����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userAccount);
				// ������ɫid��ǩ
				Element roleId = doc.createElement("roleId");
				// ���ý�ɫid��ǩ�ı�����
				roleId.setTextContent(info.getRoleId() + "");
				// ����ɫid��ǩ����Ϊ�û���ǩ���ӱ�ǩ
				user.appendChild(roleId);
				// ������ɫ����ǩ
				Element roleName = doc.createElement("roleName");
				// ���ý�ɫ����ǩ�ı�����
				roleName.setTextContent(info.getRoleName());
				// ����ɫ����ǩ����Ϊ�û���ǩ���ӱ�ǩ
				user.appendChild(roleName);
				// �����û�����״̬��ǩ
				Element locked = doc.createElement("locked");
				// �����û�����״̬��ǩ�ı�����
				locked.setTextContent(info.getLocked() + "");
				// ���û�����״̬��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(locked);
				// ������ɫͷ���ǩ
				Element faceimg = doc.createElement("faceimg");
				// ���ý�ɫͷ���ǩ�ı�����
				faceimg.setTextContent(info.getFaceimg() + "");
				// ����ɫͷ���ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(faceimg);
				// ����ɫ��ǩ����Ϊ����ǩ�ӽڵ�
				root.appendChild(user);

			}
			// �����Ự����ǩ
			Element sessionNum = doc.createElement("sessionNum");
			// ���ûỰ����ǩ�ı�����
			sessionNum.setTextContent(sessions + "");
			// ���Ự����ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(sessionNum);
			// ��������Ա����ǩ
			Element waitersNum = doc.createElement("waitersNum");
			// ���÷���Ա����ǩ�ı�����
			waitersNum.setTextContent(waiters.size() + "");
			// ������Ա����ǩ����Ϊ����ǩ���ӱ�ǩ
			root.appendChild(waitersNum);
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

	@RequestMapping("/getuserbypage")
	public void getUserByPage(HttpServletRequest request,
			HttpServletResponse response) {
		// ���÷��ص�MIME����Ϊxml
		response.setContentType("text/xml");
		// ��ȡϣ����ʾ��ҳ����
		int page = Integer.parseInt(request.getParameter("page"));

		// ��ȡ���ҳ����
		int maxPage = service.getMaxPage(10);
		// �Ե�ǰ��ҳ�������о������С��1����ֱ����ʾ��һҳ������
		page = page < 1 ? 1 : page;
		// �Ե�ǰ��ҳ�������о�������������ҳ�룬��ֱ����ʾ���һҳ������
		page = page > maxPage ? maxPage : page;
		// ���з�ҳ���ݲ�ѯ
		List<UserInfo> list = service.getByPage(page, 10);
		// ���Խ�����ṹ��Ϊxml�ĵ�
		try {
			// ����XML DOM��
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().newDocument();
			// ����XML���ڵ�
			Element root = doc.createElement("users");
			// �����ڵ����DOM��
			doc.appendChild(root);
			// ѭ��������������е��û���Ϣ
			for (UserInfo info : list) {
				// ÿһ���û�����һ���û���ǩ
				Element user = doc.createElement("user");
				// �����û�ID��ǩ
				Element userId = doc.createElement("userId");
				// �����û�ID��ǩ�ı�����
				userId.setTextContent(info.getUserId() + "");
				// ���û�ID��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userId);
				// �����û�����ǩ
				Element userAccount = doc.createElement("userAccount");
				// �����û�����ǩ�ı�����
				userAccount.setTextContent(info.getUserAccount());
				// ���û�����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(userAccount);
				// ������ɫid��ǩ
				Element roleId = doc.createElement("roleId");
				// ���ý�ɫid��ǩ�ı�����
				roleId.setTextContent(info.getRoleId() + "");
				// ����ɫid��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(roleId);
				// ������ɫ����ǩ
				Element roleName = doc.createElement("roleName");
				// ���ý�ɫ����ǩ�ı�����
				roleName.setTextContent(info.getRoleName());
				// ����ɫ����ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(roleName);
				// ������ɫ������Ϣ��ǩ
				Element locked = doc.createElement("locked");
				// ���ý�ɫ������Ϣ��ǩ�ı�����
				locked.setTextContent(info.getLocked() + "");
				// ����ɫ������Ϣ��ǩ����Ϊ�û���ǩ�ӱ�ǩ
				user.appendChild(locked);
				// ������ɫͷ���ǩ
				Element faceimg = doc.createElement("faceimg");
				// ���ý�ɫͷ���ǩ�ı�����
				faceimg.setTextContent(info.getFaceimg() + "");
				// ����ͷ���ǩΪ�û���ǩ�ӱ�ǩ
				user.appendChild(faceimg);
				// �����û���ǩΪ����ǩ�ӱ�ǩ
				root.appendChild(user);
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

	@RequestMapping("/modifyuser")
	public String modifyMyInfo(HttpServletRequest request,
			HttpServletResponse response) {

		// �������������������
		MultipartRequestParser parser = new MultipartRequestParser();
		// ���������ȡUserInfo�û���Ϣ����
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);
		// ִ���û���Ϣ�޸Ĳ���
		service.modify(info);
		// �޸���Ϣ���Զ�ע��
		return "redirect:logut.order";

	}

	@RequestMapping("/adduser")
	public String addUser(HttpServletRequest request,
			HttpServletResponse response) {

		// �������������������
		MultipartRequestParser parser = new MultipartRequestParser();
		// ������ȡUserInfo�û���Ϣ����
		UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);

		// ����û�
		service.addUser(info);
		
		

		// ��ת���û��������
		return "redirect:touseradmin.order";

	}

	@RequestMapping("/checkuser")
	public void checkAddUser(HttpServletRequest request,
			HttpServletResponse response) {

		// ��ȡ��������е��û�����Ϣ
		String userAccount = request.getParameter("name");
		try {
			// ������ajax���������Ҫת��
			userAccount = new String(userAccount.getBytes("iso8859-1"), "utf-8");
			// ��ȡ�������ӳص����ݿ�ģ��������߶���

			// ��ѯ��Ӧ�û������û���Ϣ
			List<UserInfo> list = service.findUserByName(userAccount);

			PrintWriter pw = response.getWriter();
			// ������ݿ���������
			if (list.size() == 0) {
				// ���������ӱ�ʶ
				pw.print("OK");
				// ������ݿ���������
			} else {
				// ���������ӱ�ʶ
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
			// �������������ݽ������߶���
			MultipartRequestParser parser = new MultipartRequestParser();
			// ��������ȡUserInfo����
			UserInfo info = (UserInfo) parser.parse(request, UserInfo.class);

			// ��ȡ��Կͻ��˵��ı������
			PrintWriter pw = response.getWriter();
			// ���������ȷ
			if (cservice.checkPass(info)) {
				// ���������ȷ�ı�ʶ
				pw.print("OK");
				// �������
			} else {
				// ����������ı�ʶ
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
		// ���û���Ϣ����request������
		request.setAttribute("MODIFY_INFO", info);
		// ��ת���û���Ϣ�޸Ľ���
		return "/pages/admin/modifyuser.jsp";
	}

}
