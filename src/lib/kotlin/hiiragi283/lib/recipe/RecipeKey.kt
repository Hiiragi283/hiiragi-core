package hiiragi283.lib.recipe

import hiiragi283.lib.registry.createKey
import hiiragi283.lib.resource.toId
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe

typealias RecipeKey = ResourceKey<Recipe<*>>

fun RecipeKey(namespace: String, path: String): RecipeKey = RecipeKey(namespace.toId(path))

fun RecipeKey(namespace: String, vararg path: String): RecipeKey = RecipeKey(namespace.toId(*path))

fun RecipeKey(id: Identifier): RecipeKey = Registries.RECIPE.createKey(id)
