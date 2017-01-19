package ro.payu;

import org.apache.http.NameValuePair;
import ro.payu.authentication.AuthenticationService;
import ro.payu.client.*;

import java.util.List;

public class ClientUsageExample {
    public static void main(String[] args) throws BadResponseSignatureException, CommunicationException, InvalidXmlResponseException {


        final ApiClient apiClient = new ApiClient(
                new ApiAuthenticationService(
                        "SECRET_KEY",
                        new AuthenticationService()
                ),
                new ApiHttpClient("secure.payu.com.tr"),
                new AluResponseXmlParser()
        );

        final AluRequestParameterBuilder aluRequestParameterBuilder = new AluRequestParameterBuilder();
        final AluResposeParameterInterpretor aluResposeParameterInterpretor = new AluResposeParameterInterpretor();

        final List<NameValuePair> aluRequestParameters = aluRequestParameterBuilder.buildRequestParameters();
        final List<NameValuePair> aluResponseParameters = apiClient.callALU(aluRequestParameters);
        aluResposeParameterInterpretor.interpretResponseParameters(aluResponseParameters);

    }

}
