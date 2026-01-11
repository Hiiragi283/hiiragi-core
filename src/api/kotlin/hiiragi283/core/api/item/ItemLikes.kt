package hiiragi283.core.api.item

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

@Suppress("DEPRECATION")
fun ItemLike.builtInRegistryHolder(): Holder.Reference<Item> = this.asItem().builtInRegistryHolder()
