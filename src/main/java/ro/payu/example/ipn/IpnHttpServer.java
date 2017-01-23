package ro.payu.example.ipn;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;

public class IpnHttpServer {

    public static final int SERVER_PORT = 8000;

    public static void main(String[] args) {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

            server.createContext("/ipn", new IpnHttpHandler());
            server.setExecutor(null); // creates a default executor (taken from oracle doc example)
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class IpnHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            // parse request
            InputStream requestBody = httpExchange.getRequestBody();
            BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, "utf-8"));
            String urlEncodedString = br.readLine();
            List<NameValuePair> requestParameters = URLEncodedUtils.parse(urlEncodedString, Charset.defaultCharset());

            System.out.println(requestParameters);

            // send response
            Headers h = httpExchange.getResponseHeaders();
            h.add("Content-Type", "text/xml");

            StringBuilder response = new StringBuilder("This is the response");
            response.append(requestParameters.toString());

            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }
}
