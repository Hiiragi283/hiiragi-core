package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.VanillaFluidContents
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.crafting.FluidIngredient

/**
 * [HTFluidIngredient]を返す[HTIngredientCreator]の拡張インターフェース
 * @see mekanism.api.recipes.ingredients.creator.IFluidStackIngredientCreator
 */
interface HTFluidIngredientCreator : HTIngredientCreator<Fluid, HTFluidIngredient> {
    fun from(ingredient: FluidIngredient, amount: Int): HTFluidIngredient = HTFluidIngredient(ingredient, amount)

    fun fromTagKey(content: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient = fromTagKey(content.fluidTag, amount)

    fun fromTagKeys(vararg contents: HTFluidContent<*, *, *>, amount: Int): HTFluidIngredient =
        fromTagKeys(contents.map(HTFluidContent<*, *, *>::fluidTag), amount)

    fun water(amount: Int): HTFluidIngredient = fromTagKey(VanillaFluidContents.WATER, amount)

    fun lava(amount: Int): HTFluidIngredient = fromTagKey(VanillaFluidContents.LAVA, amount)

    fun milk(amount: Int): HTFluidIngredient = fromTagKey(VanillaFluidContents.MILK, amount)
}
