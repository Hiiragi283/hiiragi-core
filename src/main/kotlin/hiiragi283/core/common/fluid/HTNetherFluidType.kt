package hiiragi283.core.common.fluid

import hiiragi283.lib.fluid.HTFluidType
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTNetherFluidType(properties: Properties) : HTFluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = !super.isVaporizedOnPlacement(level, pos, stack)
}
