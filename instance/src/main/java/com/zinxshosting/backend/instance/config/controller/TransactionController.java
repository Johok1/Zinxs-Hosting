package com.zinxshosting.backend.instance.config.controller;

import com.zinxshosting.backend.instance.config.TransactionConfiguration;
import com.zinxshosting.backend.instance.config.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/zinxshosting/transaction")
public class TransactionController {

    @Autowired
    TransactionRepository repository;

    @GetMapping("/{provider}")
    public List<TransactionConfiguration> findById(@PathVariable String provider){
        return repository.findByProvider(provider);
    }

  //TODO: Further augment to allow frontend to add new transactions, use json files and @RequestBody



}
