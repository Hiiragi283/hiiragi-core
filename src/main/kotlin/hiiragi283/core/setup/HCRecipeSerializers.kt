package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCMeltingRecipe
import hiiragi283.core.impl.registry.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer

data object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Basic    //

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = REGISTER.registerSerializer(
        HTConst.CHARGING,
        MapBiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCChargingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.ENERGY, HCChargingRecipe.DEFAULT_ENERGY).forGetter(HCChargingRecipe::energy),
            ::HCChargingRecipe,
        ),
    )

    @JvmField
    val CRUSHING: RecipeSerializer<HCCrushingRecipe> = REGISTER.registerSerializer(
        HTConst.CRUSHING,
        MapBiCodec.composite(
            VanillaBiCodecs.SIZED_INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCCrushingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCCrushingRecipe::result),
            HTItemResult.CODEC.optionalFieldOf(HTConst.EXTRA_RESULT).forGetter(HCCrushingRecipe::extraResult),
            HTProcessingRecipe.timeCodec(),
            ::HCCrushingRecipe,
        ),
    )

    @JvmField
    val MELTING: RecipeSerializer<HCMeltingRecipe> = REGISTER.registerSerializer(
        HTConst.MELTING,
        MapBiCodec.composite(
            VanillaBiCodecs.INGREDIENT.fieldOf(HTConst.INGREDIENT).forGetter(HCMeltingRecipe::ingredient),
            HTFluidResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCMeltingRecipe::result),
            VanillaBiCodecs.INT_BOUNDS.fieldOf("heat_range").forGetter(HCMeltingRecipe::heatRange),
            HTProcessingRecipe.timeCodec(),
            ::HCMeltingRecipe,
        ),
    )
}
