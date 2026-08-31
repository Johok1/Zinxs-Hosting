package com.zinxshosting.backend.instance.config;

import com.zinxshosting.backend.instance.config.htmltransaction.HTMLTransaction;
import com.zinxshosting.backend.instance.config.htmltransaction.HTMLTransactionAuthenticationOVH;

import java.util.List;

public class APITransactionListOVH extends APITransactionList{
    private HTMLTransactionAuthenticationOVH transactionAuthOVH;
    public APITransactionListOVH(List<HTMLTransaction> requests, String provider, String serviceType, HTMLTransactionAuthenticationOVH
                                 transactionAuthOVH) {
        super(requests, provider, serviceType);
        this.transactionAuthOVH = transactionAuthOVH;
    }

    public HTMLTransactionAuthenticationOVH authenticationOVH(){
        return transactionAuthOVH;
    }
}
