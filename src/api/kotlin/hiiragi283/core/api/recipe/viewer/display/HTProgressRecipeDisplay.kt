package hiiragi283.core.api.recipe.viewer.display

import com.mojang.serialization.Codec
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.resources.ResourceLocation

class HTProgressRecipeDisplay(id: ResourceLocation, contents: HTRecipeContents, val progressData: HTProgressData) : HTRecipeDisplay.Simple(id, contents) {
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
