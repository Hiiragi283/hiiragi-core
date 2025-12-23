package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.math.toFraction
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.common.recipe.HCLightningChargingRecipe
import net.minecraft.resources.ResourceLocation
import org.apache.commons.lang3.math.Fraction

class HTChargingRecipeBuilder(private val ingredient: HTItemIngredient, private val result: HTItemResult, private val energy: Int) :
    HTRecipeBuilder<HTChargingRecipeBuilder>(HTConst.CHARGING) {
    companion object {
        @JvmStatic
        fun create(
            ingredient: HTItemIngredient,
            result: HTItemResult,
            energy: Int = HCLightningChargingRecipe.DEFAULT_ENERGY,
        ): HTChargingRecipeBuilder = HTChargingRecipeBuilder(ingredient, result, energy)
    }

    private var exp: Fraction = Fraction.ZERO

    fun setExp(exp: Float): HTChargingRecipeBuilder = setExp(exp.toFraction())

    fun setExp(exp: Fraction): HTChargingRecipeBuilder = apply {
        this.exp = maxOf(Fraction.ZERO, exp)
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HCLightningChargingRecipe = HCLightningChargingRecipe(ingredient, result, energy, exp)
}
