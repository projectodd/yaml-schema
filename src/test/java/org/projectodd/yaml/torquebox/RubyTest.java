package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class RubyTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void testValid() throws Exception {
        schema.validate( loadResource( "ruby/valid-full-doc.yml" ) );
    }

    @Test
    public void testInvalidCompileMode() throws Exception {
        try {
            schema.validate( loadResource( "ruby/invalid-badcompilemode-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "goober is not a valid value for the enumeration on field compile_mode", e.getMessage() );
        }
    }

    @Test
    public void testInvalidBadVersion() throws Exception {
        try {
            schema.validate( loadResource( "ruby/invalid-badversion-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "2.1 is not a valid value for the enumeration on field version", e.getMessage() );
        }
    }

    @Test
    public void testInvalidBadDebug() throws Exception {
        try {
            schema.validate( loadResource( "ruby/invalid-baddebug-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "Schema for field debug does not accept cmontgomery-burns of type class " +
                    "java.lang.String as input for type boolean",
                    e.getMessage() );
        }
    }
    
    @Test
    public void testInvalidBadInteractive() throws Exception {
        try {
            schema.validate( loadResource( "ruby/invalid-badinteractive-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "Schema for field interactive does not accept ham of type class " +
                    "java.lang.String as input for type boolean",
                    e.getMessage() );
        }
    }
    
    @Test
    public void testInvalidBadProfileApi() throws Exception {
        try {
            schema.validate( loadResource( "ruby/invalid-badprofileapi-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "Schema for field profile_api does not accept cheese of type class " +
                    "java.lang.String as input for type boolean",
                    e.getMessage() );
        }
    }

}
