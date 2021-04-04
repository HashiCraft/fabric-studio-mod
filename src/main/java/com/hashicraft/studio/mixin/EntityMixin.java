package com.hashicraft.studio.mixin;

import com.hashicraft.studio.config.Config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Mixin(Entity.class)
public class EntityMixin {
	@Unique
	private float cameraPitch;

	@Unique
	private float cameraYaw;
	
  @Unique
	private Vec3d cameraPos;

  private static final Logger LOGGER = LogManager.getLogger();

	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	public void changeCameraLookDirection(double xDelta, double yDelta, CallbackInfo ci) {
	  if (!Config.isActive() || !((Object) this instanceof ClientPlayerEntity)) return;
    LOGGER.info("look direction {} {}", yDelta, xDelta);
    
		//ci.cancel();
	}
}