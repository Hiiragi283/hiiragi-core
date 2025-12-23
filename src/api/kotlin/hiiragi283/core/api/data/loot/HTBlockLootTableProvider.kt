package hiiragi283.core.api.data.loot

import hiiragi283.core.api.registry.HTHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockLootSubProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockLootTableProvider(registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    private val blockCache: MutableSet<Block> = hashSetOf()

    final override fun add(block: Block, builder: LootTable.Builder) {
        super.add(block, builder)
        blockCache += block
    }

    final override fun getKnownBlocks(): Iterable<Block> = blockCache

    //    Extensions    //

    /**
     * 幸運エンチャントのインスタンス
     */
    protected val fortune: Holder<Enchantment> by lazy { registries.holderOrThrow(Enchantments.FORTUNE) }

    /**
     * ブロックをそのままドロップするルートテーブルを指定します。
     */
    protected fun dropSelf(like: HTHolderLike<Block, *>) {
        dropSelf(like.get())
    }

    /**
     * 鉱石向けのルートテーブルを指定します。
     * @param item 採掘後にドロップするアイテム
     * @param range ドロップするアイテムの個数の範囲
     * @param block 対象となるブロック
     * @see BlockLootSubProvider.createOreDrop
     */
    protected fun createOreDrops(item: ItemLike, range: NumberProvider?, block: Block): LootTable.Builder = createSilkTouchDispatchTable(
        block,
        applyExplosionDecay(
            block,
            LootItem
                .lootTableItem(item)
                .also { range?.let(SetItemCountFunction::setCount)?.let(it::apply) }
                .apply(ApplyBonusCount.addOreBonusCount(fortune)),
        ),
    )

    protected fun <BLOCK : Block> add(like: HTHolderLike<Block, BLOCK>, table: LootTable.Builder) {
        add(like.get(), table)
    }

    protected inline fun <BLOCK : Block> add(like: HTHolderLike<Block, BLOCK>, factory: (BLOCK) -> LootTable.Builder) {
        val block: BLOCK = like.get()
        add(block, factory(block))
    }
}
