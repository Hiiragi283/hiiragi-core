package hiiragi283.core.api.recipe

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

typealias RecipeKey = ResourceKey<Recipe<*>>

data class FakeRecipeHolder<RECIPE : Any>(val key: RecipeKey, val recipe: RECIPE)

fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.toFake(): FakeRecipeHolder<RECIPE> = FakeRecipeHolder(this.id(), this.value())

inline fun <RECIPE : Recipe<*>, T : Any> RecipeHolder<RECIPE>.toFake(transform: (RECIPE) -> T): FakeRecipeHolder<T> =
    FakeRecipeHolder(this.id(), transform(this.value()))

fun <RECIPE : Recipe<*>> FakeRecipeHolder<RECIPE>.toVanilla(): RecipeHolder<RECIPE> = RecipeHolder(this.key, this.recipe)
