package ro.payu.example.ios;

import org.apache.http.NameValuePair;

import java.util.List;

public class IosResponseInterpreter {

    public boolean isSuccess(List<NameValuePair> responseParameters) {
        for (NameValuePair pair : responseParameters) {
            if (pair.getName().equals("REFNO") && !pair.getValue().equals("")) {
                return true;
            }
        }
        return false;
    }

    public void interpretResponseParameters(List<NameValuePair> responseParameters) {
        System.out.println();
        System.out.println("IOS response:");
        System.out.println(responseParameters);
    }
}
