package ro.payu.example;

import org.apache.http.NameValuePair;
import ro.payu.example.alu.AluRequestParametersBuilder;
import ro.payu.example.alu.AluResponseInterpreter;
import ro.payu.example.idn.IdnRequestParametersBuilder;
import ro.payu.example.idn.IdnResponseInterpreter;
import ro.payu.example.ipn.IpnHttpServer;
import ro.payu.example.ipn.IpnRequestInterpreter;
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

        final AluRequestParametersBuilder aluRequestParametersBuilder = new AluRequestParametersBuilder(MERCHANT_CODE);
        final AluResponseInterpreter aluResponseInterpreter = new AluResponseInterpreter();

        final IdnRequestParametersBuilder idnRequestParametersBuilder = new IdnRequestParametersBuilder(MERCHANT_CODE);
        final IdnResponseInterpreter idnResponseInterpreter = new IdnResponseInterpreter();

        final IpnRequestInterpreter ipnRequestInterpreter = new IpnRequestInterpreter();

        Semaphore semaphore = new Semaphore(1);
        IpnHttpServer ipnHttpServer = new IpnHttpServer(semaphore);
        ipnHttpServer.start();

        try {
            final List<NameValuePair> aluRequestParameters = aluRequestParametersBuilder.buildRequestParameters();

            semaphore.acquire();
            final List<NameValuePair> aluResponseParameters = aluClient.call(aluRequestParameters);

            aluResponseInterpreter.interpretResponseParameters(aluResponseParameters);
            if (!aluResponseInterpreter.isSuccess(aluResponseParameters)) {
                throw new RuntimeException("ALU response ERROR!");
            }

            semaphore.acquire();
            final List<NameValuePair> ipnRequestParameters = ipnHttpServer.getIpnRequestParameters();
            ipnRequestInterpreter.interpretResponseParameters(ipnRequestParameters);
            if (!ipnRequestInterpreter.isSuccess(ipnRequestParameters)) {
                throw new RuntimeException("IPN request ERROR!");
            }

            final List<NameValuePair> idnRequestParameters = idnRequestParametersBuilder.build(aluResponseParameters);

            final List<NameValuePair> idnResponseParameters = idnClient.call(idnRequestParameters);

            idnResponseInterpreter.interpretResponseParameters(idnResponseParameters);
            if (!idnResponseInterpreter.isSuccess(idnResponseParameters)) {
                throw new RuntimeException("IDN response ERROR!");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            ipnHttpServer.stop();
        }
    }

}
