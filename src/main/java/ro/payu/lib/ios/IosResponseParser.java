package ro.payu.lib.ios;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ro.payu.lib.common.client.ApiResponseParser;
import ro.payu.lib.common.client.InvalidXmlResponseParsingException;
import ro.payu.lib.common.client.XmlResponseParser;

import java.util.ArrayList;
import java.util.List;

public class IosResponseParser implements ApiResponseParser {

    private XmlResponseParser xmlResponseParser;

    public IosResponseParser(XmlResponseParser xmlResponseParser) {
        this.xmlResponseParser = xmlResponseParser;
    }

    public List<NameValuePair> parseResponse(final HttpResponse httpResponse) throws InvalidXmlResponseParsingException {

        final List<NameValuePair> responseParameters = new ArrayList<>();

        final NodeList firstLevelChildren = xmlResponseParser.getNodeList(httpResponse, "Order");
        final NodeList nodes = firstLevelChildren.item(0).getChildNodes();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String nodeName = node.getNodeName();
                String nodeValue = node.getTextContent();
                responseParameters.add(new BasicNameValuePair(nodeName, nodeValue));
            }
        }

        return responseParameters;
    }
}
