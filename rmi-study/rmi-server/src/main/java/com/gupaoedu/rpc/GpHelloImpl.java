package com.gupaoedu.rpc;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/2</p>
 */
public class GpHelloImpl implements IGpHello {
    public String sayHello(String msg) {
        return "Hello, " + msg;
    }
}
