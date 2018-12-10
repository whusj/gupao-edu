package com.gupaoedu.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.awt.*;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/4</p>
 */
public class CuratorWatcherDemo {
    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().connectString("192.168.202.64:2181")
                .sessionTimeoutMs(4000).retryPolicy(new ExponentialBackoffRetry(1000,3))
                .namespace("curator").build();
        curatorFramework.start();

        //当前节点的创建和删除事件监听
        addListenerWithNodeCache(curatorFramework,"/mic");
        //子节点的增加、修改、删除的事件监听
        addListenerWithPathChildCache(curatorFramework,"/mic");
        //综合节点监听事件
        addListenerWithTreeCache(curatorFramework,"/mic");

        System.in.read();
    }

    private static void addListenerWithTreeCache(CuratorFramework curatorFramework, String path) throws Exception {
        TreeCache treeCache = new TreeCache(curatorFramework,path);
        TreeCacheListener treeCacheListener = new TreeCacheListener() {
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                System.out.println(event.getType()+" -> " + event.getData().getPath());
            }
        };
        treeCache.getListenable().addListener(treeCacheListener);
        treeCache.start();

    }

    private static void addListenerWithPathChildCache(CuratorFramework curatorFramework, String path) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener pathChildrenCacheListener = new PathChildrenCacheListener() {
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType()+" -> " + event.getData().getPath());
            }
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();

    }

    private static void addListenerWithNodeCache(CuratorFramework curatorFramework, String path) throws Exception {
        final NodeCache nodeCache = new NodeCache(curatorFramework,path,false);
        NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("Receive Event: " + nodeCache.getCurrentData().getPath());
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }
}
