package org.projectodd.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.BooleanType;
import org.projectodd.yaml.schema.types.MapType;

public class BooleanTest extends AbstractBaseTest {

    @Test
    public void testBasic() throws SchemaException {
        Schema schema = new Schema( loadResource( "basic-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        BooleanType foo = (BooleanType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        BooleanType bar = (BooleanType) children.get( "bar" );
        assertTrue( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        BooleanType baz = (BooleanType) children.get( "baz" );
        assertFalse( baz.isRequired() );
        assertEquals( "baz", baz.getName() );
        schema.validate( loadResource( "valid-doc.yml" ) );
    }

    @Test
    public void testComplex() throws SchemaException {
        Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        BooleanType foo = (BooleanType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        BooleanType bar = (BooleanType) children.get( "bar" );
        assertTrue( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        BooleanType baz = (BooleanType) children.get( "baz" );
        assertFalse( baz.isRequired() );
        assertEquals( "baz", baz.getName() );
        schema.validate( loadResource( "valid-doc.yml" ) );
    }

    @Test
    public void testInvalid() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
            schema.validate( loadResource( "invalid-doc.yml" ) );
            fail( "Invalid doc should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Schema for field foo does not accept arglebargle of type class " +
            		"java.lang.String as input for type boolean", e.getMessage() );
        }
    }
    
    @Test
    public void testInvalidNullValue() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
            schema.validate( loadResource( "invalid-nullvalue-doc.yml" ) );
            fail( "Invalid doc should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Schema for field baz does not accept null as input for " +
            		"type boolean", e.getMessage() );
        }
    }

    @Override
    public String getType() {
        return "boolean";
    }

}
