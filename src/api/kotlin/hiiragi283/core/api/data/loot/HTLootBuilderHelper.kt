package hiiragi283.core.api.data.loot

import net.minecraft.advancements.critereon.EnchantmentPredicate
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.advancements.critereon.ItemSubPredicates
import net.minecraft.advancements.critereon.MinMaxBounds
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.MatchTool

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 * @see net.minecraft.data.loot.BlockLootSubProvider
 */
class HTLootBuilderHelper(provider: HolderLookup.Provider) {
    /**
     * 指定した[conditionBuilder]に基づいて分岐する[ルートテーブル][LootTable.Builder]を作成します。
     * @param block [conditionBuilder]の条件を満たす時にドロップするブロック
     * @param conditionBuilder ルートテーブルの分岐の定義
     * @param alternativeBuilder [conditionBuilder]の条件を満たさない時に適応されるドロップ
     */
    fun createSelfDropDispatchTable(
        block: Block,
        conditionBuilder: LootItemCondition.Builder,
        alternativeBuilder: LootPoolEntryContainer.Builder<*>,
    ): LootTable.Builder = LootTable
        .lootTable()
        .withPool(
            LootPool
                .lootPool()
                .add(
                    LootItem
                        .lootTableItem(block)
                        .`when`(conditionBuilder)
                        .otherwise(alternativeBuilder),
                ),
        )

    //    Fortune    //

    /**
     * 幸運エンチャントのインスタンス
     */
    val fortune: Holder<Enchantment> by lazy { provider.holderOrThrow(Enchantments.FORTUNE) }

    //    Silk Touch    //

    /**
     * ツールにシルクタッチが付与されているかの判定
     */
    val hasSilkTouch: LootItemCondition.Builder = MatchTool.toolMatches(
        ItemPredicate.Builder
            .item()
            .withSubPredicate(
                ItemSubPredicates.ENCHANTMENTS,
                ItemEnchantmentsPredicate.enchantments(
                    listOf(
                        EnchantmentPredicate(
                            provider.holderOrThrow(Enchantments.SILK_TOUCH),
                            MinMaxBounds.Ints.atLeast(1),
                        ),
                    ),
                ),
            ),
    )

    /**
     * ツールにシルクタッチが付与されていないかの判定
     */
    val doesNotHaveSilkTouch: LootItemCondition.Builder = hasSilkTouch.invert()

    /**
     * シルクタッチの有無で分岐する[ルートテーブル][LootTable.Builder]を作成します。
     * @param block シルクタッチ時にドロップするブロック
     * @param alternativeBuilder 非シルクタッチ時に適応されるドロップ
     */
    fun createSilkTouchDispatchTable(block: Block, alternativeBuilder: LootPoolEntryContainer.Builder<*>): LootTable.Builder =
        createSelfDropDispatchTable(block, hasSilkTouch, alternativeBuilder)
}
