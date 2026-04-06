package hiiragi283.core.common.registry.register

import hiiragi283.core.api.registry.HTDeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import java.util.function.Supplier

class HTDeferredRecipeTypeRegister(namespace: String) : HTDeferredRegister<RecipeType<*>>(Registries.RECIPE_TYPE, namespace) {
    fun <RECIPE : Recipe<*>> registerType(name: String): Supplier<RecipeType<RECIPE>> =
        delegate.register(name) { id: ResourceLocation -> RecipeType.simple<RECIPE>(id) }
}
