package ro.payu.lib.alu;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.ApiAuthenticationService;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.SignatureCalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AluAuthenticationService implements ApiAuthenticationService {

    private static final String REQUEST_SIGNATURE_NAME = "ORDER_HASH";
    private static final String RESPONSE_SIGNATURE_NAME = "HASH";

    private static final List<String> PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE = new ArrayList<>();
    private static final List<String> PARAMETERS_EXCLUDED_FROM_RESPONSE_SIGNATURE = Arrays.asList("URL_3DS", RESPONSE_SIGNATURE_NAME);

    final private AuthenticationService authenticationService;

    public AluAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public List<NameValuePair> addRequestSignature(final List<NameValuePair> requestParameters) {

        return authenticationService.addSignature(
                requestParameters,
                PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE,
                SignatureCalculator.getParameterNameSortedComparator(),
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
