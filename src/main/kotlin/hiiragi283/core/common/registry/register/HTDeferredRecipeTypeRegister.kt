package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredRegister
import hiiragi283.core.common.registry.HTDeferredRecipeType
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> registerType(name: String): HTDeferredRecipeType<INPUT, RECIPE> {
        val recipeType = HTDeferredRecipeType<INPUT, RECIPE>(createId(name))
        register(name) { id: ResourceLocation -> RecipeType.simple<RECIPE>(id) }
        return recipeType
    }
}
