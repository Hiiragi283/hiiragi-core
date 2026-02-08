package hiiragi283.core.common.fluid

import hiiragi283.core.util.HTExperienceHelper
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

class HTExperienceFluidType(properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        level.playSound(player, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 0.5F, 1.0F)
        val expAmount: Int = HTExperienceHelper.expAmountFromFluid(stack.amount)
        if (player != null) {
            player.giveExperiencePoints(expAmount)
        } else {
            HTExperienceHelper.popExperienceOrb(level, pos, expAmount)
        }
    }
}
