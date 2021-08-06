package org.chaostocosmos.io.console;

import java.util.Map;

/**
 * ConsoleTrigger class
 */
public interface ConsoleTrigger {

    /**
     * To be processed when interactive query process end.
     * @param inputMap
     * @throws Exception
     */
    public void trigger(Map<String, Object> inputMap) throws Exception;

    /**
     * To be processed if caceled while interactive query.
     */
    public void canceled();
}
