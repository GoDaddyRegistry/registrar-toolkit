package com.ausregistry.jtoolkit2.se.blocked;

import com.ausregistry.jtoolkit2.se.Command;
import com.ausregistry.jtoolkit2.se.CommandExtension;
import com.ausregistry.jtoolkit2.se.ExtendedObjectType;
import com.ausregistry.jtoolkit2.xml.XMLWriter;
import org.w3c.dom.Element;

/**
 * <p>Extension for the EPP Domain delete command, representing the Delete Blocked Domain Name aspect of the
 * Blocked Domain Name extension.</p>
 *
 * <p>Use this to mark the ID of a Blocked Domain Name to delete as part of an EPP Domain Delete command
 * compliant with RFC5730 and RFC5731. The response expected from a server should be
 * handled by a generic Response.</p>
 *
 * @see com.ausregistry.jtoolkit2.se.DomainDeleteCommand
 * @see com.ausregistry.jtoolkit2.se.Response
 * @see <a href="http://ausregistry.github.io/doc/blocked-1.0/blocked-1.0.html">Blocked Domain Name
 * Extension Mapping for the Extensible Provisioning Protocol (EPP)</a>
 */
public class DomainDeleteBlockCommandExtension implements CommandExtension {
    private static final long serialVersionUID = -3106443818428865374L;

    private String id;

    @Override
    public void addToCommand(Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();
        final Element infoElement = xmlWriter.appendChild(extensionElement, "delete",
                ExtendedObjectType.BLOCKED.getURI());

        xmlWriter.appendChild(infoElement, "id", ExtendedObjectType.BLOCKED.getURI()).setTextContent(id);
    }

    public void setId(String id) {
        this.id = id;
    }
}
