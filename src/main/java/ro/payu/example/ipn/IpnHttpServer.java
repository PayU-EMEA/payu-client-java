package ro.payu.example.ipn;


import com.sun.net.httpserver.HttpServer;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnHttpServer {

    public static final int SERVER_PORT = 8000;
    public static final String IPN_ENDPOINT = "/ipn";

    private HttpServer server;
    private IpnHttpHandler ipnHttpHandler;

    public IpnHttpServer() {
        this(IPN_ENDPOINT, SERVER_PORT, new Semaphore(1));
    }

    public IpnHttpServer(Semaphore semaphore) {
        this(IPN_ENDPOINT, SERVER_PORT, semaphore);
    }

    public IpnHttpServer(String endpoint, int port, Semaphore semaphore) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);

            ipnHttpHandler = new IpnHttpHandler(semaphore);
            server.createContext(endpoint, ipnHttpHandler);
            server.setExecutor(null); // creates a default executor (taken from oracle doc example)

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public List<NameValuePair> getIpnRequestParameters() {
        return ipnHttpHandler.getRequestParameters();
    }

    public static void main(String[] args) {
        IpnHttpServer server = new IpnHttpServer();
        server.start();
    }
}
