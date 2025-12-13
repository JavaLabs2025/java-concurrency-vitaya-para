package org.labs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.labs.entity.LunchManager;
import org.labs.entity.Programmer;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScenarioTest {
    @Test
    @Timeout(10)
    void testSingleProgrammerScenario() throws Exception {
        int programmers = 1;
        int spoons = 2;
        int waiters = 1;
        int portions = 10;

        LunchManager lunch = new LunchManager(programmers, spoons, waiters);
        lunch
            .start(portions)
            .awaitCompletion(30, TimeUnit.SECONDS);

        Programmer p = lunch.getProgrammers().get(0);

        assertEquals(portions, p.getPortionCounter(), "One programmer must eat all the portions");
        assertEquals(0, lunch.getPortionsLeft().get(), "Portions should be 0 servings left.");
    }
}
