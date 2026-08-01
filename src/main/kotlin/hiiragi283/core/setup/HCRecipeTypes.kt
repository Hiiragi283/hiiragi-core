package hiiragi283.core.setup

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.recipe.HTRecipeType
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.common.recipe.HCChargingRecipe
import hiiragi283.core.common.recipe.HCCrushingRecipe
import hiiragi283.core.common.recipe.HCExplodingRecipe
import hiiragi283.core.common.recipe.HCTankEmptyingRecipe
import hiiragi283.core.common.recipe.HCTankFillingRecipe
import net.minecraft.world.item.crafting.Recipe

data object HCRecipeTypes {
    @JvmStatic
    val allTypes: Set<HTRecipeType<*>> field: MutableSet<HTRecipeType<*>> = mutableSetOf()

    @JvmStatic
    private fun <T : Recipe<*>> create(name: String): HTRecipeType<T> = HTRecipeType<T>(HiiragiCoreAPI.id(name)).also(allTypes::add)

    //    Basic    //

    @JvmField
    val BREWING: HTRecipeType<HCBrewingRecipe> = create(HTConst.BREWING)

    @JvmField
    val CHARGING: HTRecipeType<HCChargingRecipe> = create(HTConst.CHARGING)

    @JvmField
    val CRUSHING: HTRecipeType<HCCrushingRecipe> = create(HTConst.CRUSHING)

    @JvmField
    val EXPLODING: HTRecipeType<HCExplodingRecipe> = create(HTConst.EXPLODING)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeType<HCTankEmptyingRecipe> = create(HTConst.EMPTYING)

    @JvmField
    val FILLING: HTRecipeType<HCTankFillingRecipe> = create(HTConst.FILLING)
}
