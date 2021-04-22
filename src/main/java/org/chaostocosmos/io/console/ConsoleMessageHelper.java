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
 */
public class ConsoleMessageHelper {

    private File yamlFile;
    private String tradeMark;
    private String title;
    private LinkedHashMap<String, String> querys;
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
        Map<String, Object> map = (LinkedHashMap<String, Object>) yaml.loadAs(fis, Map.class);
        verify(map);
        
        File trademarkFile = new File(map.get("TRADEMARK_FILE")+"");
        if(!trademarkFile.exists()) {
            this.tradeMark = "";
        } else {
            this.tradeMark = Files.lines(trademarkFile.toPath()).collect(Collectors.joining(System.lineSeparator()));
        }
        this.title = map.get("TITLE")+"";
        this.querys = (LinkedHashMap<String, String>)map.get("QUERYS");
        this.conti = map.get("CONTINUE")+"";
    }

    public void verify(Map<String, Object> map) {
        if(map.get("TRADEMARK_FILE") == null) {
            throw new IllegalArgumentException("Missing TRADEMARK_FILE key in YAML file !!!");
        }
        if(map.get("TITLE") == null) {
            throw new IllegalArgumentException("Missing TITLE key in YAML file !!!");
        }
        if(map.get("QUERYS") == null) {
            throw new IllegalArgumentException("Missing QUERYS key in YAML file !!!");
        }
    }

    public File getYamlFile() {
        return this.yamlFile;
    }

    public String getTradeMark() {
        return this.tradeMark;
    }

    public String getTitle() {
        return this.title;
    }

    public LinkedHashMap<String, String> getQuerys() {
        return this.querys;
    }

    public String getQuery(String queryKey) {
        return this.querys.get(queryKey)+"";
    }

    public String getContinue() {
        return this.conti;
    }
}
