package com.chinasofti.ordersys.controller.admin;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.util.web.serverpush.Message;
import com.chinasofti.util.web.serverpush.MessageConsumer;
import com.chinasofti.util.web.serverpush.MessageHandler;
import com.chinasofti.util.web.serverpush.MessageProducer;
import com.chinasofti.util.web.serverpush.ServerPushKey;

@Controller
public class RTBordController {

	/**
	 * ��ȡʵʩ������Ϣ�ĵȴ��б�
	 * */
	public static ArrayList<String> bords = new ArrayList<String>();

	@RequestMapping("/getrtbord")
	public void getRTBordMsg(HttpServletRequest request,
			HttpServletResponse response) {

		String messageTitleParameterName = "messageTitle";

		// ������ȷ�������ַ����Է�ֹ��������
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ���õ��ͻ����������������ݵ��ַ���
		response.setCharacterEncoding("utf-8");
		// ��ȡ�û���session�Ự����

		HttpSession session = request.getSession(true);

		// ��ȡ�û�ϣ��ץȡ���ݵ�����
		String messageTitle = request.getParameter(messageTitleParameterName);
		// �����������������͵���Ϣ������
		MessageConsumer mconsumer = new MessageConsumer();

		// ������Ҫ�������ڲ��������ʹ����Ӧ������˶���һ��final�汾
		final HttpServletResponse rsp = response;

		// ����ǰ�Ự���뵽ʵʱ��Ϣ�ȴ��б�
		bords.add(session.getId());
		// ����������ʵ�ֵ�setHandler����������Ϣ�Ĵ������
		MessageHandler handler = getHandler(request, response);

		// ������Ϣ�����߳��Ի�ȡ��Ϣ����
		mconsumer.searchMessage(session.getId(), messageTitle, handler);

	}

	@RequestMapping("/sendbord")
	public void sendBord(HttpServletRequest request,
			HttpServletResponse response) {

		// ������Ӧ�ַ���
		response.setCharacterEncoding("utf-8");
		// ��ȡ������Ϣ
		String bord = request.getParameter("bord");
		// ����ʵʩ��Ϣ������
		MessageProducer producer = new MessageProducer();
		// ��ȡʵʱ������Ϣ�ȴ��б�
		ArrayList<String> list = bords;
		// �����ȴ��б�
		for (int i = list.size() - 1; i >= 0; i--) {
			// ��ȡ�ȴ���Ϣ���û�sessionID
			String id = list.get(i);
			// ��Ը�sessionID����Ϣ���⡢����������Ϣ
			producer.sendMessage(id, "rtbord", bord);
			// ����sessionID�ӵȴ��б���ɾ��
			list.remove(id);
		}

	}

	/**
	 * ��ȡʵʱ��Ϣ�������Ļص�
	 * 
	 * @param request
	 *            �������
	 * @param response
	 *            ��Ӧ����
	 * @return ��Servletʹ�õ�ʵʱ��Ϣ������
	 * */
	public MessageHandler getHandler(HttpServletRequest request,
			HttpServletResponse response) {
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
			// ���ؿյ���Ϣ������
			return null;
		}
	}

}
