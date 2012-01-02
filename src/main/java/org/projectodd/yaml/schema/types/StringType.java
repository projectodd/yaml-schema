package org.projectodd.yaml.schema.types;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.projectodd.yaml.SchemaException;
import org.projectodd.yaml.schema.metadata.DependencyIndexer;

@SchemaType({ "str", "string" })
public class StringType extends AbstractBaseType {

    private Pattern regex;

    @SuppressWarnings("unchecked")
    @Override
    StringType build(Object yamlData) throws SchemaException {
        if (yamlData instanceof Map) {
            Map<String, Object> yamlMap = (Map<String, Object>) yamlData;
            String pattern = (String) yamlMap.get( "regex" );
            if (pattern != null) {
                try {
                    regex = Pattern.compile( pattern );
                } catch (PatternSyntaxException e) {
                    throw new SchemaException( "Invalid regex pattern: " + regex, e );
                }
            }
        }
        return this;
    }

    public String getPattern() {
        return regex == null ? null : regex.pattern();
    }

    @Override
    public void validateType(DependencyIndexer indexer, Object value) throws SchemaException {
        if (value instanceof Collection || value instanceof Map) {
            throw new SchemaException( "String field " + getName() + " only accepts scalar values." );
        }
        if (regex != null) {
            if (!regex.matcher( (String) value ).matches()) {
                throw new SchemaException( "Value " + value + " for field " + getName() +
                        " does not match regular expression " + regex.pattern() );
            }
        }
    }
}
