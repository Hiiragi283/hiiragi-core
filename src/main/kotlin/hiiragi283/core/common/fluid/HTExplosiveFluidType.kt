package hiiragi283.core.common.fluid

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTExplosiveFluidType(private val power: Float, properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    override fun onVaporize(
        entity: LivingEntity?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(entity, level, pos, stack)
        level.explode(
            entity,
            null,
            null,
            pos.center,
            power,
            true,
            Level.ExplosionInteraction.BLOCK,
        )
    }
}
