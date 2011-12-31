package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.StringType;

// TODO: Finish this after all other tests pass.
public class SchemaTest extends AbstractBaseTorqueBoxTest {

    private MapType root;

    @Test
    public void validSchema() throws Exception {
        root = (MapType) schema.getRoot();
        this.validateApplication();

    }

    private void validateApplication() {
        MapType application = getCategory( "application" );
        Map<String, AbstractBaseType> kids = application.getChildren();
        assertEquals( 4, application.getChildren().size() );
        validateString( kids.get( "root" ), "root" );
        validateString( kids.get( "env" ), "env" );
        validateString( kids.get( "RAILS_ROOT" ), "RAILS_ROOT" );
        validateString( kids.get( "RAILS_ENV" ), "RAILS_ENV" );
    }

    private MapType getCategory(String category) {
        AbstractBaseType t = root.getChildren().get( category );
        assertNotNull( t );
        assertTrue( t instanceof MapType );
        return (MapType) t;

    }

    private void validateString(AbstractBaseType type, String name) {
        validateString( type, name, false );
    }

    private void validateString(AbstractBaseType type, String name, boolean required) {
        assertTrue( type instanceof StringType );
        assertEquals( required, type.isRequired() );
        assertEquals( name, type.getName() );
    }
}
