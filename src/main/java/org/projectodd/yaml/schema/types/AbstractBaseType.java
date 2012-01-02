package org.projectodd.yaml.schema.types;

import static org.projectodd.yaml.schema.types.TypeUtils.asTypedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.Dependency;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

public abstract class AbstractBaseType {

    private static Logger log = Logger.getLogger( AbstractBaseType.class );

    private static final String OPTIONAL_PREFIX = "-";

    private static final String REQUIRED_PREFIX = "+";

    private String name;

    private boolean required = true;

    private List<Dependency> dependencies = new ArrayList<Dependency>( 10 );

    abstract AbstractBaseType build(Object yamlData) throws SchemaException;

    public String getName() {
        return name;
    }

    protected boolean acceptsConfiguration(Object yamlData) throws SchemaException {
        return true;
    }

    protected boolean acceptsValue(Object yamlData) throws SchemaException {
        return true;
    }

    AbstractBaseType initialize(String name, Object yamlData) throws SchemaException {
        String fieldName = name;
        if (fieldName.startsWith( REQUIRED_PREFIX )) {
            fieldName = fieldName.substring( 1 );
            this.required = true;
        }
        else if (fieldName.startsWith( OPTIONAL_PREFIX )) {
            fieldName = fieldName.substring( 1 );
            this.required = false;
        }
        this.name = fieldName;
        if (!acceptsConfiguration( yamlData )) {
            throw new SchemaException( "Schema for field " + name + " does not accept "
                    + yamlData
                    + (yamlData != null ? " of type " + yamlData.getClass() : "")
                    + " as configuration input for type "
                    + TypeFactory.instance().getSchemaTypeId( this.getClass() ) );
        }

        if (yamlData instanceof Map) {
            Map<String, Object> yamlMap = asTypedMap( yamlData );
            if (yamlMap.containsKey( "required" )) {
                required = Boolean.valueOf( yamlMap.get( "required" ).toString() );
            }
            initializeDependencies( yamlMap );
        }
        log.debugf( "initialized %s field %s.", (required ? "required" : "optional"), this.name );
        return this;
    }

    public void initializeDependencies(Map<String, Object> yamlMap) throws SchemaException {
        Object deps = yamlMap.get( "dependencies" );
        if (deps != null) {
            if (deps instanceof Map) {
                Map<String, Object> mapDeps = asTypedMap( deps );
                for (String dep : mapDeps.keySet()) {
                    dependencies.add( new Dependency().initialize( getName(), dep ) );
                }
            }
            else if (deps instanceof List) {
                for (Object dep : (List<?>) deps) {
                    dependencies.add( new Dependency().initialize( getName(), dep ) );
                }
            }
            dependencies.add( new Dependency().initialize( getName(), (String) deps ) );
            yamlMap.remove( "dependencies" );
        }
        else {
            log.tracef( "No dependencies for field %s.", getName() );
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public void validate(DependencyIndexer index, Object yamlData) throws SchemaException {
        log.debugf( "Validating type %s using value %s.", this.getClass(), yamlData );
        if (yamlData == null && required) {
            throw new SchemaException( "Field " + name + " was required but is not present." );
        }

        if (!acceptsValue( yamlData )) {
            throw new SchemaException( "Schema for field " + name + " does not accept "
                    + yamlData
                    + (yamlData != null ? " of type " + yamlData.getClass() : "")
                    + " as input for type "
                    + TypeFactory.instance().getSchemaTypeId( this.getClass() ) );
        }
        validateDependencies( index );
        validateType( index, yamlData );
    }

    private void validateDependencies(DependencyIndexer indexer) throws SchemaException {
        if (indexer.isVerifyingDependencies()) {
            for (Dependency dep : dependencies) {
                dep.validate( indexer );
            }
        }
    }

    public abstract void validateType(DependencyIndexer indexer, Object value) throws SchemaException;

}
