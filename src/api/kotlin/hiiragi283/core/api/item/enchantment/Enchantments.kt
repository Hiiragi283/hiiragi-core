package hiiragi283.core.api.item.enchantment

import net.minecraft.world.item.enchantment.ItemEnchantments

inline fun buildEnchantments(
    parent: ItemEnchantments = ItemEnchantments.EMPTY,
    builderAction: ItemEnchantments.Mutable.() -> Unit,
): ItemEnchantments = ItemEnchantments.Mutable(parent).apply(builderAction).toImmutable()
