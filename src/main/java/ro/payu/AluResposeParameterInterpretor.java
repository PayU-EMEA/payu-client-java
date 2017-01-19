package ro.payu;

import org.apache.http.NameValuePair;

import java.util.List;

public class AluResposeParameterInterpretor {

    public void interpretResponseParameters(List<NameValuePair> response) {
        System.out.println(response);
    }
}
