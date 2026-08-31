package com.zinxshosting.backend.instance.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionConfigurationService {

    @Autowired
    private final TransactionRepository repository;

    public TransactionConfiguration saveTransactionConfiguration(TransactionConfiguration t_config) {
        return repository.save(t_config);
    }

    public Optional<TransactionConfiguration> getTConfigurationByID(Long id) {
        return repository.findById(id);
    }

    public List<TransactionConfiguration> getTConfigurationByType(String type){
        return repository.findByType(type);
    }

    public List<TransactionConfiguration> getTConfigurationByProvider(String provider){
        return repository.findByProvider(provider);
    }


}
