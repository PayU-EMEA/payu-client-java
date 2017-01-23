package ro.payu.example.idn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IdnResponseInterpreter {

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
        System.out.println("IDN response:");
        System.out.println(responseParameters);
    }
}
