package ro.payu.example.ipn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.server.RequestProcessor;

import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnRequestProcessor implements RequestProcessor {

    final private Semaphore semaphore;

    private List<NameValuePair> requestParameters;

    public IpnRequestProcessor(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void process(List<NameValuePair> requestParameters) {
        this.requestParameters = requestParameters;

        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(requestParameters);

        semaphore.release();
    }

    public List<NameValuePair> getRequestParameters() {
        return requestParameters;
    }
}
