package com.hashicraft.studio.keybinding;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class ZoomKeyBinding extends KeyBinding {
 	public ZoomKeyBinding(Identifier identifier) {
		super(Util.createTranslationKey("key", identifier), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, "key.categories.misc");
	} 

  private static final Logger LOGGER = LogManager.getLogger();
  private MinecraftClient mc = MinecraftClient.getInstance();

	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
    //mc.options.fov = mc.options.fov - 5; 
    //LOGGER.info("zoom {}", mc.options.fov);
  }
}