package org.projectodd.yaml.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.yaml.BaseTest;
import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;

public class ValueTypesTest extends BaseTest {

    private Schema schema;

    @Test
    public void testInvalidBadType() throws Exception {
        try {
            schema.validate( loadResource( "value-types/invalid-badtype-doc.yml" ) );
            fail("Should have failed.");
        } catch (SchemaException e) {
            assertEquals( "No valid value found for field fooferaw", e.getMessage() );
        }
    }

    @Test
    public void testValidMixedType() throws Exception {
        schema.validate( loadResource( "value-types/valid-mixedtype-doc.yml" ) );
    }
    
    @Test
    public void testValid() throws Exception {
        schema.validate( loadResource( "value-types/valid-doc.yml" ) );
    }

    @Before
    public void before() throws Exception {
        schema = new Schema( loadResource( "value-types/schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 2, children.size() );
        MapType bob = (MapType) children.get( "bob" );
        assertTrue( bob.isAllowingArbitraryKeys() );
        assertFalse( bob.isRequired() );
        List<AbstractBaseType> valueTypes = bob.getValueTypes();
        assertEquals( 2, valueTypes.size() );
        MapType valueType = (MapType) valueTypes.get( 0 );
        assertEquals( 3, valueType.getChildren().size() );
    }

    @Override
    public String getType() {
        return "map";
    }

}
