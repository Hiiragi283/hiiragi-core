package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * [DataProvider]を登録するインターフェース
 * @see HTRootDataGenerator
 * @see HTDataPackGenerator
 */
interface HTDataGenerator {
    fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA

    fun <DATA : DataProvider> addProvider(factory: GatherDataEvent.DataProviderFromOutputLookup<DATA>): DATA

    fun <DATA : DataProvider> addProvider(factory: Factory<DATA>): DATA

    fun addLootTables(vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, LootContextParamSet>): LootTableProvider =
        addProvider { output, lookupProvider ->
            LootTableProvider(
                output,
                setOf(),
                pairs.map { LootTableProvider.SubProviderEntry(it.first, it.second) },
                lookupProvider,
            )
        }

    fun interface Factory<DATA : DataProvider> {
        fun create(context: HTDataGenContext): DATA
    }
}
