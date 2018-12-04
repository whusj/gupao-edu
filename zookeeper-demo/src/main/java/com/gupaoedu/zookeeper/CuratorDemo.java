package com.gupaoedu.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/4</p>
 */
public class CuratorDemo {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.202.64:2181")
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("curator").build();
        curatorFramework.start();

        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/mic/node1","1".getBytes());
        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath("/mic/node1");
        curatorFramework.setData().withVersion(stat.getVersion()).forPath("/mic/node1","xx".getBytes());
        curatorFramework.delete().deletingChildrenIfNeeded().forPath("mic/node1");

        curatorFramework.close();

    }
}
