package org.projectodd.yaml.schema.metadata;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;

public class DependencyIndexer {

    private Map<String, String> nodes = new LinkedHashMap<String, String>( 20 );

    private boolean ready = false;
    
    private boolean verifyingDependencies;

    public boolean isNodeDefined(String node) throws SchemaException {
        if (!ready) {
            throw new SchemaException( "Cannot query dependency indexer prior to scanning." );
        }
        return nodes.containsKey( node );
    }

    public void scan(Object yamlObject) {
        if (ready) {
            log.debug( "Indexer has already scanned this doc - no need to rescan." );
            return;
        }
        if (yamlObject != null) {
            scan( null, null, yamlObject );
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void scan(String parentName, String name, Object yamlObject) {
        if (yamlObject instanceof Map) {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlObject;
            if (yamlMap.isEmpty()) {
                nodes.put( appendName( parentName, name ), "" );
            }
            else {
                for (String key : yamlMap.keySet()) {
                    scan( appendName( parentName, name ), key, yamlMap.get( key ) );
                }
            }
        }
        else if (yamlObject instanceof List) {
            List<?> yamlList = (List<?>) yamlObject;
            for (int i = 0; i < yamlList.size(); i++) {
                Object item = yamlList.get( i );
                scan( appendName( parentName, name ), buildListName( i ), item );
            }
        }
        else {
            nodes.put( appendName( parentName, name ), "" );
        }
        ready = true;
    }

    private String appendName(String parent, String name) {
        String result = (parent == null ? "" : parent)
                + (parent != null && parent.equals( "/" ) ? "" : '/')
                + (name == null ? "" : name);
        return result;
    }

    private String buildListName(int index) {
        return "#" + index;
    }

    Map<String, String> getDocumentNodes() {
        return nodes;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean isVerifyingDependencies() {
        return verifyingDependencies;
    }

    public void setVerifyingDependencies(boolean verifyingDependencies) {
        this.verifyingDependencies = verifyingDependencies;
    }

    private static final Logger log = Logger.getLogger( DependencyIndexer.class );

}
