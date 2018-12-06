package com.gupaoedu.rpc;

import com.gupaoedu.rpc.zk.IRegisterCenter;
import com.gupaoedu.rpc.zk.IRegisterCenterImpl;

import java.io.IOException;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class LBServerDemo {
    public static void main(String[] args) throws IOException {
        IGpHello iGpHello = new GpHelloImpl();
        IRegisterCenter registerCenter = new IRegisterCenterImpl();
        RpcServer rpcServer = new RpcServer(registerCenter,"127.0.0.1:8080");
        rpcServer.bind(iGpHello);
        rpcServer.publisher();
        System.in.read();
    }
}
