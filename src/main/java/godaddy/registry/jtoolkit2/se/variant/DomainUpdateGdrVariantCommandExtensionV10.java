package godaddy.registry.jtoolkit2.se.variant;

import godaddy.registry.jtoolkit2.se.ExtendedObjectType;

/**
 * <p>Extension for the EPP Domain Update command, representing the Update Variants aspect of the
 * Domain Name GDR Variant extension (v1.0)</p>
 *
 * <p>Use this to add or remove variants for a domain as part of an EPP Domain Update
 * command compliant with RFC5730 and RFC5731. The response expected from a server
 * should be handled by a generic Response.</p>
 *
 * <p>This extension will throw an {@link IllegalArgumentException} if no variants
 * are added or removed as part of the "Update Variants" operation.</p>
 *
 * @see godaddy.registry.jtoolkit2.se.DomainUpdateCommand
 * @see godaddy.registry.jtoolkit2.se.Response
 * @see <a href="http://godaddyregistry.github.io/doc/gdr-variant-1.0/gdr-variant-1.0.html">Domain Name Variant
 * Extension Mapping for the Extensible Provisioning Protocol (EPP)</a>
 */
public class DomainUpdateGdrVariantCommandExtensionV10 extends AbstractDomainUpdateVariantCommandExtension {

    private static final long serialVersionUID = -3032129437463439207L;

    @Override
    protected String getNamespaceURI() {
        return ExtendedObjectType.GDR_VARIANT_V1_0.getURI();
    }
}
