package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.registry.TypedInstance
import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.HTKeyLike
import hiiragi283.core.api.text.HTHasText
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceKey
import net.neoforged.neoforge.registries.datamaps.DataMapType
import net.neoforged.neoforge.registries.datamaps.IWithData

/**
 * 種類を保持する不変なオブジェクトを表すインターフェースです。
 *
 * EMPTYなんか大っ嫌い！
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
interface HTResourceType :
    HTHasText,
    HTIdLike {
    /**
     * 値を保持する[HTResourceType]の拡張インターフェースです。
     * @param TYPE 保持する種類のクラス
     * @author Hiiragi Tsubasa
     * @since 0.4.0
     */
    interface Registered<TYPE : Any> :
        HTResourceType,
        HTKeyLike<TYPE>,
        IWithData<TYPE>,
        TypedInstance<TYPE> {
        override fun getResourceKey(): ResourceKey<TYPE> = typeHolder().getKeyOrThrow()

        override fun <T : Any> getData(type: DataMapType<TYPE, T>): T? = typeHolder().getData(type)
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
