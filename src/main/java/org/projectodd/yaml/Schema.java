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

    private DependencyIndexer indexer = new DependencyIndexer();

    public Schema(InputStream stream) throws SchemaException {
        root = initializeSchema( new Yaml().load( stream ) );
    }

    public Schema(String schemaFile) throws SchemaException {
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

    public void validate(Object yamlObject) throws SchemaException {
        indexer.scan( yamlObject );
        root.validate( this, yamlObject );
    }

    public void validate(InputStream stream) throws SchemaException {
        validate( new Yaml().load( stream ) );
    }

    public void validate(String fileName) throws SchemaException {
        try {
            validate( new FileInputStream( fileName ) );
        } catch (FileNotFoundException e) {
            throw new SchemaException( "Could not find file to validate: " + fileName );
        }
    }
    
    public DependencyIndexer getDependencyIndexer() {
        return this.indexer;
    }
    
    public AbstractBaseType getRoot() {
        return this.root;
    }

}
