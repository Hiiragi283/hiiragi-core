package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HTClearComponentRecipe
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.base.HTBasicItemToChancedRecipe
import hiiragi283.core.common.recipe.base.HTBasicItemToItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        REGISTER.registerSerializer("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> =
        REGISTER.registerSerializer("eternal_upgrade", MapBiCodecs.unit(HCEternalSmithingRecipe))

    //    Basic    //

    @JvmStatic
    private fun <R : HTBasicItemToItemRecipe> itemToItem(
        factory: HTItemToItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToItemRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToItemRecipe::result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    private fun <R : HTBasicItemToChancedRecipe> itemChanced(
        factory: HTItemToChancedRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HTBasicItemToChancedRecipe::ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HTBasicItemToChancedRecipe::result),
        HTItemResult.CHANCED_CODEC.optionalFieldOf(HTConst.EXTRA_RESULT).forGetter(HTBasicItemToChancedRecipe::extraResult),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = REGISTER.registerSerializer(HTConst.CHARGING, itemToItem(::HCChargingRecipe))

    @JvmField
    val CRUSHING: RecipeSerializer<HCCrushingRecipe> = REGISTER.registerSerializer(HTConst.CRUSHING, itemChanced(::HCCrushingRecipe))

    @JvmField
    val EXPLODING: RecipeSerializer<HCExplodingRecipe> = REGISTER.registerSerializer(
        HTConst.EXPLODING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCExplodingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCExplodingRecipe::result),
            BiCodecs.NON_NEGATIVE_FRACTION.optionalFieldOf("min_power", fraction(4)).forGetter(HCExplodingRecipe::minPower),
            ::HCExplodingRecipe,
        ),
    )
}
