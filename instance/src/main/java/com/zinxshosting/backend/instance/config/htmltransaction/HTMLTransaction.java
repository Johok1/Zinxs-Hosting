package com.zinxshosting.backend.instance.config.htmltransaction;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;


public abstract class HTMLTransaction {

    private Object object;
    private HTMLTransaction next;

    private HttpRequest post;
    public HTMLTransaction(Object object, HTMLTransaction next){
        this.object = object;
        this.next = next;
    }

    public void setPost(){
        this.post = post;
    }

    public Object getObject(){
        return this.object;
    }

    public void setObject(Object newObj){
        this.object = newObj;
    }

    public HTMLTransaction getNext(){
        return this.next;
    }

    public abstract String getResponse() throws IOException;

    /**
     * This method should run the htmlrequest and send any required data to the next transaction, and run the next transaction.
     */
    public  void run() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newBuilder().build();

        //Assume the request has proper parameters, pull response handler from some file path (figure that out), then send
        String response = getResponse();

        //do something to the data of the next based on the response
        this.next.setObject(response);

        //run the next request
        this.next.run();
    }
}
