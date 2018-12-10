package com.gupaoedu.rpc.zk;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public interface IRegisterCenter {

    /**
     * 注册服务名称和服务地址
     * @param serviceName
     * @param serviceAddress
     */
    public void register(String serviceName, String serviceAddress);
}
