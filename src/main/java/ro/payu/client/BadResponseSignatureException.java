package ro.payu.client;

public class BadResponseSignatureException extends Throwable {
    public BadResponseSignatureException(String s) {
        super(s);
    }
}
