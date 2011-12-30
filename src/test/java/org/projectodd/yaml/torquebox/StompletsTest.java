package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class StompletsTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void validFull() throws Exception {
        schema.validate( loadResource( "stomplets/valid-full-doc.yml" ) );
    }

    @Test
    public void invalidBadKey() throws Exception {
        try {
            schema.validate( loadResource( "stomplets/invalid-badkey-doc.yml" ) );
            fail("Should have failed.");
        } catch (SchemaException e) {
            assertEquals( "Unrecognized field: hummus", e.getCause().getMessage() );
        }
    }
    
    @Test
    public void invalidBadHost() throws Exception {
        try {
            schema.validate( loadResource( "stomplets/invalid-badhost-doc.yml" ) );
            fail("Should have failed.");
        } catch (SchemaException e) {
            assertEquals( "String field host only accepts scalar values.", e.getMessage() );
        }
    }
    
    @Test
    public void invalidBadRoute() throws Exception {
        try {
            schema.validate( loadResource( "stomplets/invalid-badroute-doc.yml" ) );
            fail("Should have failed.");
        } catch (SchemaException e) {
            assertEquals( "String field route only accepts scalar values.", e.getCause().getMessage() );
        }
    }
    
    @Test
    public void invalidBadClass() throws Exception {
        try {
            schema.validate( loadResource( "stomplets/invalid-badclass-doc.yml" ) );
            fail("Should have failed.");
        } catch (SchemaException e) {
            assertEquals( "String field class only accepts scalar values.", e.getCause().getMessage() );
        }
    }

}
