package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTRecipeBiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.common.recipe.HTChargingRecipe
import hiiragi283.core.common.recipe.HTCrushingRecipe
import hiiragi283.core.common.recipe.HTDryingRecipe
import hiiragi283.core.common.recipe.HTExplodingRecipe
import hiiragi283.core.common.recipe.HTSingleItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    @JvmField
    val CHARGING: RecipeSerializer<HTChargingRecipe> = REGISTER.registerSerializer(
        HTConst.CHARGING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTChargingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTChargingRecipe::result),
            HTRecipeBiCodecs.ENERGY.forGetter(HTChargingRecipe::energy),
            HTRecipeBiCodecs.EXP.forGetter(HTChargingRecipe::exp),
            ::HTChargingRecipe,
        ),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HTCrushingRecipe> = REGISTER.registerSerializer(
        HTConst.CRUSHING,
        HTSingleItemRecipe.codec(::HTCrushingRecipe),
    )

    @JvmField
    val DRYING: RecipeSerializer<HTDryingRecipe> = REGISTER.registerSerializer(
        HTConst.DRYING,
        HTSingleItemRecipe.codec(::HTDryingRecipe),
    )

    @JvmField
    val EXPLODING: RecipeSerializer<HTExplodingRecipe> = REGISTER.registerSerializer(
        HTConst.EXPLODING,
        HTSingleItemRecipe.codec(::HTExplodingRecipe),
    )
}
