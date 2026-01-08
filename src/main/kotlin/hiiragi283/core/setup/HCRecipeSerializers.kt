package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.HTRecipeBiCodecs
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.core.common.crafting.HTClearComponentRecipe
import hiiragi283.core.common.crafting.HTEternalUpgradeRecipe
import hiiragi283.core.common.recipe.HCAnvilCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import hiiragi283.core.common.recipe.HCSingleItemRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val CLEAR_COMPONENT: RecipeSerializer<HTClearComponentRecipe> =
        REGISTER.registerSerializer("clear_component", HTClearComponentRecipe.CODEC)

    @JvmField
    val ETERNAL_UPGRADE: SimpleCraftingRecipeSerializer<HTEternalUpgradeRecipe> =
        REGISTER.registerSerializer("eternal_upgrade", SimpleCraftingRecipeSerializer(::HTEternalUpgradeRecipe))

    //    Misc    //

    @JvmField
    val CHARGING: RecipeSerializer<HCLightningChargingRecipe> = REGISTER.registerSerializer(
        HTConst.CHARGING,
        MapBiCodec.composite(
            HTItemIngredient.CODEC.fieldOf(HTConst.INGREDIENT).forGetter(HCLightningChargingRecipe::ingredient),
            HTItemResult.CODEC.fieldOf(HTConst.RESULT).forGetter(HCLightningChargingRecipe::result),
            HTRecipeBiCodecs.ENERGY.forGetter(HCLightningChargingRecipe::energy),
            HTRecipeBiCodecs.EXP.forGetter(HCLightningChargingRecipe::exp),
            ::HCLightningChargingRecipe,
        ),
    )

    @JvmField
    val ANVIL_CRUSHING: RecipeSerializer<HCAnvilCrushingRecipe> = REGISTER.registerSerializer(
        HTConst.ANVIL_CRUSHING,
        HCSingleItemRecipe.codec(::HCAnvilCrushingRecipe),
    )

    @JvmField
    val EXPLODING: RecipeSerializer<HCExplodingRecipe> = REGISTER.registerSerializer(
        HTConst.EXPLODING,
        HCSingleItemRecipe.codec(::HCExplodingRecipe),
    )
}
