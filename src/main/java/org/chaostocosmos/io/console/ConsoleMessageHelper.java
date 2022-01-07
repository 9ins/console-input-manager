package org.chaostocosmos.io.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

/**
 * ConsoleMessageHelper class
 * 
 * @author 9ins
 */
public class ConsoleMessageHelper {

    private File yamlFile;
    private String tradeMark;
    private String title;
    private String prologue;
    private Map<String, Object> querys;
    private String conti;

    /**
     * Constructor
     * @param yamlFile
     * @throws IOException
     */
    public ConsoleMessageHelper(File yamlFile) throws IOException {
        this.yamlFile = yamlFile;
        load(this.yamlFile);
    }

    /**
     * Load yaml file
     * @param yamlFile
     * @throws IOException
     */
    public void load(File yamlFile) throws IOException {
        Yaml yaml = new Yaml();
        FileInputStream fis = new FileInputStream(yamlFile);
        final Map<String, Object> map = ((LinkedHashMap<?, ?>) yaml.loadAs(fis, Map.class)).entrySet().stream().collect(Collectors.toMap(k -> (String)k.getKey(), v -> (Object)v.getValue()));
        verify(map);        
        File trademarkFile = new File(map.get("TRADEMARK_FILE")+"");
        if(!trademarkFile.exists()) {
            this.tradeMark = "";
        } else {
            this.tradeMark = Files.lines(trademarkFile.toPath())
                                  .map(l -> l.contains("@version") ? l.replace("@version", map.get("VERSION")+"") : l)
                                  .collect(Collectors.joining(System.lineSeparator()));
        }
        System.out.println(map.get("PROLOGUE"));
        this.title = map.get("TITLE") != null ? map.get("TITLE").toString() : "";
        this.prologue = map.get("PROLOGUE") != null ? map.get("PROLOGUE").toString() : "";
        this.querys = ((LinkedHashMap<?, ?>)map.get("QUERYS")).entrySet().stream().collect(Collectors.toMap(k -> (String)k.getKey(), v -> (Object)v.getValue()));
        this.conti = map.get("CONTINUE") != null ? map.get("CONTINUE").toString() : "";
    }

    /**
     * Verify query map
     * @param map
     */
    public void verify(Map<String, Object> map) {
        //Implement verifing logic later
    }

    /**
     * Get Yaml file
     * @return
     */
    public File getYamlFile() {
        return this.yamlFile;
    }

    /**
     * Get trademark string(ascii art)
     * @return
     */
    public String getTradeMark() {
        return this.tradeMark;
    }

    /**
     * Get title string
     * @return
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get prologue string
     * @return
     */
    public String getPrologue() {
        return this.prologue;
    }

    /**
     * Get query map
     * @return
     */
    public LinkedHashMap<String, Object> getQuerys() {
        return (LinkedHashMap<String, Object>)this.querys;
    }

    /**
     * Get query matching param
     * @param queryKey
     * @return
     */
    public String getQuery(String queryKey) {
        return this.querys.get(queryKey)+"";
    }

    /**
     * Get continue string
     * @return
     */
    public String getContinue() {
        return this.conti;
    }
}
