package com.gupaoedu.rpc.zk.loadbalance;

import java.util.List;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public interface LoadBalance {

    String selectHost(List<String> repos);

}
