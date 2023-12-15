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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * ConsoleInput object
 * 
 * @author 9ins
 */
public class ConsoleInput {

    private static LinkedHashMap<String, Object> querys;
    private static LinkedHashMap<String, Object> queryValues;
    String tradeMark;
    String title;
    String prologue;
    BufferedReader reader;
    PrintStream out;
    ConsoleTrigger trigger;
    int exitMode = -1; //exit mode 0 is exit immediately, mode 1 is graceful exit
    int exitCode = -1;

    /**
     * Constructor with ConsoleMessageHelper 
     * @param messageHelper ConsoleMessageHelper
     * @throws Exception throw Exception
     */
    public ConsoleInput(ConsoleMessageHelper messageHelper) throws Exception {
        this(messageHelper, false);
    }

    /**
     * Constructor with ConsoleMessageHelper and condition of auto starting
     * @param messageHelper ConsoleMessageHelper
     * @param autoStart auto start parameter
     * @throws Exception throw Exception
     */
    public ConsoleInput(ConsoleMessageHelper messageHelper, boolean autoStart) throws Exception {
        this(messageHelper, null, autoStart);
    }

    /**
     * Constructor with ConsoleMessageHelper and ConsoleTrigger and condition of auto starting
     * @param helper ConsoleMessageHelper
     * @param trigger Triggered Object
     * @param autoStart Whether auto starting
     * @throws Exception  throw Exception
     */
    public ConsoleInput(ConsoleMessageHelper helper, ConsoleTrigger trigger, boolean autoStart) throws Exception {
        this(helper.getTradeMark(), helper.getTitle(), helper.getPrologue(), helper.getQuerys(), trigger, autoStart);
    }

    /**
     * Constructor
     * @param tradeMark Trademark String
     * @param title Title of console application
     * @param prologue Prolog string
     * @param queryMap Query Map
     * @param trigger Triggered object
     * @param autoStart Whether auto starting
     * @throws Exception throw Exception
     */
    public ConsoleInput(String tradeMark, String title, String prologue, LinkedHashMap<String, Object> queryMap, ConsoleTrigger trigger, boolean autoStart) throws Exception {
        querys = queryMap;        
        queryValues = new LinkedHashMap<>();
        this.tradeMark = tradeMark;
        this.title = title;
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
     * Get loaded query Map
     * @return return Map
     */
    public static LinkedHashMap<String, Object> getQuerys() {
        return querys;
    }

    /**
     * Get query input value Map
     * @return return Map
     */
    public static LinkedHashMap<String, Object> getQueryValues() {
        return queryValues;
    }

    /**
     * Start console input query
     * @throws Exception throw Exception
     */
    public void startQuery() throws Exception {
        while(true) {
            List<String> keys = new ArrayList<>(querys.keySet());
            Object input = null;
            for(int i=0; i<keys.size(); i++) {
                String key = keys.get(i);
                if(Pattern.matches("QUERY\\d+", key)) {
                    input = query(this.reader, querys.get(key));
                    if(input == null) {
                        exit(-1);
                    }
                    if(!queryValues.containsKey(key)) {
                        queryValues.put(key, input);
                    } else {
                        if(queryValues.keySet().stream().filter(k -> k.startsWith(key)).count() == 1) {
                            queryValues.put(key+"_1", input);
                        } else {
                            String key_ = queryValues.keySet().stream().filter(k -> k.startsWith(key+"_")).max(Comparator.comparingInt(k1 -> Integer.parseInt(k1.substring(k1.lastIndexOf("_")+1)))).get();
                            queryValues.put(key+"_"+(Integer.parseInt(key_.substring(key_.lastIndexOf("_")+1))+1), input);    
                        }
                    }
                } else if(Pattern.matches("MESSAGE\\d+", key)) {
                    this.out.println(querys.get(key));
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
                            if(input.equals(querys.get(key))) {
                                continue;
                            } else {
                                i++;
                            }
                        } else if(Pattern.matches("ELSE\\d+", key)) {
                            continue;
                        }
                    }                                       
                } else if(Pattern.matches("TRIGGER", key)) {
                    if(this.trigger != null) {
                        this.out.println();
                        if(this.trigger.trigger(queryValues)) {
                            continue;
                        }
                        this.out.println();
                        break;
                    }             
                } else if(key.equals("EXIT")) {
                    this.exitMode = 0;
                    this.exitCode = 1;
                    break;
                } else if(key.equals("GRACE_EXIT")) {
                    this.exitMode = 1;
                    this.exitCode = 0;
                    break;
                }
            }
            if(this.exitMode == 0) {
                input = query(this.reader, querys.get("EXIT"));
                if(input != null && ((input+"").equalsIgnoreCase("y") || (input+"").equalsIgnoreCase("yes") )) {
                    break;
                }
            } else if(this.exitMode == 1) {
                input = query(this.reader, querys.get("GRACE_EXIT"));
                if(input != null && ((input+"").equalsIgnoreCase("y") || (input+"").equalsIgnoreCase("yes") )) {
                    break;
                }
            }
        }
        if(this.exitMode == 0) {
            exit(this.exitCode);            
        } else if(this.exitMode == 1) {
            Runtime.getRuntime().addShutdownHook(new Thread(this.trigger::exit));
        }
    }

    /** 
     * Exit
     * @param status Exit status
     * @throws Exception throw Exception
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
     * @return return query value
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
