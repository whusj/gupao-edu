package com.gupaoedu.rmi.rpc;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/2</p>
 */
public class ServerDemo {
    public static void main(String[] args) {
        IGpHello iGpHello = new GpHelloImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.publisher(iGpHello,8888);
        System.out.println("服务已启动...");
    }
}
