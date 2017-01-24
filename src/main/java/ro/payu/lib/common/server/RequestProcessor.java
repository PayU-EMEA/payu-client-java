package ro.payu.lib.common.server;

import org.apache.http.NameValuePair;

import java.util.List;

public interface RequestProcessor {

    boolean process(List<NameValuePair> requestParameters);

    void error(String error, List<NameValuePair> requestParameters);
}
