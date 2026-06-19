@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.serialization.codec.HTCodecs
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier

/**
 * レシピビューワーで使用されるインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTRecipeDisplay : HTIdLike {
    companion object {
        @JvmStatic
        fun <T : HTRecipeDisplay> idCodec(): RecordCodecBuilder<T, Identifier> = Identifier.CODEC.fieldOf(HTConstants.ID).forGetter(HTRecipeDisplay::getId)
    }

    /**
     * レシピビューワーで表示するかどうか判定します。
     */
    fun isHandled(): Boolean = true

    /**
     * [HTRecipeContents]を提供する[HTRecipeDisplay]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    open class Simple(private val id: Identifier, val contents: HTRecipeContents) : HTRecipeDisplay {
        companion object {
            @JvmField
            val CODEC: Codec<Simple> = HTCodecs.record { it.group(idCodec(), contentsCodec()).apply(it, ::Simple) }

            @JvmStatic
            fun <T : Simple> contentsCodec(): RecordCodecBuilder<T, HTRecipeContents> = HTRecipeContents.CODEC.forGetter(Simple::contents)
        }

        final override fun getId(): Identifier = id
    }
}

//    Extensions    //

/**
 * 新しい[HTRecipeDisplay.Simple]のインスタンスを作成します。
 * @param key ディスプレイのキー
 * @param builderAction [HTRecipeContents.Builder]を初期化するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("FunctionName")
inline fun HTRecipeDisplay(key: RecipeKey, builderAction: HTRecipeContents.Builder.() -> Unit): HTRecipeDisplay.Simple {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return HTRecipeDisplay.Simple(key.identifier(), HTRecipeContents.create(builderAction))
}
