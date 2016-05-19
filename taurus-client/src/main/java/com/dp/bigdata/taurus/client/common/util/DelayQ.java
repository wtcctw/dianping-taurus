package com.dp.bigdata.taurus.client.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 延迟队列
 * User: hongbin03
 * Date: 16/1/23
 * Time: 下午4:44
 * MailTo: hongbin03@meituan.com
 */
public class DelayQ {

    public static interface Cleaner {
        void doSomething() throws InterruptedException;
    }

    private static final Logger LOG = LoggerFactory.getLogger(DelayQ.class);

    private volatile DelayQueue<DelayItem> delayQueue = new DelayQueue<DelayItem>();

    private Thread daemonThread;

    public DelayQ() {

        final Runnable daemonTask = new Runnable() {
            public void run() {
                daemonCheck();
            }
        };

        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Purgatory check service");
        daemonThread.start();
    }

    private void daemonCheck() {

        LOG.info("Purgatory service started.");

        for (; ; ) {
            try {
                DelayItem delayItem = delayQueue.take();
                if (delayItem != null) {
                    // 超时对象处理
                    delayItem.doCleaner();
                }
            } catch (InterruptedException e) {
                LOG.error("Purgatory thread has been interrrupted!", e);
                break;
            }
        }

        LOG.info("Purgatory service stopped.");
    }

    public <T> void addDelayedItem(T item, long timeout, TimeUnit timeUnit, Cleaner cleaner) {
        long nanoTime = TimeUnit.NANOSECONDS.convert(timeout, timeUnit);
        delayQueue.put(new DelayItem<T>(item, nanoTime, cleaner));
    }


    public <T> void removeItem(T item) {
        Iterator<DelayItem> iterator = delayQueue.iterator();
        while(iterator.hasNext()){
            DelayItem di = iterator.next();
            if(di.getItem().equals(item)){
                iterator.remove();
            }
        }
    }

    public static void main(String []a){
        DelayQ delayQ = new DelayQ();
        delayQ.addDelayedItem(1,3000, TimeUnit.MILLISECONDS,new Cleaner() {
            @Override
            public void doSomething() {
                System.out.println("dddd111");
            }
        });

        delayQ.removeItem(1);

        delayQ.addDelayedItem(2,3000, TimeUnit.MILLISECONDS,new Cleaner() {
            @Override
            public void doSomething() {
                System.out.println("dddd222");
            }
        });

        delayQ.removeItem(2);
        ThreadUtils.sleepUnInterrupted(8000);
    }

    static class DelayItem<T> implements Delayed {

        private static final long NANO_ORIGIN = System.nanoTime();

        private static final AtomicLong sequencer = new AtomicLong(0);

        /**
         * Sequence number to break ties FIFO
         */
        private final long sequenceNumber;

        /**
         * The time the task is enabled to execute in nanoTime units
         */
        private final long time;

        private final T item;
        private final Cleaner cleaner;

        public DelayItem(T submit, long timeout, Cleaner cleaner) {
            this.time = now() + timeout;
            this.item = submit;
            this.cleaner = cleaner;
            this.sequenceNumber = sequencer.getAndIncrement();
        }

        public void doCleaner() throws InterruptedException {
            cleaner.doSomething();
        }

        private final static long now() {
            return System.nanoTime() - NANO_ORIGIN;
        }

        public T getItem() {
            return this.item;
        }

        public long getDelay(TimeUnit unit) {
            long d = unit.convert(time - now(), TimeUnit.NANOSECONDS);
            return d;
        }

        public int compareTo(Delayed other) {
            if (other == this)
                return 0;
            if (other instanceof DelayItem) {
                DelayItem x = (DelayItem) other;
                long diff = time - x.time;
                if (diff < 0)
                    return -1;
                else if (diff > 0)
                    return 1;
                else if (sequenceNumber < x.sequenceNumber)
                    return -1;
                else
                    return 1;
            }
            long d = (getDelay(TimeUnit.NANOSECONDS) - other.getDelay(TimeUnit.NANOSECONDS));
            return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
        }

        @Override
        public String toString() {
            return "DelayItem{" +
                    "item=" + item +
                    '}';
        }
    }
}
