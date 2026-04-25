package hiiragi283.core.common.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.common.recipe.HCExplodingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HCExplodingRecipeDisplay(id: ResourceLocation, contents: HTRecipeContents, val requiredPower: Fraction) :
    HTRecipeDisplay.Simple(id, contents) {
    companion object {
        @JvmField
        val CODEC: Codec<HCExplodingRecipeDisplay> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTRecipeDisplay.idCodec(),
                    contentsCodec(),
                    HTCodecs.NON_NEGATIVE_FRACTION.fieldOf("required_power").forGetter(HCExplodingRecipeDisplay::requiredPower),
                ).apply(instance, ::HCExplodingRecipeDisplay)
        }

        @JvmStatic
        fun fromHolder(holder: HTRecipeHolder<HCExplodingRecipe>): HCExplodingRecipeDisplay {
            val (id: ResourceLocation, recipe: HCExplodingRecipe) = holder
            return HCExplodingRecipeDisplay(
                id,
                HTRecipeContents.create {
                    addInput(recipe.ingredient)
                    addOutput(recipe.result)
                },
                recipe.requiredPower,
            )
        }
    }
}
