package hiiragi283.core.api.data.loot

import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.asBlockSequence
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
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
abstract class HTBlockLootTableProvider(protected val modId: String, registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    final override fun getKnownBlocks(): Iterable<Block> = getRawBlocks()
        .filter { holder: HTBlockHolderLike<*> -> holder.namespace == modId }
        .map(HTBlockHolderLike<*>::asBlock)
        .filter { block: Block -> block.lootTable != BuiltInLootTables.EMPTY }
        .toList()

    /**
     * @since 0.10.0
     */
    protected open fun getRawBlocks(): Sequence<HTBlockHolderLike<*>> = registries
        .lookupOrThrow(Registries.BLOCK)
        .asBlockSequence()

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
        dropSelf(like.asBlock())
    }

    protected fun add(like: HTBlockHolderLike<*>, table: LootTable.Builder) {
        add(like.asBlock(), table)
    }

    protected inline fun <BLOCK : Block> add(like: HTBlockHolderLike<BLOCK>, factory: (BLOCK) -> LootTable.Builder) {
        val block: BLOCK = like.asBlock()
        add(block, factory(block))
    }
}
