package com.zinxshosting.client.controller;

import com.zinxshosting.client.discovery.InstanceDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InstanceController {

    @Autowired
    private InstanceDiscoveryClient instanceDiscoveryClient;

    @GetMapping("/get-instance")
    public int instancePort() {
        return instanceDiscoveryClient.instancePort();
    }
}
