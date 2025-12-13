package org.labs;


import org.junit.jupiter.api.Test;
import org.labs.entity.LunchManager;
import org.labs.entity.Programmer;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FairnessTest {

    @Test
    void testFairDistributionWithSevenProgrammers() throws Exception {
        int programmers = 7;
        int spoons = 7;
        int waiters = 2;
        int portions = 1_000;

        LunchManager lunch = new LunchManager(programmers, spoons, waiters);

        lunch
            .start(portions)
            .awaitCompletion(30, TimeUnit.SECONDS);

        int totalEaten = lunch.getProgrammers()
                .stream()
                .mapToInt(p -> p.getPortionCounter())
                .sum();

        assertEquals(portions, totalEaten, "The total number of servings must be equal to the specified number");

        double avg = totalEaten / (double) programmers;
        double minAllowed = avg * 0.9;
        double maxAllowed = avg * 1.1;

        for (Programmer p : lunch.getProgrammers()) {
            int eaten = p.getPortionCounter();

            assertTrue(eaten >= minAllowed,String.format("Programmer %d ate too little: %d < %.1f",p.getId(), eaten, minAllowed));
            assertTrue(eaten <= maxAllowed,String.format("Пользователь %d ate too much: %d > %.1f",p.getId(), eaten, maxAllowed));
        }
    }
}