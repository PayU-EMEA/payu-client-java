package ro.payu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import ro.payu.authentication.AuthenticationService;
import ro.payu.client.AluResponseXmlParser;
import ro.payu.client.ApiAuthenticationService;
import ro.payu.client.ApiClient;
import ro.payu.client.ApiHttpClient;
import ro.payu.client.BadResponseSignatureException;
import ro.payu.client.CommunicationException;

public class FlowExample {

    public static void main(String[] args) throws BadResponseSignatureException, CommunicationException {



        final ApiClient apiClient = new ApiClient(
                new ApiAuthenticationService(
                        "SECRET_KEY",
                        new AuthenticationService()
                ),
                new ApiHttpClient("secure.payu.com.tr"),
                new AluResponseXmlParser()
        );

        final List<NameValuePair> aluRequestParameters = new ArrayList<>();

        final List<NameValuePair> aluResponseParameters;

        aluResponseParameters = apiClient.callALU(aluRequestParameters);
    }
}
