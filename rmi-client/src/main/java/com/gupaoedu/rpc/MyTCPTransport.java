package com.gupaoedu.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/2</p>
 */
public class MyTCPTransport {
    private String host;
    private int port;

    public MyTCPTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    //创建一个socket连接
    private Socket newSocket(){
        System.out.println("创建一个新的连接");
        Socket socket = null;
        try {
            socket = new Socket(host,port);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException("连接建立失败");
        }
    }

    public Object send(RpcRequest request){
        Socket socket = null;
        try {
            socket = newSocket();
            //获取输出流,将客户端需要调用的远程方法参数request发送给客户端
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();
            //获取输入流，得到服务端的返回结果
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object result = inputStream.readObject();
            inputStream.close();
            outputStream.close();
            return result;
        }catch (Exception e){
            throw new RuntimeException("发起远程调用异常: ",e);
        }finally {
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
