package godaddy.registry.jtoolkit2.se.price;

import java.math.BigDecimal;

import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * <p>Extension for the EPP Domain Create command, representing the Create Premium Domain aspect of the
 * Domain Name Price Extension.</p>
 *
 * <p>Use this to acknowledge the price associated with this domain name as part of an EPP Domain Create
 * command compliant with RFC5730 and RFC5731. The "price" values is optional, but if it is
 * supplied, should match the create fee that is set for the domain name for the requested period.
 * The response expected from a server should be handled by a Domain Create Response.</p>
 *
 * @see godaddy.registry.jtoolkit2.se.DomainCreateCommand
 * @see godaddy.registry.jtoolkit2.se.DomainCreateResponse
 * @see <a href="http://godaddyregistry.github.io/doc/price-1.2/price-1.2.html">Domain Name Price Extension
 * Mapping for the Extensible Provisioning Protocol (EPP)</a>
 */

public class DomainCreatePriceV1_2CommandExtension implements CommandExtension {
    private static final long serialVersionUID = 5099827464190747273L;

    private BigDecimal price;

    @Override
    public void addToCommand(Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();
        final Element createElement = xmlWriter.appendChild(extensionElement, "create",
                ExtendedObjectType.PRICEV12.getURI());
        Element ackElement = xmlWriter.appendChild(createElement, "ack");
        if (price != null) {
            xmlWriter.appendChild(ackElement, "price").setTextContent(price.toPlainString());
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
