package godaddy.registry.jtoolkit2.se.tmch;

import static godaddy.registry.jtoolkit2.se.ExtendedObjectType.SIGNED_MARK_DATA;
import static godaddy.registry.jtoolkit2.se.ExtendedObjectType.XML_DSIG;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.codec.DecoderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import godaddy.registry.jtoolkit2.se.tmch.exception.ExpiredSignedMarkDataException;
import godaddy.registry.jtoolkit2.se.tmch.exception.InvalidSignedMarkDataException;
import godaddy.registry.jtoolkit2.se.tmch.exception.NotYetValidSignedMarkDataException;
import godaddy.registry.jtoolkit2.se.tmch.exception.SmdSignatureInvalidException;
import godaddy.registry.jtoolkit2.se.tmch.exception.SmdSignatureMissingException;
import godaddy.registry.jtoolkit2.se.tmch.exception.TmchCertificateInvalidTypeException;
import godaddy.registry.jtoolkit2.se.tmch.exception.TmchCertificateRevokedException;
import godaddy.registry.jtoolkit2.se.tmch.exception.TmchInvalidCertificateException;
import godaddy.registry.jtoolkit2.se.tmch.exception.TmchSmdRevokedException;
import godaddy.registry.jtoolkit2.xml.NamespaceContextImpl;
import godaddy.registry.jtoolkit2.xml.ParsingException;

/**
 * This defines the operations to facilitate validation and parsing of signed mark data for TMCH.
 */
public class TmchValidatingParser extends TmchXmlParser {

    public static final String CERTIFICATE_BEGIN_DELIMITER = "-----BEGIN CERTIFICATE-----\n";
    public static final String CERTIFICATE_END_DELIMITER = "\n-----END CERTIFICATE-----\n";

    private static final String SMD_BASE_EXPR = "/" + SIGNED_MARK_DATA.getName() + ":";
    private static final String DS_BASE_EXPR = "/" + XML_DSIG.getName() + ":";
    private static final String SIGNED_MARK = "signedMark";

    private static final String SMD_NOT_BEFORE_EXPR = SMD_BASE_EXPR + SIGNED_MARK + SMD_BASE_EXPR + "notBefore";
    private static final String SMD_NOT_AFTER_EXPR = SMD_BASE_EXPR + SIGNED_MARK + SMD_BASE_EXPR + "notAfter";
    private static final String SMD_ID_EXPR = SMD_BASE_EXPR + SIGNED_MARK + SMD_BASE_EXPR + "id";
    private static final String SMD_DS_SIGNATURE_EXPR = SMD_BASE_EXPR + SIGNED_MARK + DS_BASE_EXPR + "Signature";
    private static final String CERTIFICATE_XPATH_EXPR = SMD_BASE_EXPR + SIGNED_MARK
            + DS_BASE_EXPR + "Signature" + DS_BASE_EXPR + "KeyInfo" + DS_BASE_EXPR + "X509Data"
            + DS_BASE_EXPR + "X509Certificate";

    private final List<String> smdrlIdList = new ArrayList<String>();

    private final CRL certRevocationList;
    private final CertificateFactory certificateFactory;
    private final KeyStore icannCertificateTrustStore;

    private XPath xPath;
    private final XMLSignatureFactory xmlSignatureFactory;

    /**
     * Instantiate a TmchValidatingParser which validates and parses an encoded SMD.
     *
     * @param certificateRevocationList the Certificate Revocation List
     * @param smdRevocationList the SMD Revocation List
     * @param tmchIssuingAuthorityCert the SMD Issuing Authority Certificate
     * @throws CertificateException if an exception occurs while processing CRL or Issuing Authority certificate
     * @throws CRLException if an exception occurs while processing CRL
     * @throws IOException if an exception occurs while processing SMDRL or Issuing Authority certificate
     * @throws KeyStoreException if an exception occurs while processing Issuing Authority certificate
     * @throws NoSuchAlgorithmException if an exception occurs while processing Issuing Authority certificate
     */
    public TmchValidatingParser(InputStream certificateRevocationList, InputStream smdRevocationList,
                                InputStream tmchIssuingAuthorityCert)
            throws CertificateException, CRLException, IOException, KeyStoreException, NoSuchAlgorithmException {

        certificateFactory = CertificateFactory.getInstance("X.509");
        certRevocationList = certificateFactory.generateCRL(certificateRevocationList);

        xPath = XPathFactory.newInstance().newXPath();
        xPath.setNamespaceContext(new NamespaceContextImpl());

        readSmdRevocationList(smdRevocationList);

        Certificate icannTmchCACertificate = certificateFactory.generateCertificate(tmchIssuingAuthorityCert);

        icannCertificateTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        icannCertificateTrustStore.load(null, null);
        icannCertificateTrustStore.setCertificateEntry("tmchCA", icannTmchCACertificate);

        xmlSignatureFactory = XMLSignatureFactory.getInstance();
    }

