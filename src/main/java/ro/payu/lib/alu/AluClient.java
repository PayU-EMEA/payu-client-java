package ro.payu.lib.alu;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.client.ApiClient;
import ro.payu.lib.common.client.CommunicationException;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;

import java.util.List;

public class AluClient {

    private static final String ALU_ENDPOINT = "/order/alu/v3";

    private final ApiClient apiClient;

    public AluClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<NameValuePair> call(final List<NameValuePair> requestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        return apiClient.call(ALU_ENDPOINT, requestParameters);
    }
}
