package ro.payu.lib.ipn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.SignatureCalculator;

import java.util.*;

public class IpnAuthenticationService {

    private static final List<String> REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE = Arrays.asList("IPN_PID[]", "IPN_PNAME[]", "IPN_DATE");

    private AuthenticationService authenticationService;

    public IpnAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public String computeResponseSignature(final List<NameValuePair> requestParameters, final String responseDate) {

        List<String> requiredParameterNames = new ArrayList<>(REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE);

        final Map<String, String> neededParamsMap = new HashMap<>();

        for (NameValuePair pair : requestParameters) {
            if (requiredParameterNames.contains(pair.getName())) {
                neededParamsMap.put(pair.getName(), pair.getValue());
                requiredParameterNames.remove(pair.getName()); // we need only the first parameter with each name
            }
        }

        final List<NameValuePair> parametersToBeSigned = new ArrayList<>();
        for (String name : REQUEST_PARAMETERS_INCLUDED_IN_RESPONSE_SIGNATURE) {
            // we need to keep a predefined order of parameters
            parametersToBeSigned.add(new BasicNameValuePair(name, neededParamsMap.get(name)));
        }
        parametersToBeSigned.add(new BasicNameValuePair("DATE", responseDate));

        return authenticationService.computeSignature(
                parametersToBeSigned,
                SignatureCalculator.getKeepSameParameterOrderComparator()
        );
    }
}
