package ro.payu.lib.common.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DefaultHttpHandler implements HttpHandler {

    final private RequestProcessor requestProcessor;
    final private ResponseBuilder responseBuilder;
    private List<NameValuePair> requestParameters;

    public DefaultHttpHandler(RequestProcessor requestProcessor, ResponseBuilder responseBuilder) {
        this.requestProcessor = requestProcessor;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // parse request
        requestParameters = requestProcessor.getRequestParameters(httpExchange.getRequestBody());

        // send response
        Headers responseHeaders = httpExchange.getResponseHeaders();

        List<NameValuePair> headersList = responseBuilder.getHeaders();
        for (NameValuePair header : headersList) {
            responseHeaders.add(header.getName(), header.getValue());
        }

        String responseBody = responseBuilder.getBody(requestParameters);

        httpExchange.sendResponseHeaders(200, responseBody.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }

    public List<NameValuePair> getRequestParameters() {
        return requestParameters;
    }
}
