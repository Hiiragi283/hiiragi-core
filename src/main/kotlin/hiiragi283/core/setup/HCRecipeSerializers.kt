package hiiragi283.core.setup

import com.mojang.serialization.MapCodec
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCForgingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.registry.register.HTDeferredRecipeSerializerRegister
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

object HCRecipeSerializers {
    @JvmField
    val REGISTER = HTDeferredRecipeSerializerRegister(HiiragiCoreAPI.MOD_ID)

    //    Custom    //

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> =
        REGISTER.registerSerializer("eternal_upgrade", MapCodec.unit(HCEternalSmithingRecipe))

    @JvmField
    val BLUEPRINT_CLONING: SimpleCraftingRecipeSerializer<HTBlueprintCloningRecipe> =
        REGISTER.registerSerializer("blueprint_cloning", SimpleCraftingRecipeSerializer(::HTBlueprintCloningRecipe))

    @JvmField
    val EXPERIENCE_STORING: SimpleCraftingRecipeSerializer<HCExperienceStoringRecipe> =
        REGISTER.registerSerializer("experience_storing", SimpleCraftingRecipeSerializer(::HCExperienceStoringRecipe))

    //    Basic    //

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = REGISTER.registerSerializer(HTConst.CHARGING, HCChargingRecipe.CODEC)

    @JvmField
    val CRUSHING: RecipeSerializer<HCCrushingRecipe> = REGISTER.registerSerializer(HTConst.CRUSHING, HCCrushingRecipe.CODEC)

    @JvmField
    val EXPLODING: RecipeSerializer<HCExplodingRecipe> = REGISTER.registerSerializer(HTConst.EXPLODING, HCExplodingRecipe.CODEC)

    @JvmField
    val FORGING: RecipeSerializer<HCForgingRecipe> = REGISTER.registerSerializer(HTConst.FORGING, HCForgingRecipe.CODEC)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: RecipeSerializer<HCTankEmptyingRecipe> = REGISTER.registerSerializer(HTConst.EMPTYING, HCTankEmptyingRecipe.CODEC)

    @JvmField
    val FILLING: RecipeSerializer<HCTankFillingRecipe> = REGISTER.registerSerializer(HTConst.FILLING, HCTankFillingRecipe.CODEC)
}
