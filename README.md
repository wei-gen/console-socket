# console-socket
这是一个控制台程序,没有用户界面  
现在只有两个功能,开启socket客户端和socket服务端  
使用命令进行选择性开启客户端还是服务端  
-m 指令  
-m client 开启一个socket客户端  
-m server 开启一个socket服务端  
-m cdc -ca xxx(客户端名称) 表示进入客户端名称为xxx的数据传输命令行  
-ca 指令  
-ca 客户端名称
有-ca表示开启的客户端不会立马进入数据传输命令行  
没有-ca表示开启客户端并立马进入数据传输命令行
-i 指令
-i 127.0.0.1 设置socket ip地址  
-p 指令  
-p 3333 服务端设置监听端口，客户端设置目标端口  

## 例子
-m client -ca aaa -i 127.0.0.1 -p 7397  
开启一个名为aaa的客户端  
-m cdc -ca aaa  
进入数据传输命令行  
....  
输入为内容后  
quit  
退出数据传输命令行  
