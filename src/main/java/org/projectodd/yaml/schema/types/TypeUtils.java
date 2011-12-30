package org.projectodd.yaml.schema.types;

import java.util.List;

import org.projectodd.yaml.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeUtils {

    public static void ensureOneTypeValid(String fieldName, List<AbstractBaseType> types, Object yamlData) throws SchemaException {
        boolean foundValid = false;
        for (int i = 0; foundValid == false && i < types.size(); i++) {
            try {
                types.get( i ).validate( yamlData );
                foundValid = true;
            } catch (SchemaException e) {
                log.trace( "Type " + types.get( i ) + " was not valid; trying others." );
            }
        }
        if (!foundValid) {
            throw new SchemaException( yamlData + " is not a valid value for field " + fieldName );
        }
    }

    private static Logger log = LoggerFactory.getLogger( TypeUtils.class );

}
