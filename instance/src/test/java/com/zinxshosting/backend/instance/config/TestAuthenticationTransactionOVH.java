package com.zinxshosting.backend.instance.config;

import com.zinxshosting.backend.instance.config.htmltransaction.HTMLTransactionAuthenticationOVH;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class TestAuthenticationTransactionOVH {

    HTMLTransactionAuthenticationOVH ovhAuth;

    @Test
    public void testOVHAuthentication() throws IOException {

      //  CloseableHttpResponse responseEntity =  HTMLTransactionAuthenticationOVH.authenticate2("C0TpC58bR73UAVpJ",
       //         "HpOYQaWGon7TFrWu5ALyvnFn1CGyVtRF","endpoint","JSPZFwYvxoehrQbFcUfDgfgqWceh8ZMi");

        System.out.println(HTMLTransactionAuthenticationOVH.authenticate2("we","we","we","wuh"));

        /**
         * 19:57:50.645 [main] DEBUG org.apache.http.wire - http-outgoing-0 <<
         * "{"class":"Client::BadRequest","message":
         * "The input that you provided isn't valid JSON or not well formatted"}"
         */

    }
}
