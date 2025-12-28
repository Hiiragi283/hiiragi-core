package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.IWithData

/**
 * 種類を保持する不変なオブジェクトを表すインターフェースです。
 *
 * EMPTYなんか大っ嫌い！
 * @param TYPE 保持する種類のクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTResourceType<TYPE : Any> :
    HTHasText,
    HTIdLike {
    fun type(): TYPE

    /**
     * [Holder]を保持する[HTResourceType]の拡張インターフェースです。
     * @param TYPE 保持する種類のクラス
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    interface Registered<TYPE : Any> :
        HTResourceType<TYPE>,
        HTKeyLike.HolderDelegate<TYPE>,
        IWithData<TYPE> {
        /**
         * 指定した[種類][type]が，このスタックが保持している[種類][ImmutableStack.type]と一致するか判定します。
         */
        fun isOf(type: TYPE): Boolean = type() == type

        /**
         * 指定した[ID][key]が，このスタックが保持しているIDと一致するか判定します。
         */
        fun isOf(key: ResourceKey<TYPE>): Boolean = getHolder().`is`(key)

        /**
         * 指定した[TagKey]に，このスタックの[種類][type]が含まれているか判定します。
         */
        fun isOf(tagKey: TagKey<TYPE>): Boolean = getHolder().`is`(tagKey)

        /**
         * 指定した[Holder]が，このスタックが保持している[Holder][getHolder]と一致するか判定します。
         */
        @Suppress("DEPRECATION")
        fun isOf(holder: Holder<TYPE>): Boolean = getHolder().`is`(holder)

        /**
         * 指定した[HolderSet]に，このスタックの[Holder][getHolder]が含まれているか判定します。
         */
        fun isOf(holderSet: HolderSet<TYPE>): Boolean = holderSet.contains(getHolder())

        override fun <T : Any> getData(type: DataMapType<TYPE, T>): T? = getHolder().getData(type)
    }

    /**
     * コンポーネントを保持する[Registered]の拡張インターフェースです。
     * @param TYPE 保持する種類のクラス
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    interface DataComponent<TYPE : Any> :
        Registered<TYPE>,
        DataComponentHolder {
        /**
         * このスタックの[DataComponentPatch]を返します。
         */
        fun componentsPatch(): DataComponentPatch
    }
}
