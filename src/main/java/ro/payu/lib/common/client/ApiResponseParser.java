package ro.payu.lib.common.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.util.List;

public interface ApiResponseParser {

    List<NameValuePair> parseResponse(HttpResponse httpResponse) throws InvalidXmlResponseParsingException;
}
