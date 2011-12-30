package org.projectodd.yaml;

public class SchemaException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public SchemaException(String message, Throwable t) {
        super(message, t);
    }
    
    public SchemaException(String message) {
        super(message);
    }

}
