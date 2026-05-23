package hiiragi283.core.api.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    fun <RECIPE : Recipe<*>> registerType(name: String): HTDeferredRecipeType<RECIPE> {
        val holder = HTDeferredRecipeType<RECIPE>(createId(name))
        this.register(name) { id: ResourceLocation -> RecipeType.simple<RECIPE>(id) }
        return holder
    }
}
