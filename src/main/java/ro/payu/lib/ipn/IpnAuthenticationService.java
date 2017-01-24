package ro.payu.lib.ipn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.InvalidSignatureException;
import ro.payu.lib.common.authentication.SignatureCalculator;
import ro.payu.lib.common.authentication.VerifyAuthenticationService;

import java.util.*;

public class IpnAuthenticationService implements VerifyAuthenticationService {

    private static final String REQUEST_SIGNATURE_NAME = "HASH";

    private static final List<String> REQUEST_PARAMETERS_EXCLUDED_FOR_SIGNATURE = Collections.singletonList(REQUEST_SIGNATURE_NAME);

    private static final List<String> REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE = Arrays.asList(
            "IPN_PID[]",
            "IPN_PNAME[]",
            "IPN_DATE"
    );

    private AuthenticationService authenticationService;

    public IpnAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void verifySignature(final List<NameValuePair> parameters) throws InvalidSignatureException {

        authenticationService.verifySignature(
                parameters,
                REQUEST_PARAMETERS_EXCLUDED_FOR_SIGNATURE,
                SignatureCalculator.getKeepSameParameterOrderComparator(),
                REQUEST_SIGNATURE_NAME
        );
    }

    String computeResponseSignature(final List<NameValuePair> requestParameters, final String responseDate) {

        List<String> requiredParameterNames = new ArrayList<>(REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE);

        final Map<String, String> neededParamsMap = new HashMap<>();

        for (NameValuePair pair : requestParameters) {
            if (requiredParameterNames.contains(pair.getName())) {
                neededParamsMap.put(pair.getName(), pair.getValue());
                requiredParameterNames.remove(pair.getName()); // we need only the first parameter with each name
            }
        }

        final List<NameValuePair> parametersToBeSigned = new ArrayList<>();

        // we need to keep a predefined order of parameters
        for (String name : REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE) {
            if (neededParamsMap.containsKey(name)) {
                parametersToBeSigned.add(new BasicNameValuePair(name, neededParamsMap.get(name)));
            }
        }
        parametersToBeSigned.add(new BasicNameValuePair("DATE", responseDate));

        return authenticationService.computeSignature(
                parametersToBeSigned,
                SignatureCalculator.getKeepSameParameterOrderComparator()
        );
    }
}
