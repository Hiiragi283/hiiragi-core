package hiiragi283.core.api.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.serialization.codec.HTCodecs
import net.minecraft.resources.ResourceLocation

class HTProcessingRecipeDisplay(id: ResourceLocation, contents: HTRecipeContents, val time: Int) : HTRecipeDisplay.Simple(id, contents) {
    companion object {
        @JvmField
        val CODEC: Codec<HTProcessingRecipeDisplay> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTRecipeDisplay.idCodec(),
                    contentsCodec(),
                    HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.TIME).forGetter(HTProcessingRecipeDisplay::time),
                ).apply(instance, ::HTProcessingRecipeDisplay)
        }
    }
}
