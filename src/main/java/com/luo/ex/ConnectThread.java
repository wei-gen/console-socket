package com.luo.ex;

import com.luo.ex.client.NettyClient;
import com.luo.ex.server.NettyServer;

public class ConnectThread implements Runnable {
    NettyToolInter nti = null;
    String ip;
    int port;

    public ConnectThread(NettyToolInter nti,String ip,int port) {
        this.nti = nti;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        nti.connect(ip,port);
        if(nti instanceof NettyServer){
            System.out.println("->服务器开启");
        }else if(nti instanceof NettyClient){
            System.out.println("->客户端开启");
        }
        synchronized (nti){
            nti.notifyAll();
        }

    }
}
