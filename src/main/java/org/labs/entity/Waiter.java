package org.labs.entity;

import java.util.concurrent.ThreadLocalRandom;

public class Waiter implements Runnable {

    private final String name;
    private final Table table;

    private final long minDeliveryTime = 1;
    private final long maxDeliveryTime = 5;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public Waiter(String name, Table table) {
        this.name = name;
        this.table = table;
    }

    @Override
    public void run() {
        try {
            while (Programmer.isRunning()) {
                Programmer p = table.takeNextRequester();
                boolean ok = table.givePortion(p);

                if (!ok) {
                    return;
                }
                Thread.sleep(random.nextLong(minDeliveryTime, maxDeliveryTime));
            }
        } catch (InterruptedException ignored) {}
    }

    @Override
    public String toString() {
        return name;
    }
}
