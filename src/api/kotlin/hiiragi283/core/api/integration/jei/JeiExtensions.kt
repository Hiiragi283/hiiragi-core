package hiiragi283.core.api.integration.jei

import mezz.jei.api.recipe.RecipeType
import net.minecraft.world.item.crafting.RecipeHolder

typealias JeiRecipeType<T> = RecipeType<T>
typealias JeiRecipeHolderType<T> = JeiRecipeType<RecipeHolder<T>>
