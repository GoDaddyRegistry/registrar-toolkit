package godaddy.registry.jtoolkit2.se.unspec;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.se.Command;
import godaddy.registry.jtoolkit2.se.CommandExtension;
import godaddy.registry.jtoolkit2.se.ExtendedObjectType;
import godaddy.registry.jtoolkit2.xml.XMLWriter;


/**
 * <p>Extension for the EPP Domain Update command, representing the Update  Domain aspect of the
 * Domain Name Unspec Extension.</p>
 *
 * <p>Use this to identify the unspec associated with this domain name as part of an EPP Domain Update
 * command compliant with RFC5730 and RFC5731. The "extContact" value, WhoisType or Publish values
 * can be supplied depending on the usage.
 * The response expected from a server should be handled by a Domain Update Response.</p>
 *
 * @see godaddy.registry.jtoolkit2.se.DomainUpdateCommand
 * @see godaddy.registry.jtoolkit2.se.Response
 */
public class DomainUpdateCommandUnspecExtension implements CommandExtension {

    private static final long serialVersionUID = 5982521830455586062L;

    private String domainManagerId;
    private String extContactId;
    private WhoisType whoisType;
    private Boolean publish;
    private String resellerName;
    private String resellerUrl;
    private String resellerPhone;
    private String uin;

    @Deprecated
    public DomainUpdateCommandUnspecExtension(String extContactId) {
        this.extContactId = extContactId;
    }

    public DomainUpdateCommandUnspecExtension() {
    }

    @Override
    public void addToCommand(Command command) {
        final XMLWriter xmlWriter = command.getXmlWriter();
        final Element extensionElement = command.getExtensionElement();

        final Element unspecElement = xmlWriter.appendChild(extensionElement, "extension",
                ExtendedObjectType.UNSPEC.getURI());

        StringBuilder unspecValue = new StringBuilder();
        if (domainManagerId != null) {
            unspecValue.append(" domainManager=").append(domainManagerId);
        }
        if (extContactId != null) {
            unspecValue.append(" extContact=").append(extContactId);
        }
        if (whoisType != null) {
            unspecValue.append(" WhoisType=").append(whoisType.getValue());
        }
        if (publish != null) {
            unspecValue.append(" Publish=").append(publish ? "Y" : "N");
        }
        if (resellerName != null) {
            unspecValue.append(" ResellerName=").append(resellerName.replaceAll("\\s", "+"));
        }
        if (resellerUrl != null) {
            unspecValue.append(" ResellerUrl=").append(resellerUrl);
        }
        if (resellerPhone != null) {
            unspecValue.append(" ResellerPhone=").append(resellerPhone);
        }
        if (uin != null) {
            unspecValue.append(" UIN=").append(uin);
        }
        xmlWriter.appendChild(unspecElement, "unspec", ExtendedObjectType.UNSPEC.getURI())
                .setTextContent(unspecValue.toString().trim());
    }

    public void setExtContactId(String extContactId) {
        this.extContactId = extContactId;
    }

    public void setWhoisType(WhoisType whoisType) {
        this.whoisType = whoisType;
    }

    public void setPublish(Boolean publish) {
        this.publish = publish;
    }

    public void setResellerName(String resellerName) {
        this.resellerName = resellerName;
    }

    public void setResellerUrl(String resellerUrl) {
        this.resellerUrl = resellerUrl;
    }

    public void setResellerPhone(String resellerPhone) {
        this.resellerPhone = resellerPhone;
    }

    public void setUin(String uin) {
        this.uin = uin;
    }

    public void setDomainManagerId(String domainManagerId) {
        this.domainManagerId = domainManagerId;
    }
}
