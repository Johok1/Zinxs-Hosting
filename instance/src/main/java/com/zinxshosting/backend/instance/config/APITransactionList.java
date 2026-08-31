package com.zinxshosting.backend.instance.config;

import com.zinxshosting.backend.instance.config.htmltransaction.HTMLTransaction;
import lombok.Getter;

import java.util.List;

@Getter
public class APITransactionList extends TransactionList{

    //name of company who provides this service
    private String provider;

    //type of service
    private String serviceType;

    public APITransactionList(List<HTMLTransaction> requests, String provider, String serviceType) {
        super(requests);
        this.provider = provider;
        this.serviceType = serviceType;
    }
}
