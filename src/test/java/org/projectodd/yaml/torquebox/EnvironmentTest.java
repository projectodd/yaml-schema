package org.projectodd.yaml.torquebox;

import org.junit.Test;

public class EnvironmentTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void testValid() throws Exception {
        schema.validate( loadResource( "environment/valid-full-doc.yml" ) );
    }
    
    @Test
    public void testEmpty() throws Exception {
        schema.validate( loadResource( "environment/valid-empty-doc.yml" ) );
    }

}
