package ro.payu.client;

public class BadResponseSignatureException extends Exception {
    public BadResponseSignatureException(String s) {
        super(s);
    }
}
