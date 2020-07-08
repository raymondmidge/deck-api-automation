package com.example.utils;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class HttpUtil {

    private String apiUrl;

    public HttpUtil(String apiUrl){
        this.apiUrl = apiUrl;
    }

    public HttpResponse post(String body, String apiEndpoint) {
        return post(body, apiEndpoint, null);
    }

    public HttpResponse post(String body, String apiEndpoint, Map<String,String> queryParameters) {
        HttpPost post = new HttpPost(buildUri(apiEndpoint, queryParameters));
        StringEntity requestEntity;
        try {
            requestEntity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        post.setEntity(requestEntity);
        return makeRequest(post);
    }


    public HttpResponse get(String apiEndpoint) {
        return get(apiEndpoint, null);
    }

    public HttpResponse get(String apiEndpoint, Map<String,String> queryParameters) {
        HttpGet get = new HttpGet(buildUri(apiEndpoint, queryParameters));
        return makeRequest(get);
    }


    private HttpResponse makeRequest(HttpRequestBase request) {

        CloseableHttpClient client = null;
        CloseableHttpResponse apacheHttpResponse;
        String responseBody = null;
        try {
            client = HttpClients.createDefault();
            apacheHttpResponse = client.execute(request);
            HttpEntity responseEntity = apacheHttpResponse.getEntity();
            if (responseEntity != null) {
                responseBody = EntityUtils.toString(responseEntity);
            }
        } catch (IOException e) {
            throw new RuntimeException("failed making HTTP request", e);
        } finally {
            try{
                if(client != null){
                    client.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("failed closing HTTP client", e);
            }
        }

        return HttpResponse.builder()
                .status(apacheHttpResponse.getStatusLine().getStatusCode())
                .content(responseBody)
                .build();
    }


    private URI buildUri(String apiEndpoint, Map<String,String> queryParameters){
        URIBuilder uriBuilder;
        try{
            uriBuilder = new URIBuilder(apiUrl + apiEndpoint);
            if(!MapUtils.isEmpty(queryParameters)){
                queryParameters.keySet().forEach(key -> uriBuilder.setParameter(key, queryParameters.get(key)));
            }
            return uriBuilder.build();
        } catch (URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

}
