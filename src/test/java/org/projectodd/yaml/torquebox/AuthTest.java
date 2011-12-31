package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuthTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void validFull() throws Exception {
        schema.validate( loadResource( "auth/valid-full-doc.yml" ) );
    }

    @Test
    public void invalidArbitraryKey() throws Exception {
        try {
            schema.validate( loadResource( "auth/invalid-arbitrarykey-doc.yml" ) );
        } catch (Exception e) {
            assertEquals( "Unrecognized field: springfield", e.getCause().getMessage() );
        }
    }
    
    @Test
    public void invalidBadDomain() throws Exception {
        try {
            schema.validate( loadResource( "auth/invalid-baddomain-doc.yml" ) );
        } catch (Exception e) {
            assertEquals( "String field domain only accepts scalar values.", e.getCause().getMessage() );
        }
    }
    
    @Test
    public void invalidBadValueType() throws Exception {
        try {
            schema.validate( loadResource( "auth/invalid-badvaluetype-doc.yml" ) );
        } catch (Exception e) {
            assertEquals( "Schema for field auth does not accept [yellow, blue, red] of type class " +
            		"java.util.ArrayList as input for schema type map", 
            		e.getCause().getMessage() );
        }
    }

}
