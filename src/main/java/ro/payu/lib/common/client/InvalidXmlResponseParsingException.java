package ro.payu.lib.common.client;

import org.xml.sax.SAXException;

public class InvalidXmlResponseParsingException extends Exception {
    public InvalidXmlResponseParsingException(SAXException e) {
        super(e);
    }

    public InvalidXmlResponseParsingException(String s) {
        super(s);
    }
}
