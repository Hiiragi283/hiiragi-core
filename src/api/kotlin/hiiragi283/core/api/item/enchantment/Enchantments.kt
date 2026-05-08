package hiiragi283.core.api.item.enchantment

import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance
import net.minecraft.world.item.enchantment.ItemEnchantments

/**
 * 新しい[ItemEnchantments]のインスタンスを作成します。
 * @param builderAction [ItemEnchantments]の値を指定するブロック
 * @author Hiiragi Tsubasa
 * @since 0.8.0
 */
inline fun buildEnchantments(
    parent: ItemEnchantments = ItemEnchantments.EMPTY,
    builderAction: ItemEnchantments.Mutable.() -> Unit,
): ItemEnchantments = ItemEnchantments.Mutable(parent).apply(builderAction).toImmutable()

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun List<EnchantmentInstance>.toMap(): Map<Holder<Enchantment>, Int> = this.associate { instance: EnchantmentInstance ->
    instance.enchantment to instance.level
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun ItemEnchantments.toInstances(): List<EnchantmentInstance> = this.toMap().toInstances()

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun Map<Holder<Enchantment>, Int>.toInstances(): List<EnchantmentInstance> = this.map { (holder: Holder<Enchantment>, level: Int) ->
    EnchantmentInstance(holder, level)
}

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun Map<Holder<Enchantment>, Int>.toItem(parent: ItemEnchantments = ItemEnchantments.EMPTY): ItemEnchantments = buildEnchantments(parent) { this@toItem.forEach(this::set) }

/**
 * @author Hiiragi Tsubasa
 * @since 0.10.0
 */
fun ItemEnchantments.toMap(): Map<Holder<Enchantment>, Int> = object : AbstractMap<Holder<Enchantment>, Int>() {
    override val entries: Set<Map.Entry<Holder<Enchantment>, Int>>
        get() = this@toMap.entrySet()
}
