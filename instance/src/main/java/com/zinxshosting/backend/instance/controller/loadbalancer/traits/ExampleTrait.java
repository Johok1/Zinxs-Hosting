package com.zinxshosting.backend.instance.controller.loadbalancer.traits;

public class ExampleTrait extends Trait{
    private String name;
    private Integer value;

    /**
     *
     * @param name
     * @param value
     */
    public ExampleTrait(String name, Integer value) {
        super(name, value);
        this.name = name;
        this.value = value;
    }

    /**
     * Takes the Integer value registered with the trait and
     * multiplies it by .2 and returns that Float as the score
     * @return the score
     */
    @Override
    public Float calculateScore() {
        return (float) (Float.valueOf(this.value) *0.2);
    }
}