    private void readSmdRevocationList(InputStream smdRevocationList) throws IOException {
        BufferedReader smdrlReader = new BufferedReader(new InputStreamReader(smdRevocationList));
        smdrlReader.readLine();
        smdrlReader.readLine();
        String line;
        while ((line = smdrlReader.readLine()) != null) {
            String[] smdrlTokens = line.split(",");
            smdrlIdList.add(smdrlTokens[0]);
        }
    }

    /**
     * Decodes and validates the provided base64-encoded SMD based on the provided date.
     * If the SMD passes validation, it is parsed into a SignedMarkData bean.
     *
     * @param encodedSignedMarkData Input stream to the base64-encoded SMD to be validated.
     * @param dateForValidation The date against which the input SMD needs to be validated against.
     * @return if the input SMD is valid the parsed SignedMarkDate object
     * @throws IOException In case the input stream cannot be read
     * @throws ParsingException In case an error occurs while parsing
     * @throws DecoderException In case the stream cannot be decoded
     * @throws XPathExpressionException if an exception occurs while parsing the decoded SMD
     * @throws ParserConfigurationException if an exception occurs while parsing the decoded SMD
     * @throws NoSuchAlgorithmException if an exception occurs while parsing the SMD certificate
     * @throws CertificateException if an exception occurs while validating SMD certificate
     * @throws KeyStoreException if an exception occurs while validating SMD certificate
     * @throws InvalidAlgorithmParameterException if an exception occurs while validating SMD certificate
     */
    public SignedMarkData validateAndParseEncodedSignedMarkData(InputStream encodedSignedMarkData,
                                                                Date dateForValidation)
            throws ParsingException, IOException, DecoderException, ParserConfigurationException,
            XPathExpressionException, NoSuchAlgorithmException, CertificateException, KeyStoreException,
            InvalidAlgorithmParameterException, SAXException {
        return validateEncodedSignedMarkDataForDate(encodedSignedMarkData, dateForValidation);
    }

    /**
     * Decodes and validates the provided base64-encoded SMD against the current date.
     * If the SMD passes validation, it is parsed into a SignedMarkData bean.
     *
     * @param encodedSignedMarkData Input stream to the base64-encoded SMD to be validated
     * @return if the input SMD is valid the parsed SignedMarkDate object
     * @throws IOException In case the input stream cannot be read
     * @throws DecoderException In case the stream cannot be decoded
     * @throws XPathExpressionException if an exception occurs while parsing the decoded SMD
     * @throws ParserConfigurationException if an exception occurs while parsing the decoded SMD
     * @throws NoSuchAlgorithmException if an exception occurs while parsing the SMD certificate
     * @throws CertificateException if an exception occurs while validating SMD certificate
     * @throws KeyStoreException if an exception occurs while validating SMD certificate
     * @throws InvalidAlgorithmParameterException if an exception occurs while validating SMD certificate
     */
    public SignedMarkData validateAndParseEncodedSignedMarkData(InputStream encodedSignedMarkData) throws
            IOException, DecoderException, ParserConfigurationException, XPathExpressionException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException, InvalidAlgorithmParameterException,
            SAXException {
        return validateEncodedSignedMarkDataForDate(encodedSignedMarkData, new Date());
    }

