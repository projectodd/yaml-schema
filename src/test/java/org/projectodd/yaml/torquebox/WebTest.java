package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class WebTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void testGeneralValid() throws SchemaException {
        schema.validate( loadResource( "web/general-valid-doc.yml" ) );
    }
    
    @Test
    public void testValidMsecSessionTimeout() throws SchemaException {
        schema.validate( loadResource( "web/valid-msecsessiontimeout-doc.yml" ) );
    }

    @Test
    public void testHostString() throws SchemaException {
        schema.validate( loadResource( "web/host-string-valid-doc.yml" ) );
    }

    @Test
    public void testHostList() throws SchemaException {
        schema.validate( loadResource( "web/host-list-valid-doc.yml" ) );
    }

    @Test
    public void testHostListInvalid() throws SchemaException {
        try {
            schema.validate( loadResource( "web/host-list-invalid-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "[foo.com, bar.com, " +
                    "{foobaria={name=foo.com}}] is not a valid value for field host",
                    e.getMessage() );
        }
    }

    @Test
    public void testSessionTimeoutInvalid() throws SchemaException {
        try {
            schema.validate( loadResource( "web/session-timeout-invalid-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Value 42glibs for field session-timeout does not match regular " +
                    "expression ^[0-9]+\\s*(ms|s|m|h)\\s*",
                    e.getMessage() );
        }
    }
    
    @Test
    public void testSessionTimeoutSpaces() throws Exception {
        schema.validate( loadResource( "web/session-timeout-spaces-doc.yml" ) );
    }

}
