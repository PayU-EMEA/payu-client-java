package ro.payu.example.ipn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IpnRequestInterpreter {

    public boolean isSuccess(List<NameValuePair> response) {
        for (NameValuePair pair : response) {
            if (pair.getName().equals("ORDERSTATUS") && pair.getValue().equals("PAYMENT_AUTHORIZED")) {
                return true;
            }
        }
        return false;
    }

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println();
        System.out.println("IPN incoming request:");
        System.out.println(response);
    }
}
