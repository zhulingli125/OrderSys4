package com.chinasofti.ordersys.controller.kitchen;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.chinasofti.util.web.serverpush.MessageConsumer;
import com.chinasofti.util.web.serverpush.MessageHandler;

@Component
public class GetPushMsgTemplate {

	public void getMsg(HttpServletRequest request,
			HttpServletResponse response, GetPushMsgHandler handler) {

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
		handler.initService(request, response, session);

		// ��ȡ�û�ϣ��ץȡ���ݵ�����
		String messageTitle = request.getParameter(messageTitleParameterName);
		// �����������������͵���Ϣ������
		MessageConsumer mconsumer = new MessageConsumer();

		// ������Ҫ�������ڲ��������ʹ����Ӧ������˶���һ��final�汾
		final HttpServletResponse rsp = response;

		// ����������ʵ�ֵ�setHandler����������Ϣ�Ĵ������
		MessageHandler mHandler = handler.getHandler(request, response);

		// ������Ϣ�����߳��Ի�ȡ��Ϣ����
		mconsumer.searchMessage(session.getId(), messageTitle, mHandler);
	}

}
