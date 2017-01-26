package ro.payu.example.ipn;


import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import ro.payu.lib.common.server.ApiHttpHandler;

import java.io.IOException;

public class IpnHttpServer {

    private final HttpServer server;

    public IpnHttpServer(String endpoint, int port, ApiHttpHandler httpHandler) {

        server = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .registerHandler(endpoint, httpHandler)
                .create();
    }

    public void start() {
        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop();
    }
}
