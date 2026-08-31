package com.zinxshosting.backend.instance.spring.service;

import com.zinxshosting.backend.instance.Instance;
import com.zinxshosting.backend.instance.config.*;
import com.zinxshosting.backend.instance.controller.loadbalancer.LoadBalancer;
import com.zinxshosting.backend.instance.controller.loadbalancer.traits.ExampleTrait;
import com.zinxshosting.backend.instance.controller.loadbalancer.traits.Trait;
import com.zinxshosting.backend.instance.spring.entity.InstanceConfiguration;
import com.zinxshosting.backend.instance.spring.repository.InstanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class InstanceService {

    @Autowired
    private final InstanceRepository repository;

    @Autowired
    private final TransactionRepository transactionRepository;
    public InstanceConfiguration saveInstanceConfiguration(InstanceConfiguration instance) {
        return repository.save(instance);
    }

    public Optional<InstanceConfiguration> getInstanceConfiguration(Long id) {
        return repository.findByid(id);
    }

    public List<InstanceConfiguration> getInstances(){
        return repository.findAll();
    }

    public int setPassword(String password, Long id) {
        return repository.updatePassword(id, password);
    }

    public int setUsername(String username, Long id) {
        return repository.updateUsername(id, username);
    }

    public int setPort(Integer port, Long id){
        return repository.updatePort(id, port);
    }

    public int setHostname(String hostname, Long id){
        return repository.updateHostname(id, hostname);
    }

    //TODO: Here is where the load balancing algorithms can apply intelligent instance provider functions using the
    // transaction builder to execute transaction lists in order to use the provider api's to build instances.

    public InstanceConfiguration buildInstanceOVHLoadBalancedBasicCPU() throws IOException, InterruptedException {
        TransactionConfigurationService service = new TransactionConfigurationService(transactionRepository);


        if(!service.getTConfigurationByProvider("OVH").isEmpty()) {
            List<TransactionConfiguration> ovhTransactionConfig = service.getTConfigurationByProvider("OVH");

            for (TransactionConfiguration tconfig : ovhTransactionConfig){
                tconfig = (TransactionConfigurationOVH) tconfig;
                if (tconfig.getType().equals("CPU")) {
                    tconfig.getTransactionList().run();
                    //TODO: This should return the CPU clock speed, use it ...
                }
            }
            //Execute the Transactionlist from basicDiscovery in order to get the data from the api

            //Get response as a string, in this case we will assume it contains a list of a list of traits(a list of instances)
            // in some order
          //  String response = (String) transactionList.run();

            //TODO: write functionality to take the response from the transationlist and turn it into a list of lists which
            // contain traits, to be used for load balancing. This is the logic that turns what is in the api's discovery to
            // load balancing rules for choosing an instance.

            List<List<Trait>> instances = new ArrayList<>();

            //then we would create a new load balancer
            LoadBalancer loadBalancer = new LoadBalancer();

            //then we would apply multipliers to each of the traits we are concerned with.

            //keep in mind you likely need to create more trait types to accommodate different things.
            loadBalancer.loadTraitMultiplier(new ExampleTrait("trait", Integer.valueOf(1)), 1f);


            Map<List<Trait>,Float> scores = new HashMap<>();

            //from here you can loop through all of the instances and calculate the score of each
            for(List<Trait> traits : instances) {
                //from here you can simply calculate the score based on all of the provided traits
                float score = loadBalancer.calculateScore(traits);
                scores.put(traits, score);
            }

            //then we would compare all of the scores and get the highest one to construct the instance out of
            List<Float> values = scores.values().stream().toList();
            float biggest = 0f;
            for(Float value : values){
                if(value > biggest){
                    biggest = value;
                }
            }
            //parse the trait lists and find the one that cooresponds to the greatest score, then assign that one
            // to a variable that represents the target instance
            List<Trait> topInstance;
            for(List<Trait> traitInstance : scores.keySet().stream().toList()){
                if(scores.get(traitInstance) >= biggest){
                    topInstance = traitInstance;
                }
            }

            //TODO: Construct an instance configuration out of this list of traits

        }else{
            //we would attempt to make some transactions with the transaction builder and then try again
        }


        return null;
    }

}
