package org.labs.entity;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Spoon {
    private final int id;
    private final ReentrantLock lock;

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public Spoon() {
        this.id = idCounter.getAndIncrement();
        lock = new ReentrantLock(true);
    }

    public int getId() {
        return id;
    }

    public void lock() {
        System.out.println("Spoon-" + id + " locked.");
        lock.lock();
    }

    public void unlock() {
        System.out.println("Spoon-" + id + " unlocked.");
        lock.unlock();
    }
}
