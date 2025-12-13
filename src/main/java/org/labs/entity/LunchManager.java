package org.labs.entity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class LunchManager {
    private final Table table;
    private final ExecutorService exec;

    public LunchManager(int programmers, int spoons, int waiters) {
        this.table = new Table(programmers, spoons, waiters);
        exec = Executors.newFixedThreadPool(programmers + waiters);
    }

    public LunchManager start(int totalPortions) {
        table.setTotalPortions(totalPortions);

        for (Waiter w : table.getWaiters())
            exec.submit(w);

        for (Programmer p : table.getProgrammers())
            exec.submit(p);

        return this;
    }

    public void awaitCompletion(long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException {
        exec.shutdown();
        if (!exec.awaitTermination(timeout, unit))
            throw new TimeoutException("Lunch didn't finish in time");
    }

    public List<Programmer> getProgrammers() {
        return List.of(table.getProgrammers());
    }

    public AtomicInteger getPortionsLeft() {
        return table.portionsLeft;
    }
}
