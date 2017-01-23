package ro.payu.example.ipn;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.Semaphore;

public class IpnHttpHandler implements HttpHandler {

    private final Semaphore semaphore;
    private List<NameValuePair> requestParameters;

    public IpnHttpHandler(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // parse request
        InputStream requestBody = httpExchange.getRequestBody();
        BufferedReader br = new BufferedReader(new InputStreamReader(requestBody, "utf-8"));
        String urlEncodedString = br.readLine();
        requestParameters = URLEncodedUtils.parse(urlEncodedString, Charset.defaultCharset());

        semaphore.release();

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

    public List<NameValuePair> getRequestParameters() {
        return requestParameters;
    }
}
