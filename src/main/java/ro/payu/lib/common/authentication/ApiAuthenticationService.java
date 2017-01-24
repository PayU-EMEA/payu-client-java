package ro.payu.lib.common.authentication;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ApiAuthenticationService extends VerifyAuthenticationService {

    List<NameValuePair> addSignature(final List<NameValuePair> parameters);
}
