package com.gupaoedu.rpc;

import java.lang.reflect.Proxy;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/2</p>
 */
public class RpcClientProxy {
    public <T> T clientProxy(final Class<T> interfaceCls,final String host,final int port){

        //使用到了动态代理
        return (T)Proxy.newProxyInstance(interfaceCls.getClassLoader(),new Class[]{interfaceCls},new RemoteInvocationHandler(host,port));
    }
}
