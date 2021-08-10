/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package org.chaostocosmos.io.console;
 
import java.io.File;
import java.util.Map;

public class SimpleConsoleTest implements ConsoleTrigger { 

    ConsoleInput consoleInput;

    public SimpleConsoleTest() throws Exception {
        createConsoleInputTest();
    }

    //@Before
    public void createConsoleInputTest() throws Exception {
        File messageFile = new File("D:\\Github\\console-input-manager\\messages.yml");
        this.consoleInput = ConsoleFactory.getDefaultConsoleInput(messageFile, this);
    }

    //@Test
    public void consoleInputTest() throws Exception {
        //this.consoleInput.startQuery();
    }
    
    @Override
    public boolean trigger(Map<String, Object> inputMap) throws Exception {
        System.out.println("========== Receive input map ==========");
        inputMap.entrySet().stream().forEach(e -> System.out.println("key: "+e.getKey()+"   value: "+e.getValue()));
        return true;
    }

    @Override
    public void exit() {
        System.out.println("Console canceled...");        
    }   

    public static void main(String[] args) throws Exception {
        new SimpleConsoleTest();
    }
}
