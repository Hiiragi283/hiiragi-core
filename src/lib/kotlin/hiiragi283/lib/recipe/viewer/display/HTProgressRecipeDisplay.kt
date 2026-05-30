package hiiragi283.lib.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.recipe.base.HTProgressData
import net.minecraft.resources.Identifier

class HTProgressRecipeDisplay(id: Identifier, contents: HTRecipeContents, val progressData: HTProgressData) : HTRecipeDisplay.Simple(id, contents) {
    companion object {
        @JvmField
        val CODEC: Codec<HTProgressRecipeDisplay> = RecordCodecBuilder.create { instance ->
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

inline fun HTProgressRecipeDisplay(key: RecipeKey, progressData: HTProgressData, builderAction: HTRecipeContents.Builder.() -> Unit): HTProgressRecipeDisplay = HTProgressRecipeDisplay(key.identifier(), HTRecipeContents.create(builderAction), progressData)
