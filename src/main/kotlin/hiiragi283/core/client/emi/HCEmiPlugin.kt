package hiiragi283.core.client.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiRegistry
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.function.partially1
import hiiragi283.core.api.integration.emi.HTEmiPlugin
import hiiragi283.core.client.emi.recipe.HTChargingEmiRecipe
import hiiragi283.core.client.emi.recipe.HTSingleItemEmiRecipe
import hiiragi283.core.setup.HCRecipeTypes

@EmiEntrypoint
class HCEmiPlugin : HTEmiPlugin(HiiragiCoreAPI.MOD_ID) {
    override fun register(registry: EmiRegistry) {
        // Category
        listOf(
            HCEmiRecipeCategories.CHARGING,
            HCEmiRecipeCategories.CRUSHING,
            HCEmiRecipeCategories.DRYING,
            HCEmiRecipeCategories.EXPLODING,
        ).forEach(::addCategory.partially1(registry))

        // Recipes
        addRegistryRecipes(registry, HCRecipeTypes.CHARGING, ::HTChargingEmiRecipe)
        addRegistryRecipes(registry, HCRecipeTypes.CRUSHING, HTSingleItemEmiRecipe.Companion::crushing)
        addRegistryRecipes(registry, HCRecipeTypes.DRYING, HTSingleItemEmiRecipe.Companion::drying)
        addRegistryRecipes(registry, HCRecipeTypes.EXPLODING, HTSingleItemEmiRecipe.Companion::exploding)
    }
}
