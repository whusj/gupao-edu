package com.gupaoedu.rpc;

import com.gupaoedu.rpc.zk.IServiceDiscovery;
import com.gupaoedu.rpc.zk.ServiceDiscoveryImpl;
import com.gupaoedu.rpc.zk.ZkConfig;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class ClientDemo {
    public static void main(String[] args) throws InterruptedException {
        IServiceDiscovery serviceDiscovery = new ServiceDiscoveryImpl(ZkConfig.CONNECTION_STR);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(serviceDiscovery);
        for (int i = 0; i < 10; i++) {
            IGpHello hello = rpcClientProxy.clientProxy(IGpHello.class,null);
            System.out.println(hello.sayHello("mic"));
            Thread.sleep(1000);
        }
    }
}
