package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * @see RecipeOutput
 */
fun interface HTRecipeExporter {
    fun accept(key: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition>)

    fun accept(key: RecipeKey, recipe: Recipe<*>) {
        accept(key, recipe, listOf())
    }

    fun accept(id: Identifier, recipe: Recipe<*>, conditions: List<ICondition> = listOf()) {
        accept(RecipeKey(id), recipe, conditions)
    }

    fun asOutput(): RecipeOutput = object : RecipeOutput {
        override fun accept(key: RecipeKey, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) {
            this@HTRecipeExporter.accept(key, recipe, conditions.toList())
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()

        override fun includeRootAdvancement() {}
    }
}
