package org.chaostocosmos.io.console;

import java.util.Map;

/**
 * ConsoleTrigger class
 */
public interface ConsoleTrigger {

    /**
     * To be processed when interactive query process end.
     * @param inputMap
     * @return false if being needed to terminate. if you wanna run again, return true;
     * @throws Exception
     */
    public boolean trigger(Map<String, Object> inputMap) throws Exception;

    /**
     * To be processed if caceled while interactive query.
     */
    public void exit() throws Exception;
}
