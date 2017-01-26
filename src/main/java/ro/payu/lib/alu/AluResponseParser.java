package ro.payu.lib.alu;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.w3c.dom.NodeList;
import ro.payu.lib.common.client.ApiResponseParser;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;
import ro.payu.lib.common.client.XmlResponseParser;

import java.util.List;

public class AluResponseParser implements ApiResponseParser {

    private final XmlResponseParser xmlResponseParser;

    public AluResponseParser(XmlResponseParser xmlResponseParser) {
        this.xmlResponseParser = xmlResponseParser;
    }

    public List<NameValuePair> parseResponse(final HttpResponse httpResponse) throws InvalidXmlResponseParsingException {

        final NodeList firstLevelChildren = xmlResponseParser.getNodeList(httpResponse, "EPAYMENT");

        return xmlResponseParser.getParametersFromNodeList(firstLevelChildren);
    }
}
