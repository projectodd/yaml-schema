package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> asTypedMap(Object map) throws SchemaException {
        if (!(map instanceof Map)) {
            throw new SchemaException( "Cannot convert object of type " + map.getClass() + " to map." );
        }
        return (Map<String, Object>) map;
    }

    public static void ensureOneTypeValid(Schema schema, String fieldName, List<AbstractBaseType> types, Object yamlData) throws SchemaException {
        boolean foundValid = false;
        for (int i = 0; foundValid == false && i < types.size(); i++) {
            try {
                types.get( i ).validate( schema, yamlData );
                foundValid = true;
            } catch (SchemaException e) {
                log.trace( "Type " + types.get( i ) + " was not valid; trying others." );
            }
        }
        if (!foundValid) {
            log.debug( "Value " + yamlData + " failed validation for all possible types." );
            throw new SchemaException( yamlData + " is not a valid value for field " + fieldName );
        }
    }

    private static Logger log = LoggerFactory.getLogger( TypeUtils.class );

}
