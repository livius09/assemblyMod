package livi.assembly.Blocks;

import livi.assembly.Assembly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;


public class FarmerBlock extends Block {
    public FarmerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        Block crop = world.getBlockState(pos.down()).getBlock();

        if( crop instanceof CropBlock){
            BlockState cropstate =  world.getBlockState(pos.down());
            CropBlock fixcrop = (CropBlock) crop;

            Assembly.LOGGER.info("detected crop. Age:"+fixcrop.getAge(cropstate));

            if (fixcrop.isMature(cropstate)){


                Item cropseed = fixcrop.asItem();

                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0; // spawn above the harvester
                double z = pos.getZ() + 0.5;


                // Prepare LootContext
                //yeah would be nice right


                ItemStack loot = new ItemStack(cropseed,5);

                world.spawnEntity(new ItemEntity(world, x, y, z, loot));
                Assembly.LOGGER.info("harvested crop:"+cropseed);


                world.setBlockState(pos.down(), fixcrop.withAge(0));

            }
        }
        world.scheduleBlockTick(pos, this, 20); // reschedule tick
    }
}
