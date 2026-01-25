package hiiragi283.core.api.item.enchantment

import hiiragi283.core.api.HTBuilderMarker
import net.minecraft.world.item.enchantment.ItemEnchantments

/**
 * 新しい[ItemEnchantments]のインスタンスを作成します。
 * @param builderAction [ItemEnchantments]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
@HTBuilderMarker
inline fun buildEnchantments(
    parent: ItemEnchantments = ItemEnchantments.EMPTY,
    builderAction: ItemEnchantments.Mutable.() -> Unit,
): ItemEnchantments = ItemEnchantments.Mutable(parent).apply(builderAction).toImmutable()
