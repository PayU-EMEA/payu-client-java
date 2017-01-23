package ro.payu.lib.common.authentication;

public class BadResponseSignatureException extends Exception {
    public BadResponseSignatureException(String s) {
        super(s);
    }
}
