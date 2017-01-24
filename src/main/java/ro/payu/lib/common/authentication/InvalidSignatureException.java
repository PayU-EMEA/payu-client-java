package ro.payu.lib.common.authentication;

public class InvalidSignatureException extends Exception {
    public InvalidSignatureException(String s) {
        super(s);
    }
}
