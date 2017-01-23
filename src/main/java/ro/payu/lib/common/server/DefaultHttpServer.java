package ro.payu.lib.common.server;


import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DefaultHttpServer {

    private HttpServer server;

    public DefaultHttpServer(String endpoint, int port, DefaultHttpHandler httpHandler) {

        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext(endpoint, httpHandler);
        server.setExecutor(null); // creates a default executor (taken from oracle doc example)
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }
}
