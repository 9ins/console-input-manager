package org.chaostocosmos.io.console;

/**
 * QUERYS
 * 
 * @author 9ins
 */
public enum QUERYS {
    MESSAGE,
    QUERY,
    TRIGGER,
    IF,
    ELSE,
    GOTO,
    EXIT,
    GRACE_EXIT;

    /**
     * Get query message
     * @param queryNum query number
     * @return return message
     */
    public Object getQueryMessage(int queryNum) {
        return ConsoleInput.getQuerys().entrySet().stream().filter(e -> e.getKey().equals(name()+queryNum)).map(e -> e.getValue()).findFirst().orElse(null);
    }
    
    /**
     * Get query input value
     * @param queryNum query number
     * @return reutrn query value
     */
    public Object getQueryValue(int queryNum) {
        return ConsoleInput.getQueryValues().entrySet().stream().filter(e -> e.getKey().equals(name() + queryNum)).map(e -> e.getValue()).findFirst().orElse(null);
    }
}
