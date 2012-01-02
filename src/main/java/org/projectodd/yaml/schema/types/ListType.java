package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

@SchemaType("list")
public class ListType extends AbstractCollectionType {

    @SuppressWarnings("unchecked")
    @Override
    @Requires(Map.class)
    AbstractBaseType build(Object yamlData) throws SchemaException {
        Map<String, Object> data = (Map<String, Object>) yamlData;
        if (data.containsKey( "value-types" )) {
            buildValueTypes( data.get( "value-types" ) );
            data.remove( "value-types" );
        }
        return this;
    }

    @Override
    @Requires(List.class)
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
