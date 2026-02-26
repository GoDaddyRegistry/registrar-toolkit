package godaddy.registry.jtoolkit2.xml;

import java.util.Collections;
import java.util.Set;

public final class XmlOutputConfig {

    /* Enable the output of XML namespace prefix when the flag is set to true.
     */
    private boolean prefixNamespace;
    private Set<String> skipNamespacePrefixForUris;

    private XmlOutputConfig(boolean prefixNamespace) {
        this(prefixNamespace, Collections.<String>emptySet());
    }

    private XmlOutputConfig(boolean prefixNamespace, Set<String> skipNamespacePrefixForUris) {
        this.prefixNamespace = prefixNamespace;
        this.skipNamespacePrefixForUris = skipNamespacePrefixForUris;
    }

    public static XmlOutputConfig defaultConfig() {
        return new XmlOutputConfig(false);
    }

    public static XmlOutputConfig prefixAllNamespaceConfig() {
        return new XmlOutputConfig(true);
    }

    /**
     * Prefix all object-level namespaces
     * but keep the specified namespaces as the unprefixed namespaces.
     */
    public static XmlOutputConfig prefixAllExcept(Set<String> excludedNamespaces) {
        return new XmlOutputConfig(true, excludedNamespaces);
    }

    @Deprecated
    public boolean needsPrefixNamespace() {
        return prefixNamespace;
    }

    /**
     * Determines whether the given namespace URI should be prefixed in XML output.
     * <p>
     * This method encapsulates the prefixing logic:
     * <ul>
     *   <li>If prefixing is disabled globally, returns false</li>
     *   <li>If the URI is in the skip list, returns false</li>
     *   <li>Otherwise, returns true</li>
     * </ul>
     *
     * @param namespaceUri The namespace URI to evaluate (e.g., "urn:ietf:params:xml:ns:domain-1.0")
     * @return true if the namespace should be prefixed, false otherwise
     * @since 5.7.0
     */
    public boolean shouldPrefixNamespace(String namespaceUri) {
        return prefixNamespace && !skipNamespacePrefixForUris.contains(namespaceUri);
    }
}
