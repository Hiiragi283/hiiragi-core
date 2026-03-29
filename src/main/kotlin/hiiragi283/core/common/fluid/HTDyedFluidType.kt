package hiiragi283.core.common.fluid

import hiiragi283.core.util.HTItemDropHelper
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredItem

class HTDyedFluidType(private val color: DyeColor, properties: Properties) : FluidType(properties) {
    override fun isVaporizedOnPlacement(level: Level, pos: BlockPos, stack: FluidStack): Boolean = true

    override fun onVaporize(
        entity: LivingEntity?,
        level: Level,
        pos: BlockPos,
        stack: FluidStack,
    ) {
        super.onVaporize(entity, level, pos, stack)
        val dye: ItemStack =
            DeferredItem.createItem<Item>(Identifier.withDefaultNamespace("${color.serializedName}_dye")).toStack(4)
        if (entity != null) {
            HTItemDropHelper.giveOrDropStack(entity, dye)
        } else {
            HTItemDropHelper.dropStackAt(level, Vec3.atCenterOf(pos), dye)
        }
    }
}
