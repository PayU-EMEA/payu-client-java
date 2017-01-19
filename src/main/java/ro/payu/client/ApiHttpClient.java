package ro.payu.client;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ApiHttpClient {


    private final CloseableHttpClient httpClient;
    private final HttpHost httpHost;

    public ApiHttpClient(String hostname) {
        httpClient = HttpClients.createDefault();
        httpHost = new HttpHost(hostname, 443, "https");
    }

    public HttpResponse callHttp(HttpRequest httpRequest) throws HttpException {

        final CloseableHttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpHost, httpRequest);
        } catch (IOException e) {
            throw new HttpException("Error calling http", e);
        }

        return httpResponse;
    }
}
