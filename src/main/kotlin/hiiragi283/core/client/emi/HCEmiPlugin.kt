package hiiragi283.core.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipe
import hiiragi283.core.api.function.partially1
import hiiragi283.core.client.emi.category.HCEmiRecipeCategories
import hiiragi283.core.client.emi.category.HCEmiRecipeCategory
import hiiragi283.core.client.emi.recipe.HTSingleItemEmiRecipe
import hiiragi283.core.common.registry.HTDeferredRecipeType
import hiiragi283.core.setup.HCRecipeTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput

@EmiEntrypoint
class HCEmiPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            HCEmiRecipeCategories.DRYING,
            HCEmiRecipeCategories.FROSTING,
        ).forEach { category: HCEmiRecipeCategory ->
            registry.addCategory(category)
            registry.addWorkstation(category, category.icon)
        }
        // Recipes
        addRecipes(registry, HCRecipeTypes.DRYING, ::HTSingleItemEmiRecipe.partially1(HCEmiRecipeCategories.DRYING))
        addRecipes(registry, HCRecipeTypes.FROSTING, ::HTSingleItemEmiRecipe.partially1(HCEmiRecipeCategories.FROSTING))
    }

    private fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>, EMI_RECIPE : EmiRecipe> addRecipes(
        registry: EmiRegistry,
        recipeType: HTDeferredRecipeType<INPUT, RECIPE>,
        factory: (RECIPE, ResourceLocation) -> EMI_RECIPE?,
    ) {
        registry.recipeManager
            .getAllRecipesFor(recipeType.get())
            .mapNotNull { holder: RecipeHolder<RECIPE> -> factory(holder.value, holder.id) }
            .forEach(registry::addRecipe)
    }
}
