package org.projectodd.yaml.schema.types;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class NaturalType extends IntegerType {

    @Override
    protected boolean acceptsValue(Object yamlData) {
        return yamlData instanceof Integer;
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        if (value == null) {
            throw new SchemaException( "Natural field " + getName() + " cannot be null." );
        }
        else if (((Integer) value) < 1) {
            throw new SchemaException( "Natural field " + getName() + " must have a value >= 1." );
        }
    }

}
