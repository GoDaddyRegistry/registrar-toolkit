package godaddy.registry.jtoolkit2.se.association;

import static godaddy.registry.jtoolkit2.se.ExtendedObjectType.ASSOCIATION;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.DomainCreateCommand;
import godaddy.registry.jtoolkit2.xml.XMLWriter;

/**
 * <p>Extension for the EPP Domain Create command, representing the Create aspects of the Association extension.</p>
 *
 * <p>Use this to identify the association contact client assigned id that this command is being submitted
 * in as part of an EPP Domain Create command compliant with RFC5730 and RFC5731. The response expected from a
 * server should be handled by a Domain Create Response.</p>
 *
 * @see DomainCreateCommand
 */
public class AssociationCreateCommandExtension implements CommandExtension {
    private static final long serialVersionUID = 4324879283895987704L;

    private final AssociationContact contact;

    public AssociationCreateCommandExtension(AssociationContact contact) {
        super();
        this.contact = contact;
    }

    @Override
    public void addToCommand(Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();
        final Element createElement = xmlWriter.appendChild(extensionElement, "create", ASSOCIATION.getURI());
        contact.addContact(xmlWriter, createElement);
    }

}
