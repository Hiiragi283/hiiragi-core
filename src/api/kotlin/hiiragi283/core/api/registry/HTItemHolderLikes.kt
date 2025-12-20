package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

fun ItemLike.toHolderLike(): HTItemHolderLike<*> = (this as? HTItemHolderLike<*>) ?: HTItemHolderLike(this@toHolderLike::asItem)

fun Holder<Item>.toItemLike(): HTItemHolderLike<*> = (this as? HTItemHolderLike<*>) ?: HTItemHolderLike(this@toItemLike::value)
