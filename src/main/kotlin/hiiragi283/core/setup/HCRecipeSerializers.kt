package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HTMinMaxRange
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.data.recipe.builder.HTItemToChancedRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTItemToItemRecipeBuilder
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.core.impl.recipe.HTBasicItemToChancedRecipe
import hiiragi283.core.impl.recipe.HTBasicItemToItemRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> =
        REGISTER.registerSerializer("eternal_upgrade", MapBiCodecs.unit(HCEternalSmithingRecipe))

    @JvmField
    val EXPERIENCE_STORING: SimpleCraftingRecipeSerializer<HCExperienceStoringRecipe> =
        REGISTER.registerSerializer("experience_storing", SimpleCraftingRecipeSerializer(::HCExperienceStoringRecipe))

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

    @JvmField
    val MELTING: RecipeSerializer<HCMeltingRecipe> = REGISTER.registerSerializer(
        HTConst.MELTING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCMeltingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCMeltingRecipe::result),
            HTMinMaxRange.INT_CODEC.fieldOf("heat_range").forGetter(HCMeltingRecipe::heatRange),
            HTProcessingRecipe.timeCodec(),
            ::HCMeltingRecipe,
        ),
    )
}
