package org.drver.vocabulary.tool.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationConfig {

  private static Properties props;
  private static File configFile;
  private static long lastModified = 0l;  

  private static void init() {
    configFile = new File("application.properties");  
    props = new Properties();
    load();
  }

  private static void load() {
    try {
      props.load(new FileInputStream(configFile));
      lastModified = configFile.lastModified();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  } 

  public static String getConfig(String key) {
    if (props == null || configFile == null) {
      init();
    }
    if (lastModified != configFile.lastModified()) {
      load();
    }
    return props.getProperty(key);
  }
}
