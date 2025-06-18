package commands;

import java.util.HashSet;
import java.util.Set;

public class ScriptsHandler {
    private static Set<String> finishedScripts = new HashSet<>();

    public static void addScript(String scriptName) {
        finishedScripts.add(scriptName);
    }
    public static Set getScripts() {
        return finishedScripts;
    }
}
