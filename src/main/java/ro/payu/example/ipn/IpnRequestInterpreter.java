package ro.payu.example.ipn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IpnRequestInterpreter {

    public boolean isSuccess(List<NameValuePair> requestParameters) {
        for (NameValuePair pair : requestParameters) {
            if (pair.getName().equals("ORDERSTATUS") && pair.getValue().equals("PAYMENT_AUTHORIZED")) {
                return true;
            }
        }
        return false;
    }

    public void interpretRequestParameters(List<NameValuePair> requestParameters) {
        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(requestParameters);
    }
}
