package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.core.common.data.recipe.builder.HTDoubleMultiOutputRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleItemRecipeBuilder
import hiiragi283.core.common.data.recipe.builder.HTSingleMultiOutputRecipeBuilder
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleItemRecipe
import hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import java.util.Optional

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> =
        REGISTER.registerSerializer("eternal_upgrade", MapBiCodecs.unit(HCEternalSmithingRecipe))

    @JvmField
    val BLUEPRINT_CLONING: SimpleCraftingRecipeSerializer<HTBlueprintCloningRecipe> = REGISTER.registerSerializer(
        "blueprint_cloning",
        SimpleCraftingRecipeSerializer(::HTBlueprintCloningRecipe),
    )

    @JvmField
    val EXPERIENCE_STORING: SimpleCraftingRecipeSerializer<HCExperienceStoringRecipe> =
        REGISTER.registerSerializer("experience_storing", SimpleCraftingRecipeSerializer(::HCExperienceStoringRecipe))

    //    Basic    //

    @JvmStatic
    fun <R : HTProcessingRecipe.Serializable<*>> singleItem(
        ingredient: (R) -> HTItemIngredient,
        result: (R) -> HTItemResult,
        factory: HTSingleItemRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(ingredient),
        HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(result),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    fun <R : HTProcessingRecipe.Serializable<*>> singleItemToMulti(
        outputRange: IntRange,
        ingredient: (R) -> HTItemIngredient,
        results: (R) -> List<HTItemResult>,
        factory: HTSingleMultiOutputRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(ingredient),
        HTItemResult.CODEC
            .listOrElement(outputRange)
            .fieldOf(HTConst.RESULTS)
            .forGetter(results),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    fun <R : HTProcessingRecipe.Serializable<*>> doubleItemToMulti(
        outputRange: IntRange,
        base: (R) -> HTItemIngredient,
        addition: (R) -> Optional<HTItemIngredient>,
        results: (R) -> List<HTItemResult>,
        factory: HTDoubleMultiOutputRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = MapBiCodec.composite(
        HTItemIngredient.CODEC.fieldOf("base").forGetter(base),
        HTItemIngredient.CODEC.optionalFieldOf("addition").forGetter(addition),
        HTItemResult.CODEC
            .listOrElement(outputRange)
            .fieldOf(HTConst.RESULTS)
            .forGetter(results),
        HTProcessingRecipe.timeCodec(),
        factory::create,
    )

    @JvmStatic
    fun <R : HTBasicSingleItemRecipe> singleItem(factory: HTSingleItemRecipeBuilder.Factory<R>): MapBiCodec<RegistryFriendlyByteBuf, R> =
        singleItem(HTBasicSingleItemRecipe::ingredient, HTBasicSingleItemRecipe::result, factory)

    @JvmStatic
    fun <R : HTBasicSingleMultiOutputRecipe> singleItemToMulti(
        outputRange: IntRange,
        factory: HTSingleMultiOutputRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = singleItemToMulti(
        outputRange,
        HTBasicSingleMultiOutputRecipe::ingredient,
        HTBasicSingleMultiOutputRecipe::results,
        factory,
    )

    @JvmStatic
    fun <R : HTBasicDoubleMultiOutputRecipe> doubleItemToMulti(
        outputRange: IntRange,
        factory: HTDoubleMultiOutputRecipeBuilder.Factory<R>,
    ): MapBiCodec<RegistryFriendlyByteBuf, R> = doubleItemToMulti(
        outputRange,
        HTBasicDoubleMultiOutputRecipe::base,
        HTBasicDoubleMultiOutputRecipe::addition,
        HTBasicDoubleMultiOutputRecipe::results,
        factory,
    )

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = REGISTER.registerSerializer(
        HTConst.CHARGING,
        MapBiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
            BiCodecs.NON_NEGATIVE_INT
                .optionalFieldOf("energy", HCChargingRecipe.DEFAULT_ENERGY)
                .forGetter(HCChargingRecipe::requiredEnergy),
            ::HCChargingRecipe,
        ),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HCCrushingRecipe> =
        REGISTER.registerSerializer(HTConst.CRUSHING, singleItemToMulti(HCCrushingRecipe.OUTPUT_RANGE, ::HCCrushingRecipe))

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
    val FORGING: RecipeSerializer<HCForgingRecipe> =
        REGISTER.registerSerializer(HTConst.FORGING, doubleItemToMulti(HCForgingRecipe.OUTPUT_RANGE, ::HCForgingRecipe))

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: RecipeSerializer<HCTankEmptyingRecipe> = REGISTER.registerSerializer(
        HTConst.EMPTYING,
        MapBiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCTankEmptyingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.FLUID_RESULT).forGetter(HCTankEmptyingRecipe::fluidResult),
            HTItemResult.CODEC.optionalFieldOf(HTConst.ITEM_RESULT).forGetter(HCTankEmptyingRecipe::itemResult),
            ::HCTankEmptyingRecipe,
        ),
    )

    @JvmField
    val FILLING: RecipeSerializer<HCTankFillingRecipe> = REGISTER.registerSerializer(
        HTConst.FILLING,
        MapBiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.ITEM_INGREDIENT).forGetter(HCTankFillingRecipe::itemIngredient),
            HTFluidIngredient.CODEC.fieldOf(HTConst.FLUID_INGREDIENT).forGetter(HCTankFillingRecipe::fluidIngredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCTankFillingRecipe::result),
            ::HCTankFillingRecipe,
        ),
    )
}
