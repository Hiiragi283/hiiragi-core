package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidWithTag
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [HTFluidIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    fun from(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    fun fromTagKey(fluid: HTFluidWithTag<*>, amount: Int): HTFluidIngredient = fromTagKey(fluid.getFluidTag(), amount)

    fun fromTagKeys(vararg fluids: HTFluidWithTag<*>, amount: Int): HTFluidIngredient =
        fromTagKeys(fluids.map(HTFluidWithTag<*>::getFluidTag), amount)

    fun water(amount: Int): HTFluidIngredient = fromTagKey(HTFluidWithTag.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromTagKey(HTFluidWithTag.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromTagKey(HTFluidWithTag.MILK, amount)
}
