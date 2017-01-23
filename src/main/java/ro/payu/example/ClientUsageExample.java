package ro.payu.example;

import org.apache.http.NameValuePair;
import ro.payu.authentication.AuthenticationService;
import ro.payu.client.AluResponseXmlParser;
import ro.payu.client.ApiAuthenticationService;
import ro.payu.client.ApiClient;
import ro.payu.client.ApiHttpClient;
import ro.payu.example.alu.AluRequestParameterBuilder;
import ro.payu.example.alu.AluResposeParameterInterpretor;
import ro.payu.example.idn.IdnRequestParameterBuilder;
import ro.payu.example.idn.IdnResposeParameterInterpreter;
import ro.payu.example.ipn.IpnHttpServer;

import java.util.List;
import java.util.concurrent.Semaphore;

public class ClientUsageExample {

    public static final String SERVER_SCHEMA = "http";
    public static final int SERVER_PORT = 80;
    public static final String SERVER_HOST = "tr.payu.local";

    public static final String MERCHANT_CODE = "PAYU_2";
    public static final String MERCHANT_SECRET_KEY = "SECRET_KEY";

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

        Semaphore semaphore = new Semaphore(1);
        IpnHttpServer ipnHttpServer = new IpnHttpServer(semaphore);
        ipnHttpServer.start();

        try {
            final List<NameValuePair> aluRequestParameters = aluRequestParameterBuilder.buildRequestParameters();

            semaphore.acquire();
            final List<NameValuePair> aluResponseParameters = apiClient.callALU(aluRequestParameters);
            aluResposeParameterInterpretor.interpretResponseParameters(aluResponseParameters);

            semaphore.acquire();
            System.out.println("IPN incoming request:\n" + ipnHttpServer.getIpnRequestParameters());

            final List<NameValuePair> idnRequestParameters = idnRequestParameterBuilder.build(aluResponseParameters);
            final List<NameValuePair> idnResponseParameters = apiClient.callIDN(idnRequestParameters);
            idnResposeParameterInterpreter.interpretResponseParameters(idnResponseParameters);
            semaphore.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ipnHttpServer.stop();
    }

}
