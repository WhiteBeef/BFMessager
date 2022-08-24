package ru.whitebeef.bfserver.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NetworkHandler extends SimpleChannelInboundHandler<String> {

    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channel = ctx.channel();
        System.out.println("Connected with " + channel);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String message) throws Exception {
        if(message.equalsIgnoreCase("exit")) {
            channel.close();
            System.out.println("Successful close connection with " + channel);
            return;
        }
        System.out.println("id" + Long.parseLong(channel.id().asShortText().toUpperCase(), 16) + ": " + message);
    }
}
