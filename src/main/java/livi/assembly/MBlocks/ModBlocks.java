package livi.assembly.MBlocks;

import livi.assembly.Assembly;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class ModBlocks {

    public static Block BELT_BLOCK;
    public static Block SHOCKER_BLOCK;
    public static Block BREAKER_BLOCK;
    public static Block FARMER_BLOCK;
    public static Block Placer_BLOCK;


    public static void initialize() {
        Assembly.LOGGER.info("Registering blocks...");

        // Register each block
        BELT_BLOCK = register("belt_block", BeltBlock::new, AbstractBlock.Settings.copy(Blocks.BRICK_SLAB));
        SHOCKER_BLOCK = register("shocker_block", BeltBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
        BREAKER_BLOCK = register("breaker_block", BreakerBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
        FARMER_BLOCK = register("farmer_block", FarmerBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));
        Placer_BLOCK = register("placer_block", PlacerBlock::new, AbstractBlock.Settings.copy(Blocks.IRON_BLOCK));

        // Optional: also ensure they appear in your custom creative tab
        /*
        ItemGroupEvents.modifyEntriesEvent(ModItems.ASSEMBLY_ITEM_GROUP_KEY).register(entries -> {
            entries.add(BELT_BLOCK.asItem());
            entries.add(SHOCKER_BLOCK.asItem());
            entries.add(BREAKER_BLOCK.asItem());
            entries.add(FARMER_BLOCK.asItem());
        });
        */

    }


    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings) {
        // Create a registry key for the block
        RegistryKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`

        // Items need to be registered with a different type of registry key, but the ID
        // can be the same.
        RegistryKey<Item> itemKey = keyOfItem(name);

        BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
        Registry.register(Registries.ITEM, itemKey, blockItem);


        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Assembly.MOD_ID, name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Assembly.MOD_ID, name));
    }


}
