package com.hashicraft.studio;

import java.util.function.ToIntFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.block.BlockState;

public class StudioMod implements ModInitializer {

  public static final Block CHROMAGREEN_BLOCK = new Block(
    FabricBlockSettings.of(Material.WOOL).strength(4.0f, 0).luminance(getLightValueLit(15))
  );

	@Override
	public void onInitialize() {
		System.out.println("Loading HashiCraft Studio Mod");

    Registry.register(Registry.BLOCK, new Identifier("studio", "chromagreen_block"), CHROMAGREEN_BLOCK);
    Registry.register(Registry.ITEM, new Identifier("studio", "chromagreen"), new BlockItem(CHROMAGREEN_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
	}

  private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
	  return (block) -> {
		  return lightValue;
	  };
  }
}
