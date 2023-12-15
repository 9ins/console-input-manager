package org.chaostocosmos.io.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

/**
 * ConsoleMessageHelper
 * 
 * @author 9ins
 */
public class ConsoleMessageHelper {

    private File yamlFile;
    private Charset charset; 
    private String tradeMark;
    private String title;
    private String prologue;
    private LinkedHashMap<String, Object> querys;
    private String exit;

    /**
     * Constructor
     * @param yamlFile Message YAML file
     * @param charset Charset
     * @throws IOException  throw Exception
     * @throws URISyntaxException throw Exception
     */
    public ConsoleMessageHelper(File yamlFile, Charset charset) throws IOException, URISyntaxException {
        this.yamlFile = yamlFile;
        setup();
        load(this.yamlFile);
    }

    /**
     * Set up 
     * @throws URISyntaxException throw Exception
     * @throws IOException throw Exception
     */
    private void setup() throws IOException, URISyntaxException {
        if(this.yamlFile == null || !this.yamlFile.exists()) {
            this.yamlFile = new File("messages.yml");
            try(FileOutputStream out = new FileOutputStream(this.yamlFile)) {
                out.write(loadResourceBytes(this.yamlFile.getName()));
            }
        }
        if(this.tradeMark == null || this.tradeMark.equals("")) {
            byte[] bytes = loadResourceBytes("trademark");
            this.tradeMark = new String(bytes, this.charset != null ? this.charset : StandardCharsets.UTF_8);
        }
    }

    /**
     * Extract messages.yml and trademark
     * @param resourceName Resource name
     * @return return bytes
     * @throws IOException throw Exception
     * @throws URISyntaxException throw Exception
     */
    private byte[] loadResourceBytes(String resourceName) throws IOException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource("");
        String protocol = url.getProtocol();
        File file = new File(resourceName);
        byte[] bytes = null;
        if(protocol.equals("jar")) {
            try(FileSystem fileSystem = FileSystems.newFileSystem(url.toURI(), new HashMap<>());
                FileOutputStream output = new FileOutputStream(file)) {
                bytes = Files.readAllBytes(fileSystem.getPath(resourceName));
                output.write(bytes);
            }
        } else if(protocol.equals("file")) {
            try(FileOutputStream output = new FileOutputStream(file)) {                                                        
                bytes = Files.readAllBytes(Paths.get(url.toURI()).resolve(resourceName));
                output.write(bytes);
            }
        } else {
            throw new IllegalArgumentException("Protocol not supported: "+protocol);
        }    
        return bytes;
    }

    /**
     * Load yaml file
     * @param yamlFile Message YAML file
     * @throws IOException throw Exception
     */
    public void load(File yamlFile) throws IOException {
        Yaml yaml = new Yaml();
        FileInputStream fis = new FileInputStream(yamlFile);
        final Map<String, Object> map = (Map<String, Object>) yaml.loadAs(fis, LinkedHashMap.class);
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
        this.querys = ((LinkedHashMap<String, Object>) map.get("QUERYS"));
        this.exit = map.get("CONTINUE") != null ? map.get("CONTINUE").toString() : "";
    }

    /**
     * Verify query map
     * @param map Query Map verification
     */
    public void verify(Map<String, Object> map) {
        //Implement verifing logic later
    }

    /**
     * Get Yaml file
     * @return return file
     */
    public File getYamlFile() {
        return this.yamlFile;
    }

    /**
     * Get trademark string(ascii art)
     * @return return trademark
     */
    public String getTradeMark() {
        return this.tradeMark;
    }

    /**
     * Get title string
     * @return return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get prologue string
     * @return return prologue
     */
    public String getPrologue() {
        return this.prologue;
    }

    /**
     * Get query map
     * @return return Map
     */
    public LinkedHashMap<String, Object> getQuerys() {
        return this.querys;
    }

    /**
     * Get query matching param
     * @param queryKey query key
     * @return return query
     */
    public String getQuery(String queryKey) {
        return this.querys.get(queryKey)+"";
    }
}
