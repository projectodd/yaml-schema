package org.projectodd.yaml.schema.types;

import java.util.List;
import java.util.Map;

import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;

@SchemaType({ "sequence", "seq" })
public class SequenceType extends AbstractBaseType {

    private List<AbstractBaseType> items;

    @Override
    @SuppressWarnings("unchecked")
    SequenceType build(Object yamlData) throws SchemaException {
        if (!(yamlData instanceof Map)) {
            throw new SchemaException( "Cannot build sequence from non-map data." );
        }

        Map<String, Object> data = (Map<String, Object>) yamlData;
        for (String key : data.keySet()) {
            items.add( TypeFactory.instance().buildType( key, data.get( key ) ));
        }
        return this;
    }

    @Override
    public void validateType(Schema schema, Object value) throws SchemaException {
        // TODO Auto-generated method stub

    }

}
