package com.xqmetting.entity;

import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobin {

    private int weight;
    private AtomicInteger current = new AtomicInteger(0);
    private long lastUpdate;

    public void setWeight(int weight) {
        this.weight = weight;
        current.set(0);
    }

    public long increaseCurrent() {
        return current.addAndGet(weight);
    }

    public void sel(int total) {
        current.addAndGet(-1 * total);
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
