package com.chinasofti.ordersys.controller.common;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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
import com.chinasofti.util.web.upload.FormFile;
import com.chinasofti.util.web.upload.MultipartRequestParser;

@Controller
public class PreviewImageUploadController {

	@RequestMapping("/upimg")
	public void upload(HttpServletRequest request, HttpServletResponse response) {

		// ����Ĭ�ϵ�ͼƬ���·��
		String savepath = "/img/faces";
		// �ж�����������Ƿ�����Զ�����·��
		if (request.getParameter("path") != null) {
			// �����Զ���ͼƬ���·��
			savepath = request.getParameter("path");
		}
		// ��ȡ�����������
		MultipartRequestParser parser = new MultipartRequestParser();
		// �������ݲ���ȡ��Ӧvo
		PreviewImageInfo info = (PreviewImageInfo) parser.parse(request,
				"com.chinasofti.ordersys.controller.common.PreviewImageInfo");
		// ��ȡ�ϴ��ļ�����
		FormFile img = info.getUploadFile();

		// ��ȡ���ͼ�������·��
		String path = request.getSession().getServletContext()
				.getRealPath(savepath);
		// ���ϴ���ͼƬ��ŵ�����·����
		img.saveToFileSystem(request, path + "/" + img.getFileName());

		// ���������ݵ��ַ���������"utf-8",��ȷ���ú��ֹ��ȡ�������ݱ������
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �����������������ݵ���Ϣ������
		MessageProducer producer = new MessageProducer();

		// �򱾻Ự����һ���ļ��ϴ��ɹ���ʵʱ������Ϣ
		producer.sendMessage(request.getSession().getId().toString(),
				"upstate", img.getFileName());

	}

	@RequestMapping("/state")
	public void getPreviewMsg(HttpServletRequest request,
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
	public MessageHandler getHandler(final HttpServletRequest request,
			final HttpServletResponse response) {
		// TODO Auto-generated method stub
		MessageHandler handler = new MessageHandler() {
			// ʵ�ֵ���ȡ��Ϣ�����Ϣ����ʵ�ʴ���Ļص�������
			// �ص�������messageQueue���������˵�ǰϵͳʹ�õ���Ϣ�ȴ�����
			// �ص�������key���������˵�ǰ��ȡ������Ϣ���͵�Ŀ��sessionid����Ϣ����
			// �ص�������msg����������ʵ�ʵ���Ϣ����
			@Override
			public void handle(Hashtable<ServerPushKey, Message> messageQueue,
					ServerPushKey key, Message msg) {

				try {
					// ��ȡ��Կͻ��˵��ַ������
					PrintWriter pw = response.getWriter();
					// ����Ϣ�ַ���ֱ�ӷ��͸��ͻ��������
					pw.print(msg.getMsg());
				} catch (Exception ex) {
					// ��������쳣������쳣��Ϣ
					ex.printStackTrace();
				}
			}
		};
		// ���ش����õ���Ϣ������
		return handler;
	}

}
