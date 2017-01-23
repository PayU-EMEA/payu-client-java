package ro.payu.example.alu;

import org.apache.http.NameValuePair;

import java.util.List;

public class AluResponseInterpreter {

    public boolean isSuccess(List<NameValuePair> response) {
        for (NameValuePair pair : response) {
            if (pair.getName().equals("STATUS") && pair.getValue().equals("SUCCESS")) {
                return true;
            }
        }
        return false;
    }

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println();
        System.out.println("ALU response:");
        System.out.println(response);
    }
}
