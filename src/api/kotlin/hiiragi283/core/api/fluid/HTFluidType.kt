package hiiragi283.core.api.fluid

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[FluidType]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
open class HTFluidType(properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = level.dimensionType().ultraWarm()
}
