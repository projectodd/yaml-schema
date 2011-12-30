package org.projectodd.yaml.schema.types;

import java.util.Map;

import org.projectodd.yaml.SchemaException;

@SchemaType({ "boolean", "bool" })
public class BooleanType extends AbstractBaseType {

    @Override
    @Requires({ Map.class, String.class })
    AbstractBaseType build(Object yamlData) throws SchemaException {
        return this;
    }

    @Override
    @Requires(Boolean.class)
    public void validateType(Object value) throws SchemaException {
        if (value == null) {
            throw new SchemaException( "Boolean type values cannot be null." );
        }
    }

}
