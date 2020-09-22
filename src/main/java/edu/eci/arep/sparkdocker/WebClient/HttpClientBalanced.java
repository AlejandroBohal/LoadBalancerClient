package edu.eci.arep.sparkdocker.WebClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Http client balanced.
 */
public class HttpClientBalanced {

    private HttpClient httpClient;
    private Integer serverNumber = 0;
    private Map<Integer, String> serverMap = new ConcurrentHashMap<>();
    private String baseHost = "http://172.17.0.1";
    private String ports[] = {"8087", "8088", "8089"};

    /**
     * Instantiates a new Http client balanced.
     */
    public HttpClientBalanced() {
        httpClient = HttpClientBuilder.create().build();
        for(int i=0; i<ports.length;i++){
            serverMap.put(i,ports[i]);
        }

    }

    /**
     * Gets messages.
     *
     * @param path the path
     * @return the messages
     */
    public String getMessages(String path) {
        try {
            String uri = getServer() + path;
            HttpGet request = new HttpGet(uri);
            HttpResponse response;
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Post message string.
     *
     * @param message the message
     * @param path    the path
     * @return the string
     */
    public String postMessage(String message, String path) {
        message = message.replace("<","");
        message = message.replace(">","");
        String payload = message;
        try {
            StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            String uri = getServer() + path;
            System.out.println(uri);
            HttpPost request = new HttpPost(uri);
            request.setEntity(entity);
            HttpResponse response;
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }return "";

    }

    /**
     * Get server string.
     *
     * @return the string
     */
    public String getServer(){
        roundRobin();
        return MessageFormat.format("{0}:{1}",baseHost,ports[serverNumber]);
    }
    private void roundRobin(){
        this.serverNumber=(this.serverNumber+1)% ports.length;
    }


}
