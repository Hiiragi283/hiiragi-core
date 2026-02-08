package hiiragi283.core.common.fluid

import hiiragi283.core.api.fluid.HTFluidType
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTExplosiveFluidType(private val power: Float, properties: Properties) : HTFluidType(properties) {
    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(player, level, pos, stack)
        level.explode(
            null,
            null,
            null,
            pos.center,
            power,
            true,
            Level.ExplosionInteraction.BLOCK,
        )
    }
}
