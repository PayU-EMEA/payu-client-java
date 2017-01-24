package ro.payu.example.ipn;

import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.SignatureCalculator;
import ro.payu.lib.common.server.DefaultHttpHandler;
import ro.payu.lib.common.server.DefaultHttpServer;
import ro.payu.lib.ipn.IpnAuthenticationService;
import ro.payu.lib.ipn.IpnResponseBuilder;

import java.util.concurrent.Semaphore;

public class IpnHttpServerBuilder {

    private static final int SERVER_PORT = 8000;
    private static final String IPN_ENDPOINT = "/ipn";

    public static DefaultHttpServer createServer(IpnRequestProcessor ipnRequestProcessor, AuthenticationService authenticationService) {
        DefaultHttpHandler defaultHttpHandler = new DefaultHttpHandler(
                ipnRequestProcessor,
                new IpnResponseBuilder(new IpnAuthenticationService(authenticationService))
        );
        return new DefaultHttpServer(IPN_ENDPOINT, SERVER_PORT, defaultHttpHandler);
    }

    public static void main(String[] args) {
        DefaultHttpServer httpServer = IpnHttpServerBuilder.createServer(
                new IpnRequestProcessor(),
                new AuthenticationService(new SignatureCalculator())
        );
        httpServer.start();
    }
}
