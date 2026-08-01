package hiiragi283.core.setup

import hiiragi283.core.api.recipe.RecipeSerializer
import hiiragi283.core.common.crafting.HCEternalSmithingRecipe
import hiiragi283.core.common.crafting.HCExperienceStoringRecipe
import hiiragi283.core.common.crafting.HTBlueprintCloningRecipe
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer

data object HCRecipeSerializers {
    //    Custom    //

    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> = RecipeSerializer(HCEternalSmithingRecipe)

    @JvmField
    val BLUEPRINT_CLONING: SimpleCraftingRecipeSerializer<HTBlueprintCloningRecipe> = SimpleCraftingRecipeSerializer(::HTBlueprintCloningRecipe)

    @JvmField
    val EXPERIENCE_STORING: SimpleCraftingRecipeSerializer<HCExperienceStoringRecipe> = SimpleCraftingRecipeSerializer(::HCExperienceStoringRecipe)

    //    Basic    //

    @JvmField
    val BREWING: RecipeSerializer<HCBrewingRecipe> = RecipeSerializer(HCBrewingRecipe.CODEC)

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = RecipeSerializer(HCChargingRecipe.CODEC)

    @JvmField
    val CRUSHING: RecipeSerializer<HCCrushingRecipe> = RecipeSerializer(HCCrushingRecipe.CODEC)

    @JvmField
    val EXPLODING: RecipeSerializer<HCExplodingRecipe> = RecipeSerializer(HCExplodingRecipe.CODEC)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: RecipeSerializer<HCTankEmptyingRecipe> = RecipeSerializer(HCTankEmptyingRecipe.CODEC)

    @JvmField
    val FILLING: RecipeSerializer<HCTankFillingRecipe> = RecipeSerializer(HCTankFillingRecipe.CODEC)
}
