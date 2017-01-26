package ro.payu.lib.ios;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.w3c.dom.NodeList;
import ro.payu.lib.common.client.ApiResponseParser;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;
import ro.payu.lib.common.client.XmlResponseParser;

import java.util.List;

public class IosResponseParser implements ApiResponseParser {

    private final XmlResponseParser xmlResponseParser;

    public IosResponseParser(XmlResponseParser xmlResponseParser) {
        this.xmlResponseParser = xmlResponseParser;
    }

    public List<NameValuePair> parseResponse(final HttpResponse httpResponse) throws InvalidXmlResponseParsingException {

        final NodeList firstLevelChildren = xmlResponseParser.getNodeList(httpResponse, "Order");

        return xmlResponseParser.getParametersFromNodeList(firstLevelChildren);
    }
}
