package godaddy.registry.jtoolkit2.se;

import org.w3c.dom.Element;

import godaddy.registry.jtoolkit2.ErrorPkg;

/**
 * Use this to request the update of a domain object provisioned in an EPP
 * Registry.  Instances of this class generate RFC5730 and RFC5731 compliant
 * domain update EPP command service elements via the toXML method.  The
 * response expected from a server should be handled by a Response object.
 *
 * @see godaddy.registry.jtoolkit2.se.Response
 */
public class DomainUpdateCommand extends UpdateCommand {
    private static final long serialVersionUID = 2409916920503111390L;

    /**
     * Minimal constructor used primarily for extension updates that are
     * exclusive of standard updates.
     */
    public DomainUpdateCommand(String name) {
        this(name, null, null, null, null);
    }

    /**
     * The complete set of attributes of a domain which may be updated as per
     * RFC5731.
     * @throws IllegalArgumentException if {@code name} is {@code null}.
     */
    public DomainUpdateCommand(String name, String pw,
                               DomainAdd add, DomainRem rem, String registrantID, boolean removeAuthInfo) {
        super(StandardObjectType.DOMAIN, name);

        if (name == null) {
            throw new IllegalArgumentException(ErrorPkg.getMessage(
                    "se.domain.update.name.missing"));
        }

        if (add != null) {
            add.appendToElement(xmlWriter, objElement);
        }

        if (rem != null) {
            rem.appendToElement(xmlWriter, objElement);
        }

        if (registrantID != null || pw != null || removeAuthInfo) {
            Element chg = xmlWriter.appendChild(objElement, "chg");
            if (registrantID != null) {
                xmlWriter.appendChild(
                        chg,
                        "registrant").setTextContent(registrantID);
            }
            if (pw != null) {
                xmlWriter.appendChild(
                        xmlWriter.appendChild(
                            chg,
                            "authInfo"),
                    "pw").setTextContent(pw);
            } else if (removeAuthInfo) {
                xmlWriter.appendChild(
                        xmlWriter.appendChild(
                            chg,
                            "authInfo"),
                    "null");
            }

        }
    }

    public DomainUpdateCommand(String name, String pw,
            DomainAdd add, DomainRem rem, String registrantID) {
        this(name, pw, add, rem, registrantID, false);
    }
}

