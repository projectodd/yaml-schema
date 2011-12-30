package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class ServicesTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void testInvalidBadSingleton() throws Exception {
        try {
            schema.validate( loadResource( "services/invalid-badsingleton-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Schema for field singleton does not accept mrplow of " +
                    "type class java.lang.String as input for schema type boolean",
                    e.getCause().getMessage() );
        }
    }

    @Test
    public void testInvalidBadConfig() throws Exception {
        try {
            schema.validate( loadResource( "services/invalid-badconfig-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Schema for field config does not accept pants of " +
                    "type class java.lang.String as input for schema type map",
                    e.getCause().getMessage() );
        }
    }

    @Test
    public void testInvalidBadService() throws Exception {
        try {
            schema.validate( loadResource( "services/invalid-badservice-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "String field service only accepts scalar values.", e.getCause().getMessage() );
        }
    }

    @Test
    public void testValidFull() throws Exception {
        schema.validate( loadResource( "services/valid-full-doc.yml" ) );
    }

}
