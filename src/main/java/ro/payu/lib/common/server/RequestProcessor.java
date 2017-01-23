package ro.payu.lib.common.server;

import org.apache.http.NameValuePair;

import java.util.List;

public interface RequestProcessor {

    void process(List<NameValuePair> requestParameters);
}
