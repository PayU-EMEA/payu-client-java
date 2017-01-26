package ro.payu.lib.ios;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.client.ApiClient;
import ro.payu.lib.common.client.CommunicationException;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;

import java.util.List;

public class IosClient {

    private static final String IOS_ENDPOINT = "/order/ios.php";

    private final ApiClient apiClient;

    public IosClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<NameValuePair> call(final List<NameValuePair> requestParameters) throws CommunicationException, InvalidXmlResponseParsingException, InvalidSignatureException {

        return apiClient.call(IOS_ENDPOINT, requestParameters);
    }
}
