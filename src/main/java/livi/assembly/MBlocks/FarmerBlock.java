package livi.assembly.MBlocks;

import livi.assembly.Assembly;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;


public class FarmerBlock extends Block {
    public FarmerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        // Schedule the first tick (20 ticks = 1 second)
        world.scheduleBlockTick(pos, this, 20);
    }


    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        BlockPos target = pos.down();
        Block crop = world.getBlockState(target).getBlock();


        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.3; // spawn above the harvester
        double z = pos.getZ() + 0.5;


        ItemStack lootStack = ItemStack.EMPTY;

        if (crop instanceof CropBlock) {

            BlockState cropstate = world.getBlockState(target);
            CropBlock fixcrop = (CropBlock) crop;


            //Assembly.LOGGER.info("detected crop. Age:{}", fixcrop.getAge(cropstate));

            if (fixcrop.isMature(cropstate)) {


                if (fixcrop instanceof net.minecraft.block.CarrotsBlock) {
                    lootStack = new ItemStack(Items.CARROT, 3);
                } else if (fixcrop instanceof net.minecraft.block.PotatoesBlock) {
                    lootStack = new ItemStack(Items.POTATO, 4);
                } else if (fixcrop instanceof net.minecraft.block.BeetrootsBlock) {
                    lootStack = new ItemStack(Items.BEETROOT, 2);
                } else if (fixcrop instanceof CropBlock) {
                    // This is wheat, since it's the "default" crop
                    lootStack = new ItemStack(Items.WHEAT, 2);
                } else {
                    // fallback: just give the seed item
                    lootStack = new ItemStack(fixcrop.asItem(), 2);
                }


                world.setBlockState(pos.down(), fixcrop.withAge(0));

            }
        }

        if (crop instanceof SugarCaneBlock) {
            lootStack = new ItemStack(Items.SUGAR_CANE, 2);
            world.breakBlock(target, false);
        } else if (crop instanceof BambooBlock) {
            lootStack = new ItemStack(Items.BAMBOO, 2);
            world.breakBlock(target, false);
        } else if (crop instanceof PumpkinBlock) {
            lootStack = new ItemStack(Items.PUMPKIN);
            world.breakBlock(target, false);
        } else if(crop instanceof MushroomPlantBlock) {
            lootStack = new ItemStack(Items.BROWN_MUSHROOM);
            world.breakBlock(target, false);
        }

        if (!lootStack.isEmpty()) {
            world.spawnEntity(new ItemEntity(world, x, y, z, lootStack));

            Assembly.LOGGER.info("harvested crop:" + lootStack);
        }

        world.scheduleBlockTick(pos, this, 20); // reschedule tick
    }
}
