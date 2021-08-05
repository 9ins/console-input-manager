package org.chaostocosmos.io.console;

import java.io.File;
import java.util.Map;

/**
 * ConsoleFactory class
 */
public class ConsoleFactory {

    /**
     * Get default console input object 
     * @param yamlFile
     * @param triggered
     * @return
     * @throws Exception
     */
    public static ConsoleInput getDefaultConsoleInput(File yamlFile, ConsoleTrigger triggered) throws Exception {
        ConsoleMessageHelper helper = new ConsoleMessageHelper(yamlFile);
        return new ConsoleInput(helper, triggered);
    }
}
