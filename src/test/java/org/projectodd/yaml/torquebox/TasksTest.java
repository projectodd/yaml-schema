package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class TasksTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void validFull() throws Exception {
        schema.validate( loadResource( "tasks/valid-full-doc.yml" ) );
    }

    @Test
    public void invalidBadConcurrency() throws Exception {
        try {
            schema.validate( loadResource( "tasks/invalid-badconcurrency-doc.yml" ) );
        } catch (SchemaException e) {
            assertEquals( "Schema for field concurrency does not accept ham of type " +
            		"class java.lang.String as input for type natural", 
            		e.getCause().getMessage() );
        }
    }

}
