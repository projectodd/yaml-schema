package org.projectodd.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.projectodd.yaml.schema.metadata.DependencyIndexer;
import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.TypeFactory;
import org.yaml.snakeyaml.Yaml;

public class Schema {

    private AbstractBaseType root = new MapType();

    private DependencyIndexer indexer;

    public Schema(InputStream stream) throws SchemaException {
        this( stream, true );
    }

    public Schema(InputStream stream, boolean validatingDeps) throws SchemaException {
        root = initializeSchema( new Yaml().load( stream ) );
    }

    public Schema(String schemaFile) throws SchemaException {
        this( schemaFile, true );
    }

    public Schema(String schemaFile, boolean validatingDeps) throws SchemaException {
        try {
            FileInputStream fis = new FileInputStream( schemaFile );
            root = initializeSchema( new Yaml().load( fis ) );
        } catch (FileNotFoundException e) {
            throw new SchemaException( "File " + schemaFile + " was not found." );
        }
    }

    private AbstractBaseType initializeSchema(Object yamlData) throws SchemaException {
        return TypeFactory.instance().buildRoot( yamlData );
    }

    public void validate(Object yamlObject, boolean verifyDependencies) throws SchemaException {
        indexer = new DependencyIndexer();
        indexer.setVerifyingDependencies( verifyDependencies );
        indexer.scan( yamlObject );
        root.validate( indexer, yamlObject );
    }

    public void validate(InputStream stream) throws SchemaException {
        validate( new Yaml().load( stream ), true );
    }
    
    public void validate(InputStream stream, boolean verifyDependencies) throws SchemaException {
        validate( new Yaml().load( stream ), verifyDependencies );
    }

    public void validate(String fileName, boolean verifyDependencies) throws SchemaException {
        try {
            validate( new FileInputStream( fileName ), verifyDependencies );
        } catch (FileNotFoundException e) {
            throw new SchemaException( "Could not find file to validate: " + fileName );
        }
    }

    public AbstractBaseType getRoot() {
        return this.root;
    }

}
