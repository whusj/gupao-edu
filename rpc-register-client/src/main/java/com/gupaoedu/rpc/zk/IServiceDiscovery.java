package com.gupaoedu.rpc.zk;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public interface IServiceDiscovery {

    /**
     * 根据请求的地址，获得对应的调用地址
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
