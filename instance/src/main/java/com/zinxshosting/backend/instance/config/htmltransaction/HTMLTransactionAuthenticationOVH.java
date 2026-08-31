package com.zinxshosting.backend.instance.config.htmltransaction;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.http.client.methods.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Base64;

public class HTMLTransactionAuthenticationOVH extends HTMLTransaction{

    private String applicationKey, secretKey, endpoint;

    public HTMLTransactionAuthenticationOVH(Object object, HTMLTransaction next, String applicationKey, String secretKey, String endpoint) {
        super(object, next);
        this.applicationKey = applicationKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
    }

    @Override
    public String getResponse() throws IOException {
        return authenticate2(this.applicationKey,this.secretKey,this.endpoint,this.applicationKey);
    }

   /* public static ResponseEntity<String> authenticate(String applicationKey, String secretKey, String endpoint) {
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        resourceDetails.setAccessTokenUri(endpoint);
        resourceDetails.setClientId(applicationKey);
        resourceDetails.setClientSecret(secretKey);
        resourceDetails.setGrantType("client_credentials");

        resourceDetails.setScope(Arrays.asList("GET", "POST", "PUT", "DELETE"));

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        //is it setAuthorizationCode or setHeaders ??
        restTemplate.getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode("Basic " + Base64.getEncoder().encodeToString((applicationKey + ":" + secretKey).getBytes()));

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Failed to authenticate. Response code: " + response.getStatusCodeValue());
        }

        return response;
    }*/

    public static String authenticate2(String applicationKey, String secretKey, String endpoint, String consumerKey) throws IOException, IOException {
        //CloseableHttpClient client = HttpClientBuilder.create().build();
        URL request = new URL("https://api.us.ovhcloud.com/1.0/auth/currentCredential");
        HttpURLConnection url = (HttpURLConnection) request.openConnection();

        url.setRequestMethod("GET");
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://api.us.ovhcloud.com/1.0/auth/currentCredential"))
                .header("X-RapidAPI-Host", "jokes-by-api-ninjas.p.rapidapi.com")
                .header("X-RapidAPI-Key", "your-rapidapi-key")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        //url.addRequestProperty("Accept", "application/json");
        //url.addRequestProperty("X-Ovh-Application", "iE3vL3mgAtLZg00l");
       // url.addRequestProperty("X-Ovh-Consumer", "450dd277ee22eae4ff395ae4c40704ad");
       // url.addRequestProperty("X-Ovh-Signature", "$1$d227ebefea46ca4a55e8fa45b51ae4db8f1a5047");
       // url.addRequestProperty("X-Ovh-Timestamp", "1682497017");

        url.setDoOutput(true);
        url.connect();
        //request.addHeader("Host", "api.us.ovhcloud.com");
       // request.addHeader("Accept", "application/json");
       // request.addHeader("X-Ovh-Application", "iE3vL3mgAtLZg00l");
      //  request.addHeader("X-Ovh-Consumer", "450dd277ee22eae4ff395ae4c40704ad");

        //Check the ovh API to see how to make the signature
        // request.addHeader("X-Ovh-Signature", "$1$d227ebefea46ca4a55e8fa45b51ae4db8f1a5047");
      //  request.addHeader("X-Ovh-Timestamp", "1682497017");

       // CloseableHttpResponse response = client.execute(request);

        // Get the input stream from the HTTP connection
        InputStream inputStream = url.getInputStream();
        // Read the response from the input stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        inputStream.close();
        url.disconnect();

// The response is stored in the `response` StringBuilder object
       return  response.toString();
    }
}

