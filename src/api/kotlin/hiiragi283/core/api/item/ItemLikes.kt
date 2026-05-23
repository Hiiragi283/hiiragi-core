package hiiragi283.core.api.item

import java.util.function.Supplier
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

fun Supplier<out ItemLike>.toStack(count: Int = 1, patch: DataComponentPatch = DataComponentPatch.EMPTY): ItemStack = createItemStack(this.get(), count, patch)
