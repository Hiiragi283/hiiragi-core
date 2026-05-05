package hiiragi283.core.api.data.loot

import hiiragi283.core.api.registry.HTBlockHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.BuiltInLootTables
import net.minecraft.world.level.storage.loot.LootTable

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockLootSubProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockLootTableProvider(
    registries: HolderLookup.Provider,
    protected val modId: String,
    private val rawBlocks: Sequence<HTBlockHolderLike<*>>,
) : BlockLootSubProvider(emptySet(), FeatureFlags.REGISTRY.allFlags(), registries) {
    final override fun getKnownBlocks(): Iterable<Block> = rawBlocks
        .filter { holder: HTBlockHolderLike<*> -> holder.namespace == modId }
        .map(HTBlockHolderLike<*>::get)
        .filter { block: Block -> block.lootTable != BuiltInLootTables.EMPTY }
        .toList()

    //    Extensions    //

    /**
     * 幸運エンチャントのインスタンス
     * @since 0.10.0
     */
    val fortune: Holder<Enchantment> by lazy { registries.holderOrThrow(Enchantments.FORTUNE) }

    /**
     * ブロックをそのままドロップするルートテーブルを指定します。
     */
    protected fun dropSelf(like: HTBlockHolderLike<*>) {
        dropSelf(like.get())
    }

    protected fun add(like: HTBlockHolderLike<*>, table: LootTable.Builder) {
        add(like.get(), table)
    }

    protected inline fun <BLOCK : Block> add(like: HTBlockHolderLike<BLOCK>, factory: (BLOCK) -> LootTable.Builder) {
        val block: BLOCK = like.get()
        add(block, factory(block))
    }
}
