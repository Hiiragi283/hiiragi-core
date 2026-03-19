package hiiragi283.core.common.registry

import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> registerType(name: String): HTDeferredRecipeType<INPUT, RECIPE> {
        val recipeType = HTDeferredRecipeType<INPUT, RECIPE>(createId(name))
        delegate.register(name) { id: Identifier -> RecipeType.simple<RECIPE>(id) }
        return recipeType
    }
}
