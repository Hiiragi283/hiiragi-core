package hiiragi283.core.api.data.loot

import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition

/**
 * Hiiragi Coreとそれを前提とするmodで使用される[GlobalLootModifierProvider]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTGlobalLootModifierProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>, modid: String) : GlobalLootModifierProvider(output, registries, modid) {
    protected fun add(key: ResourceKey<LootTable>, vararg conditions: LootItemCondition) {
        add(key.location().path, AddTableLootModifier(arrayOf(*conditions), key))
    }

    protected fun builder(block: Block): LootTableIdCondition.Builder = LootTableIdCondition.builder(block.lootTable.location())

    protected fun builder(entityType: EntityType<*>): LootTableIdCondition.Builder = LootTableIdCondition.builder(entityType.defaultLootTable.location())
}
