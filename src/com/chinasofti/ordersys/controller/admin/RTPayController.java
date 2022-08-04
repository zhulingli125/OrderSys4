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
import com.chinasofti.util.web.serverpush.ServerPushKey;
@Controller
public class RTPayController {

	public static ArrayList<String> pays = new ArrayList<String>();
	@RequestMapping("/getrtpay")
	public void getRTPay(HttpServletRequest request,
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
		pays.add(session.getId());
		// ����������ʵ�ֵ�setHandler����������Ϣ�Ĵ������
		MessageHandler handler = getHandler(request, response);

		// ������Ϣ�����߳��Ի�ȡ��Ϣ����
		mconsumer.searchMessage(session.getId(), messageTitle, handler);
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
