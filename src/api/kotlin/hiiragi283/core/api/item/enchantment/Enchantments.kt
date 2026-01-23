package hiiragi283.core.api.item.enchantment

import hiiragi283.core.api.HTBuilderMarker
import net.minecraft.world.item.enchantment.ItemEnchantments

@HTBuilderMarker
inline fun buildEnchantments(
    parent: ItemEnchantments = ItemEnchantments.EMPTY,
    builderAction: ItemEnchantments.Mutable.() -> Unit,
): ItemEnchantments = ItemEnchantments.Mutable(parent).apply(builderAction).toImmutable()
