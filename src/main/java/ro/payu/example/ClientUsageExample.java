package ro.payu.example;

import org.apache.http.NameValuePair;
import ro.payu.example.alu.AluRequestParametersBuilder;
import ro.payu.example.alu.AluResponseInterpreter;
import ro.payu.example.idn.IdnRequestParametersBuilder;
import ro.payu.example.idn.IdnResponseInterpreter;
import ro.payu.example.ipn.IpnHttpServerBuilder;
import ro.payu.example.ipn.IpnRequestProcessor;
import ro.payu.example.irn.IrnRequestParametersBuilder;
import ro.payu.example.irn.IrnResponseInterpreter;
import ro.payu.lib.alu.AluAuthenticationService;
import ro.payu.lib.alu.AluClient;
import ro.payu.lib.alu.AluResponseParser;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.SignatureCalculator;
import ro.payu.lib.common.client.*;
import ro.payu.lib.common.server.ApiHttpServer;
import ro.payu.lib.idn.IdnAuthenticationService;
import ro.payu.lib.idn.IdnClient;
import ro.payu.lib.idn.IdnResponseParser;
import ro.payu.lib.irn.IrnAuthenticationService;
import ro.payu.lib.irn.IrnClient;
import ro.payu.lib.irn.IrnResponseParser;

import java.util.List;

public class ClientUsageExample {

    private static final String SERVER_SCHEMA = "http";
    private static final int SERVER_PORT = 80;
    private static final String SERVER_HOST = "tr.payu.local";

    // production connection settings
//    private static final String SERVER_SCHEMA = "https";
//    private static final int SERVER_PORT = 443;
//    private static final String SERVER_HOST = "secure.payu.com.tr";

    private static final String MERCHANT_CODE = "PAYU_2";
    private static final String MERCHANT_SECRET_KEY = "SECRET_KEY";

    // production credentials
//    private static final String MERCHANT_CODE = "OPU_TEST";
//    private static final String MERCHANT_SECRET_KEY = "SECRET_KEY";

    // production credentials
//    private static final String MERCHANT_CODE = "PALJZXGV";
//    private static final String MERCHANT_SECRET_KEY = "f*%J7z6_#|5]s7V4[g3]";

    private static AluClient aluClient;
    private static IdnClient idnClient;
    private static IrnClient irnClient;

    private static AluRequestParametersBuilder aluRequestParametersBuilder;
    private static AluResponseInterpreter aluResponseInterpreter;

    private static IdnRequestParametersBuilder idnRequestParametersBuilder;
    private static IdnResponseInterpreter idnResponseInterpreter;

    private static IrnRequestParametersBuilder irnRequestParametersBuilder;
    private static IrnResponseInterpreter irnResponseInterpreter;

    private static ApiHttpServer ipnHttpServer;
    private static IpnRequestProcessor ipnRequestProcessor;

    public static void main(String[] args) {

        /*
         * You may want to checkout first the branch "java_server_ipn_library" from the epayment repository
         * and run liquibase with context "tr" (run-all.sh tr)
         */

        setUp();

        try {
            callAlu();
            waitForIpn();

            final List<NameValuePair> ipnRequestParameters = getIpnRequestParameters();

            callIdn(ipnRequestParameters);
            waitForIpn();

            callIrn(ipnRequestParameters);
            waitForIpn();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ipnHttpServer.stop();
        }
    }

    private static void callAlu() throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        final List<NameValuePair> aluRequestParameters = aluRequestParametersBuilder.buildRequestParameters();

        final List<NameValuePair> aluResponseParameters = aluClient.call(aluRequestParameters);

        aluResponseInterpreter.interpretResponseParameters(aluResponseParameters);
        if (!aluResponseInterpreter.isSuccess(aluResponseParameters)) {
            throw new RuntimeException("ALU response ERROR!");
        }
    }

    private static List<NameValuePair> getIpnRequestParameters() {

        if (ipnRequestProcessor.getRequestParameters() == null || !ipnRequestProcessor.isSuccess()) {
            throw new RuntimeException("IPN request ERROR!");
        }

        return ipnRequestProcessor.getRequestParameters();
    }

    private static void waitForIpn() {
        ipnRequestProcessor.waitForIpn();
    }

    private static void callIdn(List<NameValuePair> ipnRequestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        final List<NameValuePair> idnRequestParameters = idnRequestParametersBuilder.build(ipnRequestParameters);

        final List<NameValuePair> idnResponseParameters = idnClient.call(idnRequestParameters);

        idnResponseInterpreter.interpretResponseParameters(idnResponseParameters);
        if (!idnResponseInterpreter.isSuccess(idnResponseParameters)) {
            throw new RuntimeException("IDN response ERROR!");
        }
    }

    private static void callIrn(List<NameValuePair> ipnRequestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {
        final List<NameValuePair> irnRequestParameters = irnRequestParametersBuilder.build(ipnRequestParameters);

        final List<NameValuePair> irnResponseParameters = irnClient.call(irnRequestParameters);

        irnResponseInterpreter.interpretResponseParameters(irnResponseParameters);
        if (!irnResponseInterpreter.isSuccess(irnResponseParameters)) {
            throw new RuntimeException("IRN response ERROR!");
        }
    }

    private static void setUp() {

        final ApiHttpClient apiHttpClient = new ApiHttpClient(SERVER_HOST, SERVER_PORT, SERVER_SCHEMA);
        final AuthenticationService authenticationService = new AuthenticationService(
                new SignatureCalculator(),
                MERCHANT_SECRET_KEY
        );
        final XmlResponseParser xmlResponseParser = new XmlResponseParser();
        aluClient = new AluClient(new ApiClient(
                apiHttpClient,
                new AluAuthenticationService(authenticationService),
                new AluResponseParser(xmlResponseParser)
        ));

        idnClient = new IdnClient(new ApiClient(
                apiHttpClient,
                new IdnAuthenticationService(authenticationService),
                new IdnResponseParser(xmlResponseParser)
        ));

        irnClient = new IrnClient(new ApiClient(
                apiHttpClient,
                new IrnAuthenticationService(authenticationService),
                new IrnResponseParser(xmlResponseParser)
        ));

        aluRequestParametersBuilder = new AluRequestParametersBuilder(MERCHANT_CODE);
        aluResponseInterpreter = new AluResponseInterpreter();

        idnRequestParametersBuilder = new IdnRequestParametersBuilder(MERCHANT_CODE);
        idnResponseInterpreter = new IdnResponseInterpreter();

        irnRequestParametersBuilder = new IrnRequestParametersBuilder(MERCHANT_CODE);
        irnResponseInterpreter = new IrnResponseInterpreter();

        ipnRequestProcessor = new IpnRequestProcessor();
        ipnHttpServer = IpnHttpServerBuilder.createServer(ipnRequestProcessor, authenticationService);
        ipnHttpServer.start();
    }
}
