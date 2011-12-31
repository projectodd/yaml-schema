package org.projectodd.yaml.schema.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SchemaType("map")
@Requires(Map.class)
public class MapType extends AbstractCollectionType {

    private boolean allowingArbitraryKeys = false;

    private Map<String, AbstractBaseType> children = new HashMap<String, AbstractBaseType>( 10 );

    @SuppressWarnings("unchecked")
    @Override
    @Requires(Map.class)
    MapType build(Object yamlData) throws SchemaException {
        Map<String, Object> data = (Map<String, Object>) yamlData;
        if (data.containsKey( "arbitrary" )) {
            allowingArbitraryKeys = (Boolean) data.get( "arbitrary" );
            data.remove( "arbitrary" );
        }
        if (data.containsKey( "value-types" )) {
            if (!this.allowingArbitraryKeys) {
                throw new SchemaException( "value-types can only be specified if arbitrary keys are allowed." );
            }
            buildValueTypes( data.get( "value-types" ) );
            data.remove( "value-types" );
        }
        for (String key : data.keySet()) {
            Object value = data.get( key );
            AbstractBaseType type = TypeFactory.instance().buildType( (String) key, value );
            children.put( type.getName(), type );
        }
        return this;
    }

    public Map<String, AbstractBaseType> getChildren() {
        return children;
    }

    public Boolean isAllowingArbitraryKeys() {
        return this.allowingArbitraryKeys;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validateType(Schema schema, Object value) throws SchemaException {
        Map<String, Object> yamlData = (Map<String, Object>) value;
        List<AbstractBaseType> valueTypes = this.getValueTypes();

        for (String child : children.keySet()) {
            if (children.get( child ).isRequired() && !yamlData.containsKey( child )) {
                throw new SchemaException( "Value " + value + " for field " + getName() + " does not contain required field " + child );
            }
        }

        if (yamlData != null && !yamlData.isEmpty()) {
            for (String key : yamlData.keySet()) {
                AbstractBaseType type = children.get( key );
                if (type == null) {
                    if (!allowingArbitraryKeys) {
                        throw new SchemaException( "Unrecognized field: " + key );
                    }
                    else {
                        log.debug( "Map for field " + getName() + " allows arbitrary keys." );
                        if (valueTypes != null) {
                            validateValueTypes( schema, key, yamlData );
                        }
                    }
                }
                else
                    type.validate( schema, yamlData.get( key ) );
            }
        }
    }

    private void validateValueTypes(Schema schema, String key, Map<String, Object> yamlData) throws SchemaException {
        boolean foundValid = false;
        SchemaException cause = null;
        List<AbstractBaseType> valueTypes = this.getValueTypes();
        for (int i = 0; i < valueTypes.size() && !foundValid; i++) {
            AbstractBaseType valueType = valueTypes.get( i );
            try {
                valueType.validate( schema, yamlData.get( key ) );
                foundValid = true;
            } catch (SchemaException e) {
                cause = e;
            }
        }
        if (!foundValid) {
            throw new SchemaException( "No valid value found for field " + key, cause );
        }
    }

    private static final Logger log = LoggerFactory.getLogger( MapType.class );

}
