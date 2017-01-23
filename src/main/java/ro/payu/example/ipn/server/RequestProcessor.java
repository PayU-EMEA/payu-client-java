package ro.payu.example.ipn.server;

import org.apache.http.NameValuePair;

import java.io.InputStream;
import java.util.List;

public interface RequestProcessor {

    List<NameValuePair> getRequestParameters(InputStream requestBody);
}
