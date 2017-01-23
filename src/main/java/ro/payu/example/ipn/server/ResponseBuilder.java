package ro.payu.example.ipn.server;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ResponseBuilder {

    List<NameValuePair> getHeaders();
    String getBody(List<NameValuePair> requestParameters);
}
