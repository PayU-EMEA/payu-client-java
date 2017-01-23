package ro.payu.lib.common.server;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ResponseBuilder {

    List<NameValuePair> getHeaders();
    String getBody(List<NameValuePair> requestParameters);
}
