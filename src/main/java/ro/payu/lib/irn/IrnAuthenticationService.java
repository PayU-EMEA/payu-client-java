package ro.payu.lib.irn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.ApiAuthenticationService;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.SignatureCalculator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class IrnAuthenticationService implements ApiAuthenticationService {

    private static final String REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final String RESPONSE_SIGNATURE_NAME = "ORDER_HASH";

    private static final List<String> PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE = Arrays.asList("REF_URL", REQUEST_SIGNATURE_NAME);
    private static final List<String> PARAMETERS_EXCLUDED_FROM_RESPONSE_SIGNATURE = Collections.singletonList(RESPONSE_SIGNATURE_NAME);

    final private AuthenticationService authenticationService;

    public IrnAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public List<NameValuePair> addRequestSignature(final List<NameValuePair> requestParameters) {

        return authenticationService.addSignature(
                requestParameters,
                PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE,
                SignatureCalculator.getKeepSameParameterOrderComparator(),
                REQUEST_SIGNATURE_NAME
        );
    }

    public void verifyResponseSignature(final List<NameValuePair> responseParameters) throws InvalidSignatureException {

        authenticationService.verifySignature(
                responseParameters,
                PARAMETERS_EXCLUDED_FROM_RESPONSE_SIGNATURE,
                SignatureCalculator.getKeepSameParameterOrderComparator(),
                RESPONSE_SIGNATURE_NAME
        );
    }
}
