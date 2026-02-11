package hiiragi283.core.api.data.recipe

import hiiragi283.core.api.data.HTDataGenContext
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.function.Consumer

/**
 * [HTSubRecipeProvider]に基づいた[RecipeProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTRecipeProvider(context: HTDataGenContext) : RecipeProvider(context.output, context.registries) {
    final override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {
        for (provider: HTSubRecipeProvider in buildList { collectProviders(::add) }) {
            provider.buildRecipes(recipeOutput, holderLookup)
        }
    }

    /**
     * レシピを生成させたい[HTSubRecipeProvider]を[consumer]に登録します。
     */
    protected abstract fun collectProviders(consumer: Consumer<HTSubRecipeProvider>)
}
