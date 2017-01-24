package ro.payu.example.ipn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.server.RequestProcessor;

import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnRequestProcessor implements RequestProcessor {

    final private Semaphore semaphore;

    private List<NameValuePair> requestParameters;
    private boolean isError;

    public IpnRequestProcessor() {
        semaphore = new Semaphore(1);
        waitForIpn();
    }

    @Override
    public void process(List<NameValuePair> requestParameters) {
        this.requestParameters = requestParameters;
        isError = false;

        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(requestParameters);

        semaphore.release();
    }

    @Override
    public void error(String error, List<NameValuePair> requestParameters) {
        isError = true;

        if (requestParameters != null) {
            System.out.println();
            System.out.println("IPN incoming request:");
            System.out.println(requestParameters);
        }

        System.out.println();
        System.out.println("IPN ERROR: " + error);

        semaphore.release();
    }

    public void waitForIpn() {
        semaphore.acquireUninterruptibly();
    }

    public List<NameValuePair> getRequestParameters() {
        return requestParameters;
    }

    public boolean isSuccess() {
        if (isError) {
            return false;
        }

        for (NameValuePair pair : requestParameters) {
            if (pair.getName().equals("ORDERSTATUS") && !pair.getValue().equals("PAYMENT_AUTHORIZED")) {
                return false;
            }
        }
        return true;
    }
}
