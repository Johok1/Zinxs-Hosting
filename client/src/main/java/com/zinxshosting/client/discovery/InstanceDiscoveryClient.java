package com.zinxshosting.client.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class InstanceDiscoveryClient {

    @Autowired
    private TheClient theClient;

    @FeignClient(name = "instance")
    interface TheClient {

        @RequestMapping(path = "/instancecore", method = RequestMethod.GET)
        @ResponseBody
        int instancePort();
    }
    public int instancePort() {
        return theClient.instancePort();
    }
}
