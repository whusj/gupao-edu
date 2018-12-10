package com.gupaoedu.dubbo;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/10</p>
 */
public class DemoServiceImpl implements IDemoService {
    @Override
    public String protocolDemo(String msg) {
        return "I'm Protocol Demo:"+msg;
    }
}
