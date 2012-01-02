package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class ListType extends AbstractCollectionType {

    @Override
    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return yamlData instanceof Map;
    }
    
    @Override
    protected boolean acceptsValue(Object yamlData) {
        return yamlData instanceof List;
    }    
    
    @SuppressWarnings("unchecked")
    @Override
    AbstractBaseType build(Object yamlData) throws SchemaException {
        Map<String, Object> data = (Map<String, Object>) yamlData;
        if (data.containsKey( "value-types" )) {
            buildValueTypes( data.get( "value-types" ) );
            data.remove( "value-types" );
        }
        return this;
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        List<AbstractBaseType> valueTypes = this.getValueTypes();
        if (valueTypes != null) {
            // lists only validate if value-types are specified.
            List<?> yamlDataList = (List<?>) value;
            for (Object yamlData : yamlDataList) {
                TypeUtils.ensureOneTypeValid( indexer, getName(), this.getValueTypes(), yamlData );
            }
        }
    }

}
