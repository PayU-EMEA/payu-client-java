package ro.payu.example.ipn;

import ro.payu.example.ipn.server.DefaultHttpHandler;
import ro.payu.example.ipn.server.DefaultHttpServer;

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
