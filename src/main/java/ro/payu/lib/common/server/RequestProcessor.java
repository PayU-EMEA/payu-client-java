package ro.payu.lib.common.server;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;

public interface RequestProcessor {

    List<NameValuePair> getRequestParameters(InputStream requestBody);
}
