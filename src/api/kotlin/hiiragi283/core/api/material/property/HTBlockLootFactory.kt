package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.loot.HTLootBuilderHelper
import hiiragi283.core.api.material.HTMaterialManager
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
        val DEFAULT = HTBlockLootFactory { (_, _, block: Block) ->
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
            val (entry: HTMaterialManager.Entry, helper: HTLootBuilderHelper, block: Block) = context
            val dropItem: ItemLike = dropPrefix?.let { HiiragiCoreAccess.INSTANCE.getItemOrVanilla(it, entry) } ?: block
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

    fun create(entry: HTMaterialManager.Entry, provider: HolderLookup.Provider, block: Block): LootTable.Builder =
        fromContext(Context(entry, HTLootBuilderHelper(provider), block))

    @JvmRecord
    data class Context(val entry: HTMaterialManager.Entry, val helper: HTLootBuilderHelper, val block: Block)
}
