package com.zinxshosting.backend.instance.config.htmltransaction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OVHApi {

    private String endpointBase;
    private String applicationKey;
    private String applicationSecret;
    private String consumerKey;
    private long startTime;
    private int timeout;

    public OVHApi(String endpointBase, String applicationKey, String applicationSecret, String consumerKey, int startTime, int timeout) {
        this.endpointBase = endpointBase;
        this.applicationKey = applicationKey;
        this.applicationSecret = applicationSecret;
        this.consumerKey = consumerKey;
        //System.currentTimeMillis at the time of the connection to the api
        this.startTime = startTime;
        this.timeout = timeout;
    }

    public String rawCall(String method, String path) throws IOException, NoSuchAlgorithmException {
        String body = "";
        String target = endpointBase + path;

        String data = null;

        boolean needAuth = true;

        Map<String, String> headers = null;

        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("X-Ovh-Application", applicationKey);

        // include payload
        if (data != null) {
            headers.put("Content-type", "application/json");
            body = data;
        }

        // sign request. Never sign 'time' or will recurse infinitely
        if (needAuth) {
            if (applicationSecret == null) {
                throw new RuntimeException("Invalid ApplicationSecret");
            }
            if (consumerKey == null) {
                throw new RuntimeException("Invalid ConsumerKey");
            }

            String now = String.valueOf((System.currentTimeMillis() - startTime) / 1000);
            String signatureInput = applicationSecret + "+" + consumerKey + "+" + method.toUpperCase() + "+" + target + "+" + body + "+" + now;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] signatureBytes = digest.digest(signatureInput.getBytes());
            String signature = "$1$" + bytesToHex(signatureBytes);

            headers.put("X-Ovh-Consumer", consumerKey);
            headers.put("X-Ovh-Timestamp", now);
            headers.put("X-Ovh-Signature", signature);
        }

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(target);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            connection.setDoOutput(true);
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            if (body.length() > 0) {
                connection.getOutputStream().write(body.getBytes("UTF-8"));
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String rawCall2(String method, String path) throws IOException, NoSuchAlgorithmException, InterruptedException {
        String body = "";
        String target = endpointBase + path;

        String data = null;

        boolean needAuth = true;

        Map<String, String> headers = null;

        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put("X-Ovh-Application", applicationKey);

        // include payload
        if (data != null) {
            headers.put("Content-type", "application/json");
            body = data;
        }

        // sign request. Never sign 'time' or will recurse infinitely
        if (needAuth) {
            if (applicationSecret == null) {
                throw new RuntimeException("Invalid ApplicationSecret");
            }
            if (consumerKey == null) {
                throw new RuntimeException("Invalid ConsumerKey");
            }

            String now = String.valueOf((System.currentTimeMillis() - startTime) / 1000);
            String signatureInput = applicationSecret + "+" + consumerKey + "+" + method.toUpperCase() + "+" + target + "+" + body + "+" + now;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] signatureBytes = digest.digest(signatureInput.getBytes());
            String signature = "$1$" + bytesToHex(signatureBytes);

            headers.put("X-Ovh-Consumer", consumerKey);
            headers.put("X-Ovh-Timestamp", now);
            headers.put("X-Ovh-Signature", signature);
        }

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(target))
                .method(method, HttpRequest.BodyPublishers.ofString(body))
                .headers(headers.entrySet().stream()
                        .flatMap(e -> e.getValue() != null ? Stream.of(e.getKey(), e.getValue()) : Stream.empty())
                        .toArray(String[]::new))
                .timeout(Duration.ofMillis(timeout))
                .build();

        System.out.println("Headers " + httpRequest.headers().toString());
        System.out.println("HTTP Request: " + httpRequest.toString());
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpHeaders httpHeaders = httpResponse.headers();
        Map<String, List<String>> responseHeaders = httpHeaders.map();

        return httpResponse.body();

    }
        private static String bytesToHex ( byte[] bytes){
            StringBuilder builder = new StringBuilder();
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        }


}