package ro.payu;

import org.apache.http.NameValuePair;
import ro.payu.authentication.AuthenticationService;
import ro.payu.client.*;

import java.util.List;

public class ClientUsageExample {
    public static void main(String[] args) {


        final ApiClient apiClient = new ApiClient(
                new ApiAuthenticationService(
                        "SECRET_KEY",
                        new AuthenticationService()
                ),
                new ApiHttpClient("tr.payu.local", 80, "http"),
                new AluResponseXmlParser()
        );

        final AluRequestParameterBuilder aluRequestParameterBuilder = new AluRequestParameterBuilder();
        final AluResposeParameterInterpretor aluResposeParameterInterpretor = new AluResposeParameterInterpretor();

        try {
            final List<NameValuePair> aluRequestParameters = aluRequestParameterBuilder.buildRequestParameters();
            final List<NameValuePair> aluResponseParameters = apiClient.callALU(aluRequestParameters);
            aluResposeParameterInterpretor.interpretResponseParameters(aluResponseParameters);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
