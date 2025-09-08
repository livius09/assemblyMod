package livi.assembly.MBlocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class PlacerBlock extends HorizontalFacingBlock {

    protected PlacerBlock(AbstractBlock.Settings settings) {
            super(settings);
            setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
        }

        @Override
        protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
            return null;
        }

        @Override
        protected void appendProperties(StateManager.Builder< Block, BlockState > builder) {
            builder.add(FACING);
        }

        @Override
        public BlockState getPlacementState(ItemPlacementContext ctx) {
            return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
        }

        @Override
        public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
            super.onBlockAdded(state, world, pos, oldState, notify);

            // Schedule the first tick (20 ticks = 1 second)
            world.scheduleBlockTick(pos, this, 2);
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            // 16 = 1 block; so 4 = 1/4 block high
            return Block.createCuboidShape(0, 0, 0, 16, 4, 16);
        }

        @Override
        public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

            Box area = new Box(pos).expand(0.2); // detect item
            List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, area, e -> true);

            for (ItemEntity item : items) {
                ItemStack stack = item.getStack();

                if (stack.getItem() instanceof BlockItem blockItem) {
                    BlockPos targetPos = pos.north(); // default

                    if (world.isReceivingRedstonePower(pos)) {
                        // Scan north for up to 10 blocks
                        for (int i = 0; i < 100; i++) {
                            BlockPos checkPos = pos.add(-1, i-1, 0); // move in Z direction
                            if (world.getBlockState(checkPos).isAir() ||
                                    world.getBlockState(checkPos).isReplaceable()) {
                                targetPos = checkPos;
                                break;
                            }
                        }
                    } else {
                        // Scan east for up to 10 blocks
                        for (int i = 0; i < 100; i++) {
                            BlockPos checkPos = pos.add(i, 0, 0); // move in X direction
                            if (world.getBlockState(checkPos).isAir() ||
                                    world.getBlockState(checkPos).isReplaceable()) {
                                targetPos = checkPos;
                                break;
                            }
                        }
                    }


                    if (world.isAir(pos)||
                            world.getBlockState(targetPos).isReplaceable()) {

                            world.setBlockState(targetPos, blockItem.getBlock().getDefaultState());
                            stack.decrement(1);

                            if (stack.isEmpty()) {
                                item.discard(); // remove entity if no item left
                            }
                        }
                    }

            }
            // Always reschedule
            world.scheduleBlockTick(pos, this, 2);
        }

}
