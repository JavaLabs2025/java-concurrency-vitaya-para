package org.labs.entity;

/**
 * left spoon more that right spoon
 */
public class SpoonPair {
    private final Spoon leftSpoon;
    private final Spoon rightSpoon;

    public SpoonPair(Spoon firstSpoon, Spoon secondSpoon) {
        if (firstSpoon == null || secondSpoon == null) {
            throw new IllegalArgumentException("Spoons cannot be null");
        }

        if (firstSpoon.getId() > secondSpoon.getId()) {
            leftSpoon = secondSpoon;
            rightSpoon = firstSpoon;
        }
        else {
            leftSpoon = firstSpoon;
            rightSpoon = secondSpoon;
        }
    }

    public Spoon getLeftSpoon() {
        return leftSpoon;
    }

    public Spoon getRightSpoon() {
        return rightSpoon;
    }
}
