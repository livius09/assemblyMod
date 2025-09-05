package livi.assembly.Blocks;

import livi.assembly.Assembly;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
        Block crop = world.getBlockState(pos.down()).getBlock();

        if( crop instanceof CropBlock){
            BlockState cropstate =  world.getBlockState(pos.down());
            CropBlock fixcrop = (CropBlock) crop;

            Assembly.LOGGER.info("detected crop. Age:{}", fixcrop.getAge(cropstate));

            if (fixcrop.isMature(cropstate)){


                Item cropseed = fixcrop.asItem();

                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0; // spawn above the harvester
                double z = pos.getZ() + 0.5;


                Item produce = null;


                if (fixcrop instanceof net.minecraft.block.CarrotsBlock) {
                    produce = Items.CARROT;
                } else if (fixcrop instanceof net.minecraft.block.PotatoesBlock) {
                    produce = Items.POTATO;
                } else if (fixcrop instanceof net.minecraft.block.BeetrootsBlock) {
                    produce = Items.BEETROOT;
                } else if (fixcrop instanceof CropBlock){
                    // This is wheat, since it's the "default" crop
                    produce = Items.WHEAT;
                } else {
                    // fallback: just give the seed item
                    produce = fixcrop.asItem();
                }


                ItemStack loot = new ItemStack(produce,2);

                world.spawnEntity(new ItemEntity(world, x, y, z, loot));



                Assembly.LOGGER.info("harvested crop:"+loot);


                world.setBlockState(pos.down(), fixcrop.withAge(0));

            }
        }
        world.scheduleBlockTick(pos, this, 20); // reschedule tick
    }
}
