package hiiragi283.core.common.recipe.viewer.display

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.HTRecipeHolder
import hiiragi283.core.api.recipe.viewer.display.HTRecipeContents
import hiiragi283.core.api.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.core.api.serialization.codec.HTCodecs
import hiiragi283.core.common.recipe.HCChargingRecipe
import net.minecraft.resources.ResourceLocation

class HCChargingRecipeDisplay(id: ResourceLocation, contents: HTRecipeContents, val requiredEnergy: Int) :
    HTRecipeDisplay.Simple(id, contents) {
    companion object {
        @JvmField
        val CODEC: Codec<HCChargingRecipeDisplay> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    HTRecipeDisplay.idCodec(),
                    contentsCodec(),
                    HTCodecs.NON_NEGATIVE_INT.fieldOf(HTConst.ENERGY).forGetter(HCChargingRecipeDisplay::requiredEnergy),
                ).apply(instance, ::HCChargingRecipeDisplay)
        }

        @JvmStatic
        fun frolHolder(holder: HTRecipeHolder<HCChargingRecipe>): HCChargingRecipeDisplay {
            val (id: ResourceLocation, recipe: HCChargingRecipe) = holder
            return HCChargingRecipeDisplay(
                id,
                HTRecipeContents.create {
                    addInput(recipe.ingredient)
                    addOutput(recipe.result)
                },
                recipe.requiredEnergy,
            )
        }
    }
}
