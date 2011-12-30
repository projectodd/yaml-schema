package org.projectodd.yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.projectodd.yaml.schema.types.AbstractBaseType;
import org.projectodd.yaml.schema.types.MapType;
import org.projectodd.yaml.schema.types.TypeFactory;
import org.yaml.snakeyaml.Yaml;

public class Schema {

    private AbstractBaseType root = new MapType();

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
    
    public AbstractBaseType getRoot() {
        return root;
    }

    private AbstractBaseType initializeSchema(Object yamlData) throws SchemaException {
        return TypeFactory.instance().buildRoot( yamlData );
    }

    public void validate(InputStream stream) throws SchemaException {
        Object doc = new Yaml().load( stream );
        root.validate( doc );
    }

    public void validate(String fileName) throws SchemaException {
        try {
            validate( new FileInputStream( fileName ) );
        } catch (FileNotFoundException e) {
            // TODO: Handle exception
        }
    }

}
