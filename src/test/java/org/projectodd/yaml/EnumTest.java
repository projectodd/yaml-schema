package org.projectodd.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.EnumType;
import org.projectodd.yaml.schema.types.MapType;

public class EnumTest extends BaseTest {

    @Test
    public void testBasic() throws SchemaException {
        Schema schema = new Schema( loadResource( "basic-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 1, children.size() );
        EnumType foo = (EnumType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        schema.validate( loadResource( "valid-doc.yml" ) );
    }

    @Test
    public void testBadSchemaValues() {
        try {
            Schema schema = new Schema( loadResource( "invalid-values-schema.yml" ) );
            MapType root = (MapType) schema.getRoot();
            Map<String, AbstractBaseType> children = root.getChildren();
            assertEquals( 1, children.size() );
            EnumType foo = (EnumType) children.get( "foo" );
            assertTrue( foo.isRequired() );
            assertEquals( "foo", foo.getName() );
            schema.validate( loadResource( "valid-doc.yml" ) );
            fail( "Error message about schema expected." );
        } catch (SchemaException e) {
            assertEquals( e.getCause().getMessage(), "The schema for an enum type must supply a values field with a list of values." );
        }
    }
    
    @Test
    public void testBadDocValue() {
        try {
            Schema schema = new Schema( loadResource( "basic-schema.yml" ) );
            MapType root = (MapType) schema.getRoot();
            Map<String, AbstractBaseType> children = root.getChildren();
            assertEquals( 1, children.size() );
            EnumType foo = (EnumType) children.get( "foo" );
            assertTrue( foo.isRequired() );
            assertEquals( "foo", foo.getName() );
            schema.validate( loadResource( "invalid-doc.yml" ) );
            fail( "Error message about schema expected." );
        } catch (SchemaException e) {
            assertEquals( e.getMessage(), "1.2 is not a valid value for this enumeration." );
        }
    }

    @Override
    public String getType() {
        return "enum";
    }

}
