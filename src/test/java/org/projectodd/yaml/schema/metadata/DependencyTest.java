package org.projectodd.yaml.schema.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.yaml.SchemaException;

public class DependencyTest {

    private DependencyIndexer indexer;

    private Map<String, Object> docRoot;

    @Test
    public void validMinimumRequires() throws Exception {
        Dependency dep = new Dependency().initialize( "ackbar", "/list/#1/one" );
        dep.validate( indexer );
        dep = new Dependency().initialize( "vader", "/root-string" );
        dep.validate( indexer );
    }

    @Test
    public void validMapReq() throws Exception {
        Map<String, Object> config = new HashMap<String, Object>( 2 );
        config.put( "type", "req" );
        config.put( "value", "/list/#1/one" );
        Dependency dep = new Dependency().initialize( "ackbar", config );
        dep.validate( indexer );
    }

    @Test
    public void validMapRequires() throws Exception {
        Map<String, Object> config = new HashMap<String, Object>( 2 );
        config.put( "type", "requires" );
        config.put( "value", "/list/#1/one" );
        Dependency dep = new Dependency().initialize( "ackbar", config );
        dep.validate( indexer );
    }

    @Test
    public void invalidMapNotFound() throws SchemaException {
        try {
            Map<String, Object> config = new HashMap<String, Object>( 2 );
            config.put( "type", "requires" );
            config.put( "value", "/cheese-n-bacon" );
            Dependency dep = new Dependency().initialize( "ackbar", config );
            dep.validate( indexer );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Could not find dependency /cheese-n-bacon, which is required " +
                    "by field ackbar",
                    e.getMessage() );
        }
    }

    @Test
    public void invalidMapConfig() throws Exception {
        try {
            Dependency dep = new Dependency().initialize( "ackbar", 12 );
            dep.validate( indexer );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Dependencies for field ackbar must be initialized with either " +
                    "map or string config data.",
                    e.getMessage().trim() );
        }
    }

    @Test
    public void invalidMapType() throws Exception {
        try {
            Map<String, Object> config = new HashMap<String, Object>( 2 );
            config.put( "type", "hog" );
            config.put( "value", "/list/#1/one" );
            Dependency dep = new Dependency().initialize( "ackbar", config );
            dep.validate( indexer );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Type hog is not a valid dependency type. Valid types are: " +
                    "requires req mutex mutually-exclusive",
                    e.getMessage().trim() );
        }
    }

    @Test
    public void validMapMutexType() throws SchemaException {
        Map<String, Object> config = new HashMap<String, Object>( 2 );
        config.put( "type", "mutex" );
        config.put( "value", "/cheese-n-bacon" );
        Dependency dep = new Dependency().initialize( "ackbar", config );
        dep.validate( indexer );
    }

    @Test
    public void invalidMapMutexDepExists() throws SchemaException {
        try {
            Map<String, Object> config = new HashMap<String, Object>( 2 );
            config.put( "type", "mutex" );
            config.put( "value", "/root-string" );
            Dependency dep = new Dependency().initialize( "ackbar", config );
            dep.validate( indexer );
        } catch (Exception e) {
            assertEquals( "Found dependency /root-string, which field ackbar mutually excludes.", e.getMessage() );
        }
    }

    @Before
    public void before() {
        docRoot = new LinkedHashMap<String, Object>( 10 );
        List<Object> list = new ArrayList<Object>( 10 );
        list.add( "first item" );
        Map<String, Object> nestedMap = new LinkedHashMap<String, Object>( 10 );
        nestedMap.put( "one", "111" );
        nestedMap.put( "two", 222 );
        list.add( nestedMap );
        list.add( "third" );
        docRoot.put( "root-string", "abcde" );
        docRoot.put( "root-integer", 123 );
        docRoot.put( "list", list );
        indexer = new DependencyIndexer();
        indexer.scan( docRoot );
    }

}
