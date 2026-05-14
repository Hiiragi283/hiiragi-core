package hiiragi283.lib.registry

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType

class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    fun <RECIPE : Recipe<*>> registerType(name: String): HTDeferredRecipeType<RECIPE> {
        val holder = HTDeferredRecipeType<RECIPE>(createId(name))
        this.register(name) { id: Identifier -> RecipeType.simple<RECIPE>(id) }
        return holder
    }
}
