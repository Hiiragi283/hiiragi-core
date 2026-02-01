package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.loot.HTLootBuilderHelper
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.core.HolderLookup
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition

/**
 * @author Hiiragi Tsubasa
 * @since 0.9.0
 */
fun interface HTBlockLootFactory {
    companion object {
        @JvmField
        val DEFAULT = HTBlockLootFactory { (_, _, _, block: Block) ->
            LootTable
                .lootTable()
                .withPool(
                    LootPool
                        .lootPool()
                        .add(LootItem.lootTableItem(block))
                        .`when`(ExplosionCondition.survivesExplosion()),
                )
        }

        @JvmStatic
        fun createOre(dropPrefix: HTTagPrefix?): HTBlockLootFactory = HTBlockLootFactory { context: Context ->
            val (key: HTMaterialKey, _, helper: HTLootBuilderHelper, block: Block) = context
            val dropItem: ItemLike = dropPrefix?.let { HiiragiCoreAccess.INSTANCE.getItemOrVanilla(it, key) } ?: block
            helper.createSilkTouchDispatchTable(
                block,
                LootItem
                    .lootTableItem(dropItem)
                    .apply(ApplyBonusCount.addOreBonusCount(helper.fortune))
                    .apply(ApplyExplosionDecay.explosionDecay()),
            )
        }
    }

    fun fromContext(context: Context): LootTable.Builder

    fun create(
        key: HTMaterialKey,
        propertyMap: HTPropertyMap,
        provider: HolderLookup.Provider,
        block: Block,
    ): LootTable.Builder = fromContext(Context(key, propertyMap, HTLootBuilderHelper(provider), block))

    @JvmRecord
    data class Context(
        val key: HTMaterialKey,
        val propertyMap: HTPropertyMap,
        val helper: HTLootBuilderHelper,
        val block: Block,
    )
}
