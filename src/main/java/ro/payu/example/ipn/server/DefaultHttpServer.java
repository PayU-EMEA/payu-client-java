package ro.payu.example.ipn.server;


import com.sun.net.httpserver.HttpServer;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class DefaultHttpServer {

    private HttpServer server;
    private DefaultHttpHandler httpHandler;

    public DefaultHttpServer(String endpoint, int port, DefaultHttpHandler httpHandler) {

        this.httpHandler = httpHandler;

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

    public List<NameValuePair> getRequestParameters() {
        return httpHandler.getRequestParameters();
    }
}
