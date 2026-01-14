package godaddy.registry.jtoolkit2.se.variant;

import java.util.ArrayList;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.ErrorPkg;
import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.se.IdnDomainVariant;
import godaddy.registry.jtoolkit2.xml.XMLWriter;

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
public class DomainUpdateGdrVariantCommandExtensionV10 implements CommandExtension {

    private static final long serialVersionUID = -3032129437463439207L;

    private final ArrayList<IdnDomainVariant> addVariants = new ArrayList<IdnDomainVariant>();
    private final ArrayList<IdnDomainVariant> remVariants = new ArrayList<IdnDomainVariant>();

    /**
     * @throws IllegalArgumentException if any variants in the list are missing their name or user form.
     */
    public void addVariant(final IdnDomainVariant... variant) {
        addVariantsToList(variant, addVariants);
    }

    /**
     * @throws IllegalArgumentException if any variants in the list are missing their name or user form.
     */
    public void remVariant(final IdnDomainVariant... variant) {
        addVariantsToList(variant, remVariants);
    }

    @Override
    public void addToCommand(final Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();
        final Element updateElement =
                xmlWriter.appendChild(extensionElement, "update",
                        ExtendedObjectType.GDR_VARIANT_V1_0.getURI());

        if (addVariants.size() == 0 && remVariants.size() == 0) {
            throw new IllegalStateException("No variants found");
        }

        writeVariantsToXml(xmlWriter, updateElement, "rem", remVariants);
        writeVariantsToXml(xmlWriter, updateElement, "add", addVariants);
    }

    private void addVariantsToList(final IdnDomainVariant[] variantsToAdd,
                                   final ArrayList<IdnDomainVariant> variantList) {

        for (final IdnDomainVariant variant : variantsToAdd) {
            if (variant == null) {
                throw new IllegalArgumentException(
                        ErrorPkg.getMessage("se.domain.update.idna.variant.missing"));
            }

            if (variant.getName() == null) {
                throw new IllegalArgumentException(
                        ErrorPkg.getMessage("se.domain.update.idna.variant.name.missing"));
            }

            variantList.add(variant);
        }
    }

    private void writeVariantsToXml(final XMLWriter xmlWriter, final Element updateElement,
                                    final String updateType, final ArrayList<IdnDomainVariant> variants) {
        if (variants.size() > 0) {
            final Element element = xmlWriter.appendChild(updateElement, updateType);

            for (final IdnDomainVariant variant : variants) {
                xmlWriter.appendChild(element, "variant").setTextContent(variant.getName());
            }
        }
    }
}
