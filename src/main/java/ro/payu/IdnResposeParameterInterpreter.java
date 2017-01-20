package ro.payu;

import org.apache.http.NameValuePair;

import java.util.List;

public class IdnResposeParameterInterpreter {

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println("IDN response:");
        System.out.println(response);
    }
}
