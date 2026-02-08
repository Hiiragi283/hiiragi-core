package hiiragi283.core.common.fluid

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.TntBlock
import net.minecraft.world.level.material.FluidState
import net.neoforged.neoforge.fluids.BaseFlowingFluid

class HTLatexFluid(properties: Properties) : BaseFlowingFluid.Source(properties) {
    override fun isRandomlyTicking(): Boolean = true

    override fun randomTick(
        level: Level,
        pos: BlockPos,
        state: FluidState,
        random: RandomSource,
    ) {
        if (random.nextInt(7) == 0) {
            level.setBlockAndUpdate(pos, Blocks.TNT.defaultBlockState().setValue(TntBlock.UNSTABLE, true))
        }
    }
}
