package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.text.HTHasText
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
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
interface HTResourceType<TYPE : Any> : HTHasText {
    fun type(): TYPE

    /**
     * [Holder]を保持する[HTResourceType]の拡張インターフェースです。
     * @param TYPE 保持する種類のクラス
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    interface Registered<TYPE : Any> :
        HTResourceType<TYPE>,
        HTKeyLike<TYPE>,
        IWithData<TYPE> {
        /**
         * @since 0.13.0
         */
        fun getHolder(): Holder<TYPE>

        override fun getResourceKey(): ResourceKey<TYPE> = getHolder().unwrapKey().orElseThrow()

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
         * このリソースの[DataComponentPatch]を返します。
         */
        fun componentsPatch(): DataComponentPatch
    }
}
