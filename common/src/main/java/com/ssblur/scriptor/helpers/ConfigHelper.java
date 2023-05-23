package com.ssblur.scriptor.helpers;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ssblur.scriptor.ScriptorExpectPlatform;
import com.ssblur.scriptor.ScriptorMod;

import java.io.*;
import java.lang.reflect.Type;

public class ConfigHelper {
  public static class Config {
    public int basicTomeMaxCost = 50;

    public int vocalCastingHungerThreshold = 300;
    public int vocalCastingHurtThreshold = 900;
    public int vocalCastingMaxCost = -1;

    public Config(){}
  }

  @SuppressWarnings("UnstableApiUsage")
  static Type CONFIG_TYPE = new TypeToken<Config>() {}.getType();
  static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

  static Config CONFIG;

  public static Config getConfig() {
    if(CONFIG == null)
      try(BufferedReader reader = new BufferedReader(new FileReader(ScriptorExpectPlatform.getConfigDirectory().toFile()))) {
        CONFIG = GSON.fromJson(reader, CONFIG_TYPE);
      } catch (IOException e) {
        try {
          CONFIG = new Config();
          var writer = new FileWriter(ScriptorExpectPlatform.getConfigDirectory().toFile());
          GSON.toJson(CONFIG, writer);
          writer.flush();
          writer.close();
        } catch (IOException ex) {
          ScriptorMod.LOGGER.error(ex);
          ScriptorMod.LOGGER.error("Cannot create or load config file!");
          ScriptorMod.LOGGER.error("Please be sure {} is writable", ScriptorExpectPlatform.getConfigDirectory());
        }
      }
    return CONFIG;
  }
}
