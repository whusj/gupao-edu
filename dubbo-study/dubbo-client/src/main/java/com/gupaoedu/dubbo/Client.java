package com.gupaoedu.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/11</p>
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-client.xml");

        //得到IDemoService服务的远程代理对象
        IDemoService demoService = (IDemoService)context.getBean("demoService");
        //得到IHelloService服务的远程代理对象
        IHelloService helloService = (IHelloService)context.getBean("helloService");
        System.out.println(demoService.protocolDemo("hession"));
        System.out.println(helloService.sayHello("Jack"));

    }
}
