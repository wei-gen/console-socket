package com.luo.ex;

import com.luo.ex.client.NettyClient;
import com.luo.ex.domain.Argss;
import com.luo.ex.domain.ModePaConstant;
import com.luo.ex.server.NettyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 主类
 */

public class Main {
    public static void main(String[] args) throws IOException {
        HashMap<String,NettyToolInter> nettyToolInters=new HashMap<>();
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        while (true) {
            //有 > 表示输入的是指令
            System.out.print(">");
            String[] s = br.readLine().split(" ");
            Argss ca = Argss.parse(s);
            NettyToolInter nti = null;
            switch (ca.mode) {
                case ModePaConstant.CLIENT:
                    //客户端  -m client -ca aaa -i 127.0.0.1 -p 7397
                    nti = new NettyClient(ca.alias);
                    //开启链接
                    new Thread(new ConnectThread(nti, ca.ip, ca.port)).start();
                    //让主线程等待
                    synchronized (nti) {
                        try {
                            nti.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //没有-ca直接进入命令行
                    if(ca.alias == null){
                        interaction(nti);
                    }else{
                        //放入map里面保存
                        nettyToolInters.put(ca.alias,nti);
                    }
                    break;
                case ModePaConstant.SERVER:
                    //服务端  -m server -p 7397
                    nti = new NettyServer();
                    //开启链接
                    new Thread(new ConnectThread(nti, ca.ip, ca.port)).start();
                    //让主线程等待
                    synchronized (nti) {
                        try {
                            nti.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    interaction(nti);
                    break;
                case "cdc":
                    //进入客户端交互模式 -m cdc
                    NettyToolInter tem = nettyToolInters.get(ca.alias);
                    interaction(tem);
                    break;
                default:
                    System.out.println("->没有这个指令");
                    break;
            }

        }
    }

    //进入交互命令行
    public static void interaction(NettyToolInter nti){
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        while (true) {
            System.out.print("-：");
            String data = null;
            try {
                data = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //不能传空格或空值
            if (data.trim().isEmpty()) {
                continue;
            }
            //关闭网络连接
            if (data.trim().equalsIgnoreCase("close")) {
                nti.close();
                break;
            }
            //客户端的退出交互命令行
            if(data.trim().equalsIgnoreCase("quit")){
                break;
            }
            //发送消息
            nti.send(data);
        }
    }
}
