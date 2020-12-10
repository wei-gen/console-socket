package com.luo.ex.server;

import com.luo.ex.NettyTool;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛获取学习源码｝
 * Create by fuzhengwei on 2019
 */
public class NettyServer extends NettyTool {
    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private ServerBootstrap b = new ServerBootstrap();
    private List<Channel> channels = new ArrayList<>();

    public NettyServer() {
        init();
    }

    public void init() {
        //配置服务端NIO线程组
        parentGroup = new NioEventLoopGroup();
        childGroup = new NioEventLoopGroup();
        b.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)    //非阻塞模式
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ServerChannelInitializer(this));
    }

    @Override
    public void connect(String host, int port) {
        try {
            ChannelFuture f = b.bind(port).sync();
            new Thread(()->{
                try {
                    // 这里会进行channel监听，阻塞当前线程。
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (channels.size() == 0) {
            return;
        }
        for (Channel c : channels) {
            c.close();
        }
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }

    @Override
    public void setChannel(Channel channel) {
        channels.add(channel);
    }

    @Override
    public void send(String msg) {
        if (channels.size() == 0) {
            return;
        }
        for (Channel c : channels) {
            if (!c.isActive()) {
                continue;
            }
            c.writeAndFlush(msg);
        }
    }
}
