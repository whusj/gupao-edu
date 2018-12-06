package com.gupaoedu.rpc.zk;

import com.gupaoedu.rpc.zk.loadbalance.LoadBalance;
import com.gupaoedu.rpc.zk.loadbalance.RandomLoadBalance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class ServiceDiscoveryImpl implements IServiceDiscovery {

    List<String> repos = new ArrayList<>();
    private String address;
    private CuratorFramework curatorFramework;

    public ServiceDiscoveryImpl(String address) {
        this.address = address;
        curatorFramework = CuratorFrameworkFactory.builder().connectString(address)
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,10))
                .build();
        curatorFramework.start();
    }

    /**
     * 根据请求的地址，获得对应的调用地址
     *
     * @param serviceName
     * @return
     */
    @Override
    public String discover(String serviceName) {
        String path = ZkConfig.ZK_REGISTER_PATH + "/" +serviceName;
        try {
            repos = curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException("获取子节点异常: " + e);
        }

        //动态发现服务节点的变化
        registerWatcher(path);

        //负载均衡机制
        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.selectHost(repos);//返回调用的服务地址
    }

    private void registerWatcher(final String path) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                repos = curatorFramework.getChildren().forPath(path);
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("注册PathChild Watcher 异常" + e);
        }
    }
}
