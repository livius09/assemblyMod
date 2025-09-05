package livi.assembly.Blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

import static net.minecraft.state.property.Properties.FACING;


public class BeltBlock extends HorizontalFacingBlock {

    protected BeltBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        // Schedule the first tick (20 ticks = 1 second)
        world.scheduleBlockTick(pos, this, 2);
    }



    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        Box area = new Box(pos).expand(0.2); // detect items

        List<ItemEntity> items = world.getEntitiesByClass(ItemEntity.class, area, item -> true);

        for (ItemEntity item : items) {
            Direction dir = state.get(FACING);
            Vec3d push = Vec3d.of(dir.getVector()).multiply(-0.1); //whitout the - it pushes against the placed direction
            item.addVelocity(push.x, 0, push.z);

            // Centering toward belt middle
            double centerX = pos.getX() + 0.5;
            double centerZ = pos.getZ() + 0.5;
            double dx = (centerX - item.getX()) * 0.05;
            double dz = (centerZ - item.getZ()) * 0.05;
            item.addVelocity(dx, 0, dz);

            item.velocityModified = true; // tell MC that velocity changed
        }

        // Always reschedule
        world.scheduleBlockTick(pos, this, 2);
    }
}
