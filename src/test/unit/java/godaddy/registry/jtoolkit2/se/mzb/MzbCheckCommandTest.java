package godaddy.registry.jtoolkit2.se.mzb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import godaddy.registry.jtoolkit2.Timer;
import godaddy.registry.jtoolkit2.se.CLTRID;

public class MzbCheckCommandTest {
    private MzbCheckCommand cmd1;

    @Before
    public void setUp() throws Exception {
        Timer.setTime("20070101.010101");
        CLTRID.setClID("JTKUTEST");
        cmd1 = new MzbCheckCommand("test");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void shouldAddLabelToEpsCheckCommand() {
        try {
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\""
                    + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                    + " xsi:schemaLocation=\"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd\">"
                    + "<command><check><check xmlns=\"urn:gdreg:params:xml:ns:mzb-1.0\""
                    + " xsi:schemaLocation=\"urn:gdreg:params:xml:ns:mzb-1.0 mzb-1.0.xsd\">"
                    + "<label>test</label></check></check>"
                    + "<clTRID>JTKUTEST.20070101.010101.0</clTRID></command></epp>", cmd1.toXML());
        } catch (SAXException saxe) {
            fail(saxe.getMessage());
        }
    }

    @Test
    public void shouldAddMultipleLabelsToEpsCheckCommand() {
        try {
            cmd1 = new MzbCheckCommand(new String[] {"test", "test2"});
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><epp xmlns=\"urn:ietf:params:xml:ns:epp-1.0\""
                    + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                    + " xsi:schemaLocation=\"urn:ietf:params:xml:ns:epp-1.0 epp-1.0.xsd\">"
                    + "<command><check><check xmlns=\"urn:gdreg:params:xml:ns:mzb-1.0\""
                    + " xsi:schemaLocation=\"urn:gdreg:params:xml:ns:mzb-1.0 mzb-1.0.xsd\">"
                    + "<label>test</label><label>test2</label>"
                    + "</check></check><clTRID>JTKUTEST.20070101.010101.0</clTRID></command></epp>", cmd1.toXML());
        } catch (SAXException saxe) {
            fail(saxe.getMessage());
        }
    }

    @Test
    public void testGetCommandType() {
        assertEquals("check", cmd1.getCommandType().getCommandName());
    }

    @Test
    public void testGetObjectType() {
        assertEquals("mzb", cmd1.getObjectType().getName());
    }

    @Test
    public void testToXML() {
        try {
            assertNotNull(cmd1.toXML());
        } catch (SAXException saxe) {
            fail(saxe.getMessage());
        }
    }
}

