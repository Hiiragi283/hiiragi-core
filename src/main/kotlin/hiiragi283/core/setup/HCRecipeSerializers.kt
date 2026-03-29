package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.serialization.codec.BiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.common.recipe.HCChargingRecipe
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
            VanillaBiCodecs.ITEM_STACK_TEMPLATE.fieldOf(HTConst.RESULT).forGetter(HCChargingRecipe::result),
            BiCodecs.POSITIVE_INT.optionalFieldOf(HTConst.ENERGY, HCChargingRecipe.DEFAULT_ENERGY).forGetter(HCChargingRecipe::energy),
            ::HCChargingRecipe,
        ),
    )
}
