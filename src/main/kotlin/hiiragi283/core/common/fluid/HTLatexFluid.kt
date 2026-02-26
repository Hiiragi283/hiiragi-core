package hiiragi283.core.common.fluid

import hiiragi283.core.setup.HCItems
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
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
            HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), HCItems.RAW_RUBBER.toStack(random.nextInt(1, 4)))
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        }
    }
}
