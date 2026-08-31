package com.zinxshosting.backend.instance.controller.loadbalancer;

import com.zinxshosting.backend.instance.controller.loadbalancer.traits.Trait;
import com.zinxshosting.backend.instance.spring.entity.InstanceConfiguration;

import java.util.List;

public interface Scorer {
    Float calculateScore(List<Trait> traitList);
}
