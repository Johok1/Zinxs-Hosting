package com.zinxshosting.backend.instance.controller.loadbalancer.traits;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Trait implements Scoreable {
    private String name;
    private Object value;
    public Trait(String name, Object value){
        this.name = name;
        this.value = value;
    }


}
