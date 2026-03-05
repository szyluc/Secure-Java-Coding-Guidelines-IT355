import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Demonstrates safe usage of the javax.script API by
 * validating (whitelisting) untrusted input before execution.
 */
public class SafeScriptExample {

    /**
     * A method to evaluluate a script
     * @param firstName is the first name string that the user provides
     * @throws ScriptException which happens when the Script has an error
     * @throws IllegalArgumentException if the firstName has an invalid characters
     */
    public static void evalScript(String firstName) throws ScriptException {

        // Whitelist validation: allow only letters, digits, underscore
        if (!firstName.matches("[\\w]+")) {
            throw new IllegalArgumentException(
                "Invalid input: Only letters, digits, and underscores are allowed."
            );
        }

        // Create script engine
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        // Safe execution (input already validated)
        engine.eval("print('" + firstName + "')");
    }

    /**
     * Example main method demonstrating safe execution.
     */
    public static void main(String[] args) {
        try {
            evalScript("Brady_123");  // Valid input
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
