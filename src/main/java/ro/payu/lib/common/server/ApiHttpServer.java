package ro.payu.lib.common.server;


import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;

import java.io.IOException;

public class ApiHttpServer {

    private HttpServer server;

    public ApiHttpServer(String endpoint, int port, ApiHttpHandler httpHandler) {

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
