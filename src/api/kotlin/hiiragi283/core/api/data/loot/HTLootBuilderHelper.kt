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

    val fortune: Holder<Enchantment> by lazy { provider.holderOrThrow(Enchantments.FORTUNE) }

    //    Silk Touch    //

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

    val doesNotHaveSilkTouch: LootItemCondition.Builder = hasSilkTouch.invert()

    fun createSilkTouchDispatchTable(block: Block, alternativeBuilder: LootPoolEntryContainer.Builder<*>): LootTable.Builder =
        createSelfDropDispatchTable(block, hasSilkTouch, alternativeBuilder)
}
