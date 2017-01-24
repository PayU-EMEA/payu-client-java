package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ApiAuthenticationService {

    List<NameValuePair> addRequestSignature(final List<NameValuePair> requestParameters);

    void verifyResponseSignature(final List<NameValuePair> responseParameters) throws InvalidSignatureException;
}
