package hiiragi283.core.common.fluid

import hiiragi283.core.api.HTDefaultColor
import hiiragi283.core.api.VanillaColoredContents
import hiiragi283.core.api.fluid.HTFluidType
import hiiragi283.core.api.item.toStack
import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack

class HTDyedFluidType(private val color: HTDefaultColor, properties: Properties) : HTFluidType(properties) {
    override fun getNameColor(stack: FluidStack): TextColor = color.textColor

    override fun onVaporize(
        player: Player?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(player, level, pos, stack)
        val dye: ItemStack = VanillaColoredContents.DYE[color]?.toStack(4) ?: return
        if (player != null) {
            HTItemDropHelper.giveStackTo(player, dye)
        } else {
            HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), dye)
        }
    }
}
