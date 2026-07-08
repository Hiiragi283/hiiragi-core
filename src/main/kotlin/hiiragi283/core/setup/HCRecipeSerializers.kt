package hiiragi283.core.setup

import com.mojang.serialization.MapCodec
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCChoppingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import hiiragi283.core.common.recipe.custom.HCEternalSmithingRecipe
import hiiragi283.lib.recipe.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeSerializer

data object HCRecipeSerializers {
    @JvmField
    val ETERNAL_UPGRADE: RecipeSerializer<HCEternalSmithingRecipe> = RecipeSerializer(MapCodec.unit(HCEternalSmithingRecipe))

    //    In World    //

    @JvmField
    val CHARGING: RecipeSerializer<HCChargingRecipe> = RecipeSerializer(HCChargingRecipe.CODEC)

    @JvmField
    val CHOPPING: RecipeSerializer<HCChoppingRecipe> = RecipeSerializer(HCChoppingRecipe.CODEC)

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
