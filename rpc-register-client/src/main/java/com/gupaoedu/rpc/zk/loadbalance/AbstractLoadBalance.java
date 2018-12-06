package com.gupaoedu.rpc.zk.loadbalance;

import java.util.List;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectHost(List<String> repos) {
        if(repos == null || repos.size() == 0){
            return null;
        }
        if(repos.size() == 1){
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(List<String> repos);
}
