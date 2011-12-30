package org.projectodd.yaml.schema.types;

import java.util.Map;

import org.projectodd.yaml.SchemaException;

@SchemaType({ "int", "integer" })
public class IntegerType extends AbstractBaseType {

    @Override
    @Requires({ Map.class, String.class })
    AbstractBaseType build(Object yamlData) throws SchemaException {
        return this;
    }

    @Override
    @Requires(Integer.class)
    public void validateType(Object value) throws SchemaException {
        if (value == null) {
            throw new SchemaException( "Integer type values cannot be null." );
        }
    }

}
