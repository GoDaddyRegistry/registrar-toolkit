package godaddy.registry.jtoolkit2.xml;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class XmlOutputConfigTest {

    @Test
    public void testDefaultConfigShouldPrefixNamespace() {
        XmlOutputConfig config = XmlOutputConfig.defaultConfig();
        assertFalse(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:epp-1.0"));
        assertFalse(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:domain-1.0"));
    }

    @Test
    public void testPrefixAllNamespaceConfigShouldPrefixNamespace() {
        XmlOutputConfig config = XmlOutputConfig.prefixAllNamespaceConfig();
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:epp-1.0"));
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:domain-1.0"));
    }

    @Test
    public void testPrefixAllNamespaceConfigShouldPrefixNamespaceExcept() {
        Set<String> skipUris = new HashSet<>();
        skipUris.add("urn:ietf:params:xml:ns:epp-1.0");
        XmlOutputConfig config = XmlOutputConfig.prefixAllExcept(skipUris);
        assertFalse(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:epp-1.0"));
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:domain-1.0"));
    }

    @Test
    public void testPrefixAllNamespaceConfigExceptWithEmptySet() {
        XmlOutputConfig config = XmlOutputConfig.prefixAllExcept(Collections.<String>emptySet());
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:epp-1.0"));
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:domain-1.0"));
    }

    @Test
    public void testPrefixAllNamespaceConfigShouldPrefixNamespaceExceptWithMultipleUris() {
        Set<String> skipUris = new HashSet<>();
        skipUris.add("urn:ietf:params:xml:ns:epp-1.0");
        skipUris.add("urn:ietf:params:xml:ns:domain-1.0");
        XmlOutputConfig config = XmlOutputConfig.prefixAllExcept(skipUris);
        assertFalse(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:epp-1.0"));
        assertFalse(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:domain-1.0"));
        assertTrue(config.shouldPrefixNamespace("urn:ietf:params:xml:ns:host-1.0"));
    }
}