package hiiragi283.core.api.registry

import net.minecraft.core.Holder
import net.minecraft.world.item.Item

/**
 * この[アイテム][this]を[HTItemHolderLike]に変換します。
 * @param ITEM アイテムのクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun <ITEM : Item> ITEM.toHolderLike(): HTItemHolderLike<ITEM> = HTItemHolderLike { this@toHolderLike }

/**
 * この[Holder][this]を[HTItemHolderLike]に変換します。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
fun Holder<Item>.toItemLike(): HTItemHolderLike<*> = (this as? HTItemHolderLike<*>) ?: HTItemHolderLike(this@toItemLike::value)
