package org.projectodd.yaml.schema.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCollectionType extends AbstractBaseType {

    private List<AbstractBaseType> valueTypes;

    @SuppressWarnings("unchecked")
    protected void buildValueTypes(Object yamlData) throws SchemaException {
        valueTypes = new ArrayList<AbstractBaseType>( 10 );
        if (yamlData instanceof String) {
            valueTypes.add( TypeFactory.instance().buildType( getName(), (String) yamlData, null ) );
        }
        else if (!(yamlData instanceof Map)) {
            throw new SchemaException( "value-types must be specified in map format." );
        }
        else {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
            for (String typeId : yamlMap.keySet()) {
                log.debug( "Adding value type: " + typeId + " for field " + getName() );
                valueTypes.add( TypeFactory.instance().buildType( getName(), typeId, yamlMap.get( typeId ) ) );
            }
        }
    }

    public List<AbstractBaseType> getValueTypes() {
        return valueTypes;
    }

    private static final Logger log = LoggerFactory.getLogger( AbstractCollectionType.class );

}
