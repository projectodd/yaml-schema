package org.projectodd.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.NaturalType;
import org.projectodd.yaml.schema.types.MapType;

public class NaturalTest extends AbstractBaseTest {

    @Test
    public void testBasic() throws SchemaException {
        Schema schema = new Schema( loadResource( "basic-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        NaturalType foo = (NaturalType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        NaturalType bar = (NaturalType) children.get( "bar" );
        assertTrue( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        NaturalType baz = (NaturalType) children.get( "baz" );
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
        NaturalType foo = (NaturalType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        NaturalType bar = (NaturalType) children.get( "bar" );
        assertTrue( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        NaturalType baz = (NaturalType) children.get( "baz" );
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
            assertEquals( "Natural field foo must have a value >= 1.", e.getMessage() );
        }
    }
    
    @Test
    public void testInvalidNullValue() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
            schema.validate( loadResource( "invalid-nullvalue-doc.yml" ) );
            fail( "Invalid doc should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Natural field baz cannot be null.", e.getMessage() );
        }
    }

    @Override
    public String getType() {
        return "natural";
    }

}
