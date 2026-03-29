package hiiragi283.core.common.fluid

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.fluids.BaseFlowingFluid

class HTLatexFluid(properties: Properties) : BaseFlowingFluid.Source(properties) {
    override fun isRandomlyTicking(): Boolean = true

    override fun randomTick(
        level: ServerLevel,
        pos: BlockPos,
        fluidState: FluidState,
        random: RandomSource,
    ) {
        if (random.nextInt(7) == 0) {
            // HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), HCItems.RAW_RUBBER.toStack(random.nextInt(1, 4)))
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        }
    }
}
