package ro.payu.lib.common.server;


import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;

import java.io.IOException;

public class DefaultHttpServer {

    private HttpServer server;

    public DefaultHttpServer(String endpoint, int port, DefaultHttpHandler httpHandler) {

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
