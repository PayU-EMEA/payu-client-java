package ro.payu.example.alu;

import org.apache.http.NameValuePair;

import java.util.List;

public class AluResponseParametersInterpreter {

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println("ALU response:");
        System.out.println(response);
    }
}
