package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.RecipeBookCategory

class HTDeferredRecipeBookCategoryRegister(namespace: String) :
    HTDeferredRegister<RecipeBookCategory>(Registries.RECIPE_BOOK_CATEGORY, namespace) {
    fun register(name: String): RecipeBookCategory {
        val category = RecipeBookCategory()
        delegate.register(name) { _ -> category }
        return category
    }
}
