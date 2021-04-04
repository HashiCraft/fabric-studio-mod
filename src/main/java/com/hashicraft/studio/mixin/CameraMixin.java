package com.hashicraft.studio.mixin;

import com.hashicraft.studio.config.Config;
import com.hashicraft.studio.config.Position;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	public abstract void setPos(double x, double y, double z);
	
  @Shadow
  public abstract void setRotation(float yaw, float pitch);

  @Shadow
  public abstract double clipToSpace(double desiredCameraDistance);

  private static final Logger LOGGER = LogManager.getLogger();

	@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V", ordinal = 0, shift = At.Shift.AFTER))
	public void lockPosition(BlockView area, Entity player, boolean isThirdPerson, boolean inverseView, float f, CallbackInfo ci) {
		if (Config.isActive() && player instanceof PlayerEntity) {
      Position pos = Config.getActive(); 
      
      LOGGER.info("Setting camera position x:{} y:{} z:{}",pos.getCamera().x, pos.getCamera().y, pos.getCamera().z);
      this.setPos(pos.getCamera().x, pos.getCamera().y, pos.getCamera().z);
      MinecraftClient mc = MinecraftClient.getInstance();
      
      LOGGER.info("fov {}", mc.options.fov);
		}
	}
	
  @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 0, shift = At.Shift.AFTER))
	public void lockRotation(BlockView focusedBlock, Entity player, boolean isThirdPerson, boolean inverseView, float f, CallbackInfo ci) {
		if (Config.isActive() && player instanceof PlayerEntity) {
      Position pos = Config.getActive(); 
     
      LOGGER.info("Setting camera rotation yaw:{} pitch:{}", pos.getYaw(), pos.getPitch());
      this.setRotation(pos.getYaw(), pos.getPitch());
		}
	}
	
  //@Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;clipToSpace(D)D", ordinal = 0, shift = At.Shift.AFTER))
	//public void clipPos(BlockView area, Entity player, boolean isThirdPerson, boolean inverseView, float f, CallbackInfo ci) {
		//if (Config.isActive() && player instanceof PlayerEntity) {
  //    LOGGER.info("Clip to space {}", this.clipToSpace(1));

  //    MinecraftClient mc = MinecraftClient.getInstance();
  //    LOGGER.info("Clip setting fov {}", mc.options.fov);
  //    mc.options.fov = 25;
		//}
	//}
}