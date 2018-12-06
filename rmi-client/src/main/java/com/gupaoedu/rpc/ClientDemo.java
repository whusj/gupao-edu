package com.gupaoedu.rpc;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/2</p>
 */
public class ClientDemo {
    public static void main(String[] args) {
        RpcClientProxy rpcClientProxy = new RpcClientProxy();
        IGpHello hello = rpcClientProxy.clientProxy(IGpHello.class,"localhost",8888);
        System.out.println(hello.sayHello("whusj"));
    }
}
