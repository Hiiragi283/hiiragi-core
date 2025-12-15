package hiiragi283.core.api.registry

import hiiragi283.core.api.item.builtInRegistryHolder
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike

fun ItemLike.toHolderLike(): HTItemHolderLike<*> =
    (this as? HTItemHolderLike<*>) ?: HTItemHolderLike.HolderDelegate { this@toHolderLike.builtInRegistryHolder() }

fun Holder<Item>.toItemLike(): HTItemHolderLike<*> =
    (this as? HTItemHolderLike<*>) ?: HTItemHolderLike.HolderDelegate { this@toItemLike }
