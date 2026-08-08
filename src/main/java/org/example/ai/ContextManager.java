package org.example.ai;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ContextManager {

    public String getContext(String type) throws IOException {
        String resourcePath = "/Context/" + type + ".md";

        try (InputStream in = getClass().getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Context file not found: " + resourcePath);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    // har operation ke lie alag method, taki call karte waqt file ka naam type na karna pade

    public String getInsertContext() throws IOException {
        return getContext("insert");
    }

    public String getSelectContext() throws IOException {
        return getContext("select");
    }

    public String getUpdateContext() throws IOException {
        return getContext("update");
    }

    public String getDeleteContext() throws IOException {
        return getContext("delete");
    }
}
