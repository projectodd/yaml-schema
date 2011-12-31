package org.projectodd.yaml.schema.metadata;

import static org.projectodd.yaml.schema.types.TypeUtils.asTypedMap;

import java.util.Map;

import org.projectodd.yaml.Schema;
import org.projectodd.yaml.SchemaException;

public class Dependency {

    private DependencyType type = DependencyType.REQUIRES;

    private String fieldName;

    private String value;

    public Dependency initialize(String fieldName, Object config) throws SchemaException {
        this.fieldName = fieldName;
        if (config instanceof String) {
            value = (String) config;
        }
        else if (config instanceof Map) {
            Map<String, Object> configMap = asTypedMap( config );
            if (configMap.containsKey( "type" )) {
                type = DependencyType.valueFor( (String) configMap.get( "type" ) );
            }
            Object value = configMap.get( "value" );
            if (!(value instanceof String)) {
                throw new SchemaException( "Dependency value for field " + fieldName + " must be specified as either a map or a string." );
            }
            this.value = (String) value;
        }
        else {
            throw new SchemaException( "Dependencies for field " + fieldName + " must be initialized with either map or string config data." );
        }
        return this;
    }

    void validate(DependencyIndexer indexer) throws SchemaException {
        boolean found = indexer.isNodeDefined( value );
        if (!found && type == DependencyType.REQUIRES) {
            throw new SchemaException( "Could not find dependency " + value + ", which is required " +
                    "by field " + fieldName );
        }
        else if (found && type == DependencyType.MUTEX) {
            throw new SchemaException( "Found dependency " + value + ", which field "
                    + fieldName + " mutually excludes." );
        }
    }

    public void validate(Schema schema) throws SchemaException {
        validate( schema.getDependencyIndexer() );
    }

    public DependencyType getType() {
        return type;
    }

    public static enum DependencyType {

        REQUIRES("requires", "req"), MUTEX("mutex", "mutually-exclusive");

        private String[] typeNames;

        DependencyType(String... typeNames) {
            this.typeNames = typeNames;
        }

        public static DependencyType valueFor(String typeName) throws SchemaException {
            for (DependencyType type : values()) {
                String[] typeNames = type.typeNames;
                for (String tName : typeNames) {
                    if (tName.equals( typeName )) {
                        return type;
                    }
                }
            }
            throw new SchemaException( "Type " + typeName + " is not a valid dependency type. " +
                    "Valid types are: " + validTypeNames() );
        }

        private static String validTypeNames() {
            StringBuilder builder = new StringBuilder( "" );
            DependencyType[] values = values();
            for (int i = 0; i < values.length; i++) {
                DependencyType type = values[i];
                String[] typeNames = type.typeNames;
                for (String typeName : typeNames) {
                    builder.append( typeName );
                    builder.append( " " );
                }
            }
            return builder.toString();
        }
    };

}
