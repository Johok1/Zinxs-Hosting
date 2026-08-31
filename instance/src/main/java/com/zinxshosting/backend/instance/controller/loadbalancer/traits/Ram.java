package com.zinxshosting.backend.instance.controller.loadbalancer.traits;

public class Ram extends Trait{

    private Integer spaceMB;

    public Ram(String name, Integer spaceMB) {
        super(name, spaceMB);
        this.spaceMB = spaceMB;
    }

    /**
     * Judge based on how many gigabites of ram there are
     * @return
     */
    @Override
    public Float calculateScore() {
        return Float.valueOf(this.spaceMB/1000);
    }
}
