package ro.payu.lib.common.client;

import org.apache.http.HttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlResponseParser {

    public NodeList getNodeList(HttpResponse httpResponse) throws InvalidXmlResponseParsingException {
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
        if (firstLevelChildren.getLength() != 1 || !firstLevelChildren.item(0).getNodeName().equals("EPAYMENT")) {
            throw new InvalidXmlResponseParsingException("Missing response EPAYMENT node");
        }

        return firstLevelChildren;
    }
}
