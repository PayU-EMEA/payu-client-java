package ro.payu.lib.irn;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.NodeList;
import ro.payu.lib.common.client.ApiResponseParser;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;
import ro.payu.lib.common.client.XmlResponseParser;

import java.util.ArrayList;
import java.util.List;

public class IrnResponseParser implements ApiResponseParser {

    private XmlResponseParser xmlResponseParser;

    public IrnResponseParser(XmlResponseParser xmlResponseParser) {
        this.xmlResponseParser = xmlResponseParser;
    }

    public List<NameValuePair> parseResponse(HttpResponse httpResponse) throws InvalidXmlResponseParsingException {

        final List<NameValuePair> responseParameters = new ArrayList<>();

        final NodeList firstLevelChildren = xmlResponseParser.getNodeList(httpResponse, "EPAYMENT");

        if (firstLevelChildren.getLength() > 1) {
            throw new InvalidXmlResponseParsingException("The response XML must contain only the EPAYMENT node");
        }

        final String response = firstLevelChildren.item(0).getTextContent();
        final String[] parts = response.split("\\|");

        if (parts.length < 5) {
            throw new InvalidXmlResponseParsingException("The response XML must contain at least 5 values in the EPAYMENT node");
        }

        responseParameters.add(new BasicNameValuePair("ORDER_REF", parts[0]));
        responseParameters.add(new BasicNameValuePair("RESPONSE_CODE", parts[1]));
        responseParameters.add(new BasicNameValuePair("RESPONSE_MSG", parts[2]));
        responseParameters.add(new BasicNameValuePair("IRN_DATE", parts[3]));
        responseParameters.add(new BasicNameValuePair("ORDER_HASH", parts[4]));

        return responseParameters;
    }
}
