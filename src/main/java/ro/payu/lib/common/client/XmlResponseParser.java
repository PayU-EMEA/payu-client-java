package ro.payu.lib.common.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlResponseParser {

    public NodeList getNodeList(HttpResponse httpResponse, String maineNodeName) throws InvalidXmlResponseParsingException {
        final DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        final Document responseDocument;
        try {
            responseDocument = builder.parse(httpResponse.getEntity().getContent());
        } catch (SAXException e) {
            throw new InvalidXmlResponseParsingException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NodeList firstLevelChildren = responseDocument.getChildNodes();
        if (firstLevelChildren.getLength() != 1 || !firstLevelChildren.item(0).getNodeName().equals(maineNodeName)) {
            throw new InvalidXmlResponseParsingException("Missing response " + maineNodeName + " node");
        }

        return firstLevelChildren;
    }

    public List<NameValuePair> getParametersFromNodeList(NodeList firstLevelChildren) {
        final NodeList nodes = firstLevelChildren.item(0).getChildNodes();

        final List<NameValuePair> responseParameters = new ArrayList<>();

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
