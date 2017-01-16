package ro.payu.authentication;

import org.apache.http.NameValuePair;

import java.util.List;

public final class ApiAuthenticationService {

    private final AuthenticationService authenticationService;

    private final String secretKey;

    public ApiAuthenticationService(String secretKey, AuthenticationService authenticationService) {
        this.secretKey = secretKey;

        this.authenticationService = authenticationService;
    }

    public String computeAluRequestSignature(List<NameValuePair> parameters) {
        return authenticationService.computeSignature(parameters, secretKey);
    }
}
