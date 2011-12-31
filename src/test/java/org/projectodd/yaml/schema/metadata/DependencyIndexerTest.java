package org.projectodd.yaml.schema.metadata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.projectodd.yaml.SchemaException;

/**
 * Apologies to Percy Bysshe, but I was running out of ideas for test data. :)
 * 
 * @author mdobozy
 * 
 */
public class DependencyIndexerTest {

    private DependencyIndexer indexer;

    private Map<String, Object> root;

    @Before
    public void before() {
        indexer = new DependencyIndexer();
        root = makeMap();
    }

    @Test
    public void empty() {
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        assertEquals( 1, nodes.size() );
        assertEquals( "/", nodes.keySet().iterator().next() );
    }

    @Test
    public void nullRoot() {
        indexer.scan( null );
        Map<String, String> nodes = indexer.getDocumentNodes();
        assertEquals( 0, nodes.size() );
    }

    @Test
    public void scalarsOnly() {
        root.put( "ham", 123 );
        root.put( "bacon", 234 );
        root.put( "tripe", 345 );
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        assertEquals( 3, nodes.size() );
        Iterator<String> iterator = nodes.keySet().iterator();
        assertEquals( "/ham", iterator.next() );
        assertEquals( "/bacon", iterator.next() );
        assertEquals( "/tripe", iterator.next() );
    }

    @Test
    public void nestedMapScalars() {
        root.put( "howdy", 123 );
        root.put( "doody", 234 );
        Map<String, Object> muppets = makeMap();
        muppets.put( "kermit", "frog" );
        muppets.put( "fonzie", "bear" );
        root.put( "muppets", muppets );
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        Iterator<String> iterator = nodes.keySet().iterator();
        assertEquals( 4, nodes.size() );
        assertEquals( "/howdy", iterator.next() );
        assertEquals( "/doody", iterator.next() );
        assertEquals( "/muppets/kermit", iterator.next() );
        assertEquals( "/muppets/fonzie", iterator.next() );
    }

    @Test
    public void nestedEmptyMap() {
        root.put( "key1", 123.45F );
        root.put( "key2", makeMap() );
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        Iterator<String> iterator = nodes.keySet().iterator();
        assertEquals( 2, nodes.size() );
        assertEquals( "/key1", iterator.next() );
        assertEquals( "/key2", iterator.next() );
    }

    @Test
    public void nestedListScalars() {
        root.put( "Two", "vast" );
        List<Object> and = makeList();
        and.add( "trunkless" );
        and.add( "legs" );
        and.add( "of" );
        root.put( "and", and );
        root.put( "sand", "ozymandias" );
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        Iterator<String> iterator = nodes.keySet().iterator();
        assertEquals( 5, nodes.size() );
        assertEquals( "/Two", iterator.next() );
        assertEquals( "/and/#0", iterator.next() );
        assertEquals( "/and/#1", iterator.next() );
        assertEquals( "/and/#2", iterator.next() );
        assertEquals( "/sand", iterator.next() );
    }

    @Test
    public void mapOfLists() {
        List<Object> list = makeList();
        list.add( "first item" );
        Map<String, Object> nestedMap = makeMap();
        nestedMap.put( "one", "111" );
        nestedMap.put( "two", 222 );
        list.add( nestedMap );
        list.add( "third" );
        root.put( "list", list );
        indexer.scan( root );
        Map<String, String> nodes = indexer.getDocumentNodes();
        Iterator<String> iterator = nodes.keySet().iterator();
        assertEquals( 4, nodes.size() );
        assertEquals( "/list/#0", iterator.next() );
        assertEquals( "/list/#1/one", iterator.next() );
        assertEquals( "/list/#1/two", iterator.next() );
        assertEquals( "/list/#2", iterator.next() );
    }

    @Test
    public void cannotCheckPriorToScan() throws Exception {
        try {
            DependencyIndexer foo = new DependencyIndexer();
            foo.isNodeDefined( "/abc" );
            fail( "Should have failed." );
        } catch (SchemaException e) {
            assertEquals( "Cannot query dependency indexer prior to scanning.", e.getMessage() );
        }
    }

    private List<Object> makeList() {
        return new ArrayList<Object>( 10 );
    }

    private Map<String, Object> makeMap() {
        return new LinkedHashMap<String, Object>( 10 );
    }

}
