package org.chaostocosmos.io.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * ConsoleInput object
 * 
 * @author 9ins
 */
public class ConsoleInput {

    String tradeMark;
    String title;
    String prologue;
    Map<String, Object> querys;
    BufferedReader reader;
    PrintStream out;
    ConsoleTrigger trigger;
    boolean isContinue = false;
    int exitCode = 0;

    /**
     * Constructor with ConsoleMessageHelper 
     * @param messageHelper ConsoleMessageHelper
     * @throws Exception
     */
    public ConsoleInput(ConsoleMessageHelper messageHelper) throws Exception {
        this(messageHelper, false);
    }

    /**
     * Constructor with ConsoleMessageHelper and condition of auto starting
     * @param messageHelper ConsoleMessageHelper
     * @param autoStart
     * @throws Exception
     */
    public ConsoleInput(ConsoleMessageHelper messageHelper, boolean autoStart) throws Exception {
        this(messageHelper, null, autoStart);
    }

    /**
     * Constructor with ConsoleMessageHelper and ConsoleTrigger and condition of auto starting
     * @param helper ConsoleMessageHelper
     * @param trigger Triggered Object
     * @param autoStart Whether auto starting
     * @throws Exception
     */
    public ConsoleInput(ConsoleMessageHelper helper, ConsoleTrigger trigger, boolean autoStart) throws Exception {
        this(helper.getTradeMark(), helper.getTitle(), helper.getPrologue(), helper.getQuerys(), trigger, autoStart);
    }

    /**
     * Constructor
     * @param tradeMark Trademark String
     * @param title Title of console application
     * @param prologue Prolog string
     * @param querys Query Map
     * @param trigger Triggered object
     * @param autoStart Whether auto starting
     * @throws Exception
     */
    public ConsoleInput(String tradeMark, String title, String prologue, LinkedHashMap<String, Object> querys, ConsoleTrigger trigger, boolean autoStart) throws Exception {
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
        Arrays.asList(this.prologue.split(Pattern.quote(System.lineSeparator()))).stream().forEach(l -> this.out.println(l));
        this.out.println();
        if(autoStart) {
            startQuery();
        }
    }

    /**
     * Start console input query
     * @throws Exception
     */
    public void startQuery() throws Exception {
        while(true) {
            Map<String, Object> map = new LinkedHashMap<>();
            List<String> keys = new ArrayList<>(this.querys.keySet());
            Object input = null;
            for(int i=0; i<keys.size(); i++) {
                String key = keys.get(i);
                if(Pattern.matches("QUERY\\d+", key)) {
                    input = query(this.reader, this.querys.get(key));
                    if(input == null) {
                        exit(-1);
                    }
                    if(!map.containsKey(key)) {
                        map.put(key, input);
                    } else {    
                        if(map.keySet().stream().filter(k -> k.startsWith(key)).count() == 1) {
                            map.put(key+"_1", input);
                        } else {
                            String key_ = map.keySet().stream().filter(k -> k.startsWith(key+"_")).max(Comparator.comparingInt(k1 -> Integer.parseInt(k1.substring(k1.lastIndexOf("_")+1)))).get();
                            map.put(key+"_"+(Integer.parseInt(key_.substring(key_.lastIndexOf("_")+1))+1), input);    
                        }
                    }                    
                } else if(Pattern.matches("MESSAGE\\d+", key)) {
                    this.out.println(this.querys.get(key));
                    continue;
                } else if(Pattern.matches("GOTO\\d+", key)) {
                    AtomicInteger idx = new AtomicInteger();
                    int index = keys.stream().peek(v -> idx.incrementAndGet()).anyMatch(k -> k.equals(querys.get(key))) ? idx.get() - 1 : -1;
                    if(index != -1) {
                        i = index - 1;
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
                    this.isContinue = false;
                    this.exitCode = 0;
                    break;
                } else if(key.equals("CONTINUE")) {
                    input = query(this.reader, this.querys.get(key));
                    this.exitCode = 0;
                    if(input != null && ( (input+"").equalsIgnoreCase("y") || (input+"").equalsIgnoreCase("yes") )) {
                        this.isContinue = true;
                    } else {
                        this.isContinue = false;
                    }
                    break;
                } else {
                    map.put(key, this.querys.get(key));
                }
            }
            if(this.trigger != null) {
                this.out.println();
                if(!this.trigger.trigger(map)) {
                    this.exitCode = -1;
                    this.isContinue = false;
                }
                this.out.println();
            }             
            if(!this.isContinue) {
                this.exit(this.exitCode);
            }
        }
    }

    /** 
     * Exit
     * @param status Exit status
     */
    protected void exit(int status) throws Exception {
        if(status != -1) {
            this.trigger.exit();
        }
        this.out.println("Farewell :)");
        System.exit(status);
    }

    /**
     * Query line input
     * @param reader BufferedReader
     * @param query Query
     * @return
     * @throws IOException
     */
    private Object query(BufferedReader reader, Object query) throws IOException {
        this.out.print(query);
        return reader.readLine();
    }    

    /**
     * Set trigger object to be continue process.
     * @param trigger Triggered object
     */
    public void setTrigger(ConsoleTrigger trigger) {
        this.trigger = trigger;
    }
}
