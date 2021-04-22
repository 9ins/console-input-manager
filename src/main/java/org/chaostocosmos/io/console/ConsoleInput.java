package org.chaostocosmos.io.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ConsoleInput class
 */
public class ConsoleInput {

    String tradeMark;
    String title;
    String prologue;
    String conti;
    Map<String, String> querys;
    BufferedReader reader;
    PrintStream out;

    ConsoleTrigger trigger;

    /**
     * Constructor
     * @param messageHelper
     * @throws Exception
     */
    public ConsoleInput(ConsoleMessageHelper messageHelper) throws Exception {
        this(messageHelper, null);
    }

    /**
     * Constructor
     * @param helper
     * @param trigger
     * @throws Exception
     */
    public ConsoleInput(ConsoleMessageHelper helper, ConsoleTrigger trigger) throws Exception {
        this(helper.getTradeMark(), helper.getTitle(), helper.getPrologue(), helper.getQuerys(), trigger);
    }

    /**
     * Constructor
     * @param tradeMark
     * @param title
     * @param prologue
     * @param querys
     * @param trigger
     * @throws Exception
     */
    public ConsoleInput(String tradeMark, String title, String prologue, LinkedHashMap<String, String> querys, ConsoleTrigger trigger) throws Exception {
        this.tradeMark = tradeMark;
        this.title = title;
        this.querys = querys;
        this.prologue = prologue;
        this.conti = querys.remove("CONTINUE");
        this.trigger = trigger;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.out = System.out;
        this.out.println(this.tradeMark);
        this.out.println();
        this.out.println(this.title);
        this.out.println();
        Arrays.asList(this.prologue.split(Pattern.quote("\\n"))).stream().forEach(l -> this.out.println(l));
        this.out.println();
        startQuery();
    }
    
    /**
     * Start console input query
     * @return
     * @throws Exception
     */
    public void startQuery() throws Exception {
        while(true) {
            Map<String, String> map = new LinkedHashMap<>();
            List<String> keys = new ArrayList(this.querys.keySet());
            for(String key : keys) {
                if(key.startsWith("QUERY")) {
                    String input = query(this.reader, this.querys.get(key));
                    if(input.equals("q") || input.equals("quit") || input.equals("exit") || input.equals("cancel")) {
                        if(this.trigger != null) {
                            this.trigger.canceled();
                        }
                        return;                        
                    }
                    map.put(key, input);
                } else if(key.startsWith("MESSAGE")) {
                    this.out.println(this.querys.get(key));
                }
            }
            if(this.trigger != null) {
                this.out.println();
                this.trigger.trigger(map);
                this.out.println();
            } 
            if(this.conti != null) {                
                String yn = query(this.reader, this.conti);
                if(yn.equals("n") || yn.equals("no")) {
                    this.out.println("Farewell :)");
                    break;
                }
            }
        }
    }

    /**
     * Query line input
     * @param reader
     * @param query
     * @return
     * @throws IOException
     */
    private String query(BufferedReader reader, String query) throws IOException {
        this.out.print(query);
        return reader.readLine();
    }    

    /**
     * Set trigger object to be continue process.
     * @param trigger
     */
    public void setTrigger(ConsoleTrigger trigger) {
        this.trigger = trigger;
    }
}
