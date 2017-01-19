package ro.payu.client;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

public class ApiClient {

    final private ApiAuthenticationService apiAuthenticationService;

    final private ApiHttpClient apiHttpClient;

    final private AluResponseXmlParser aluResponseXmlParser;

    public ApiClient(ApiAuthenticationService apiAuthenticationService, ApiHttpClient apiHttpClient, AluResponseXmlParser aluResponseXmlParser) {
        this.apiAuthenticationService = apiAuthenticationService;
        this.apiHttpClient = apiHttpClient;
        this.aluResponseXmlParser = aluResponseXmlParser;
    }

    public final List<NameValuePair> callALU(final List<NameValuePair> parameters) throws BadResponseSignatureException, CommunicationException {

        final List<NameValuePair> requestParams = apiAuthenticationService.addAluRequestSignature(parameters);

        // create request
        final HttpPost httpRequest;
        try {
            httpRequest = new HttpPost("/order/alu/v3");
            httpRequest.setEntity(new UrlEncodedFormEntity(requestParams));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // call http and obtain response
        final HttpResponse httpResponse;
        try {
            httpResponse = apiHttpClient.callHttp(httpRequest);
        } catch (HttpException e) {
            throw new CommunicationException(e);
        }

        final List<NameValuePair> responseParams = aluResponseXmlParser.parseAluResponse(httpResponse);

        apiAuthenticationService.verifyAluResponseSignature(responseParams);

        return responseParams;
    }
}
