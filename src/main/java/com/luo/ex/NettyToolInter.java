package com.luo.ex;

import io.netty.channel.Channel;

public interface NettyToolInter {

    public void connect(String host, int port);

    public void close();

    public void setChannel(Channel channel);

    public void send(String msg);
}
