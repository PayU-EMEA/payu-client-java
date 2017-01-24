package ro.payu.example.ipn;

import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.SignatureCalculator;
import ro.payu.lib.common.server.ApiHttpHandler;
import ro.payu.lib.common.server.ApiHttpServer;
import ro.payu.lib.ipn.IpnAuthenticationService;
import ro.payu.lib.ipn.IpnResponseBuilder;

public class IpnHttpServerBuilder {

    private static final int SERVER_PORT = 8000;
    private static final String IPN_ENDPOINT = "/ipn";

    public static ApiHttpServer createServer(IpnRequestProcessor ipnRequestProcessor, AuthenticationService authenticationService) {
        ApiHttpHandler apiHttpHandler = new ApiHttpHandler(
                ipnRequestProcessor,
                new IpnResponseBuilder(new IpnAuthenticationService(authenticationService))
        );
        return new ApiHttpServer(IPN_ENDPOINT, SERVER_PORT, apiHttpHandler);
    }

    public static void main(String[] args) {
        ApiHttpServer httpServer = IpnHttpServerBuilder.createServer(
                new IpnRequestProcessor(),
                new AuthenticationService(new SignatureCalculator())
        );
        httpServer.start();
    }
}
