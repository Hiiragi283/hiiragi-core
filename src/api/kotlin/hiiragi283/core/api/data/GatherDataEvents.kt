package hiiragi283.core.api.data

import hiiragi283.core.api.data.advancement.HTAdvancementProvider
import hiiragi283.core.api.data.advancement.HTSubAdvancementProvider
import hiiragi283.core.api.function.partially1
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * この[GatherDataEvent][this]に[DataProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun <T : DataProvider> GatherDataEvent.createProviderWithHelper(builder: (ExistingFileHelper, PackOutput) -> T): T = this.createProvider(builder.partially1(this.existingFileHelper))

/**
 * この[GatherDataEvent][this]に[DataProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun <T : DataProvider> GatherDataEvent.createProviderWithHelper(
    builder: (ExistingFileHelper, PackOutput, CompletableFuture<HolderLookup.Provider>) -> T,
): T = this.createProvider(builder.partially1(this.existingFileHelper))

/**
 * この[GatherDataEvent][this]に[LootTableProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun GatherDataEvent.createLootTables(
    vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, LootContextParamSet>,
): LootTableProvider = this.createProvider { output: PackOutput, future: CompletableFuture<HolderLookup.Provider> ->
    LootTableProvider(
        output,
        emptySet(),
        pairs.map { LootTableProvider.SubProviderEntry(it.first, it.second) },
        future,
    )
}

/**
 * この[GatherDataEvent][this]に[HTSubAdvancementProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun GatherDataEvent.createAdvancements(vararg subProviders: HTSubAdvancementProvider): HTAdvancementProvider = this.createAdvancements(subProviders.toList())

/**
 * この[GatherDataEvent][this]に[HTSubAdvancementProvider]を登録します。
 * @author Hiiragi Tsubasa
 * @since 0.15.2
 */
fun GatherDataEvent.createAdvancements(subProviders: List<HTSubAdvancementProvider>): HTAdvancementProvider = this.createProviderWithHelper(::HTAdvancementProvider.partially1(subProviders))
