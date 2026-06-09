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

inline fun HTProgressRecipeDisplay(key: RecipeKey, progressData: HTProgressData, builderAction: HTRecipeContents.Builder.() -> Unit): HTProgressRecipeDisplay {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    return HTProgressRecipeDisplay(key.identifier(), HTRecipeContents.create(builderAction), progressData)
}
