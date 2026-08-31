package com.zinxshosting.backend.instance.config.htmltransaction;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class HTMLTransactionAuthentication extends HTMLTransaction{

    private String username, password, endpoint;
    public HTMLTransactionAuthentication(Object object, HTMLTransaction next, String username, String password, String endpoint) {
        super(object, next);
        this.username = username;
        this.password = password;
        this.endpoint = endpoint;
    }

    @Override
    public String getResponse() {
        //TODO: check the response first, see if it worked
        return authenticate(username,password,endpoint).getBody();
    }

    public static ResponseEntity<String> authenticate(String username, String password, String endpoint) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            throw new RuntimeException("Failed to authenticate. Response code: " + response.getStatusCodeValue());
        }
    }



}
