package com.zinxshosting.backend.instance.controller.loadbalancer;

import com.zinxshosting.backend.instance.controller.loadbalancer.traits.Trait;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@NoArgsConstructor
public class LoadBalancer implements Scorer{
    private HashMap<Trait, Float> traitMultipliers;

    public void loadTraitMultiplier(Trait trait, Float multiplier){
        traitMultipliers.put(trait,multiplier);
    }

    public List<Trait> getTraitsAsList(){
        return traitMultipliers.keySet().stream().toList();
    }

    @Override
    public Float calculateScore(List<Trait> traitList) {
        Float score = 0f;
        for(Trait trait : traitList){
            if(traitMultipliers.containsKey(trait)){
                score += (trait.calculateScore() * traitMultipliers.get(trait));
            }
        }
        return score;
    }
}
