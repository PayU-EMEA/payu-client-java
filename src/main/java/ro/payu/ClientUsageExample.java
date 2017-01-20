package ro.payu;

import org.apache.http.NameValuePair;
import ro.payu.authentication.AuthenticationService;
import ro.payu.client.AluResponseXmlParser;
import ro.payu.client.ApiAuthenticationService;
import ro.payu.client.ApiClient;
import ro.payu.client.ApiHttpClient;

import java.util.List;

public class ClientUsageExample {

    public static final String MERCHANT_CODE = "PAYU_2";
    public static final String MERCHANT_SECRET_KEY = "SECRET_KEY";
    public static final String SERVER_HOST = "tr.payu.local";
    public static final int SERVER_PORT = 80;
    public static final String SERVER_SCHEMA = "http";

    public static void main(String[] args) {


        final ApiClient apiClient = new ApiClient(
                new ApiAuthenticationService(
                        MERCHANT_SECRET_KEY,
                        new AuthenticationService()
                ),
                new ApiHttpClient(SERVER_HOST, SERVER_PORT, SERVER_SCHEMA),
                new AluResponseXmlParser()
        );

        final AluRequestParameterBuilder aluRequestParameterBuilder = new AluRequestParameterBuilder(MERCHANT_CODE);
        final AluResposeParameterInterpretor aluResposeParameterInterpretor = new AluResposeParameterInterpretor();

        final IdnRequestParameterBuilder idnRequestParameterBuilder = new IdnRequestParameterBuilder(MERCHANT_CODE);
        final IdnResposeParameterInterpreter idnResposeParameterInterpreter = new IdnResposeParameterInterpreter();

        try {
            final List<NameValuePair> aluRequestParameters = aluRequestParameterBuilder.buildRequestParameters();
            final List<NameValuePair> aluResponseParameters = apiClient.callALU(aluRequestParameters);
            aluResposeParameterInterpretor.interpretResponseParameters(aluResponseParameters);

            final List<NameValuePair> idnRequestParameters = idnRequestParameterBuilder.build(aluResponseParameters);
            final List<NameValuePair> idnResponseParameters = apiClient.callIDN(idnRequestParameters);
            idnResposeParameterInterpreter.interpretResponseParameters(idnResponseParameters);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
