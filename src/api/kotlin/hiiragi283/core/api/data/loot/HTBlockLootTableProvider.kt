package hiiragi283.core.api.data.loot

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.function.partially3
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.property.HTBlockLootFactory
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.property.getOrDefault
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[BlockLootSubProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTBlockLootTableProvider(protected val modId: String, registries: HolderLookup.Provider) :
    BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), registries) {
    final override fun getKnownBlocks(): Iterable<Block> = registries
        .lookupOrThrow(Registries.BLOCK)
        .listElements()
        .filter { holder: Holder.Reference<Block> -> holder.toLike().namespace == modId }
        .map(Holder<Block>::value)
        .toList()

    //    Extensions    //

    protected val helper = HTLootBuilderHelper(registries)

    protected val contents: HTMaterialContents = HiiragiCoreAccess.INSTANCE.materialContents

    /**
     * ブロックをそのままドロップするルートテーブルを指定します。
     */
    protected fun dropSelf(like: HTBlockHolderLike<*, *>) {
        dropSelf(like.asBlock())
    }

    protected fun add(like: HTBlockHolderLike<*, *>, table: LootTable.Builder) {
        add(like.asBlock(), table)
    }

    protected inline fun <BLOCK : Block> add(like: HTBlockHolderLike<BLOCK, *>, factory: (BLOCK) -> LootTable.Builder) {
        val block: BLOCK = like.asBlock()
        add(block, factory(block))
    }

    /**
     * @since 0.8.0
     */
    protected fun registerMaterials() {
        for ((key: HTMaterialKey, propertyMap: HTPropertyMap) in HiiragiCoreAccess.INSTANCE.materialManager) {
            for ((prefix: HTTagPrefix, block: HTBlockHolderLike<*, *>) in contents.getBlockMap(key)) {
                if (block.namespace != modId) continue
                val lootFactory: HTBlockLootFactory =
                    propertyMap.getOrDefault(HTMaterialPropertyKeys.BLOCK_LOOT)[prefix] ?: HTBlockLootFactory.DEFAULT
                add(block, lootFactory::create.partially3(key, propertyMap, registries))
            }
        }
    }
}
