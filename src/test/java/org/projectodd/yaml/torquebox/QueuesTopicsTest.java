package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.projectodd.yaml.AbstractBaseTest;
import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;

public class QueuesTopicsTest extends AbstractBaseTest {

    @Test
    public void testInvalidKey() throws Exception {
        for (String key : new String[] { "queue", "topic" }) {
            try {
                Schema schema = new Schema( loadResource( "schema.yml" ) );
                schema.validate( loadResource( "queues_and_topics/invalid-key-" + key + "-torquebox.yml" ) );
                fail( "Should have failed." );
            } catch (SchemaException e) {
                assertEquals( "No valid value found for field /" + key + "/foo", e.getMessage() );
                assertEquals( "Unrecognized field: homer", e.getCause().getMessage() );
            }
        }
    }

    @Test
    public void testValidSimple() throws SchemaException {
        for (String key : new String[] { "queue", "topic" }) {
            Schema schema = new Schema( loadResource( "schema.yml" ) );
            schema.validate( loadResource( "queues_and_topics/valid-simple-" + key + "-torquebox.yml" ) );
        }
    }

    @Test
    public void testValidComplex() throws SchemaException {
        for (String key : new String[] { "queue", "topic" }) {
            Schema schema = new Schema( loadResource( "schema.yml" ) );
            schema.validate( loadResource( "queues_and_topics/valid-complex-" + key + "-torquebox.yml" ) );
        }
    }

    @Override
    public String getType() {
        return "torquebox";
    }

}
