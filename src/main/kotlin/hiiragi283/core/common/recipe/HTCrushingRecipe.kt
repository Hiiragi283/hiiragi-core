package hiiragi283.core.common.recipe

import hiiragi283.core.setup.HCRecipeSerializers
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import org.apache.commons.lang3.math.Fraction

class HTCrushingRecipe(
    group: String,
    ingredient: Ingredient,
    result: ItemStack,
    time: Int,
    exp: Fraction,
) : HTSingleItemRecipe(group, ingredient, result, time, exp) {
    override fun getSerializer(): RecipeSerializer<*> = HCRecipeSerializers.CRUSHING

    override fun getType(): RecipeType<*> = HCRecipeTypes.CRUSHING.get()
}
