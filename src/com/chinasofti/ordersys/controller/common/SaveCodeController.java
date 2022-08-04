package com.chinasofti.ordersys.controller.common;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chinasofti.web.common.service.SaveCodeService;

@Controller
public class SaveCodeController {

	/**
	 * ��֤���ַ����ڻỰ�е�������
	 * */
	public static final String CODE_SESSION_ATTR_NAME = "web_app_savecode_value";

	@RequestMapping("/savecode")
	public void getSaveCode(HttpServletRequest request,
			HttpServletResponse response) {

		// ͼƬ����Ҫ�������Ӧͷ
		response.setHeader("Pragma", "No-cache");
		// ͼƬ����Ҫ�������Ӧͷ
		response.setHeader("Cache-Control", "no-cache");
		// ͼƬ����Ҫ�������Ӧͷ
		response.setDateHeader("Expires", 0);
		// ������ӦMIME����ΪJPEGͼƬ
		response.setContentType("image/jpeg");
		// ������֤��������
		SaveCodeService codeService = new SaveCodeService(
				"abcdefghijklmnopqrstuvwxyz123456789".toUpperCase()
						.toCharArray(), 100, 25, 6);
		// ������֤��ͼƬ
		codeService.createSaveCodeImage();
		// ��ȡ��֤��ͼƬ
		BufferedImage img = codeService.getImage();
		// ��ȡ��֤���ַ���
		String codeString = codeService.getCodeString();
		// ��ȡ�Ự����
		HttpSession session = request.getSession();
		// ����֤���ַ�������Ự
		session.setAttribute(CODE_SESSION_ATTR_NAME, codeString);
		try {
			// ������ͼƬ����Ϊ����ͼƬ���ݲ�����Ӧ�������������ͻ���
			ImageIO.write(img, "JPEG", response.getOutputStream());
			// �����쳣
		} catch (Exception e) {

			// TODO: handle exception
		}

	}

	@RequestMapping("/checkcode")
	public void checkSaveCode(HttpServletRequest request,
			HttpServletResponse response, String code) {

		// ��ȡ�Ự����
		HttpSession session = request.getSession();
		// ��ȡ�Ự�б������֤��
		String sessionCode = session.getAttribute(CODE_SESSION_ATTR_NAME)
				.toString();

		// System.out.println(inputCode + "                   " + sessionCode);
		// ���÷���MIME����
		response.setContentType("text/html");
		// ��ȡ��Կͻ��˵��ı������

		try {
			PrintWriter out = response.getWriter();
			// ����ں��Դ�Сд��������û��������֤����Ự��Ϣ�б������֤���ܹ�ƥ��
			if (sessionCode.equalsIgnoreCase(code)) {
				// �����֤����ȷ�ı�ʶ
				out.print("OK");
				// �������ƥ��
			} else {
				// �����֤�����ı�ʶ
				out.print("FAIL");
			}

			// ˢ�������
			out.flush();
			// �ر������
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
