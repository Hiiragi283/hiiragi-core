package hiiragi283.core.api.material

import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.Text
import hiiragi283.core.api.text.translatableText
import io.netty.buffer.ByteBuf
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @param id 素材の[ID][ResourceLocation]
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey private constructor(private val id: ResourceLocation) :
    HTIdLike,
    HTMaterialLike,
    HTHasTranslationKey,
    HTHasText,
    Comparable<HTMaterialKey> {
        companion object {
            /**
             * 指定した[id]から[HTMaterialKey]のインスタンスを返します。
             * @return キャッシュから取得した[HTMaterialKey]のインスタンス
             */
            @JvmStatic
            fun of(id: ResourceLocation): HTMaterialKey = HTMaterialKey(id)

            /**
             * [HTMaterialKey]の[BiCodec]
             */
            @JvmField
            val CODEC: BiCodec<ByteBuf, HTMaterialKey> = VanillaBiCodecs.ID.flatXmap(HTMaterialKey::of, HTMaterialKey::getId)
        }

        override fun getId(): ResourceLocation = id

        override fun asMaterialKey(): HTMaterialKey = this

        /**
         * @since 0.12.0
         */
        override val translationKey: String
            get() = getId().toLanguageKey("material")

        /**
         * @since 0.12.0
         */
        override fun getText(): Text = translatableText(translationKey)

        override fun compareTo(other: HTMaterialKey): Int = this.id.compareNamespaced(other.id)
    }
