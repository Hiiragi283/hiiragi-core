package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.item.Item

/**
 * この[アイテム][this]を[HTItemHolderLike]に変換します。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@Suppress("DEPRECATION")
fun <ITEM : Item> ITEM.toHolderLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike<ITEM> {
    override fun getItemHolder(): Holder<Item> = asItem().builtInRegistryHolder()

    override fun asItem(): ITEM = this@toHolderLike
}
