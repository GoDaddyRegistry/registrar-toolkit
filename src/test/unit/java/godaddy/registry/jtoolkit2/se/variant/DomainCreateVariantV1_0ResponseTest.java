package godaddy.registry.jtoolkit2.se.variant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import godaddy.registry.jtoolkit2.se.DomainCreateResponse;
import godaddy.registry.jtoolkit2.se.DomainGdrVariantResponseExtensionV10;
import godaddy.registry.jtoolkit2.se.IdnDomainVariant;
import godaddy.registry.jtoolkit2.se.ResponseExtension;
import godaddy.registry.jtoolkit2.xml.XMLDocument;
import godaddy.registry.jtoolkit2.xml.XMLParser;

public class DomainCreateVariantV1_0ResponseTest {

    private static final XMLParser PARSER = new XMLParser();

    @Test
    public void testGetGdrVariantsV1_0() throws Exception {
        final String dnsForm = "xn--xha91b83h.com";
        final String variantDnsForm = "xn--lga31c50h.com";
        final DomainCreateResponse response = new DomainCreateResponse();
        final DomainGdrVariantResponseExtensionV10 variantsExtension =
                new DomainGdrVariantResponseExtensionV10(ResponseExtension.INFO);
        final XMLDocument doc =
                PARSER.parse(getCreateResponseExpectedXml(dnsForm, variantDnsForm));

        response.registerExtension(variantsExtension);
        response.fromXML(doc);
        assertEquals(dnsForm, response.getName());
        assertTrue("Variant extension should have been initialised",
                variantsExtension.isInitialised());
        final List<IdnDomainVariant> variantList = variantsExtension.getVariants();
        assertEquals("Incorrect number of variants returned", 1, variantList.size());
        assertEquals(variantDnsForm, variantList.get(0).getName());
    }

    @Test
    public void testGetNoGdrVariantsV1_0() throws Exception {
        final String domainName = "xn--xha91b83h.com";
        final DomainCreateResponse response = new DomainCreateResponse();
        final DomainGdrVariantResponseExtensionV10 variantsExtension =
                new DomainGdrVariantResponseExtensionV10(ResponseExtension.CREATE);
        final XMLDocument doc = PARSER.parse(getCreateResponseExpectedXml(domainName, domainName));

        response.registerExtension(variantsExtension);
        response.fromXML(doc);
        assertEquals(domainName, response.getName());
        assertFalse("Variants should not have been initialised", variantsExtension.isInitialised());
    }

    @SuppressWarnings("ParenPad")
    private static String getCreateResponseExpectedXml(final String domainName, final String variantDnsForm) {
        final StringBuilder result = new StringBuilder();
        result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        result.append("<epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\"");
        result.append(    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        result.append(    " xsi:schemaLocation=\"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd\">");
        result.append(    "<response>");
        result.append(        "<result code=\"1000\">");
        result.append(            "<msg>Command completed successfully</msg>");
        result.append(        "</result>");
        result.append(        "<resData>");
        result.append(            "<domain:creData xmlns:domain=\"urn:ietf:params:xml:ns:domain-1.0\"");
        result.append(                " xsi:schemaLocation=\"urn:ietf:params:xml:ns:domain-1.0 domain-1.0.xsd\">");
        result.append(                "<domain:name>" + domainName + "</domain:name>");
        result.append(                "<domain:crDate>1999-04-03T22:00:00.0Z</domain:crDate>");
        result.append(                "<domain:exDate>2001-04-03T22:00:00.0Z</domain:exDate>");
        result.append(            "</domain:creData>");
        result.append(        "</resData>");

        if (variantDnsForm != null) {
            result.append("<extension>");
                result.append("<infData xmlns=\"urn:gdr:params:xml:ns:variant-1.0\"");
                result.append(" xsi:schemaLocation=\"urn:gdr:params:xml:ns:variant-1.0 variant-1.0.xsd\">");
                result.append("<variant>" + variantDnsForm + "</variant>");
                result.append("</infData>");
            result.append("</extension>");
        }

        result.append(        "<trID>");
        result.append(            "<clTRID>ABC-12345</clTRID>");
        result.append(            "<svTRID>54321-XYZ</svTRID>");
        result.append(        "</trID>");
        result.append(    "</response>");
        result.append("</epp>");
        return result.toString();
    }
}
