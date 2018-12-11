package com.gupaoedu.dubbo;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/11</p>
 */
public class Main {
    /**
     * 使用Dubbo的启动方式，但要加载容器(默认使用spring容器)
     * *.xml文件必须放在resources/META-INF/spring目录下面才可以读到文件
     * @param args
     */
    public static void main(String[] args) {
        com.alibaba.dubbo.container.Main.main(new String[]{"spring","log4j"});
    }
}