    private SignedMarkData validateEncodedSignedMarkDataForDate(InputStream encodedSignedMarkData,
                                                                Date dateForValidation) throws
            IOException, DecoderException, ParserConfigurationException, XPathExpressionException,
            NoSuchAlgorithmException, CertificateException, KeyStoreException, InvalidAlgorithmParameterException,
            SAXException {

        byte[] dataBytes = decodeSignedMarkData(encodedSignedMarkData);

        Document document = null;
        try {
            document = loadSmdXmlIntoDocument(new ByteArrayInputStream(dataBytes));
        } catch (SAXException e) {
            throw new InvalidSignedMarkDataException(e);
        }

        Node signatureNode = extractSignatureNode(document);
        X509Certificate x509Certificate = extractCertificateFromDocument(document);

        validateSignature(signatureNode, x509Certificate);
        assertCertificateIsValid(dateForValidation, x509Certificate);
        assertCertificateNotRevoked(x509Certificate);
        assertSmdNotRevoked(document);

        Calendar notBeforeDate = DatatypeConverter.parseDate(xPath.evaluate(SMD_NOT_BEFORE_EXPR, document));
        Calendar notAfterDate = DatatypeConverter.parseDate(xPath.evaluate(SMD_NOT_AFTER_EXPR, document));

        if (dateForValidation.before(notBeforeDate.getTime())) {
            throw new NotYetValidSignedMarkDataException(notBeforeDate.getTime());
        }

        if (dateForValidation.after(notAfterDate.getTime())) {
            throw new ExpiredSignedMarkDataException(notAfterDate.getTime());
        }

        return parseDecodedSignedMarkData(new ByteArrayInputStream(dataBytes));
    }

    private void assertSmdNotRevoked(Document document) throws XPathExpressionException {
        String smdId = xPath.evaluate(SMD_ID_EXPR, document);

        if (smdrlIdList.contains(smdId)) {
            throw new TmchSmdRevokedException(smdId);
        }
    }

    private Node extractSignatureNode(Document document) throws XPathExpressionException {
        Node signatureNode = (Node) xPath.evaluate(SMD_DS_SIGNATURE_EXPR, document, XPathConstants.NODE);

        if (signatureNode == null) {
            throw new SmdSignatureMissingException();
        }
        return signatureNode;
    }

    private void validateSignature(Node signatureNode, X509Certificate x509Certificate) {
        DOMValidateContext validateContext = new DOMValidateContext(x509Certificate.getPublicKey(), signatureNode);
        signatureNode.getOwnerDocument().getDocumentElement().setIdAttribute("id", true);

        XMLSignature xmlSignature;
        try {
            xmlSignature = xmlSignatureFactory.unmarshalXMLSignature(validateContext);
        } catch (MarshalException e) {
            throw new InvalidSignedMarkDataException(e);
        }

        try {
            if (!xmlSignature.validate(validateContext)) {
                throw new SmdSignatureInvalidException();
            }
        } catch (XMLSignatureException e) {
            throw new InvalidSignedMarkDataException(e);
        }
    }

    private void assertCertificateIsValid(Date currentDate, X509Certificate x509Certificate) throws
            NoSuchAlgorithmException,
            CertificateException,
            KeyStoreException,
            InvalidAlgorithmParameterException {
        CertPathValidator certPathValidator = CertPathValidator.getInstance("PKIX");

        CertPath certPath = certificateFactory.generateCertPath(Arrays.asList(x509Certificate));
        PKIXParameters pkixParameters = new PKIXParameters(icannCertificateTrustStore);
        pkixParameters.setRevocationEnabled(false);
        pkixParameters.setDate(currentDate);
        try {
            certPathValidator.validate(certPath, pkixParameters);
        } catch (CertPathValidatorException e) {
            throw new TmchInvalidCertificateException(x509Certificate, e);
        }
    }

    private X509Certificate assertCertificateNotRevoked(X509Certificate x509Certificate) {

        if (certRevocationList.isRevoked(x509Certificate)) {
            throw new TmchCertificateRevokedException(x509Certificate);
        }
        return x509Certificate;
    }

    private X509Certificate extractCertificateFromDocument(Document document) throws XPathExpressionException {
        String certificateString = xPath.evaluate(CERTIFICATE_XPATH_EXPR, document);
        String certificateEntireContent = CERTIFICATE_BEGIN_DELIMITER + certificateString + CERTIFICATE_END_DELIMITER;

        try {
            Certificate certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(
                    certificateEntireContent.getBytes()));
            if (!X509Certificate.class.isAssignableFrom(certificate.getClass())) {
                throw new TmchCertificateInvalidTypeException(certificate.getClass());
            }
            return (X509Certificate) certificate;
        } catch (CertificateException e) {
            throw new InvalidSignedMarkDataException(e);
        }
    }
}