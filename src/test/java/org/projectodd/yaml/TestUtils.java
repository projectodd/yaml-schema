package org.projectodd.yaml;

public abstract class TestUtils {

    public static String join(String[] pieces, String delim) {
        StringBuffer buffer = new StringBuffer( "" );
        for (int i = 0; i < pieces.length; i++) {
            buffer.append( pieces[i] );
            if (i < pieces.length - 1) {
                buffer.append( delim );
            }
        }
        return buffer.toString();
    }

}
