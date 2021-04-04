package com.hashicraft.studio.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

import com.hashicraft.studio.config.Config;
import com.hashicraft.studio.config.Position;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.Perspective;

@Mixin(CommandManager.class)
public class CommandMixin {

  private static final Logger LOGGER = LogManager.getLogger();
  private MinecraftClient mc = MinecraftClient.getInstance();

  @Shadow
  @Final
  private CommandDispatcher<ServerCommandSource> dispatcher;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void onRegister(CommandManager.RegistrationEnvironment arg, CallbackInfo ci) {
    System.out.println("Registering custom commands");

    this.dispatcher.register(
        literal("studio")
          .then(literal("camera")
            .then(setArgs())
            .then(viewArgs())
            .then(clearArgs())
          )
      );
  }

  private LiteralArgumentBuilder<ServerCommandSource> setArgs() {
    return literal("set")
      .then(
        argument("name", string())
          .executes(context -> {
            CompletableFuture.runAsync(() -> {
                try {
                  PlayerEntity player = context.getSource().getPlayer();

                  Vec3d cameraPosition  = player.getCameraPosVec(0);
                  Vec3d playerPosition  = player.getPos();

                  float yaw = player.yaw;
                  float pitch = player.pitch;

                  String name = getString(context, "name");

                  LOGGER.info("name {}", name);
                  LOGGER.info("pos {}", cameraPosition);
                  LOGGER.info("pitch {}", pitch);
                  LOGGER.info("yaw {}", yaw);

                  Config.setPosition(name, new Position(cameraPosition,playerPosition, pitch,yaw));

                } catch (CommandSyntaxException e) {
                  e.printStackTrace();
                }
                return;
            });

            return 1;
          }
        )
      );
  }
  
  private LiteralArgumentBuilder<ServerCommandSource> viewArgs() {
    return literal("view")
      .then(
        argument("name", string())
          .executes(context -> {
            CompletableFuture.runAsync(() -> {
                String name = getString(context, "name");

                // Teleport the player to the camera location
                try {
                  PlayerEntity player = context.getSource().getPlayer();
                  Vec3d pos = Config.getPosition(name).getPlayer();
                  if(pos == null){
                    return;
                  }

                  player.teleport(pos.x, pos.y, pos.z);

                  mc.options.bobView = false; 
                  mc.options.hudHidden = true;
                  mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                  mc.options.fov = 36; // Third person moves the camera behind the player zoom in to compensate
                
                  Config.setActive(name);
                  return;
                
                } catch (CommandSyntaxException e) {
                  e.printStackTrace();
                }
            });

            return 1;
          }
        )
      );
  }

  private LiteralArgumentBuilder<ServerCommandSource> clearArgs() {
    return literal("clear")
      .executes(context -> {
        CompletableFuture.runAsync(() -> {
          Config.setActive("");

          MinecraftClient mc = MinecraftClient.getInstance();
          mc.options.bobView = true; 
          mc.options.hudHidden = false;
          mc.options.setPerspective(Perspective.FIRST_PERSON);
          mc.options.fov = 70; // reset the fov to normal

          return;
        });

        return 1;
      });
  }
}