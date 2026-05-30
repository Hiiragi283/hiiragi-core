package hiiragi283.lib.item

import hiiragi283.lib.util.HTTextResult
import java.util.function.Supplier
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.level.ItemLike

fun Supplier<out ItemLike>.toTemplate(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): HTTextResult<ItemStackTemplate> = createItemTemplate(this.get(), count, patch)

fun Supplier<out ItemLike>.toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = createItemStack(this.get(), count, patch)
