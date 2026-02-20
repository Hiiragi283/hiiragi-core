package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.fraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.MapBiCodecs
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HTClearComponentRecipe
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer
import java.util.Optional

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

    //    Misc    //

    @JvmField
    val CHARGING: RecipeSerializer<HCLightningChargingRecipe> = REGISTER.registerSerializer(
        HTConst.CHARGING,
        MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCSingleItemRecipe<*>::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCSingleItemRecipe<*>::result),
            ::HCLightningChargingRecipe,
        ),
    )

    @JvmField
    val ANVIL_CRUSHING: RecipeSerializer<HCAnvilCrushingRecipe> = REGISTER.registerSerializer(
        HTConst.ANVIL_CRUSHING,
        MapBiCodec.composite(
            HTItemIngredient.UNSIZED_CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCAnvilCrushingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCAnvilCrushingRecipe::result),
            HTItemResult.CHANCED_CODEC
                .optionalFieldOf(HTConst.EXTRA_RESULT)
                .forGetter { Optional.ofNullable(it.extraResult) },
            ::HCAnvilCrushingRecipe,
        ),
    )

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
