package hiiragi283.core.api.recipe

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

operator fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.component1(): ResourceLocation = this.id()

operator fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.component2(): RECIPE = this.value()

fun <RECIPE : Recipe<*>> RecipeHolder<RECIPE>.toPair(): Pair<ResourceLocation, RECIPE> = this.id() to this.value()

inline fun <RECIPE1 : Recipe<*>, RECIPE2 : Recipe<*>> RecipeHolder<RECIPE1>.map(transform: (RECIPE1) -> RECIPE2): RecipeHolder<RECIPE2> =
    RecipeHolder(this.id(), transform(this.value()))
