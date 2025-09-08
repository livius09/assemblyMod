package livi.assembly.MBlocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BreakerBlock extends HorizontalFacingBlock {

    protected BreakerBlock(AbstractBlock.Settings settings) {
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

}
