package ru.whitebeef.bfserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ru.whitebeef.bfserver.handlers.NetworkHandler;

import java.nio.charset.StandardCharsets;


public class BFServer {

    private static final boolean EPOLL = Epoll.isAvailable();

    private BFServer() throws Exception {

        start();
    }

    public static void main(String[] args) {
        try {
            new BFServer();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void start() throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

        try {
            Channel channel = new ServerBootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {

                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline()
                                    .addLast(new StringDecoder(StandardCharsets.UTF_8)).addLast(new StringEncoder(StandardCharsets.UTF_8))
                                    .addLast(new NetworkHandler());

                        }
                    }).bind(8000).sync().channel();

            System.out.println("Successful start server at " + channel);

            channel.closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
