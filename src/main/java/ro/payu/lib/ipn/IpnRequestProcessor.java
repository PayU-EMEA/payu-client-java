package ro.payu.lib.ipn;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import ro.payu.lib.common.server.RequestProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnRequestProcessor implements RequestProcessor {

    final private Semaphore semaphore;

    public IpnRequestProcessor(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public List<NameValuePair> getRequestParameters(final InputStream requestBody) {

        final BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, Charset.defaultCharset()));
        final String urlEncodedString;
        try {
            urlEncodedString = br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<NameValuePair> requestParameters = URLEncodedUtils.parse(urlEncodedString, Charset.defaultCharset());

        semaphore.release();

        return requestParameters;
    }
}
