package org.chaostocosmos.io.console;

import java.io.File;
import java.util.Map;

public class SimpleConsoleTest implements ConsoleTrigger {

    @Override
    public void trigger(Map<String, String> inputMap) throws Exception {
        inputMap.entrySet().stream().forEach(e -> System.out.println("key: "+e.getKey()+"   value: "+e.getValue()));
    }

    @Override
    public void canceled() {
        System.out.println("Console canceled...");
    }

    public static void main(String[] args) throws Exception {
        SimpleConsoleTest test = new SimpleConsoleTest();
        File file = new File(test.getClass().getResource("messages.yml").toURI().getPath());
        ConsoleInput input = ConsoleFactory.getDefaultConsoleInput(file, test);
    }    
}
