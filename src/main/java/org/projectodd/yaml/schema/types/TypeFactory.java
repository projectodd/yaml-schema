package org.projectodd.yaml.schema.types;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.projectodd.yaml.SchemaException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeFactory {

    private Reflections reflections;

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

    public Class<?>[] getRequirements(Class<?> clazz) {
        MethodKey key = new MethodKey( ANY_METHOD, clazz.getName() );
        return requirements.get( key );
    }

    public Class<?>[] getRequirements(String methodName, Class<?> clazz) {
        MethodKey key = new MethodKey( methodName, clazz.getName() );
        return requirements.get( key );
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

    @SuppressWarnings("unchecked")
    private void loadRegisteredTypes() {
        reflections = new Reflections( new ConfigurationBuilder()
                .setUrls( ClasspathHelper.forPackage( TypeFactory.class.getPackage().getName() ) )
                .setScanners( new TypeAnnotationsScanner(),
                        new MethodAnnotationsScanner() ) );
        Set<Class<?>> schemaTypes = reflections.getTypesAnnotatedWith( SchemaType.class );
        for (Class<?> type : schemaTypes) {
            log.debug( "Registering type " + type );
            if (AbstractBaseType.class.isAssignableFrom( type )) {
                SchemaType typeData = type.getAnnotation( SchemaType.class );
                for (String key : typeData.value()) {
                    types.put( key, (Class<AbstractBaseType>) type );
                }
            }
            else {
                throw new RuntimeException( "Schema types must extend " + AbstractBaseType.class.getName() );
            }
        }
        this.loadRequirements();

    }

    private void loadRequirements() {
        Set<Method> methods = reflections.getMethodsAnnotatedWith( Requires.class );
        for (Method method : methods) {
            MethodKey mk = new MethodKey( method.getName(), method.getDeclaringClass().getName() );
            log.debug( "Adding requirements method key " + mk );
            Requires req = method.getAnnotation( Requires.class );
            requirements.put( mk, req.value() );
        }
        Set<Class<?>> types = reflections.getTypesAnnotatedWith( Requires.class );
        for (Class<?> type : types) {
            Requires req = type.getAnnotation( Requires.class );
            MethodKey mk = new MethodKey( ANY_METHOD, type.getName() );
            log.debug( "Adding requirements method key " + mk );
            requirements.put( mk, req.value() );
        }
    }

    private Map<String, Class<AbstractBaseType>> types = new HashMap<String, Class<AbstractBaseType>>( 50 );

    private Map<MethodKey, Class<?>[]> requirements = new HashMap<MethodKey, Class<?>[]>( 50 );

    private static final String ANY_METHOD = "*";

    private static Logger log = LoggerFactory.getLogger( TypeFactory.class );

    private static TypeFactory _instance = null;

    private static class MethodKey {

        private String methodName;
        private String className;

        public MethodKey(String methodName, String className) {
            this.methodName = methodName;
            this.className = className;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((className == null) ? 0 : className.hashCode());
            result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MethodKey other = (MethodKey) obj;
            if (className == null) {
                if (other.className != null)
                    return false;
            } else if (!className.equals( other.className ))
                return false;
            if (methodName == null) {
                if (other.methodName != null)
                    return false;
            } else if (!methodName.equals( other.methodName ))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "MethodKey[" + this.methodName + "," + this.className + "]";
        }

    }

}
