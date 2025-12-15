package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.HTDataGenContext
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.function.Consumer

abstract class HTRecipeProvider(context: HTDataGenContext) : RecipeProvider(context.output, context.registries) {
    final override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        for (provider: HTSubRecipeProvider in buildList { collectProviders(::add) }) {
            provider.buildRecipes(recipeOutput, holderLookup)
        }
    }

    protected abstract fun collectProviders(consumer: Consumer<HTSubRecipeProvider>)
}
