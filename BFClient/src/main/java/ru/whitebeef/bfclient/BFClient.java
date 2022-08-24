package ru.whitebeef.bfclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BFClient {

    public static final boolean EPOLL = Epoll.isAvailable();

    public BFClient() throws Exception {
        start();
    }

    private void start() throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {

            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8)).addLast(new StringEncoder(StandardCharsets.UTF_8));
                        }
                    }).connect("127.0.0.1", 8000).sync().channel();
            System.out.println("Successful connected to " + channel);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                if(line.isEmpty())
                    continue;
                channel.writeAndFlush(line);
                if(line.startsWith("exit")) {
                    System.out.println("Waiting for disconnect..");
                    channel.closeFuture().syncUninterruptibly();
                    System.out.println("Disconnected!");
                    break;
                }
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        try {
            new BFClient();
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Bye bye!");
        }
    }


}
