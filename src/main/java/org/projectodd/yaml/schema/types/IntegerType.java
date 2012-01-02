package org.projectodd.yaml.schema.types;

import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class IntegerType extends AbstractBaseType {

    @Override
    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return yamlData instanceof Map || yamlData instanceof String;
    }
    
    @Override
    protected boolean acceptsValue(Object yamlData) {
        return yamlData instanceof Integer;
    }    
    
    @Override
    AbstractBaseType build(Object yamlData) throws SchemaException {
        return this;
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        if (value == null) {
            throw new SchemaException( "Integer field " + getName() + " cannot be null." );
        }
    }

}
