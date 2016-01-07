package com.netty.demo;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		
		buf.readBytes(bytes);
		
		String message = new String(bytes, "UTF-8");
		String body = message.substring(0, message.length()-System.getProperty("line.separator").length());
		
		System.out.println("The TimeServer receive the order is:"+body+" the counter is:"+(++counter));
		
		String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(message)?
				             new Date(System.currentTimeMillis()).toString()+System.getProperty("line.separator"):"BAD ORDER";
				             
		ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		ctx.close();
	}

}
