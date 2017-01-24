package ro.payu.lib.idn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.client.ApiClient;
import ro.payu.lib.common.client.CommunicationException;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;

import java.util.List;

public class IdnClient {

    private static final String IDN_ENDPOINT = "/order/idn.php";

    private ApiClient apiClient;

    public IdnClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<NameValuePair> call(final List<NameValuePair> requestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        return apiClient.call(IDN_ENDPOINT, requestParameters);
    }
}
