package com.gupaoedu.dubbo;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/10</p>
 */
public class HelloServiceImpl1 implements IHelloService {
    @Override
    public String sayHello(String msg) {
        return "Hello1: "+msg;
    }
}
