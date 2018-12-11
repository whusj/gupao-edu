package com.gupaoedu.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/11</p>
 */
public class ClusterClient {
    public static void main(String[] args) throws InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-client.xml");
        for (int i = 0; i < 10; i++) {
            //得到IHelloService服务的远程代理对象
            IHelloService helloService1 = (IHelloService)context.getBean("helloService");
            System.out.println(helloService1.sayHello("Jack"));
            Thread.sleep(1000);
        }
    }
}
