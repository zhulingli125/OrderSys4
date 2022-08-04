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
	 * 验证码字符串在会话中的属性名
	 * */
	public static final String CODE_SESSION_ATTR_NAME = "web_app_savecode_value";

	@RequestMapping("/savecode")
	public void getSaveCode(HttpServletRequest request,
			HttpServletResponse response) {

		// 图片不需要缓存的响应头
		response.setHeader("Pragma", "No-cache");
		// 图片不需要缓存的响应头
		response.setHeader("Cache-Control", "no-cache");
		// 图片不需要缓存的响应头
		response.setDateHeader("Expires", 0);
		// 设置响应MIME类型为JPEG图片
		response.setContentType("image/jpeg");
		// 创建验证码服务对象
		SaveCodeService codeService = new SaveCodeService(
				"abcdefghijklmnopqrstuvwxyz123456789".toUpperCase()
						.toCharArray(), 100, 25, 6);
		// 创建验证码图片
		codeService.createSaveCodeImage();
		// 获取验证码图片
		BufferedImage img = codeService.getImage();
		// 获取验证码字符串
		String codeString = codeService.getCodeString();
		// 获取会话对象
		HttpSession session = request.getSession();
		// 将验证码字符串存入会话
		session.setAttribute(CODE_SESSION_ATTR_NAME, codeString);
		try {
			// 将缓存图片编码为物理图片数据并从响应输出流中输出到客户端
			ImageIO.write(img, "JPEG", response.getOutputStream());
			// 捕获异常
		} catch (Exception e) {

			// TODO: handle exception
		}

	}

	@RequestMapping("/checkcode")
	public void checkSaveCode(HttpServletRequest request,
			HttpServletResponse response, String code) {

		// 获取会话对象
		HttpSession session = request.getSession();
		// 获取会话中保存的验证码
		String sessionCode = session.getAttribute(CODE_SESSION_ATTR_NAME)
				.toString();

		// System.out.println(inputCode + "                   " + sessionCode);
		// 设置返回MIME类型
		response.setContentType("text/html");
		// 获取针对客户端的文本输出流

		try {
			PrintWriter out = response.getWriter();
			// 如果在忽略大小写的情况下用户输入的验证码与会话信息中保存的验证码能够匹配
			if (sessionCode.equalsIgnoreCase(code)) {
				// 输出验证码正确的标识
				out.print("OK");
				// 如果不能匹配
			} else {
				// 输出验证码错误的标识
				out.print("FAIL");
			}

			// 刷新输出流
			out.flush();
			// 关闭输出流
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
