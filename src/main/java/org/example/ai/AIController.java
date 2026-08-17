package org.example.ai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.example.Main;

import java.util.Set;

public class AIController {

    // sirf yahi 4 allowed hain. operation seedha ContextManager ke resource path
    private static final Set<String> ALLOWED_OPERATIONS =
            Set.of("insert", "select", "update", "delete");

    private final ContextManager contextManager = new ContextManager();
    private final PromptBuilder promptBuilder = new PromptBuilder();
    private final OllamaClient ollamaClient;

    private final Gson gson = new Gson();

    // output user Postman mein paste karega, isliye pretty print kar rahe hain
    private final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    public AIController() {
        this(new OllamaClient());
    }

    public AIController(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    // body: {"operation":"insert", "query":"Add employee Raj Sharma", "mode":"manual"}
    // wapas vhi JSON jo user pehle khud /employees ki body mein likhta tha
    public String handle(String requestBody) throws Exception {

        JsonObject request;

        try {
            request = gson.fromJson(requestBody, JsonObject.class);

        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Request body is not valid JSON");
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Request body is empty. Example: "
                            + "{\"operation\":\"insert\",\"query\":\"Add employee ...\",\"mode\":\"manual\"}");
        }

        String operation = getString(request, "operation");
        String query = getString(request, "query");
        String mode = getString(request, "mode");

        if (operation == null || operation.isBlank()) {
            throw new IllegalArgumentException(
                    "'operation' is required. Allowed: insert, select, update, delete");
        }

        operation = operation.strip().toLowerCase();

        if (!ALLOWED_OPERATIONS.contains(operation)) {
            throw new IllegalArgumentException(
                    "Unknown operation '" + operation + "'. Allowed: insert, select, update, delete");
        }

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "'query' is required. Example: \"Add employee 500001 Raj Sharma, male, "
                            + "born 2000-01-01, hired 2025-01-01\"");
        }

        // mode na bheja toh manual, abhi bas vohi bana hai
        if (mode == null || mode.isBlank()) {
            mode = "manual";
        }

//        if(mode.equals("manual")){
//
//
//            return json;
//
//        }
//
//
//        else if(mode.equals("automatic")){
//
//            return automatic(...);
//
//        }


//        if (!mode.strip().equalsIgnoreCase("manual")) {
//            throw new IllegalArgumentException(
//                    "Only 'manual' mode is supported right now. Manual mode returns the JSON body, "
//                            + "the actual request you fire yourself.");
//        }

        mode = mode.strip().toLowerCase();

        if (!mode.equals("manual") && !mode.equals("automatic")) {
            throw new IllegalArgumentException(
                    "Mode must be either manual or automatic");
        }

        // yahi teen classes ka kaam context uthao, prompt banao, model se JSON lo
        String aiOutput;

        try {
            if (!ollamaClient.isAvailable()) {
                throw new IllegalStateException(
                        "Ollama is not reachable. Make sure Ollama is running and reachable from Docker.");
            }

            String context = contextManager.getContext(operation);
            String prompt = promptBuilder.buildPrompt(context, query);

            aiOutput = ollamaClient.generateJson(prompt);

        } catch (InterruptedException e) {

            // flag wapas set karna zaroori hai warna upar wale ko pata hi nahi chalega
            Thread.currentThread().interrupt();

            throw new IllegalStateException("AI call was interrupted");
        }

        // model ko JSON hi bolna tha par bharosa nahi, parse karke confirm karte hain
        JsonObject generated;

        try {
            generated = gson.fromJson(aiOutput, JsonObject.class);

        } catch (JsonSyntaxException e) {
            generated = null;
        }

        if (generated == null) {
            throw new IllegalStateException(
                    "AI did not return a valid JSON object. Raw output: " + aiOutput);
        }

        // select mein {} ka matlab "saare employees", par baaki operations mein iska matlab
        // model ko user ki baat se zaroori values mili hi nahi
        if (generated.size() == 0 && !operation.equals("select")) {
            throw new IllegalArgumentException(missingInfoMessage(operation));
        }

        // update.md rule 8 — khali "where" poori table update kar dega
        if (operation.equals("update")) {

            JsonElement where = generated.get("where");

            if (where == null || !where.isJsonObject() || where.getAsJsonObject().size() == 0) {
                throw new IllegalArgumentException(
                        "AI built an update body without a 'where' filter, that would update every row. "
                                + "Please mention which employee, for example: "
                                + "\"... where employee number is 10001\"");
            }
        }

//        return prettyGson.toJson(generated);

        String generatedJson = prettyGson.toJson(generated);

        if (mode.equals("manual")) {
            return generatedJson;
        }

        switch (operation) {

            case "insert":
                return Main.insertEmployee(generatedJson);

            case "update":
                return Main.updateEmployee(generatedJson);

            case "delete":
                return Main.deleteEmployee(generatedJson);

//            case "select":
//                JsonObject where = generated;
//
//                Integer limit = null;
//
//                if (where.has("limit")) {
//                    limit = where.get("limit").getAsInt();
//                }
//
//                return Main.getEmployeesAsJson(limit);

            case "select":
                return Main.getEmployees(generatedJson);


            default:
                throw new IllegalArgumentException("Unsupported operation");
        }
    }

    // model ne {} bheja matlab user ki request mein details kam thi, usse vohi puchna hai
    private String missingInfoMessage(String operation) {

        return switch (operation) {

            case "insert" -> "Could not build the insert body. All columns are mandatory: "
                    + "emp_no, birth_date, first_name, last_name, gender, hire_date. "
                    + "Please repeat the request with the missing values.";

            case "delete" -> "Could not build the delete body. Please give the employee number, "
                    + "for example: \"Delete employee 10001\"";

            case "update" -> "Could not build the update body. Please say what to change and on which row, "
                    + "for example: \"Change gender to F where employee number is 10001\"";

            default -> "Could not build the request body from that query.";
        };
    }

    // key missing ho ya object/array ho toh null, taki neeche saaf message de saken
    private String getString(JsonObject object, String key) {

        JsonElement value = object.get(key);

        if (value == null || !value.isJsonPrimitive()) {
            return null;
        }

        return value.getAsString();
    }
}
