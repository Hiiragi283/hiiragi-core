package hiiragi283.core.api.data

import hiiragi283.core.api.registry.createKey
import hiiragi283.core.api.resource.toId
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.util.context.ContextKeySet
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

fun GatherDataEvent.createLootProvider(
    vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, ContextKeySet>,
): LootTableProvider = this.createProvider { output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider> ->
    LootTableProvider(
        output,
        emptySet(),
        pairs.map { LootTableProvider.SubProviderEntry(it.first, it.second) },
        lookupProvider,
    )
}

fun GatherDataEvent.createRecipeProvider(
    name: String,
    factory: (HolderLookup.Provider, RecipeOutput) -> RecipeProvider,
): RecipeProvider.Runner = this.createProvider { output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider> ->
    object : RecipeProvider.Runner(output, lookupProvider) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider = factory(
            registries,
            object : RecipeOutput {
                override fun accept(
                    key: ResourceKey<Recipe<*>>,
                    recipe: Recipe<*>,
                    advancement: AdvancementHolder?,
                    vararg conditions: ICondition,
                ) {
                    val modId: String = this@createRecipeProvider.modContainer.modId
                    val id: Identifier = modId.toId(key.identifier().path)
                    output.accept(Registries.RECIPE.createKey(id), recipe, advancement, *conditions)
                }

                override fun advancement(): Advancement.Builder = output.advancement()

                override fun includeRootAdvancement() {
                    output.includeRootAdvancement()
                }
            },
        )

        override fun getName(): String = name
    }
}
