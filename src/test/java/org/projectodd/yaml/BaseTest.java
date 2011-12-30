package org.projectodd.yaml;

import java.io.InputStream;

public abstract class BaseTest {

    public abstract String getType();

    public InputStream loadResource(String resource) throws SchemaException {
        String path = TestUtils.join( new String[] { getType(), resource }, "/" );
        return this.getClass().getResourceAsStream( '/' + path );
    }

}
