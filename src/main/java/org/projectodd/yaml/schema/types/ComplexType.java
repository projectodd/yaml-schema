package org.projectodd.yaml.schema.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public class ComplexType extends AbstractBaseType {

    private List<AbstractBaseType> types = new ArrayList<AbstractBaseType>( 10 );

    @Override
    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return yamlData instanceof Map;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    ComplexType build(Object yamlData) throws SchemaException {
        log.debugf( "Parsing type options for complex type with data: %s", yamlData );
        Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
        Object type = yamlMap.get( "type" );
        if (type instanceof Map) {
            return buildTypeFromMap( (Map<String, Object>) type );
        }
        else if (type instanceof List) {
            return buildTypeFromList( (List<?>) type );
        }
        else {
            throw new SchemaException( "Complex type can only take a list or map as input." );
        }
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        TypeUtils.ensureOneTypeValid( indexer, getName(), types, value );
    }

    @SuppressWarnings("unchecked")
    private ComplexType buildTypeFromList(List<?> typeList) throws SchemaException {
        TypeFactory tf = TypeFactory.instance();
        for (Object typeId : typeList) {
            log.debugf( "Adding type from list: %s.", typeId );
            AbstractBaseType type = null;
            if (typeId instanceof String) {
                type = tf.buildType( getName(), (String) typeId, null );
            }
            else if (typeId instanceof Map) {
                Map<String, Object> typeData = (Map<String, Object>) typeId;
                type = tf.buildType( getName(), tf.getSchemaTypeId( ListType.class ), typeData );
            }
            type.setRequired( this.isRequired() );
            types.add( type );
        }
        return this;
    }

    private ComplexType buildTypeFromMap(Map<String, Object> typeMap) throws SchemaException {
        for (String key : typeMap.keySet()) {
            log.debugf( "Adding type from map %s.", key );
            AbstractBaseType type = TypeFactory.instance().buildType( getName(), key, typeMap.get( key ) );
            type.setRequired( this.isRequired() );
            types.add( type );
        }
        return this;
    }

    private static final Logger log = Logger.getLogger( ComplexType.class );

}
