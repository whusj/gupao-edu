package com.gupaoedu.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/3</p>
 */
public class ConnectionDemo {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.202.64:2181", 4000, new Watcher() {
                public void process(WatchedEvent event) {
                    if(Event.KeeperState.SyncConnected == event.getState()){
                        //如果收到了服务端的响应事件，则说明连接成功
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            System.out.println(zooKeeper.getState());

            //添加节点
            zooKeeper.create("/zk-persis","0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            Thread.sleep(1000);
            Stat stat = new Stat();

            //得到当前节点的值
            byte[] bytes = zooKeeper.getData("/zk-persis",null,stat);
            System.out.println(new String(bytes));

            //修改节点值
            zooKeeper.setData("/zk-persis","1".getBytes(),stat.getVersion());

            //得到当前节点的值
            bytes = zooKeeper.getData("/zk-persis",null,stat);
            System.out.println(new String(bytes));

            //删除当前节点
            zooKeeper.delete("/zk-persis",stat.getVersion());

            zooKeeper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
    }
}
