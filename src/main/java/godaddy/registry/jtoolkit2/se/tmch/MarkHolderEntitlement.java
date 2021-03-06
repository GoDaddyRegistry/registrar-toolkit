package godaddy.registry.jtoolkit2.se.tmch;

/**
 * Represents a holder's entitlement from the Trademark Clearing House extension,
 * represented by the "entitlement" element of type "entitlementType"
 * in the "urn:ietf:params:xml:ns:mark-1.0" namespace, defined in the "mark-1.0.xsd" schema.
 *
 */
public enum MarkHolderEntitlement {
    owner, assignee, licensee;
}
