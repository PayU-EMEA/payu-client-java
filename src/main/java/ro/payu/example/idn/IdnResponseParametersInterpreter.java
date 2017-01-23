package ro.payu.example.idn;

import org.apache.http.NameValuePair;

import java.util.List;

public class IdnResponseParametersInterpreter {

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println("IDN response:");
        System.out.println(response);
    }
}
