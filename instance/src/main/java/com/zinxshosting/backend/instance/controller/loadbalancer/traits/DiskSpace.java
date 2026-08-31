package com.zinxshosting.backend.instance.controller.loadbalancer.traits;

public class DiskSpace extends Trait{
    private boolean ssd;
    private Integer spaceMB;
    public DiskSpace(String name, Integer spaceMB, boolean ssd) {
        super(name, spaceMB);
        this.spaceMB = spaceMB;
        this.ssd = ssd;
    }

    @Override
    public Float calculateScore() {
        if(ssd){
            return Float.valueOf(this.spaceMB * 3);
        }else {
            return Float.valueOf(this.spaceMB*2);
        }
    }
}
