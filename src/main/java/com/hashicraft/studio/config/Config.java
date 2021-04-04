package com.hashicraft.studio.config;

import java.util.Hashtable;

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
}