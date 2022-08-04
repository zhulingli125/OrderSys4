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

		// 定义默认的图片存放路径
		String savepath = "/img/faces";
		// 判定请求参数中是否存在自定义存放路径
		if (request.getParameter("path") != null) {
			// 设置自定义图片存放路径
			savepath = request.getParameter("path");
		}
		// 获取表单请求解析器
		MultipartRequestParser parser = new MultipartRequestParser();
		// 解析数据并获取对应vo
		PreviewImageInfo info = (PreviewImageInfo) parser.parse(request,
				"com.chinasofti.ordersys.controller.common.PreviewImageInfo");
		// 获取上传文件对象
		FormFile img = info.getUploadFile();

		// 获取存放图像的物理路径
		String path = request.getSession().getServletContext()
				.getRealPath(savepath);
		// 将上传的图片存放到物理路径中
		img.saveToFileSystem(request, path + "/" + img.getFileName());

		// 请求中数据的字符集编码是"utf-8",正确设置后防止获取到的数据变成乱码
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 创建服务器推送数据的消息生产者
		MessageProducer producer = new MessageProducer();

		// 向本会话生产一个文件上传成功的实时推送消息
		producer.sendMessage(request.getSession().getId().toString(),
				"upstate", img.getFileName());

	}

	@RequestMapping("/state")
	public void getPreviewMsg(HttpServletRequest request,
			HttpServletResponse response) {

		String messageTitleParameterName = "messageTitle";

		// 设置正确的请求字符集以防止出现乱码
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 设置到客户端输出流中输出数据的字符集
		response.setCharacterEncoding("utf-8");
		// 获取用户的session会话对象

		HttpSession session = request.getSession(true);

		// 获取用户希望抓取数据的名称
		String messageTitle = request.getParameter(messageTitleParameterName);
		// 创建服务器数据推送的消息消费者
		MessageConsumer mconsumer = new MessageConsumer();

		// 由于需要在匿名内部类对象中使用响应对象，因此定义一个final版本
		final HttpServletResponse rsp = response;

		// 调用子类中实现的setHandler方法构建消息的处理对象
		MessageHandler handler = getHandler(request, response);

		// 利用消息消费者尝试获取消息数据
		mconsumer.searchMessage(session.getId(), messageTitle, handler);

	}

	/**
	 * 获取实时消息处理器的回调
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @return 本Servlet使用的实时消息处理器
	 * */
	public MessageHandler getHandler(final HttpServletRequest request,
			final HttpServletResponse response) {
		// TODO Auto-generated method stub
		MessageHandler handler = new MessageHandler() {
			// 实现当获取消息后对消息进行实际处理的回调方法、
			// 回调方法的messageQueue参数描述了当前系统使用的消息等待序列
			// 回调方法的key参数保存了当前获取到的消息发送的目标sessionid和消息名称
			// 回调方法的msg参数保存了实际的消息数据
			@Override
			public void handle(Hashtable<ServerPushKey, Message> messageQueue,
					ServerPushKey key, Message msg) {

				try {
					// 获取针对客户端的字符输出流
					PrintWriter pw = response.getWriter();
					// 将消息字符串直接发送给客户端浏览器
					pw.print(msg.getMsg());
				} catch (Exception ex) {
					// 如果遇到异常则输出异常信息
					ex.printStackTrace();
				}
			}
		};
		// 返回创建好的消息处理器
		return handler;
	}

}
