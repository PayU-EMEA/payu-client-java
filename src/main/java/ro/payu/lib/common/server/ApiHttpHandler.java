package ro.payu.lib.common.server;

import org.apache.http.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import java.io.IOException;
import java.util.List;

public class ApiHttpHandler implements HttpRequestHandler {

    final private RequestProcessor requestProcessor;
    final private ResponseBuilder responseBuilder;

    public ApiHttpHandler(RequestProcessor requestProcessor, ResponseBuilder responseBuilder) {
        this.requestProcessor = requestProcessor;
        this.responseBuilder = responseBuilder;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!(request instanceof HttpEntityEnclosingRequest)) {
            response.setStatusCode(400);
            return;
        }

        final List<NameValuePair> requestParameters = URLEncodedUtils.parse(((HttpEntityEnclosingRequest) request).getEntity());

        try {
            requestProcessor.process(requestParameters);
        } catch (Exception e) {
            response.setStatusCode(400);
            return;
        }

        List<NameValuePair> headersList = responseBuilder.getHeaders();
        for (NameValuePair header : headersList) {
            response.addHeader(header.getName(), header.getValue());
        }

        String responseBody = responseBuilder.getBody(requestParameters);

        response.setEntity(new StringEntity(responseBody));
        response.setStatusCode(200);
    }
}