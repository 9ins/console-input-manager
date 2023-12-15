package org.chaostocosmos.io.console;

import java.io.File;
import java.nio.charset.Charset;

/**
 * ConsoleFactory class
 * 
 * @author 9ins
 */
public class ConsoleFactory {
    /**
     * Get default ConsoleInput object with message.yml and triggered object.
     * @param yamlFile Message YAML file
     * @param charset Message YAML charset
     * @param toBeTriggered implemented obejct of ConsoleTrigger
     * @return ConsoleInput
     * @throws Exception throw Exception
     */
    public static ConsoleInput getDefaultConsoleInput(File yamlFile, Charset charset, ConsoleTrigger toBeTriggered) throws Exception {
        return getDefaultConsoleInput(yamlFile, charset, toBeTriggered, false);
    }

    /**
     * Get default ConsoleInput object with message.yml and triggered object and condition of auto starting.
     * @param yamlFile Message YAML file
     * @param charset Message YAML charset
     * @param toBeTriggered implemented obejct of ConsoleTrigger
     * @param isAutoStart Whether auto starting
     * @return ConsoleInput
     * @throws Exception throw Exception
     */
    public static ConsoleInput getDefaultConsoleInput(File yamlFile, Charset charset, ConsoleTrigger toBeTriggered, boolean isAutoStart) throws Exception {
        ConsoleMessageHelper helper = new ConsoleMessageHelper(yamlFile, charset);
        return new ConsoleInput(helper, toBeTriggered, isAutoStart);
    }
}
