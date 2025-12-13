package org.labs.entity;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Table {
    private final Programmer[] programmers;
    private final Spoon[] spoons;
    private final Waiter[] waiters;

    private final PriorityBlockingQueue<Programmer> portionsAskQueue;
    public final AtomicInteger portionsLeft = new AtomicInteger(0);

    public Table(int programmerCount, int spoonCount, int waiterCount) {
        programmers = new Programmer[programmerCount];
        spoons = new Spoon[spoonCount];
        waiters = new Waiter[waiterCount];

        for (int i = 0; i < spoonCount; i++) {
            this.spoons[i] = new Spoon();
        }

        for (int i = 0; i < programmerCount; i++) {
            SpoonPair pair = new SpoonPair(this.spoons[i], this.spoons[(i + 1) % programmerCount]);
            this.programmers[i] = new Programmer(pair, this);
        }

        this.portionsAskQueue = new PriorityBlockingQueue<>(
                programmerCount,
                Comparator.comparingInt(Programmer::getPortionCounter)
        );

        for (int i = 0; i < waiterCount; i++) {
            this.waiters[i] = new Waiter("Waiter-" + i, this);
        }
    }

    void requestPortion(Programmer programmer) {
        portionsAskQueue.put(programmer);
    }

    Programmer takeNextRequester() throws InterruptedException {
        return portionsAskQueue.take();
    }

    public Programmer[] getProgrammers() {
        return programmers;
    }

    public Waiter[] getWaiters() {
        return waiters;
    }

    public void setTotalPortions(int count) {
        portionsLeft.set(count);
    }

    boolean givePortion(Programmer p) {
        if (portionsLeft.get() <= 0) {

            portionsAskQueue.clear();
            Programmer.stopRunning();
            System.out.println("No portions left. Stopping all.");

            for (Programmer prog : programmers) {
                synchronized (prog.getPortionMonitor()) {
                    prog.getPortionMonitor().notifyAll();
                }
            }

            return false;
        }

        portionsLeft.getAndDecrement();
        synchronized (p.getPortionMonitor()) {
            p.receivePortion();
            p.getPortionMonitor().notify();
        }
        return true;
    }
}
