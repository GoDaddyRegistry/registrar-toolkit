package godaddy.registry.jtoolkit2.tmdb.xml;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import godaddy.registry.jtoolkit2.EPPDateFormatter;
import godaddy.registry.jtoolkit2.tmdb.model.TmNotice;
import godaddy.registry.jtoolkit2.xml.XMLDocument;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TmNoticeXmlParser {

    public TmNotice parse(String noticeXml) throws IOException, TmNoticeXmlParseException {

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new ByteArrayInputStream(noticeXml.getBytes()));

            XMLDocument xmlDocument = new XMLDocument(document.getDocumentElement());

            TmNotice tmNotice = new TmNotice();

            tmNotice.setId(xmlDocument.getNodeValue("/notice/id/text()"));

            String notBeforeDateTime = xmlDocument.getNodeValue("/notice/notBefore/text()");
            tmNotice.setNotBeforeDateTime(EPPDateFormatter.fromXSDateTime(notBeforeDateTime));

            String notAfterDateTime = xmlDocument.getNodeValue("/notice/notAfter/text()");
            tmNotice.setNotAfterDateTime(EPPDateFormatter.fromXSDateTime(notAfterDateTime));

            tmNotice.setLabel(xmlDocument.getNodeValue("/notice/label/text()"));

            TmClaimXmlParser claimXmlParser = new TmClaimXmlParser();

            NodeList elements = xmlDocument.getElements("/notice/claim");
            if (elements != null) {
                for (int i = 0; i < elements.getLength(); i++) {
                    tmNotice.addClaim(claimXmlParser.parse(elements.item(i)));
                }
            }

            return tmNotice;
        } catch (ParserConfigurationException e) {
            throw new TmNoticeXmlParseException(e);
        } catch (SAXException e) {
            throw new TmNoticeXmlParseException(e);
        } catch (XPathExpressionException e) {
            throw new TmNoticeXmlParseException(e);
        }
    }
}
