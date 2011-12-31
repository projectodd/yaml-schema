package org.projectodd.yaml.torquebox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class MessagingTest extends AbstractBaseTorqueBoxTest {

    @Test
    public void validMinimal() throws SchemaException {
        schema.validate( loadResource( "messaging/valid-minimal-doc.yml" ) );
    }

    @Test
    public void validFull() throws SchemaException {
        schema.validate( loadResource( "messaging/valid-full-doc.yml" ) );
    }

    @Test
    public void validNoValueList() throws SchemaException {
        schema.validate( loadResource( "messaging/valid-novaluelist-doc.yml" ) );
    }

    @Test
    public void invalidBadConcurrency() throws SchemaException {
        try {
            schema.validate( loadResource( "messaging/invalid-badconcurrency-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "{Handler={concurrency=ham}} is not a valid value for field " +
                    "messaging", e.getCause().getMessage() );
        }
    }

    @Test
    public void invalidBadFilter() throws SchemaException {
        try {
            schema.validate( loadResource( "messaging/invalid-badfilter-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "{Observer={filter={gravy=biscuits}, config={x=ex, y=why}}} " +
                    "is not a valid value for field " +
                    "messaging", e.getCause().getMessage() );
        }
    }

    @Test
    public void invalidBadDurable() throws SchemaException {
        try {
            schema.validate( loadResource( "messaging/invalid-baddurable-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "Schema for field messaging does not accept {VerySimpleAnalyzer=null, " +
                    "YouthMonitor={filter=y < 18, config={h=ache, i=eye}, durable=fish}, " +
                    "LookAndFeel={concurrency=12}} of type class java.util.LinkedHashMap as " +
                    "input for schema type list", e.getCause().getMessage() );
        }
    }

    @Test
    public void invalidBadMessageEncoding() throws SchemaException {
        try {
            schema.validate( loadResource( "messaging/invalid-badmessageencoding-doc.yml" ) );
            fail( "Should have failed." );
        } catch (Exception e) {
            assertEquals( "esperanto is not a valid value for the enumeration on field " +
            		"default_message_encoding", e.getMessage() );
        }
    }

}
