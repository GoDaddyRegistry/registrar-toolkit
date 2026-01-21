package godaddy.registry.jtoolkit2.se;

/**
 * <p>Extension for the EPP Domain Create and Domain Info response, representing the Variant Info
 * aspect of the Domain Name Variant extension (v1.1).</p>
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
 * @see <a href="http://godaddyregistry.github.io/doc/variant-1.1/variant-1.1.html">Domain Name Variant Extension
 * Mapping for the Extensible Provisioning Protocol (EPP)</a>
 */
public final class DomainVariantResponseExtensionV1_1 extends
        AbstractDomainVariantResponseExtension {

    private static final long serialVersionUID = -2441248857298156911L;

    public DomainVariantResponseExtensionV1_1(String responseType) {
        super(responseType);
    }

    @Override
    protected String getVariantPrefix() {
        return ExtendedObjectType.VARIANT_V1_1.getName();
    }
}
