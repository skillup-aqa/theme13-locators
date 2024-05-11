package ua.skillup.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class LocatorsReader {
    public static Map<String, Map<String, String>> getSelectors() throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream input = new FileInputStream("src/test/resources/data/locators.yaml");
        return (Map<String, Map<String, String>>) yaml.load(input);
    }
}
