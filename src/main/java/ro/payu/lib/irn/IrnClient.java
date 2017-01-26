package ro.payu.lib.irn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.client.ApiClient;
import ro.payu.lib.common.client.CommunicationException;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;

import java.util.List;

public class IrnClient {

    private static final String IRN_ENDPOINT = "/order/irn.php";

    private final ApiClient apiClient;

    public IrnClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<NameValuePair> call(final List<NameValuePair> requestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        return apiClient.call(IRN_ENDPOINT, requestParameters);
    }
}
