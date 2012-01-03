package org.projectodd.yaml.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.AbstractBaseTest;
import org.projectodd.yaml.Schema;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.EnumType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.StringType;

public class NormalMapTest extends AbstractBaseTest {

    @Test
    public void testNormal() throws Exception {
        Schema schema = new Schema( loadResource( "normal-schema.yml" ) );
        MapType root = (MapType) schema.getRoot();
        Map<String, AbstractBaseType> children = root.getChildren();
        assertEquals( 3, children.size() );
        EnumType alice = (EnumType) children.get( "alice" );
        assertTrue( alice.isRequired() );
        assertEquals( "alice", alice.getName() );
        MapType bob = (MapType) children.get( "bob" );
        assertFalse( bob.isRequired() );
        assertEquals( "bob", bob.getName() );
        Map<String, AbstractBaseType> bobKids = bob.getChildren();
        assertEquals( 3, bobKids.size() );
        assertTrue( bobKids.get( "one" ) instanceof StringType );
        assertEquals( "one", bobKids.get( "one" ).getName() );
        assertTrue( bobKids.get( "one" ).isRequired() );
        assertTrue( bobKids.get( "two" ) instanceof StringType );
        assertEquals( "two", bobKids.get( "two" ).getName() );
        assertTrue( bobKids.get( "two" ).isRequired() );
        assertTrue( bobKids.get( "three" ) instanceof StringType );
        assertEquals( "three", bobKids.get( "three" ).getName() );
        assertFalse( bobKids.get( "three" ).isRequired() );

        MapType charlie = (MapType) children.get( "charlie" );
        assertTrue( charlie.isRequired() );
        assertFalse( charlie.isAllowingArbitraryKeys() );
        Map<String, AbstractBaseType> charlieKids = charlie.getChildren();
        assertEquals( 3, charlieKids.size() );
        assertTrue( charlieKids.get( "one" ) instanceof MapType );
        assertTrue( ((MapType) charlieKids.get( "one" )).isAllowingArbitraryKeys() );
        assertTrue( ((MapType) charlieKids.get( "one" )).isRequired() );
        assertEquals( 0, ((MapType) charlieKids.get( "one" )).getChildren().size() );
        assertTrue( charlieKids.get( "two" ) instanceof StringType );
        assertFalse( ((StringType) charlieKids.get( "two" )).isRequired() );
        assertTrue( charlieKids.get( "three" ) instanceof StringType );
        assertTrue( ((StringType) charlieKids.get( "three" )).isRequired() );

        schema.validate( loadResource( "doc.yml" ) );
    }

    @Override
    public String getType() {
        return "map/normal";
    }

}
