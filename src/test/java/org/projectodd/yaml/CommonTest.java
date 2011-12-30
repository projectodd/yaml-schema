package org.projectodd.yaml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.ComplexType;
import org.projectodd.yaml.schema.types.MapType;

public class CommonTest extends BaseTest {

    @Test
    public void testComplexTypeString() throws Exception {
        Schema schema = new Schema( loadResource( "complex-type-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 1, children.size() );
        ComplexType foo = (ComplexType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        schema.validate( loadResource( "complex-type-string.yml" ) );
    }
    
    @Test
    public void testComplexTypeMap() throws Exception {
        Schema schema = new Schema( loadResource( "complex-type-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 1, children.size() );
        ComplexType foo = (ComplexType) children.get( "foo" );
        assertTrue( foo.isRequired() );
        assertEquals( "foo", foo.getName() );
        schema.validate( loadResource( "complex-type-map.yml" ) );
    }
    
    @Override
    public String getType() {
        return "common";
    }

}
