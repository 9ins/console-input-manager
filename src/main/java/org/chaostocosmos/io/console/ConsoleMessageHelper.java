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
    private String prologue;
    private LinkedHashMap<String, Object> querys;
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
        final Map<String, Object> map = (LinkedHashMap<String, Object>) yaml.loadAs(fis, Map.class);
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
        this.querys = (LinkedHashMap<String, Object>)map.get("QUERYS");
        this.conti = map.get("CONTINUE") != null ? map.get("CONTINUE").toString() : "";
    }

    public void verify(Map<String, Object> map) {
        //Implement verifing logic later
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

    public String getPrologue() {
        return this.prologue;
    }

    public LinkedHashMap<String, Object> getQuerys() {
        return this.querys;
    }

    public String getQuery(String queryKey) {
        return this.querys.get(queryKey)+"";
    }

    public String getContinue() {
        return this.conti;
    }
}
