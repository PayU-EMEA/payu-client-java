package ro.payu.lib.ios;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.ApiAuthenticationService;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.SignatureCalculator;

import java.util.Collections;
import java.util.List;

public class IosAuthenticationService implements ApiAuthenticationService {

    private static final String REQUEST_SIGNATURE_NAME = "HASH";
    private static final String RESPONSE_SIGNATURE_NAME = "HASH";

    private static final List<String> PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE = Collections.singletonList(REQUEST_SIGNATURE_NAME);
    private static final List<String> PARAMETERS_EXCLUDED_FROM_RESPONSE_SIGNATURE = Collections.singletonList(RESPONSE_SIGNATURE_NAME);

    final private AuthenticationService authenticationService;

    public IosAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public List<NameValuePair> addSignature(final List<NameValuePair> parameters) {

        return authenticationService.addSignature(
                parameters,
                PARAMETERS_EXCLUDED_FROM_REQUEST_SIGNATURE,
                SignatureCalculator.getKeepSameParameterOrderComparator(),
                REQUEST_SIGNATURE_NAME
        );
    }

    public void verifySignature(final List<NameValuePair> parameters) throws InvalidSignatureException {

        authenticationService.verifySignature(
                parameters,
                PARAMETERS_EXCLUDED_FROM_RESPONSE_SIGNATURE,
                SignatureCalculator.getKeepSameParameterOrderComparator(),
                RESPONSE_SIGNATURE_NAME
        );
    }
}
