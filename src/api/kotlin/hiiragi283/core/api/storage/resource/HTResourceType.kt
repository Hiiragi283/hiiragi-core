package hiiragi283.core.api.storage.resource

import hiiragi283.core.api.registry.getKeyOrThrow
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.text.HTHasText
import java.util.stream.Stream
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
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
        IWithData<TYPE> {
        fun typeHolder(): Holder<TYPE>

        override fun getId(): ResourceLocation = typeHolder().getKeyOrThrow().location()

        fun tags(): Stream<TagKey<TYPE>> = typeHolder().tags()

        fun isOf(tagKey: TagKey<TYPE>): Boolean = typeHolder().`is`(tagKey)

        fun isOf(holderSet: HolderSet<TYPE>): Boolean = typeHolder() in holderSet

        fun isOf(other: TYPE): Boolean = typeHolder().value() == other

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
