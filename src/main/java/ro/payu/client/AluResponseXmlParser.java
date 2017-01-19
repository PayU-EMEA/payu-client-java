package ro.payu.client;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
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
            throw new InvalidXmlResponseException("Missing ALU response EPAYMENT node");
        }

        final List<NameValuePair> responseParameters = new ArrayList<>();
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
