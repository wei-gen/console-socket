package com.luo.ex.client;

import com.luo.ex.NettyTool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛获取学习源码｝
 * Create by fuzhengwei on 2019
 */
public class NettyClient extends NettyTool {
    private Channel channel = null;
    private EventLoopGroup workerGroup;
    private Bootstrap b = null;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private String alias;
    public NettyClient(String alias){
        this.alias = alias;
        init();
    }

    public void init() {
        b = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        b.group(workerGroup)
                .option(ChannelOption.AUTO_READ, true)
                .channel(NioSocketChannel.class)
                .handler(new ClientChannelInitializer(this));
    }
    @Override
    public void connect(String host, int port) {
        ChannelFuture f = b.connect(host, port);
        new Thread(()->{
            try {
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //断线重连
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    final EventLoop loop = channelFuture.channel().eventLoop();
                    loop.schedule(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("not connect service");
                            connect(host, port);
                        }
                    }, 1L, TimeUnit.SECONDS);
                } else {
                    channel = channelFuture.channel();
                    //System.out.println("connected");
                }
            }
        });
    }

    @Override
    public void close() {
        channel.close();
        workerGroup.shutdownGracefully();
    }
    @Override
    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    @Override
    public void send(String msg) {
        if (!channel.isActive()) {
            System.out.println("not send");
            return;
        }
        channel.writeAndFlush(msg);
    }

}
