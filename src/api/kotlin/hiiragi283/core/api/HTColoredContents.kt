package hiiragi283.core.api

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.world.item.DyeColor

/**
 * 色のバリエーションを持つ要素をまとめるインターフェースです。
 * @param T 要素のクラス
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
interface HTColoredContents<T : HTHolderLike<*, *>> : Iterable<Pair<HTDefaultColor, T>> {
    /**
     * 指定した[色][color]に対応する要素を取得します。
     * @return 対応する要素がない場合は`null`
     */
    operator fun get(color: HTDefaultColor): T?

    /**
     * 指定した[色][color]に対応する要素を取得します。
     * @return 対応する要素がない場合は`null`
     */
    operator fun get(color: DyeColor): T?

    /**
     * [DyeColor]に基づいた[HTColoredContents]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.15.0
     */
    fun interface Simple<T : HTHolderLike<*, *>> : HTColoredContents<T> {
        override fun get(color: HTDefaultColor): T? = color.dyeColor.let(::get)

        override fun iterator(): Iterator<Pair<HTDefaultColor, T>> =
            HTDefaultColor.entries.mapNotNull { color: HTDefaultColor -> get(color)?.let { color to it } }.iterator()
    }
}
