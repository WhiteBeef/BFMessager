package ru.whitebeef.bfserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ru.whitebeef.bfserver.handlers.NetworkHandler;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class BFServer {

    private static final boolean EPOLL = Epoll.isAvailable();

    private BFServer(int port) throws Exception {
        start(port);
    }

    public static void main(String[] args) {
        try {
            System.out.println("Enter startup port");

            int port = -1;

            do {
                Scanner sc = new Scanner(System.in);
                System.out.println("Please, enter number from 0-65535");

                if(sc.hasNextInt()) {
                    port = sc.nextInt();
                }
                else {
                    System.out.println("It's not correct port!");
                }

                sc.nextLine();
            } while(port < 0 || port > 65535);

            new BFServer(8000);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void start(int port) throws Exception {
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
                    }).bind(port).sync().channel();

            System.out.println("Successful start server at " + channel.localAddress());

            channel.closeFuture().syncUninterruptibly();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }

}
