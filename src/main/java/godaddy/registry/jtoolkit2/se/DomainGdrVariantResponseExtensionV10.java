package godaddy.registry.jtoolkit2.se;

/**
 * <p>Extension for the EPP Domain Create and Domain Info response, representing the Variant Info
 * aspect of the Domain Name GDR Variant extension (v1.0).</p>
 *
 * <p>Use this to access a list of the activated variants for a domain as provided in an EPP Domain Info or
 * Domain Create response compliant with RFC5730 and RFC5731. Such a service element is sent by a compliant
 * EPP server in response to a valid Domain Info or Domain Create command with the Domain Info Variant
 * Command extension.</p>
 *
 * <p>For flexibility, this implementation extracts the data from the response using XPath queries, the expressions
 * for which are defined statically.</p>
 *
 * @see godaddy.registry.jtoolkit2.se.variant.DomainUpdateVariantCommandExtension
 * @see <a href="http://godaddyregistry.github.io/doc/gdr-variant-1.0/gdr-variant-1.0.html">Domain Name GDR Variant
 * Extension Mapping for the Extensible Provisioning Protocol (EPP)</a>
 */
public final class DomainGdrVariantResponseExtensionV10 extends
        AbstractDomainVariantResponseExtension {

    public DomainGdrVariantResponseExtensionV10(String responseType) {
        super(responseType);
    }

    @Override
    protected String getVariantPrefix() {
        return ExtendedObjectType.GDR_VARIANT_V1_0.getName();
    }
}
