package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

fun GatherDataEvent.createRecipeProvider(
    name: String,
    factory: (HolderLookup.Provider, RecipeOutput) -> RecipeProvider,
): RecipeProvider.Runner = this.createProvider { output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider> ->
    object : RecipeProvider.Runner(output, lookupProvider) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
            factory(registries, output)

        override fun getName(): String = name
    }
}
