package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier

fun RecipeBuilder.save(output: RecipeOutput, id: Identifier) {
    this.save(output, RecipeKey(id))
}
