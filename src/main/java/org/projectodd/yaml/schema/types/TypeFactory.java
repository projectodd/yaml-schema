package org.projectodd.yaml.schema.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;

public class TypeFactory {

    private TypeFactory() {
        loadRegisteredTypes();
    }

    @SuppressWarnings("unchecked")
    public AbstractBaseType buildRoot(Object yamlData) throws SchemaException {
        AbstractBaseType root = null;
        if (yamlData instanceof Map) {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
            if (yamlMap.containsKey( "#root" )) {
                root = buildType( "#root", yamlMap.get( "#root" ) );
            } else {
                root = new MapType();
            }
        } else {
            root = new MapType();

        }
        root.initialize( "#root", yamlData ).build( yamlData );
        return root;
    }

    @SuppressWarnings("unchecked")
    public AbstractBaseType buildType(String name, Object yamlData) throws SchemaException {
        String typeId = null;
        if (!(yamlData instanceof Map)) {
            typeId = calculateDefaultType( yamlData );
        }
        else {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
            Object mapTypeId = yamlMap.get( "type" );
            if (mapTypeId instanceof Map || mapTypeId instanceof List) {
                typeId = getSchemaTypeId( ComplexType.class );
            }
            else {
                typeId = (String) mapTypeId;
                if (typeId == null || ((String) typeId).trim().equals( "" )) {
                    typeId = calculateDefaultType( yamlData );
                }
                yamlMap.remove( "type" );
            }
        } // end else
        return buildType( name, typeId, yamlData );
    }

    public AbstractBaseType buildType(String name, String typeId, Object yamlData) throws SchemaException {
        Class<? extends AbstractBaseType> type = types.get( typeId );
        if (type == null) {
            throw new SchemaException( "Type not recognized: " + typeId );
        }
        try {
            log.debug( "Creating new instance for field " + name + " of type " + type + " using data " + yamlData );
            AbstractBaseType instance = type.newInstance();
            instance.initialize( name, yamlData ).build( yamlData );
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SchemaException( "Error building type: ", e );
        }
    }

    public static TypeFactory instance() {
        if (_instance == null) {
            _instance = new TypeFactory();
        }
        return _instance;
    }

    private String calculateDefaultType(Object yamlData) throws SchemaException {
        if (yamlData instanceof String) {
            return (String) yamlData;
        }
        else if (yamlData instanceof Map) {
            return getSchemaTypeId( MapType.class );
        } else {
            return getSchemaTypeId( StringType.class );
        }
    }

    String getSchemaTypeId(Class<?> typeClass) throws SchemaException {
        if (!types.containsValue( typeClass )) {
            throw new SchemaException( "Cannot reverse lookup type class " + typeClass + "; no such type exists." );
        }
        String typeId = null;
        for (String key : types.keySet()) {
            if (types.get( key ).equals( typeClass )) {
                typeId = key;
                break;
            }
        }
        return typeId;
    }

    private void addType(Class<? extends AbstractBaseType> clazz, String... aliases) {
        for (String alias : aliases) {
            types.put( alias, clazz );
        }
    }

    private void loadRegisteredTypes() {
        addType( StringType.class, "string", "str" );
        addType( IntegerType.class, "integer", "int" );
        addType( NaturalType.class, "natural" );
        addType( BooleanType.class, "boolean", "bool" );
        addType( EnumType.class, "enum" );
        addType( ListType.class, "list" );
        addType( MapType.class, "map" );
        addType( ComplexType.class, "complex" );
    }

    private Map<String, Class<? extends AbstractBaseType>> types = new HashMap<String, Class<? extends AbstractBaseType>>( 50 );

    private static Logger log = Logger.getLogger( TypeFactory.class );

    private static TypeFactory _instance = null;

}
