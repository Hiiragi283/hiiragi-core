package hiiragi283.core.api.data.loot

import hiiragi283.core.api.data.HTDataGenContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider
import net.neoforged.neoforge.common.loot.AddTableLootModifier
import net.neoforged.neoforge.common.loot.LootTableIdCondition

/**
 * @since 0.6.0
 */
abstract class HTGlobalLootModifierProvider(modId: String, context: HTDataGenContext) :
    GlobalLootModifierProvider(context.output, context.registries, modId) {
    protected fun add(key: ResourceKey<LootTable>, vararg conditions: LootItemCondition) {
        add(key.location().path, AddTableLootModifier(arrayOf(*conditions), key))
    }

    protected fun builder(block: Block): LootTableIdCondition.Builder = LootTableIdCondition.builder(block.lootTable.location())

    protected fun builder(entityType: EntityType<*>): LootTableIdCondition.Builder =
        LootTableIdCondition.builder(entityType.defaultLootTable.location())
}
