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
		handler.initService(request, response, session);

		// 获取用户希望抓取数据的名称
		String messageTitle = request.getParameter(messageTitleParameterName);
		// 创建服务器数据推送的消息消费者
		MessageConsumer mconsumer = new MessageConsumer();

		// 由于需要在匿名内部类对象中使用响应对象，因此定义一个final版本
		final HttpServletResponse rsp = response;

		// 调用子类中实现的setHandler方法构建消息的处理对象
		MessageHandler mHandler = handler.getHandler(request, response);

		// 利用消息消费者尝试获取消息数据
		mconsumer.searchMessage(session.getId(), messageTitle, mHandler);
	}

}
