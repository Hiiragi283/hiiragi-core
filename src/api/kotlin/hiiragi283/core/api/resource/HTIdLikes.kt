package hiiragi283.core.api.resource

import net.minecraft.resources.ResourceLocation

/**
 * `block/`で前置された[HTIdLike.getId]
 */
val HTIdLike.blockId: ResourceLocation get() = getIdWithPrefix("block/")

/**
 * `item/`で前置された[HTIdLike.getId]
 */
val HTIdLike.itemId: ResourceLocation get() = getIdWithPrefix("item/")
