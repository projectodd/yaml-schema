package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class EnumType extends AbstractBaseType {

    private List<Object> values;

    @Override
    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return yamlData instanceof Map;
    }

    @SuppressWarnings("unchecked")
    @Override
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
        log.debugf( "Validating value %s against enum values %s.", value, values );
        boolean found = false;
        for (int i = 0; i < values.size() && !found; i++) {
            Object enumValue = values.get( i );
            if (enumValue.equals( value ) ||
                    (value != null && enumValue.toString().equals( value.toString() ))) {
                found = true;
            }
        }
        if (!found) {
            throw new SchemaException( value + " is not a valid value for the enumeration on field " + getName() );
        }

    }

    private static Logger log = Logger.getLogger( EnumType.class );

}
