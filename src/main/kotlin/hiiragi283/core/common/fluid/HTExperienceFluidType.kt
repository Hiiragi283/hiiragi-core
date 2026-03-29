package hiiragi283.core.common.fluid

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTExperienceFluidType(properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    override fun onVaporize(
        entity: LivingEntity?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        level.playSound(entity, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.5F, 1.0F)
        val player: Player? = entity as? Player
        /*val expAmount: ExpValue = HTExperienceHelper.expAmountFromFluid(stack.amount)
        if (player != null) {
            player.storedExperience += expAmount
        } else {
            HTExperienceHelper.popExperienceOrb(level, pos, expAmount)
        }*/
    }
}
