package com.gupaoedu.rpc;

import com.gupaoedu.rpc.anno.RpcAnnotation;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
@RpcAnnotation(IGpHello.class)
public class GpHelloImpl2 implements IGpHello {
    @Override
    public String sayHello(String msg) {
        return "I'm 8081 Node," + msg;
    }
}
