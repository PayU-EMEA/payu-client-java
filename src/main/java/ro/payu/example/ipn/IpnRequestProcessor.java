package ro.payu.example.ipn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.server.RequestProcessor;

import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnRequestProcessor implements RequestProcessor {

    private static final String DUMMY_BAD_REFNO = "no-expected-ipn-dummy-badRefNo";
    final private Semaphore semaphore;
    private String expectedRefNo;

    private List<NameValuePair> requestParameters;

    public IpnRequestProcessor() {
        semaphore = new Semaphore(1);
        setExpectedIpn(DUMMY_BAD_REFNO);
        waitForIpn();
    }

    @Override
    public boolean process(List<NameValuePair> requestParameters) {
        this.requestParameters = requestParameters;

        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(requestParameters);

        for (NameValuePair parameter : requestParameters) {
            if (parameter.getName().equals("REFNOEXT")) {
                String refNoExt = parameter.getValue();
                if (!expectedRefNo.equals(refNoExt)) {
                    System.out.println("IPN ERROR (ignored): bad RefNoExt = " + refNoExt + ". Expecting = " + expectedRefNo);
                    System.out.println("Waiting for successful IPN request...");
                    return false;
                }
            }
        }
        expectedRefNo = DUMMY_BAD_REFNO;
        semaphore.release();
        return true;
    }

    @Override
    public void error(String error, List<NameValuePair> requestParameters) {

        if (requestParameters != null) {
            System.out.println();
            System.out.println("IPN incoming request:");
            System.out.println(requestParameters);
        }

        System.out.println();
        System.out.println("IPN ERROR (ignored): " + error);
        System.out.println("Waiting for successful IPN request...");
    }

    public void setExpectedIpn(String orderReference) {
        expectedRefNo = orderReference;
    }

    public void waitForIpn() {
        semaphore.acquireUninterruptibly();
    }

    public List<NameValuePair> getRequestParameters() {
        return requestParameters;
    }

}
