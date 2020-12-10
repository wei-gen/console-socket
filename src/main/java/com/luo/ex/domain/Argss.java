package com.luo.ex.domain;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * 接受命令行参数
 */
public class Argss {

    @Parameter(names="-m",description = "client or server")
    public String mode;

    @Parameter(names = "-i",description = "ip addr")
    public String ip;

    @Parameter(names = "-p",description = "port")
    public int port;

    @Parameter(names={"-ca","-calias"},description = "client alias")
    public String alias;

    //标记是否在传输
    public boolean isTransfer;

    public static Argss parse(String [] argv){
        Argss args=new Argss();
        JCommander cmd = JCommander
                .newBuilder().addObject(args).build();
        cmd.parse(argv);
        args.isTransfer = true;
        return args;
    }
}
