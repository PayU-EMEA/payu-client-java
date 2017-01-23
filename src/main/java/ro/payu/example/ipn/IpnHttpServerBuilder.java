package ro.payu.example.ipn;

import ro.payu.lib.common.server.DefaultHttpHandler;
import ro.payu.lib.common.server.DefaultHttpServer;
import ro.payu.lib.ipn.IpnRequestParser;
import ro.payu.lib.ipn.IpnResponseBuilder;

import java.util.concurrent.Semaphore;

public class IpnHttpServerBuilder {

    private static final int SERVER_PORT = 8000;
    private static final String IPN_ENDPOINT = "/ipn";

    public static DefaultHttpServer createServer(IpnRequestProcessor ipnRequestProcessor) {
        DefaultHttpHandler defaultHttpHandler = new DefaultHttpHandler(
                new IpnRequestParser(),
                ipnRequestProcessor,
                new IpnResponseBuilder()
        );
        return new DefaultHttpServer(IPN_ENDPOINT, SERVER_PORT, defaultHttpHandler);
    }

    public static void main(String[] args) {
        DefaultHttpServer httpServer = IpnHttpServerBuilder.createServer(new IpnRequestProcessor(new Semaphore(1)));
        httpServer.start();
    }
}
