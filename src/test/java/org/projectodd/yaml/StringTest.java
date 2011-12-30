package org.projectodd.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.StringType;

public class StringTest extends BaseTest {

    @Test
    public void testSimple() throws SchemaException {
        Schema schema = new Schema( loadResource( "basic-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 1, children.size() );
        StringType foo = (StringType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        schema.validate( loadResource( "valid-doc.yml" ) );
    }

    @Test
    public void testComplexInvalidDoc() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
            MapType root = (MapType) schema.getRoot();
            Map<String, AbstractBaseType> children = root.getChildren();
            assertEquals( 1, children.size() );
            StringType foo = (StringType) children.get( "foo" );
            assertTrue( foo.isRequired() );
            assertEquals( "^[0-9]{1,2}d$", foo.getPattern() );
            assertEquals( "foo", foo.getName() );
            schema.validate( loadResource( "invalid-doc.yml" ) );
            fail( "Invalid doc should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Value abcde for field foo does not match regular expression ^[0-9]{1,2}d$", e.getMessage() );
        }
    }

    @Test
    public void testComplexValidDoc() throws Exception {
        Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 1, children.size() );
        StringType foo = (StringType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "^[0-9]{1,2}d$", foo.getPattern() );
        assertEquals( "foo", foo.getName() );
        schema.validate( loadResource( "valid-doc.yml" ) );
    }

    @Test
    public void testInvalidStringDoc() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "complex-schema.yml" ) );
            schema.validate( loadResource( "invalid-string-doc.yml" ) );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "String field foo only accepts scalar values.", e.getMessage() );
        }
    }

    @Override
    public String getType() {
        return "string";
    }

}
