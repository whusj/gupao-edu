package com.gupaoedu.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/12/6</p>
 */
public class DistributedLock implements Lock,Watcher {

    private ZooKeeper zk = null;
    private String ROOT_LOCK = "/locks";//定义根节点
    private String WAIT_LOCK; //等待前一个锁
    private String CURRENT_LOCK; //表示当前的锁

    private CountDownLatch countDownLatch;

    public DistributedLock() {
        try {
            zk = new ZooKeeper("192.168.202.64:2181",4000,this);

            //判断根节点是否存在
            Stat stat = zk.exists(ROOT_LOCK,false);
            if(stat == null){
                zk.create(ROOT_LOCK,"0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }


    /**
     * Acquires the lock.
     * <p>
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     * <p>
     * <p><b>Implementation Considerations</b>
     * <p>
     * <p>A {@code Lock} implementation may be able to detect erroneous use
     * of the lock, such as an invocation that would cause deadlock, and
     * may throw an (unchecked) exception in such circumstances.  The
     * circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     */
    public void lock() {
        if(this.tryLock()){//如果获得锁成功
            System.out.println(Thread.currentThread().getName()+"->"+CURRENT_LOCK+"->获得锁成功");
            return;
        }
        try {
            waitForLock(WAIT_LOCK);//如果没有获得锁，继续等待获得锁
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean waitForLock(String prev) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(prev,true);//监听当前节点的上一个节点
        if(stat != null){
            System.out.println(Thread.currentThread().getName()+"->等待锁" + ROOT_LOCK + "/" + prev + "释放");
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await();
            //watcher触发以后，还需要再次判断当前等待的节点是不是最小的
            System.out.println(Thread.currentThread().getName() + "->获得锁成功");
        }
        return true;
    }

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     * <p>
     * <p>Acquires the lock if it is available and returns immediately.
     * <p>
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * one of two things happens:
     * <p>
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported.
     * </ul>
     * <p>
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
     * lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     * <p>
     * <p><b>Implementation Considerations</b>
     * <p>
     * <p>The ability to interrupt a lock acquisition in some
     * implementations may not be possible, and if possible may be an
     * expensive operation.  The programmer should be aware that this
     * may be the case. An implementation should document when this is
     * the case.
     * <p>
     * <p>An implementation can favor responding to an interrupt over
     * normal method return.
     * <p>
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would
     * cause deadlock, and may throw an (unchecked) exception in such
     * circumstances.  The circumstances and the exception type must
     * be documented by that {@code Lock} implementation.
     *
     * @throws InterruptedException if the current thread is
     *                              interrupted while acquiring the lock (and interruption
     *                              of lock acquisition is supported)
     */
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * Acquires the lock only if it is free at the time of invocation.
     * <p>
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     * <p>
     * <p>A typical usage idiom for this method would be:
     * <pre> {@code
     * Lock lock = ...;
     * if (lock.tryLock()) {
     *   try {
     *     // manipulate protected state
     *   } finally {
     *     lock.unlock();
     *   }
     * } else {
     *   // perform alternative actions
     * }}</pre>
     * <p>
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     *
     * @return {@code true} if the lock was acquired and
     * {@code false} otherwise
     */
    public boolean tryLock() {
        //创建临时有序节点
        try {
            CURRENT_LOCK = zk.create(ROOT_LOCK+"/","0".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + "->" +CURRENT_LOCK+", 尝试竞争锁");
            List<String> childrens = zk.getChildren(ROOT_LOCK,false);//获取根节点下的所有子节点
            SortedSet<String> sortedSet = new TreeSet<String>();//定义一个集合进行排序
            for (String children : childrens){
                sortedSet.add(ROOT_LOCK + "/" + children);
            }
            String firstNode = sortedSet.first();//获得当前所有子节点中最小的节点
            SortedSet<String> lessThenMe = ((TreeSet<String>)sortedSet).headSet(CURRENT_LOCK);
            if(CURRENT_LOCK.equals(firstNode)){//通过当前的节点和子节点中最小的节点进行比较，如果相等，表示获得锁成功
                return true;
            }
            if(!lessThenMe.isEmpty()){
                WAIT_LOCK = lessThenMe.last();//获得比当前节点更小的最后的一个节点，设置给WAIT_LOCK
            }

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     * <p>
     * <p>If the lock is available this method returns immediately
     * with the value {@code true}.
     * If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported; or
     * <li>The specified waiting time elapses
     * </ul>
     * <p>
     * <p>If the lock is acquired then the value {@code true} is returned.
     * <p>
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     * <p>
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.
     * If the time is
     * less than or equal to zero, the method will not wait at all.
     * <p>
     * <p><b>Implementation Considerations</b>
     * <p>
     * <p>The ability to interrupt a lock acquisition in some implementations
     * may not be possible, and if possible may
     * be an expensive operation.
     * The programmer should be aware that this may be the case. An
     * implementation should document when this is the case.
     * <p>
     * <p>An implementation can favor responding to an interrupt over normal
     * method return, or reporting a timeout.
     * <p>
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would cause
     * deadlock, and may throw an (unchecked) exception in such circumstances.
     * The circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     *
     * @param time the maximum time to wait for the lock
     * @param unit the time unit of the {@code time} argument
     * @return {@code true} if the lock was acquired and {@code false}
     * if the waiting time elapsed before the lock was acquired
     * @throws InterruptedException if the current thread is interrupted
     *                              while acquiring the lock (and interruption of lock
     *                              acquisition is supported)
     */
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    /**
     * Releases the lock.
     * <p>
     * <p><b>Implementation Considerations</b>
     * <p>
     * <p>A {@code Lock} implementation will usually impose
     * restrictions on which thread can release a lock (typically only the
     * holder of the lock can release it) and may throw
     * an (unchecked) exception if the restriction is violated.
     * Any restrictions and the exception
     * type must be documented by that {@code Lock} implementation.
     */
    public void unlock() {
        System.out.println(Thread.currentThread().getName()+"->释放锁"+CURRENT_LOCK);
        try {
            zk.delete(CURRENT_LOCK,-1);
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns a new {@link Condition} instance that is bound to this
     * {@code Lock} instance.
     * <p>
     * <p>Before waiting on the condition the lock must be held by the
     * current thread.
     * A call to {@link Condition#await()} will atomically release the lock
     * before waiting and re-acquire the lock before the wait returns.
     * <p>
     * <p><b>Implementation Considerations</b>
     * <p>
     * <p>The exact operation of the {@link Condition} instance depends on
     * the {@code Lock} implementation and must be documented by that
     * implementation.
     *
     * @return A new {@link Condition} instance for this {@code Lock} instance
     * @throws UnsupportedOperationException if this {@code Lock}
     *                                       implementation does not support conditions
     */
    public Condition newCondition() {
        return null;
    }

    public void process(WatchedEvent event) {
        if(this.countDownLatch != null){
            this.countDownLatch.countDown();
        }
    }
}
