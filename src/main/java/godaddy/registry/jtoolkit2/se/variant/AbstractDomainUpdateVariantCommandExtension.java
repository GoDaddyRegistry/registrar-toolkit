package godaddy.registry.jtoolkit2.se.variant;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.ErrorPkg;
import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.IdnDomainVariant;
import godaddy.registry.jtoolkit2.xml.XMLWriter;

public abstract class AbstractDomainUpdateVariantCommandExtension implements CommandExtension {

    private static final long serialVersionUID = -3032129437463439207L;

    private final List<IdnDomainVariant> addVariants = new ArrayList<>();
    private final List<IdnDomainVariant> remVariants = new ArrayList<>();

    protected abstract String getNamespaceURI();

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
                xmlWriter.appendChild(extensionElement, "update", getNamespaceURI());

        if (addVariants.isEmpty() && remVariants.isEmpty()) {
            throw new IllegalStateException("No variants found");
        }

        writeVariantsToXml(xmlWriter, updateElement, "rem", remVariants);
        writeVariantsToXml(xmlWriter, updateElement, "add", addVariants);
    }

    private void addVariantsToList(final IdnDomainVariant[] variantsToAdd,
                                   final List<IdnDomainVariant> variantList) {
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
                                    final String updateType, final List<IdnDomainVariant> variants) {
        if (!variants.isEmpty()) {
            final Element element = xmlWriter.appendChild(updateElement, updateType);
            for (final IdnDomainVariant variant : variants) {
                xmlWriter.appendChild(element, "variant").setTextContent(variant.getName());
            }
        }
    }
}
