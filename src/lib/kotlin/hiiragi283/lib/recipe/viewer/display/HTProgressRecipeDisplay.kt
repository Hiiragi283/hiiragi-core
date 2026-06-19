@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.serialization.codec.HTCodecs
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier

/**
 * [HTProgressData]を提供する[HTRecipeDisplay.Simple]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTProgressRecipeDisplay(id: Identifier, contents: HTRecipeContents, val progressData: HTProgressData) : HTRecipeDisplay.Simple(id, contents) {
    companion object {
        @JvmField
        val CODEC: Codec<HTProgressRecipeDisplay> = HTCodecs.record { instance ->
            instance
                .group(
                    HTRecipeDisplay.idCodec(),
                    contentsCodec(),
                    HTProgressData.CODEC.forGetter(HTProgressRecipeDisplay::progressData),
                ).apply(instance, ::HTProgressRecipeDisplay)
        }
    }
}

//    Extensions    //

/**
 * 新しい[HTProgressRecipeDisplay]のインスタンスを作成します。
 * @param key ディスプレイのキー
 * @param progressData 表示する時間またはエネルギーの値
 * @param builderAction [HTRecipeContents.Builder]を初期化するブロック
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
inline fun HTProgressRecipeDisplay(key: RecipeKey, progressData: HTProgressData, builderAction: HTRecipeContents.Builder.() -> Unit): HTProgressRecipeDisplay {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return HTProgressRecipeDisplay(key.identifier(), HTRecipeContents.create(builderAction), progressData)
}
