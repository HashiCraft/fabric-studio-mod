package com.hashicraft.studio.config;

import net.minecraft.util.math.Vec3d;

public class Position {
  private Vec3d camera;
  private Vec3d player;
  private float yaw;
  private float pitch;

  public Position(Vec3d camera, Vec3d player, float pitch, float yaw) {
    this.camera = camera;
    this.player = player;
    this .pitch = pitch;
    this .yaw = yaw;
  }

  public Vec3d getPlayer() {
    return this.player;
  }
  
  public Vec3d getCamera() {
    return this.camera;
  }
  
  public float getPitch() {
    return this.pitch;
  }
  
  public float getYaw() {
    return this.yaw;
  }
}