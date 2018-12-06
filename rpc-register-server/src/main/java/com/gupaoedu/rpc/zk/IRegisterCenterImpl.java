package com.gupaoedu.rpc.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.server.ZooKeeperCriticalThread;

import javax.sound.midi.Soundbank;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class IRegisterCenterImpl implements IRegisterCenter {

    private CuratorFramework curatorFramework;

    {
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(4000)
                .retryPolicy(new ExponentialBackoffRetry(1000,10)).build();
        curatorFramework.start();
    }

    /**
     * 注册服务名称和服务地址
     *
     * @param serviceName
     * @param serviceAddress
     */
    @Override
    public void register(String serviceName, String serviceAddress) {
        //注册相应的服务
        String servicePath = ZkConfig.ZK_REGISTER_PATH + "/" + serviceName;
        System.out.println("servicePath: " + servicePath);
        try {
            //判断/registrys/serviceName是否存在，不存在则创建
            if(curatorFramework.checkExists().forPath(servicePath) == null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath,"0".getBytes());
            }
            String addressPath = servicePath + "/" + serviceAddress;
            System.out.println("addressPath: " + addressPath);
            String rsNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL)
                    .forPath(addressPath,"0".getBytes());
            System.out.println("服务注册成功: " + rsNode);

        }catch (Exception e){

        }
    }
}
