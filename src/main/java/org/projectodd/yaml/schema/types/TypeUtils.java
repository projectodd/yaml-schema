package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class TypeUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> asTypedMap(Object map) throws SchemaException {
        if (!(map instanceof Map)) {
            throw new SchemaException( "Cannot convert object of type " + map.getClass() + " to map." );
        }
        return (Map<String, Object>) map;
    }

    public static void ensureOneTypeValid(DependencyIndexer indexer, String fieldName, List<AbstractBaseType> types, Object yamlData) throws SchemaException {
        boolean foundValid = false;
        for (int i = 0; foundValid == false && i < types.size(); i++) {
            try {
                types.get( i ).validate( indexer, yamlData );
                foundValid = true;
            } catch (SchemaException e) {
                log.tracef( "Type %s was not valid; trying others.", types.get( i ) );
            }
        }
        if (!foundValid) {
            log.errorf( "Value %s failed validation for all possible types.", yamlData );
            throw new SchemaException( yamlData + " is not a valid value for field " + fieldName );
        }
    }

    private static Logger log = Logger.getLogger( TypeUtils.class );

}
