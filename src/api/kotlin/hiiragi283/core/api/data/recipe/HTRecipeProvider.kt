package hiiragi283.core.api.data.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

/**
 * [HTSubRecipeProvider]に基づいた[RecipeProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
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
