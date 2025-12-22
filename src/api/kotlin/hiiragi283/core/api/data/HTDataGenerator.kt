package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.DataProvider
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.data.loot.LootTableSubProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.neoforged.neoforge.data.event.GatherDataEvent

/**
 * [DataProvider]を登録する処理を表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTRootDataGenerator
 * @see HTDataPackGenerator
 */
interface HTDataGenerator {
    /**
     * 指定した[factory]を登録します。
     * @param DATA [DataProvider]を継承したクラス
     * @return [factory]から生成された[DataProvider]のインスタンス
     */
    fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA

    /**
     * 指定した[factory]を登録します。
     * @param DATA [DataProvider]を継承したクラス
     * @return [factory]から生成された[DataProvider]のインスタンス
     */
    fun <DATA : DataProvider> addProvider(factory: GatherDataEvent.DataProviderFromOutputLookup<DATA>): DATA

    /**
     * 指定した[factory]を登録します。
     * @param DATA [DataProvider]を継承したクラス
     * @return [factory]から生成された[DataProvider]のインスタンス
     */
    fun <DATA : DataProvider> addProvider(factory: Factory<DATA>): DATA

    /**
     * Loot Tableを登録します。
     * @param pairs [LootTableSubProvider]を作成するブロックと[LootContextParamSet]のペアの一覧
     * @return [LootTableProvider]のインスタンス
     */
    fun addLootTables(vararg pairs: Pair<(HolderLookup.Provider) -> LootTableSubProvider, LootContextParamSet>): LootTableProvider =
        addProvider { output, lookupProvider ->
            LootTableProvider(
                output,
                setOf(),
                pairs.map { LootTableProvider.SubProviderEntry(it.first, it.second) },
                lookupProvider,
            )
        }

    /**
     * [HTDataGenContext]を受けるとって[DataProvider]に変換する処理を表すインターフェースです。
     * @param DATA [DataProvider]を継承したクラス
     * @author Hiiragi Tsubasa
     * @since 0.1.0
     * @see DataProvider.Factory
     */
    fun interface Factory<DATA : DataProvider> {
        /**
         * [DataProvider]を生成します。
         */
        fun create(context: HTDataGenContext): DATA
    }
}
