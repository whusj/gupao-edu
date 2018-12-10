package com.gupaoedu.rpc;

import com.gupaoedu.rpc.zk.IServiceDiscovery;

import java.lang.reflect.Proxy;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class RpcClientProxy {
    private IServiceDiscovery serviceDiscovery;

    public RpcClientProxy(IServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T clientProxy(final Class<T> interfaceCls,String version){
        //使用到了动态代理
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class[]{interfaceCls},new RemoteInvocationHandler(serviceDiscovery,version));
    }


}
