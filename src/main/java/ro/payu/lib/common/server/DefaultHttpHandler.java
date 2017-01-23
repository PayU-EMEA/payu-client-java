package ro.payu.lib.common.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class DefaultHttpHandler implements HttpHandler {

    final private RequestParser requestParser;
    final private RequestProcessor requestProcessor;
    final private ResponseBuilder responseBuilder;

    public DefaultHttpHandler(RequestParser requestParser, RequestProcessor requestProcessor, ResponseBuilder responseBuilder) {
        this.requestParser = requestParser;
        this.requestProcessor = requestProcessor;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        final List<NameValuePair> requestParameters = requestParser.getRequestParameters(httpExchange.getRequestBody());

        requestProcessor.process(requestParameters);

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
}
