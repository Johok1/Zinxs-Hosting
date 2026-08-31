package com.zinxshosting.backend.instance.controller.loadbalancer.traits;

public class CPUClockSpeed extends Trait{

    private Double speed;

    public CPUClockSpeed(String name, Double speed) {
        super(name, speed);
        this.speed = speed;
    }

    /**
     * In the future, higher clock speeds should give exponentially higher score
     * @return
     */
    @Override
    public Float calculateScore() {
        return speed.floatValue()*10;
    }
}
