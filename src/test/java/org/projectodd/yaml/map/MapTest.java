package org.projectodd.yaml.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.BaseTest;
import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.StringType;

public class MapTest extends BaseTest {

    @Test
    public void testMinimal() throws Exception {
        Schema schema = new Schema( loadResource( "minimal/basic-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        StringType foo = (StringType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        StringType bar = (StringType) children.get( "bar" );
        assertTrue( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        StringType baz = (StringType) children.get( "baz" );
        assertFalse( baz.isRequired() );
        assertEquals( "baz", baz.getName() );
        schema.validate( loadResource( "minimal/doc.yml" ) );
    }

    @Test
    public void testShortcut() throws Exception {
        Schema schema = new Schema( loadResource( "minimal/shortcut-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        StringType foo = (StringType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        StringType bar = (StringType) children.get( "bar" );
        assertFalse( bar.isRequired() );
        assertEquals( "bar", bar.getName() );
        StringType baz = (StringType) children.get( "baz" );
        assertTrue( baz.isRequired() );
        assertEquals( "baz", baz.getName() );
        schema.validate( loadResource( "minimal/doc.yml" ) );
    }

    @Test
    public void testArbitrary() throws Exception {
        Schema schema = new Schema( loadResource( "minimal/arbitrary-keys-schema.yml" ) );
        schema.validate( loadResource( "minimal/arbitrary-keys-doc.yml" ) );
    }

    @Test
    public void testInvalidArbitraryKeys() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "minimal/basic-schema.yml" ) );
            schema.validate( loadResource( "minimal/arbitrary-keys-doc.yml" ) );
        } catch (SchemaException e) {
            assertEquals( "Unrecognized field: ned", e.getMessage() );
        }
    }
    
    @Test
    public void testMissingKeys() throws Exception {
        try {
            Schema schema = new Schema( loadResource( "minimal/basic-schema.yml" ) );
            schema.validate( loadResource( "minimal/missing-keys-doc.yml" ) );
        } catch (SchemaException e) {
            assertEquals( "Value {foo=abc, baz=456} for field #root does not contain required field bar", e.getMessage() );
        }
    }

    @Override
    public String getType() {
        return "map";
    }

}
