package ru.whitebeef.bfclient.commands;

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

public class ConnectCommand implements Command {

    public static final boolean EPOLL = Epoll.isAvailable();

    @Override
    public String getLabel() {
        return "connect";
    }

    @Override
    public String getDescription() {
        return "connect to the server";
    }

    @Override
    public void execute(String[] args) throws Exception {

        if(args.length < 1) {
            writeErrorMessage();
            return;
        }

        args = args[0].split(":");

        if(args.length < 2) {
            writeErrorMessage();
            return;
        }

        String host = args[0];

        if(!args[0].matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) {
            writeErrorMessage();
            return;
        }

        if(!args[1].matches("^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$")) {
            writeErrorMessage();
            return;
        }

        int port = Integer.parseInt(args[1]);

        connect(host, port);

    }

    private void writeErrorMessage() {
        System.out.println("You must write host and port like this example: 'connect 127.0.0.1:8000'");
    }

    private void connect(String host, int port) throws Exception {
        EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
        try {
            Channel channel = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8)).addLast(new StringEncoder(StandardCharsets.UTF_8));
                        }
                    }).connect(host, port).sync().channel();
            System.out.println("Successful connected to " + channel.remoteAddress());
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

}
