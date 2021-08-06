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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * ConsoleInput class
 */
public class ConsoleInput {

    String tradeMark;
    String title;
    String prologue;
    Map<String, Object> querys;
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
    public ConsoleInput(String tradeMark, String title, String prologue, LinkedHashMap<String, Object> querys, ConsoleTrigger trigger) throws Exception {
        this.tradeMark = tradeMark;
        this.title = title;
        this.querys = querys;
        this.prologue = prologue;
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
            Map<String, Object> map = new LinkedHashMap<>();
            List<String> keys = new ArrayList<>(this.querys.keySet());
            Object input = null;
            for(int i=0; i<keys.size(); i++) {
                String key = keys.get(i);
                //System.out.println(key+"   "+i);
                if(Pattern.matches("QUERY\\d+", key)) {
                    input = query(this.reader, this.querys.get(key));
                    if(input == null) {
                        exit(0);
                    }
                    map.put(key, input);
                } else if(Pattern.matches("MESSAGE\\d+", key)) {
                    this.out.println(this.querys.get(key));
                    continue;
                } else if(Pattern.matches("GOTO\\d+", key)) {
                    AtomicInteger idx = new AtomicInteger();
                    int index = keys.stream().peek(v -> idx.incrementAndGet()).anyMatch(k -> k.equals(querys.get(key))) ? idx.get() - 1 : -1;
                    //System.out.println("-----------------"+index+"  "+key+"  "+querys.get(key));
                    if(index != -1) {
                        i = index - 1;
                        //System.out.println(i+"   $$$$$$$$$$$$$");
                        continue;
                    }        
                } else if(Pattern.matches("IF\\d+", key) || Pattern.matches("ELSE\\d+", key)) {                                        
                    if(keys.size() > i - 1) {
                        if(Pattern.matches("QUERY\\d+", key)) {
                            continue;
                        } else if(Pattern.matches("IF\\d+", key)) {
                            if(input.equals(this.querys.get(key))) {
                                continue;
                            } else {
                                i++;
                            }
                        } else if(Pattern.matches("ELSE\\d+", key)) {
                            continue;
                        }
                    }                
                } else if(key.equals("END")) {
                    this.exit(0);
                }
            }
            if(this.trigger != null) {
                this.out.println();
                this.trigger.trigger(map);
                this.out.println();
            } 
        }
    }

    /** 
     * Exit
     */
    protected void exit(int status) {
        this.trigger.canceled();
        this.out.println("Farewell :)");
        System.exit(status);
    }

    /**
     * Query line input
     * @param reader
     * @param query
     * @return
     * @throws IOException
     */
    private Object query(BufferedReader reader, Object query) throws IOException {
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
