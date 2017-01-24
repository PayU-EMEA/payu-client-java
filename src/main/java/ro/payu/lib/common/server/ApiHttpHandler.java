package ro.payu.lib.common.server;

import org.apache.http.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.VerifyAuthenticationService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class ApiHttpHandler implements HttpRequestHandler {

    final private RequestProcessor requestProcessor;
    final private ResponseBuilder responseBuilder;
    final private VerifyAuthenticationService authenticationService;

    public ApiHttpHandler(RequestProcessor requestProcessor, ResponseBuilder responseBuilder, VerifyAuthenticationService authenticationService) {
        this.requestProcessor = requestProcessor;
        this.responseBuilder = responseBuilder;
        this.authenticationService = authenticationService;
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        if (!(request instanceof HttpEntityEnclosingRequest)) {
            response.setStatusCode(400);
            requestProcessor.error("Request object not instance of HttpEntityEnclosingRequest", null);
            return;
        }

        // we need to parse with charset UTF-8 because otherwise the IPN signature cannot be validated
//        final List<NameValuePair> requestParameters = URLEncodedUtils.parse(((HttpEntityEnclosingRequest) request).getEntity());

        String requestBody = new BufferedReader(new InputStreamReader(((HttpEntityEnclosingRequest) request).getEntity().getContent())).readLine();
        final List<NameValuePair> requestParameters = URLEncodedUtils.parse(requestBody, Charset.forName("UTF-8"));

        try {
            authenticationService.verifySignature(requestParameters);
        } catch (InvalidSignatureException e) {
            response.setStatusCode(400);
            requestProcessor.error(e.getMessage(), requestParameters);
            return;
        }

        boolean processed = requestProcessor.process(requestParameters);
        if (!processed) {
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
