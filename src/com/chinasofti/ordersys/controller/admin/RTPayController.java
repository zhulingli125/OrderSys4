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

		// 将当前会话加入到实时消息等待列表
		pays.add(session.getId());
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

	public MessageHandler getHandler(HttpServletRequest request,
			HttpServletResponse response) {
		// 设置请求字符集
		response.setCharacterEncoding("utf-8");
		// TODO Auto-generated method stub
		// 尝试处理实时消息
		try {
			// 获取针对客户端的文本输出流
			final PrintWriter out = response.getWriter();
			// 创建消息处理器
			MessageHandler handler = new MessageHandler() {

				@Override
				public void handle(
						Hashtable<ServerPushKey, Message> messageQueue,
						ServerPushKey key, Message msg) {
					// 将消息的文本内容直接发送给客户端
					out.print(msg.getMsg());
					// TODO Auto-generated method stub

				}
			};
			// 返回创建好的消息处理器
			return handler;
			// 捕获创建消息处理器时产生的异常
		} catch (Exception ex) {
			// 输出异常信息
			ex.printStackTrace(); 
			// 返回空的消息处理器
			return null;
		}
	}

}
