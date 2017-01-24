package ro.payu.example.irn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IrnResponseInterpreter {

    public boolean isSuccess(List<NameValuePair> responseParameters) {
        for (NameValuePair pair : responseParameters) {
            if (pair.getName().equals("RESPONSE_CODE") && pair.getValue().equals("1")) {
                return true;
            }
        }
        return false;
    }

    public void interpretResponseParameters(List<NameValuePair> responseParameters) {
        System.out.println();
        System.out.println("IRN response:");
        System.out.println(responseParameters);
    }
}
