package ro.payu.lib.ipn;

import org.apache.http.NameValuePair;
import ro.payu.lib.common.authentication.AuthenticationService;
import ro.payu.lib.common.authentication.SignatureCalculator;

import java.util.ArrayList;
import java.util.List;

public class IpnAuthenticationService {

    private AuthenticationService authenticationService;

    public IpnAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public String computeResponseSignature(final List<NameValuePair> requestParameters, final String responseDate) {

        final List<NameValuePair> parametersToBeSigned = new ArrayList<>();


        return authenticationService.computeSignature(parametersToBeSigned, SignatureCalculator.getKeepSameParameterOrderComparator());
    }
}
