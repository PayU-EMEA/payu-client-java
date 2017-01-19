package ro.payu.client;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ApiClient {

    public static final String IDN_ENDPOINT = "/order/idn.php";

    final private ApiAuthenticationService apiAuthenticationService;

    final private ApiHttpClient apiHttpClient;

    final private AluResponseXmlParser aluResponseXmlParser;

    public ApiClient(ApiAuthenticationService apiAuthenticationService, ApiHttpClient apiHttpClient, AluResponseXmlParser aluResponseXmlParser) {
        this.apiAuthenticationService = apiAuthenticationService;
        this.apiHttpClient = apiHttpClient;
        this.aluResponseXmlParser = aluResponseXmlParser;
    }

    public final List<NameValuePair> callALU(final List<NameValuePair> parameters) throws BadResponseSignatureException, CommunicationException, InvalidXmlResponseException {

        final List<NameValuePair> requestParams = apiAuthenticationService.addAluRequestSignature(parameters);

        // create request
        final HttpPost httpRequest = new HttpPost("/order/alu/v3");
        httpRequest.setEntity(new UrlEncodedFormEntity(requestParams, StandardCharsets.UTF_8));

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

    public final List<NameValuePair> callIDN(final List<NameValuePair> parameters) throws CommunicationException, InvalidXmlResponseException, BadResponseSignatureException {

        final List<NameValuePair> requestParams = apiAuthenticationService.addIdnRequestSignature(parameters);

        // create request
        final HttpPost httpRequest = new HttpPost(IDN_ENDPOINT);
        httpRequest.setEntity(new UrlEncodedFormEntity(requestParams, StandardCharsets.UTF_8));

        // call http and obtain response
        final HttpResponse httpResponse;
        try {
            httpResponse = apiHttpClient.callHttp(httpRequest);
        } catch (HttpException e) {
            throw new CommunicationException(e);
        }

        final List<NameValuePair> responseParams = aluResponseXmlParser.parseIdnResponse(httpResponse);

        apiAuthenticationService.verifyIdnResponseSignature(responseParams);

        return responseParams;
    }
}
