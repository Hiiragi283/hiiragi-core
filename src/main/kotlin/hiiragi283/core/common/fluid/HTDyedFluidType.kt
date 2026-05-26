package hiiragi283.core.common.fluid

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.VanillaColoredContents
import hiiragi283.lib.fluid.HTFluidType
import hiiragi283.lib.world.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TextColor
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack

class HTDyedFluidType(private val color: HTDefaultColor, properties: Properties) : HTFluidType(properties) {
    override fun getNameColor(stack: FluidStack): TextColor = color.textColor

    override fun onVaporize(entity: LivingEntity?, level: Level, pos: BlockPos, stack: FluidStack) {
        super.onVaporize(entity, level, pos, stack)
        val dye: ItemStack = VanillaColoredContents.DYE[color]?.toStack(4) ?: return
        if (entity != null) {
            HTItemDropHelper.giveOrDropStack(entity, dye)
        } else {
            HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), dye)
        }
    }
}
