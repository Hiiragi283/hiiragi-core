package hiiragi283.core.api.resource

import net.minecraft.resources.ResourceLocation

/**
 * この[HTIdLike]から，`block/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.blockId: ResourceLocation get() = getId().withPrefix("block/")

/**
 * この[HTIdLike]から，`item/`で前置された[ID][HTIdLike.getId]を返します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
val HTIdLike.itemId: ResourceLocation get() = getId().withPrefix("item/")
