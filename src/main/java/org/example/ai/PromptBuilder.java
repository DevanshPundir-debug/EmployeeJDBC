package org.example.ai;

public class PromptBuilder {

    // context (ContextManager se aaya hua .md) + user ki baat = final prompt jo Ollama ko jayega
    public String buildPrompt(String context, String userQuery) {
        if (context == null || context.isBlank()) {
            throw new IllegalArgumentException("context is empty");
        }
        if (userQuery == null || userQuery.isBlank()) {
            throw new IllegalArgumentException("userQuery is empty");
        }

        return """
                %s

                User Request:
                %s

                Return ONLY JSON. No markdown, no code fences, no explanation.
                """.formatted(context.strip(), userQuery.strip());
    }
}
