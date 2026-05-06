package hiiragi283.core.data

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.data.recipe.HCAERecipeProvider
import hiiragi283.core.data.recipe.HCBasicRecipeProvider
import hiiragi283.core.data.recipe.HCCommonRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

class HCRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(output, registries) {
    override fun collectProviders(consumer: Consumer<HTSubRecipeProvider>) {
        consumer.accept(HCCommonRecipeProvider)
        consumer.accept(HCBasicRecipeProvider)
        // Integration
        consumer.accept(HCAERecipeProvider)
    }
}
