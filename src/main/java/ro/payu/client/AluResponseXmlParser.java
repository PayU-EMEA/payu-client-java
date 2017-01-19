package ro.payu.client;

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

public class AluResponseXmlParser {
    public List<NameValuePair> parseAluResponse(HttpResponse httpResponse) throws InvalidXmlResponseException {

        final List<NameValuePair> responseParameters = new ArrayList<>();

        final NodeList firstLevelChildren = getNodeList(httpResponse);
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

    public List<NameValuePair> parseIdnResponse(HttpResponse httpResponse) throws InvalidXmlResponseException {

        final List<NameValuePair> responseParameters = new ArrayList<>();

        final NodeList firstLevelChildren = getNodeList(httpResponse);

        if (firstLevelChildren.getLength() > 1) {
            throw new InvalidXmlResponseException("The response XML must contain only the EPAYMENT node");
        }

        final String response = firstLevelChildren.item(0).getTextContent();
        final String[] parts = response.split("\\|");

        if (parts.length < 5) {
            throw new InvalidXmlResponseException("The response XML must contain at least 5 values in the EPAYMENT node");
        }

        responseParameters.add(new BasicNameValuePair("ORDER_REF", parts[0]));
        responseParameters.add(new BasicNameValuePair("RESPONSE_CODE", parts[1]));
        responseParameters.add(new BasicNameValuePair("RESPONSE_MSG", parts[2]));
        responseParameters.add(new BasicNameValuePair("IDN_DATE", parts[3]));
        responseParameters.add(new BasicNameValuePair("ORDER_HASH", parts[4]));

        return responseParameters;
    }

    private NodeList getNodeList(HttpResponse httpResponse) throws InvalidXmlResponseException {
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
            throw new InvalidXmlResponseException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        NodeList firstLevelChildren = responseDocument.getChildNodes();
        if (firstLevelChildren.getLength() != 1 || !firstLevelChildren.item(0).getNodeName().equals("EPAYMENT")) {
            throw new InvalidXmlResponseException("Missing response EPAYMENT node");
        }

        return firstLevelChildren;
    }
}
