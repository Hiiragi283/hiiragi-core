package hiiragi283.core.api.material

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toDescriptionKey
import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.text.HTHasText
import hiiragi283.core.api.text.HTHasTranslationKey
import hiiragi283.core.api.text.translatableText
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

/**
 * 素材の種類を表すクラスです。
 *
 * まさに伝統的な設計
 * @param name 素材のID
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@JvmInline
value class HTMaterialKey private constructor(val name: String) :
    HTMaterialLike,
    HTHasTranslationKey,
    HTHasText {
        companion object {
            /**
             * 指定した[name]から[HTMaterialKey]のインスタンスを返します。
             * @return キャッシュから取得した[HTMaterialKey]のインスタンス
             * @throws IllegalStateException [name]が[IDに適切な文字列でない][ResourceLocation.isValidPath]場合
             */
            @JvmStatic
            fun of(name: String): HTMaterialKey {
                check(ResourceLocation.isValidPath(name)) { "Material name $name is not valid" }
                return HTMaterialKey(name)
            }

            /**
             * [HTMaterialKey]の[BiCodec]
             */
            @JvmField
            val CODEC: BiCodec<ByteBuf, HTMaterialKey> = BiCodec.STRING.flatXmap(
                HTMaterialKey::of,
                HTMaterialKey::name,
            )
        }

        override fun asMaterialKey(): HTMaterialKey = this

        override val translationKey: String get() = HiiragiCoreAPI.id(name).toDescriptionKey("material")

        override fun getText(): Component = translatableText(translationKey)

        override fun toString(): String = name
    }
