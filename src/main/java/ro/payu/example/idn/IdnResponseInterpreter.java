package ro.payu.example.idn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IdnResponseInterpreter {

    public boolean isSuccess(List<NameValuePair> response) {
        for (NameValuePair pair : response) {
            if (pair.getName().equals("RESPONSE_CODE") && pair.getValue().equals("1")) {
                return true;
            }
        }
        return false;
    }

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println();
        System.out.println("IDN response:");
        System.out.println(response);
    }
}
