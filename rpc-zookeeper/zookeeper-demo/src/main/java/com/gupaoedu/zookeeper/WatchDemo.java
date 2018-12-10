package com.gupaoedu.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/3</p>
 */
public class WatchDemo {
    public static void main(String[] args) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            final ZooKeeper zooKeeper = new ZooKeeper("192.168.202.64:2181", 4000, new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println("默认事件: " + event.getPath() + " -> " + event.getState());
                    if(Event.KeeperState.SyncConnected == event.getState()){
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();

            //添加节点
            zooKeeper.create("/zk-persis","1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            Thread.sleep(1000);
            Stat stat = new Stat();

            //exists getData getChildren
            //通过exists绑定事件
            stat = zooKeeper.exists("/zk-persis", new Watcher() {
                public void process(WatchedEvent event) {
                    System.out.println(event.getType()+" -> " + event.getPath());
                    try {
                        //再一次去绑定事件
                        zooKeeper.exists(event.getPath(),true);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            //通过修改的事务类型操作来触发监听事件
            stat = zooKeeper.setData("/zk-persis","2".getBytes(),stat.getVersion());

            Thread.sleep(1000);

            //删除当前节点
            zooKeeper.delete("/zk-persis",stat.getVersion());

            zooKeeper.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
    }
}
