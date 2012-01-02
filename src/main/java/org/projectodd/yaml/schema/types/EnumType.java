package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SchemaType("enum")
public class EnumType extends AbstractBaseType {

    private List<Object> values;

    @SuppressWarnings("unchecked")
    @Override
    @Requires(Map.class)
    AbstractBaseType build(Object yamlData) throws SchemaException {
        List<Object> values = null;
        if (yamlData instanceof Map) {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
            if (!(yamlMap.get( "values" ) instanceof List)) {
                throw new SchemaException( "The schema for an enum type must supply a values field with a list of values." );
            }
            values = (List<Object>) yamlMap.get( "values" );
        }
        else {
            values = (List<Object>) yamlData;
        }
        if (values == null) {
            throw new SchemaException( "Cannot build enum type without values" );
        }
        this.values = values;
        return this;
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        log.debug( "Validating value " + value + " against enum values " + values );
        boolean found = false;
        for (int i = 0; i < values.size() && !found; i++) {
            if (values.get( i ).equals( value )) {
                found = true;
            }
        }
        if (!found) {
            throw new SchemaException( value + " is not a valid value for the enumeration on field " + getName() );
        }

    }

    private static Logger log = LoggerFactory.getLogger( EnumType.class );

}
