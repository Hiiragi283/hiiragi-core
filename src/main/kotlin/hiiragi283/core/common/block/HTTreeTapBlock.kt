package hiiragi283.core.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.setup.HCFluids
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.LayeredCauldronBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTTreeTapBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {
    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun codec(): MapCodec<HTTreeTapBlock> = throw UnsupportedOperationException()

    /**
     * @see LayeredCauldronBlock.receiveStalactiteDrip
     */
    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        if (random.nextInt(5) == 0) {
            val posBelow: BlockPos = pos.below()
            HTFluidCapabilities.getCapability(level, posBelow, Direction.UP)?.let { handler: IFluidHandler ->
                handler.fill(HCFluids.LATEX.toStack(HTConst.DEFAULT_FLUID_AMOUNT / 4), IFluidHandler.FluidAction.EXECUTE)
            }
            level.levelEvent(1047, posBelow, 0)
        }
    }

    override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean = level.getBlockState(pos.relative(state.getValue(FACING))).`is`(HiiragiCoreTags.Blocks.LATEX_DRIPPING_LOGS)

    override fun getShape(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
    ): VoxelShape = super.getShape(state, level, pos, context)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        var state: BlockState = defaultBlockState()
        val level: Level = context.level
        val pos: BlockPos = context.clickedPos

        for (direction: Direction in context.nearestLookingDirections) {
            if (direction.axis.isHorizontal) {
                state = state.setValue(FACING, direction)
                if (state.canSurvive(level, pos)) {
                    return state
                }
            }
        }
        return null
    }

    override fun updateShape(
        state: BlockState,
        direction: Direction,
        neighborState: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        neighborPos: BlockPos,
    ): BlockState = when (direction) {
        state.getValue(FACING) if !state.canSurvive(level, pos) -> Blocks.AIR.defaultBlockState()
        else -> super.updateShape(state, direction, neighborState, level, pos, neighborPos)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false
}
