package ro.payu.client;

import org.xml.sax.SAXException;

public class InvalidXmlResponseException extends Throwable {
    public InvalidXmlResponseException(SAXException e) {
        super(e);
    }

    public InvalidXmlResponseException(String s) {
        super(s);
    }
}
