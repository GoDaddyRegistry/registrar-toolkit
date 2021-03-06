package godaddy.registry.jtoolkit2.se.block;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.xml.XMLWriter;

/**
 * <p>Extension for the EPP Domain Renew command, representing the Renew Block aspects of the Block extension.</p>
 *
 * <p>Use this to identify the block client assigned id that this command is being submitted
 * in as part of an EPP Domain Renew command compliant with RFC5730 and RFC5731. The response expected from a
 * server should be handled by a Domain Renew Response with the Block Renew Response extension.</p>
 *
 * @see godaddy.registry.jtoolkit2.se.DomainRenewCommand
 * @see DomainRenewBlockResponseExtension
 * @see <a href="http://godaddyregistry.github.io/doc/block-1.0/block-1.0.html">Block Extension Mapping for the
 * Extensible Provisioning Protocol (EPP)</a>
 */
public class DomainRenewBlockCommandExtension implements CommandExtension {
    private static final long serialVersionUID = 9181066107823620601L;

    private String id;

    @Override
    public void addToCommand(Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();
        final Element renewElement = xmlWriter.appendChild(extensionElement, "renew",
                ExtendedObjectType.BLOCK.getURI());
        if (id != null) {
            xmlWriter.appendChild(renewElement, "id", ExtendedObjectType.BLOCK.getURI()).setTextContent(id);
        }
    }

    public void setId(String id) {
        this.id = id;
    }
}
