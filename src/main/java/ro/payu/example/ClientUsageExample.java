package ro.payu.example;

import org.apache.http.NameValuePair;
import ro.payu.example.alu.AluRequestParameterBuilder;
import ro.payu.example.alu.AluResposeParameterInterpretor;
import ro.payu.example.idn.IdnRequestParameterBuilder;
import ro.payu.example.idn.IdnResposeParameterInterpreter;
import ro.payu.example.ipn.IpnHttpServer;
import ro.payu.lib.alu.AluAuthenticationService;
import ro.payu.lib.alu.AluClient;
import ro.payu.lib.alu.AluResponseParser;
import ro.payu.lib.common.authentication.ApiCommonAuthenticationService;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.client.ApiClient;
import ro.payu.lib.common.client.ApiHttpClient;
import ro.payu.lib.common.client.XmlResponseParser;
import ro.payu.lib.idn.IdnAuthenticationService;
import ro.payu.lib.idn.IdnClient;
import ro.payu.lib.idn.IdnResponseParser;

import java.util.List;
import java.util.concurrent.Semaphore;

public class ClientUsageExample {

    private static final String SERVER_SCHEMA = "http";
    private static final int SERVER_PORT = 80;
    private static final String SERVER_HOST = "tr.payu.local";

    private static final String MERCHANT_CODE = "PAYU_2";
    private static final String MERCHANT_SECRET_KEY = "SECRET_KEY";

    public static void main(String[] args) {

        final ApiHttpClient apiHttpClient = new ApiHttpClient(SERVER_HOST, SERVER_PORT, SERVER_SCHEMA);
        final ApiCommonAuthenticationService apiCommonAuthenticationService = new ApiCommonAuthenticationService(
                new AuthenticationService(),
                MERCHANT_SECRET_KEY
        );
        final XmlResponseParser xmlResponseParser = new XmlResponseParser();
        final AluClient aluClient = new AluClient(new ApiClient(
                apiHttpClient,
                new AluAuthenticationService(apiCommonAuthenticationService),
                new AluResponseParser(xmlResponseParser)
        ));

        final IdnClient idnClient = new IdnClient(new ApiClient(
                apiHttpClient,
                new IdnAuthenticationService(apiCommonAuthenticationService),
                new IdnResponseParser(xmlResponseParser)
        ));

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
            final List<NameValuePair> aluResponseParameters = aluClient.call(aluRequestParameters);
            aluResposeParameterInterpretor.interpretResponseParameters(aluResponseParameters);

            semaphore.acquire();
            System.out.println("IPN incoming request:\n" + ipnHttpServer.getIpnRequestParameters());

            final List<NameValuePair> idnRequestParameters = idnRequestParameterBuilder.build(aluResponseParameters);
            final List<NameValuePair> idnResponseParameters = idnClient.call(idnRequestParameters);
            idnResposeParameterInterpreter.interpretResponseParameters(idnResponseParameters);
            semaphore.release();

        } catch (Exception e) {
            e.printStackTrace();
        }

        ipnHttpServer.stop();
    }

}
