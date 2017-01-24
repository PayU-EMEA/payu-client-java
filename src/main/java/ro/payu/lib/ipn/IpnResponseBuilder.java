package ro.payu.lib.ipn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import ro.payu.lib.common.server.ResponseBuilder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class IpnResponseBuilder implements ResponseBuilder {

    private IpnAuthenticationService ipnAuthenticationService;

    public IpnResponseBuilder(IpnAuthenticationService ipnAuthenticationService) {
        this.ipnAuthenticationService = ipnAuthenticationService;
    }

    @Override
    public List<NameValuePair> getHeaders() {

        List<NameValuePair> headers = new ArrayList<>();
        headers.add(new BasicNameValuePair("Content-Type", "text/xml"));

        return headers;
    }

    @Override
    public String getBody(List<NameValuePair> requestParameters) {
        final DocumentBuilder builder;

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        Document document = builder.newDocument();

        Element rootElement = document.createElement("EPAYMENT");
        document.appendChild(rootElement);

        String responseDate = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String responseHash = ipnAuthenticationService.computeResponseSignature(requestParameters, responseDate);

        Text textElement = document.createTextNode(responseDate + "|" + responseHash);
        rootElement.appendChild(textElement);

        StringWriter stringWriter = new StringWriter();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

        return stringWriter.toString();
    }
}
