package org.labs.entity;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Programmer implements Runnable {
    private final int id;
    private final SpoonPair spoon;
    private final Table table;

    private final static AtomicBoolean isRunning = new AtomicBoolean(true);
    private static final AtomicInteger idCounter = new AtomicInteger(0);
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    private final Object portionMonitor = new Object();

    private volatile boolean hasPortion;
    private int portionCounter = 0;

    private final long minTime2Eat = 1;
    private final long maxTime2Eat = 5;

    private final long minTime2Think = 1;
    private final long maxTime2Think = 5;

    public Programmer(SpoonPair spoon, Table table) {
        this.spoon = spoon;
        this.table = table;

        this.id = idCounter.getAndIncrement();
        this.hasPortion = false;
    }

    public static void stopRunning() {
        isRunning.set(false);
    }

    @Override
    public void run() {
        try {
            while (isRunning.get()) {
                    synchronized (portionMonitor) {
                        while ( ! hasPortion  && isRunning.get()) {
                            this.table.requestPortion(this);
                            portionMonitor.wait();
                        }
                        if ( ! isRunning.get()) {
                            return;
                        }
                    }
                    this.takeSpoon();
                    this.eat();
                    this.putSpoon();
                    discuss();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void eat() throws InterruptedException {
        System.out.println("Programmer-" + id + " starts eating.");
        Thread.sleep(random.nextLong(minTime2Eat, maxTime2Eat));
        hasPortion = false;
        portionCounter += 1;
        System.out.println("Programmer-" + id + " finished eating. Portions: " + portionCounter);
    }

    private void discuss() throws InterruptedException {
        Thread.sleep(random.nextLong(minTime2Think, maxTime2Think));
    }

    public void takeSpoon() {
        spoon.getLeftSpoon().lock();
        spoon.getRightSpoon().lock();
    }

    public void putSpoon() {
        spoon.getLeftSpoon().unlock();
        spoon.getRightSpoon().unlock();
    }

    public int getId() {
        return id;
    }

    public int getPortionCounter() {
        return portionCounter;
    }

    public Object getPortionMonitor() {
        return portionMonitor;
    }

    public void receivePortion() {
        this.hasPortion = true;
    }

    public static boolean isRunning() {
        return isRunning.get();
    }

}
