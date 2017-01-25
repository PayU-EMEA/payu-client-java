package ro.payu.example.ipn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.server.RequestProcessor;

import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnRequestProcessor implements RequestProcessor {

    public static final String DUMMY_BAD_REFNO = "dummy-123qwe-badRefNo";
    final private Semaphore semaphore;
    private String expectedRefNo;

    private List<NameValuePair> requestParameters;
    private boolean isError;

    public IpnRequestProcessor() {
        semaphore = new Semaphore(1);
        waitForIpn(DUMMY_BAD_REFNO);
    }

    @Override
    public boolean process(List<NameValuePair> requestParameters) {
        this.requestParameters = requestParameters;
        isError = false;

        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(requestParameters);

        for (NameValuePair parameter : requestParameters) {
            if (parameter.getName().equals("REFNO")) {
                String refNo = parameter.getValue();
                if (!expectedRefNo.equals(refNo)) {
                    System.out.println("IPN ERROR (ignored): bad RefNo = " + refNo + ". Expecting = " + expectedRefNo);
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
        isError = true;

        if (requestParameters != null) {
            System.out.println();
            System.out.println("IPN incoming request:");
            System.out.println(requestParameters);
        }

        System.out.println();
        System.out.println("IPN ERROR (ignored): " + error);
        System.out.println("Waiting for successful IPN request...");
    }

    public void waitForIpn(String refNo) {
        expectedRefNo = refNo;
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
