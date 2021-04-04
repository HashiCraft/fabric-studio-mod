package com.hashicraft.studio.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Vec3d;

import java.util.Hashtable;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static com.mojang.brigadier.arguments.StringArgumentType.getString;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import it.unimi.dsi.fastutil.Hash;

@Mixin(CommandManager.class)
public class CommandMixin {

  private class Position {
    private Vec3d position;
    private float yaw;
    private float pitch;

    Position(Vec3d pos, float pitch, float yaw) {
      this.position = pos;
      this .pitch = pitch;
      this .yaw = yaw;
    }

    Vec3d getPosition() {
      return this.position;
    }
    
    float getPitch() {
      return this.pitch;
    }
    
    float getYaw() {
      return this.yaw;
    }
  }
  

  private Hashtable<String, Position> positions = new Hashtable<String, Position>();


  @Shadow
  @Final
  private CommandDispatcher<ServerCommandSource> dispatcher;

  @Inject(method = "<init>", at = @At("RETURN"))
  private void onRegister(CommandManager.RegistrationEnvironment arg, CallbackInfo ci) {
    System.out.println("Registering custom commands");

    this.dispatcher.register(
        literal("studio")
          .then(literal("camera")
            .then(newArgs())
            .then(setArgs())
          )
      );
  }

  private LiteralArgumentBuilder<ServerCommandSource> newArgs() {
    return literal("new")
      .then(
        argument("name", string())
          .executes(context -> {
            CompletableFuture.runAsync(() -> {
                System.out.println("camera_set: ");
                PlayerEntity player;
                try {
                  player = context.getSource().getPlayer();

                  Vec3d position  = player.getPos();
                  float yaw = player.yaw;
                  float pitch = player.pitch;

                  String name = getString(context, "name");

                  System.out.println("name " + name);
                  System.out.println("pos" + position);
                  System.out.println("pitch" + pitch);
                  System.out.println("yaw" + yaw);

                  // add the position to the collection
                  this.positions.put(name, new Position(position,pitch,yaw));
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
  
  private LiteralArgumentBuilder<ServerCommandSource> setArgs() {
    return literal("set")
      .then(
        argument("name", string())
          .executes(context -> {
            CompletableFuture.runAsync(() -> {
                System.out.println("camera_set: ");
                PlayerEntity player;
                try {
                  String name = getString(context, "name");
                  player = context.getSource().getPlayer();
                  Position position = this.positions.get(name);

                  System.out.println("name " + name);
                  System.out.println("pos" + position.getPosition());
                  System.out.println("pitch" + position.getPitch());
                  System.out.println("yaw" + position.getYaw());

                  player.yaw  = position.getYaw();
                  player.pitch = position.getPitch();

                  player.teleport(position.getPosition().x,position.getPosition().y, position.getPosition().z);

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
}