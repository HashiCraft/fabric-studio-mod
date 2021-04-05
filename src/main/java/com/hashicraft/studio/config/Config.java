package com.hashicraft.studio.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Hashtable;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.packet.s2c.play.BossBarS2CPacket.Type;

public class Config {
  private static Hashtable<String, Position> positions = new Hashtable<String, Position>();
  private static Position active;

  public static void setPosition(String name, Position position) {
    positions.put(name, position);
  }

  public static Position getPosition(String name) {
    return positions.get(name);
  }

  public static Boolean isActive() {
    return active != null;
  }

  public static void setActive(String name) {
    if(name != "") {
      active = positions.get(name);
    } else {
      active = null;
    }
  }

  public static Position getActive() {
    return active;
  }

  public static void save() throws JsonIOException,IOException {
    Path configPath = FabricLoader.getInstance().getConfigDir().resolve("studio.json");
    FileWriter file = new FileWriter(configPath.toString());
    
    Gson gson = new Gson();
    gson.toJson(positions, file);
    
    file.close();
  }
  
  public static void load() throws JsonSyntaxException,IOException {
    Path configPath = FabricLoader.getInstance().getConfigDir().resolve("studio.json");
    FileReader file = new FileReader(configPath.toString());
    
    Gson gson = new Gson();
    java.lang.reflect.Type type = new TypeToken<Hashtable<String, Position>>(){}.getType();
    positions = gson.fromJson(file, type);
    
    file.close();
  }
}