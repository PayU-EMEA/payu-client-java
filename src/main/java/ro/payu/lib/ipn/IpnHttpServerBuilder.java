package ro.payu.lib.ipn;

import ro.payu.lib.common.server.DefaultHttpHandler;
import ro.payu.lib.common.server.DefaultHttpServer;

import java.util.concurrent.Semaphore;

public class IpnHttpServerBuilder {

    private static final int SERVER_PORT = 8000;
    private static final String IPN_ENDPOINT = "/ipn";

    public static DefaultHttpServer createServer(Semaphore semaphore) {
        DefaultHttpHandler defaultHttpHandler = new DefaultHttpHandler(
                new IpnRequestProcessor(semaphore),
                new IpnResponseBuilder()
        );
        return new DefaultHttpServer(IPN_ENDPOINT, SERVER_PORT, defaultHttpHandler);
    }

    public static void main(String[] args) {
        DefaultHttpServer httpServer = IpnHttpServerBuilder.createServer(new Semaphore(1));
        httpServer.start();
    }
}
