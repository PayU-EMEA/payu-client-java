package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;

import java.util.List;

public interface VerifyAuthenticationService {

    void verifySignature(final List<NameValuePair> parameters) throws InvalidSignatureException;
}
