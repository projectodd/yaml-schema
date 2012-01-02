package org.projectodd.yaml.schema.types;

import java.util.Map;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class BooleanType extends AbstractBaseType {

    @Override
    AbstractBaseType build(Object yamlData) throws SchemaException {
        return this;
    }

    @Override
    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return yamlData instanceof Map || yamlData instanceof String;
    }
    
    @Override
    protected boolean acceptsValue(Object yamlData) {
        return yamlData instanceof Boolean;
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        if (value == null) {
            throw new SchemaException( "Boolean field " + getName() + " cannot be null." );
        }
    }

}
